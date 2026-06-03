"""Integration tests for RAG pipeline round-trip: chunk → embed → index → retrieve (T17)."""

from __future__ import annotations

from unittest.mock import MagicMock

import pytest

from core.parsing.models import FieldDeclaration, MethodSignature, ParsedJavaClass
from rag.chunker import ASTChunker
from rag.embedder import EmbeddingService
from rag.indexer import ChromaIndexer
from rag.models import CodeChunk, RetrievalResult
from rag.retriever import HybridRetriever


def _make_parsed(
    name="Service",
    package="com.example",
    methods=None,
    fields=None,
    imports=None,
    content="",
    file_path="Service.java",
) -> ParsedJavaClass:
    return ParsedJavaClass(
        name=name,
        package=package,
        imports=imports or [],
        fields=fields or [],
        methods=methods or [],
        content=content,
        file_path=file_path,
    )


SERVICE_CONTENT = """\
package com.example.demo;
import java.util.List;
import com.example.Repository;
public class UsuarioService {
    private Repository repo;
    private String name;
    public String getData() {
        return repo.findAll();
    }
    public void save(String data) {
        repo.save(data);
    }
    private void internalHelper() {}
}
"""


@pytest.fixture
def mock_embedder():
    svc = EmbeddingService()
    mock_model = MagicMock()
    mock_model.get_sentence_embedding_dimension.return_value = 384
    svc._model = mock_model

    def fake_encode(texts, convert_to_numpy=True):
        results = []
        for t in texts if isinstance(texts, list) else [texts]:
            results.append(MagicMock(tolist=lambda: [0.1 + i * 0.001 for i in range(384)]))
        return results

    mock_model.encode.side_effect = fake_encode
    return svc


@pytest.fixture
def stored():
    return {"ids": [], "embeddings": [], "metadatas": [], "documents": []}


@pytest.fixture
def mock_collection(stored):
    collection = MagicMock()

    def fake_count():
        return len(stored["ids"])

    def fake_upsert(ids, embeddings, metadatas, documents):
        for i, id_ in enumerate(ids):
            if id_ in stored["ids"]:
                idx = stored["ids"].index(id_)
                stored["embeddings"][idx] = embeddings[i]
                stored["metadatas"][idx] = metadatas[i]
                stored["documents"][idx] = documents[i]
            else:
                stored["ids"].append(id_)
                stored["embeddings"].append(embeddings[i])
                stored["metadatas"].append(metadatas[i])
                stored["documents"].append(documents[i])

    def fake_query(query_embeddings, n_results=10):
        out_ids, out_dists, out_docs, out_metas = [], [], [], []
        for q_emb in query_embeddings:
            scored = []
            for i, s_emb in enumerate(stored["embeddings"]):
                dot = sum(a * b for a, b in zip(q_emb, s_emb, strict=False))
                dist = max(0.0, 1.0 - min(max(dot / 384, 0.0), 1.0))
                scored.append((dist, i))
            scored.sort()
            top = scored[:n_results]
            out_ids.append([stored["ids"][idx] for _, idx in top])
            out_dists.append([d for d, _ in top])
            out_docs.append([stored["documents"][idx] for _, idx in top])
            out_metas.append([stored["metadatas"][idx] for _, idx in top])
        return {
            "ids": out_ids,
            "distances": out_dists,
            "documents": out_docs,
            "metadatas": out_metas,
        }

    collection.count.side_effect = fake_count
    collection.upsert.side_effect = fake_upsert
    collection.query.side_effect = fake_query
    collection.get.return_value = {"metadatas": []}
    return collection


@pytest.fixture
def real_indexer(mock_collection, mock_embedder, tmp_path):
    indexer = ChromaIndexer(persist_dir=str(tmp_path / "chroma"), embedder=mock_embedder)
    indexer._get_collection = MagicMock(return_value=mock_collection)
    return indexer


class TestChunkEmbedIndexRetrieve:
    def test_full_round_trip(self, real_indexer, mock_embedder, mock_collection):
        parsed = _make_parsed(
            methods=[
                MethodSignature(name="getData", visibility="public", return_type="String"),
                MethodSignature(name="save", visibility="public", return_type="void"),
            ],
            fields=[FieldDeclaration(name="repo", type="Repository", visibility="private")],
            imports=["com.example.Repository"],
            content=SERVICE_CONTENT,
            file_path="UsuarioService.java",
        )

        chunker = ASTChunker()
        chunks = chunker.chunk(parsed)
        assert len(chunks) >= 3

        real_indexer.index(chunks, "test_project")

        query_emb = mock_embedder.embed_single("getData method")
        results = real_indexer.query(query_emb, "test_project", k=5)
        assert len(results) >= 1

    def test_empty_project(self, real_indexer, mock_embedder):
        parsed = _make_parsed(name="Empty", content="public class Empty {}", file_path="Empty.java")
        chunker = ASTChunker()
        chunks = chunker.chunk(parsed)

        real_indexer.index(chunks, "empty_project")

        query_emb = mock_embedder.embed_single("test")
        results = real_indexer.query(query_emb, "empty_project", k=5)
        if chunks:
            assert len(results) >= 1
        else:
            assert results == []

    def test_single_file_project(self, real_indexer, mock_embedder):
        parsed = _make_parsed(
            methods=[MethodSignature(name="process", visibility="public", return_type="void")],
            content="public void process() { /* logic */ }",
            file_path="Service.java",
        )

        chunker = ASTChunker()
        chunks = chunker.chunk(parsed)
        real_indexer.index(chunks, "single_project")

        query_emb = mock_embedder.embed_single("process method")
        results = real_indexer.query(query_emb, "single_project", k=10)
        assert len(results) >= 1

    def test_multi_file_project(self, real_indexer, mock_embedder):
        chunker = ASTChunker()
        all_chunks = []
        for cls_name in ("Service", "Repository", "Entity"):
            parsed = _make_parsed(
                name=cls_name,
                methods=[
                    MethodSignature(
                        name=f"{cls_name.lower()}Method", visibility="public", return_type="void"
                    )
                ],
                content=f"public void {cls_name.lower()}Method() {{ }}",
                file_path=f"{cls_name}.java",
            )
            all_chunks.extend(chunker.chunk(parsed))

        real_indexer.index(all_chunks, "multi_project")

        query_emb = mock_embedder.embed_single("service method")
        results = real_indexer.query(query_emb, "multi_project", k=5)
        assert len(results) >= 1

    def test_reindex_upsert_behavior(self, real_indexer, mock_embedder, stored):
        parsed_v1 = _make_parsed(
            methods=[MethodSignature(name="getData", visibility="public", return_type="String")],
            content="public String getData() { return null; }",
            file_path="Service.java",
        )

        chunker = ASTChunker()
        chunks_v1 = chunker.chunk(parsed_v1)
        real_indexer.index(chunks_v1, "reindex_project")
        count_v1 = len(stored["ids"])
        assert count_v1 > 0

        parsed_v2 = _make_parsed(
            methods=[
                MethodSignature(name="getData", visibility="public", return_type="String"),
                MethodSignature(name="save", visibility="public", return_type="void"),
            ],
            content="public String getData() { return null; }\npublic void save(String x) {}",
            file_path="Service.java",
        )
        chunks_v2 = chunker.chunk(parsed_v2)
        real_indexer.index(chunks_v2, "reindex_project")
        count_v2 = len(stored["ids"])
        assert count_v2 >= count_v1

        assert len(stored["embeddings"]) == count_v2


class TestHybridRetrievalAlpha:
    def test_alpha_pure_vector(self, mock_embedder):
        mock_indexer = MagicMock()
        mock_indexer.query.return_value = [
            RetrievalResult(
                chunk=CodeChunk(
                    chunk_id="Svc.java:method:getData",
                    content="code",
                    chunk_type="method",
                    file_path="Svc.java",
                    class_name="Other",
                    method_name="getData",
                ),
                vector_score=0.9,
                source="vector",
            )
        ]

        r = HybridRetriever(
            indexer=mock_indexer, embedder=mock_embedder, alpha=1.0, ast_dependencies={"Repo"}
        )
        results = r.retrieve("test query")
        assert len(results) >= 1
        assert results[0].source == "vector"
        assert results[0].hybrid_score == pytest.approx(0.9)

    def test_alpha_pure_ast(self, mock_embedder):
        mock_indexer = MagicMock()
        mock_indexer.query.return_value = [
            RetrievalResult(
                chunk=CodeChunk(
                    chunk_id="Repo.java:method:findAll",
                    content="code",
                    chunk_type="method",
                    file_path="Repo.java",
                    class_name="Repo",
                    method_name="findAll",
                ),
                vector_score=0.3,
                source="vector",
            )
        ]

        r = HybridRetriever(
            indexer=mock_indexer, embedder=mock_embedder, alpha=0.0, ast_dependencies={"Repo"}
        )
        results = r.retrieve("test query")
        assert len(results) >= 1
        assert results[0].source == "ast"
        assert results[0].hybrid_score == pytest.approx(1.0)

    def test_alpha_hybrid_blending(self, mock_embedder):
        mock_indexer = MagicMock()
        mock_indexer.query.return_value = [
            RetrievalResult(
                chunk=CodeChunk(
                    chunk_id="Repo.java:method:findAll",
                    content="code",
                    chunk_type="method",
                    file_path="Repo.java",
                    class_name="Repo",
                    method_name="findAll",
                ),
                vector_score=0.8,
                source="vector",
            )
        ]

        r = HybridRetriever(
            indexer=mock_indexer, embedder=mock_embedder, alpha=0.5, ast_dependencies={"Repo"}
        )
        results = r.retrieve("test query")
        assert len(results) >= 1
        expected = 0.5 * 0.8 + 0.5 * 1.0
        assert results[0].hybrid_score == pytest.approx(expected)
        assert results[0].source == "hybrid"

    def test_results_ranked_by_relevance(self, mock_embedder):
        mock_indexer = MagicMock()
        mock_indexer.query.return_value = [
            RetrievalResult(
                chunk=CodeChunk(
                    chunk_id="Low.java:method:m",
                    content="low",
                    chunk_type="method",
                    file_path="Low.java",
                    class_name="Other",
                    method_name="m",
                ),
                vector_score=0.9,
                source="vector",
            ),
            RetrievalResult(
                chunk=CodeChunk(
                    chunk_id="High.java:method:n",
                    content="high",
                    chunk_type="method",
                    file_path="High.java",
                    class_name="Repo",
                    method_name="n",
                ),
                vector_score=0.3,
                source="vector",
            ),
        ]

        r = HybridRetriever(
            indexer=mock_indexer, embedder=mock_embedder, alpha=0.3, ast_dependencies={"Repo"}
        )
        results = r.retrieve("test")
        assert results[0].hybrid_score >= results[1].hybrid_score

    def test_build_context_from_pipeline_results(self, mock_embedder):
        results = [
            RetrievalResult(
                chunk=CodeChunk(
                    chunk_id="Svc.java:method:getData",
                    content="public String getData() { return repo.findAll(); }",
                    chunk_type="method",
                    file_path="Svc.java",
                    class_name="Svc",
                    method_name="getData",
                ),
                vector_score=0.9,
            )
        ]

        r = HybridRetriever()
        ctx = r.build_context(results)
        assert "Svc" in ctx
        assert "getData" in ctx
        assert "repo.findAll" in ctx


class TestEndToEndWithRetriever:
    def test_chunk_index_retrieve_build_context(self, real_indexer, mock_embedder):
        parsed = _make_parsed(
            methods=[
                MethodSignature(name="getData", visibility="public", return_type="String"),
                MethodSignature(name="save", visibility="public", return_type="void"),
            ],
            fields=[FieldDeclaration(name="repo", type="Repository", visibility="private")],
            imports=["com.example.Repository"],
            content=SERVICE_CONTENT,
            file_path="UsuarioService.java",
            package="com.example.demo",
        )

        chunker = ASTChunker()
        chunks = chunker.chunk(parsed)
        real_indexer.index(chunks, "e2e_project")

        retriever = HybridRetriever(
            indexer=real_indexer,
            embedder=mock_embedder,
            alpha=0.5,
            ast_dependencies={"Repository"},
            collection_name="e2e_project",
        )
        results = retriever.retrieve("getData method in service", k=5)
        assert len(results) >= 1

        ctx = retriever.build_context(results)
        assert len(ctx) > 0
