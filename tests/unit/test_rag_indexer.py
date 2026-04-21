"""Unit tests for rag/indexer.py (T07)."""

from __future__ import annotations

from unittest.mock import MagicMock, patch

import pytest

from rag.indexer import ChromaIndexer, _collection_name_for
from rag.models import CodeChunk


def _make_chunk(chunk_id="c1", content="x", chunk_type="method", file_path="Svc.java", **kw):
    return CodeChunk(
        chunk_id=chunk_id, content=content, chunk_type=chunk_type, file_path=file_path, **kw
    )


class TestCollectionNaming:
    def test_deterministic(self):
        assert _collection_name_for("/foo/bar") == _collection_name_for("/foo/bar")

    def test_different_paths(self):
        assert _collection_name_for("/foo") != _collection_name_for("/bar")


class TestChromaIndexer:
    def test_import_error_without_chromadb(self):
        with patch.dict("sys.modules", {"chromadb": None}):
            indexer = ChromaIndexer()
            with pytest.raises(ImportError, match="chromadb"):
                indexer.index([_make_chunk()], "test_coll")

    def test_index_empty_chunks(self):
        indexer = ChromaIndexer()
        mock_collection = MagicMock()
        indexer._get_collection = MagicMock(return_value=mock_collection)
        indexer.index([], "test")
        mock_collection.upsert.assert_not_called()

    def test_index_requires_embedder(self):
        indexer = ChromaIndexer()
        indexer._get_collection = MagicMock()
        with pytest.raises(ValueError, match="Embedder"):
            indexer.index([_make_chunk()], "test")

    def test_index_upserts_chunks(self):
        mock_embedder = MagicMock()
        mock_embedder.embed.return_value = [[0.1] * 384]
        mock_collection = MagicMock()

        indexer = ChromaIndexer(embedder=mock_embedder)
        indexer._get_collection = MagicMock(return_value=mock_collection)

        chunks = [
            _make_chunk(chunk_id="m1", content="code", class_name="Svc", package="com.example")
        ]
        indexer.index(chunks, "test_coll")

        mock_embedder.embed.assert_called_once_with(["code"])
        mock_collection.upsert.assert_called_once()
        call_kw = mock_collection.upsert.call_args
        assert call_kw.kwargs["ids"] == ["m1"]

    def test_index_metadata_includes_chunk_fields(self):
        mock_embedder = MagicMock()
        mock_embedder.embed.return_value = [[0.1] * 384]
        mock_collection = MagicMock()

        indexer = ChromaIndexer(embedder=mock_embedder)
        indexer._get_collection = MagicMock(return_value=mock_collection)

        chunks = [
            _make_chunk(
                chunk_id="m1",
                class_name="Svc",
                package="com.example",
                method_name="getData",
                metadata={"visibility": "public"},
            )
        ]
        indexer.index(chunks, "test")

        call_kw = mock_collection.upsert.call_args.kwargs
        meta = call_kw["metadatas"][0]
        assert meta["class_name"] == "Svc"
        assert meta["package"] == "com.example"
        assert meta["method_name"] == "getData"
        assert meta["visibility"] == "public"

    def test_query_returns_results(self):
        mock_collection = MagicMock()
        mock_collection.count.return_value = 2
        mock_collection.query.return_value = {
            "ids": [["c1", "c2"]],
            "distances": [[0.1, 0.3]],
            "documents": [["code1", "code2"]],
            "metadatas": [
                [
                    {
                        "file_path": "A.java",
                        "chunk_type": "method",
                        "class_name": "A",
                        "method_name": "foo",
                    },
                    {"file_path": "B.java", "chunk_type": "class_fields", "class_name": "B"},
                ]
            ],
        }

        indexer = ChromaIndexer()
        indexer._get_collection = MagicMock(return_value=mock_collection)

        results = indexer.query([0.1] * 384, "test_coll", k=5)
        assert len(results) == 2
        assert results[0].chunk.chunk_id == "c1"
        assert results[0].vector_score == pytest.approx(0.9)
        assert results[0].source == "vector"
        assert results[1].vector_score == pytest.approx(0.7)

    def test_query_empty_embedding(self):
        indexer = ChromaIndexer()
        result = indexer.query([], "test")
        assert result == []

    def test_query_missing_collection(self):
        indexer = ChromaIndexer()
        indexer._get_collection = MagicMock(side_effect=Exception("not found"))
        result = indexer.query([0.1] * 384, "missing_coll")
        assert result == []

    def test_query_empty_collection(self):
        mock_collection = MagicMock()
        mock_collection.count.return_value = 0

        indexer = ChromaIndexer()
        indexer._get_collection = MagicMock(return_value=mock_collection)

        result = indexer.query([0.1] * 384, "empty")
        assert result == []

    def test_get_stats(self):
        mock_collection = MagicMock()
        mock_collection.count.return_value = 10
        mock_collection.get.return_value = {
            "metadatas": [
                {"file_path": "A.java"},
                {"file_path": "B.java"},
                {"file_path": "A.java"},
            ]
        }

        indexer = ChromaIndexer()
        indexer._get_collection = MagicMock(return_value=mock_collection)

        stats = indexer.get_stats("test_coll")
        assert stats.total_chunks == 10
        assert stats.total_files == 2
        assert stats.collection_name == "test_coll"

    def test_get_stats_empty(self):
        indexer = ChromaIndexer()
        indexer._get_collection = MagicMock(side_effect=Exception("nope"))
        stats = indexer.get_stats("missing")
        assert stats.total_chunks == 0
        assert stats.total_files == 0

    def test_delete_collection(self):
        mock_client = MagicMock()
        indexer = ChromaIndexer()
        indexer._client = mock_client

        indexer.delete_collection("test_coll")
        mock_client.delete_collection.assert_called_once_with(name="test_coll")

    def test_delete_collection_nonexistent(self):
        mock_client = MagicMock()
        mock_client.delete_collection.side_effect = Exception("not found")
        indexer = ChromaIndexer()
        indexer._client = mock_client

        indexer.delete_collection("missing")
        mock_client.delete_collection.assert_called_once()

    def test_query_limits_k_to_count(self):
        mock_collection = MagicMock()
        mock_collection.count.return_value = 2
        mock_collection.query.return_value = {
            "ids": [["c1", "c2"]],
            "distances": [[0.1, 0.2]],
            "documents": [["a", "b"]],
            "metadatas": [
                [
                    {"file_path": "A.java", "chunk_type": "method", "class_name": "A"},
                    {"file_path": "B.java", "chunk_type": "method", "class_name": "B"},
                ]
            ],
        }

        indexer = ChromaIndexer()
        indexer._get_collection = MagicMock(return_value=mock_collection)

        indexer.query([0.1] * 384, "test", k=100)
        mock_collection.query.assert_called_once()
        call_kw = mock_collection.query.call_args.kwargs
        assert call_kw["n_results"] == 2
