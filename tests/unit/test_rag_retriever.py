"""Unit tests for rag/retriever.py (T08)."""

from __future__ import annotations

from unittest.mock import MagicMock

import pytest

from rag.models import CodeChunk, RetrievalResult
from rag.retriever import HybridRetriever, _compute_ast_scores


def _make_result(
    class_name="Svc",
    method_name="getData",
    package="com.example",
    vector_score=0.8,
) -> RetrievalResult:
    chunk = CodeChunk(
        chunk_id=f"{class_name}:{method_name}",
        content="code",
        chunk_type="method",
        file_path=f"{class_name}.java",
        package=package,
        class_name=class_name,
        method_name=method_name,
    )
    return RetrievalResult(chunk=chunk, vector_score=vector_score, source="vector")


class TestComputeAstScores:
    def test_no_deps_returns_zeros(self):
        results = [_make_result()]
        scores = _compute_ast_scores("query", results, ast_dependencies=None)
        assert scores == [0.0]

    def test_direct_class_match(self):
        results = [_make_result(class_name="Repo")]
        scores = _compute_ast_scores("query", results, ast_dependencies={"Repo"})
        assert scores == [1.0]

    def test_full_ref_match(self):
        results = [_make_result(class_name="Repo", package="com.example")]
        scores = _compute_ast_scores("query", results, ast_dependencies={"com.example.Repo"})
        assert scores == [1.0]

    def test_transitive_method_match(self):
        results = [_make_result(class_name="Repo", method_name="findAll")]
        scores = _compute_ast_scores(
            "query", results, ast_dependencies={"com.example.Repo.findAll"}
        )
        assert scores == [0.5]

    def test_no_match(self):
        results = [_make_result(class_name="Repo", method_name="findAll")]
        scores = _compute_ast_scores("query", results, ast_dependencies={"com.other.Save"})
        assert scores == [0.0]


class TestHybridRetriever:
    def test_empty_query_returns_empty(self):
        r = HybridRetriever()
        assert r.retrieve("") == []

    def test_no_embedder_returns_empty(self):
        r = HybridRetriever(indexer=MagicMock())
        assert r.retrieve("test") == []

    def test_no_indexer_returns_empty(self):
        r = HybridRetriever(embedder=MagicMock())
        assert r.retrieve("test") == []

    def test_pure_vector_search(self):
        mock_embedder = MagicMock()
        mock_embedder.embed_single.return_value = [0.1] * 384

        mock_indexer = MagicMock()
        mock_indexer.query.return_value = [_make_result(vector_score=0.9)]

        r = HybridRetriever(
            indexer=mock_indexer,
            embedder=mock_embedder,
            alpha=1.0,
        )
        results = r.retrieve("test query")
        assert len(results) == 1
        assert results[0].hybrid_score == pytest.approx(0.9)
        assert results[0].source == "vector"

    def test_pure_ast_search(self):
        mock_embedder = MagicMock()
        mock_embedder.embed_single.return_value = [0.1] * 384

        result = _make_result(class_name="Repo", vector_score=0.9)
        mock_indexer = MagicMock()
        mock_indexer.query.return_value = [result]

        r = HybridRetriever(
            indexer=mock_indexer,
            embedder=mock_embedder,
            alpha=0.0,
            ast_dependencies={"Repo"},
        )
        results = r.retrieve("test query")
        assert len(results) == 1
        assert results[0].hybrid_score == pytest.approx(1.0)
        assert results[0].source == "ast"

    def test_hybrid_scoring(self):
        mock_embedder = MagicMock()
        mock_embedder.embed_single.return_value = [0.1] * 384

        result = _make_result(class_name="Repo", vector_score=0.8)
        mock_indexer = MagicMock()
        mock_indexer.query.return_value = [result]

        r = HybridRetriever(
            indexer=mock_indexer,
            embedder=mock_embedder,
            alpha=0.5,
            ast_dependencies={"Repo"},
        )
        results = r.retrieve("test query")
        assert len(results) == 1
        expected = 0.5 * 0.8 + 0.5 * 1.0
        assert results[0].hybrid_score == pytest.approx(expected)
        assert results[0].source == "hybrid"

    def test_results_sorted_by_hybrid_score(self):
        mock_embedder = MagicMock()
        mock_embedder.embed_single.return_value = [0.1] * 384

        r1 = _make_result(class_name="A", method_name="m1", vector_score=0.5)
        r2 = _make_result(class_name="B", method_name="m2", vector_score=0.9)
        mock_indexer = MagicMock()
        mock_indexer.query.return_value = [r1, r2]

        retriever = HybridRetriever(
            indexer=mock_indexer,
            embedder=mock_embedder,
            alpha=1.0,
        )
        results = retriever.retrieve("test")
        assert results[0].hybrid_score >= results[1].hybrid_score

    def test_top_k_limit(self):
        mock_embedder = MagicMock()
        mock_embedder.embed_single.return_value = [0.1] * 384

        results = [_make_result(class_name=f"C{i}", vector_score=0.9 - i * 0.1) for i in range(5)]
        mock_indexer = MagicMock()
        mock_indexer.query.return_value = results

        r = HybridRetriever(indexer=mock_indexer, embedder=mock_embedder, alpha=1.0)
        retrieved = r.retrieve("test", k=2)
        assert len(retrieved) == 2

    def test_set_ast_dependencies(self):
        r = HybridRetriever()
        r.set_ast_dependencies({"com.example.Svc"})
        assert r._ast_dependencies == {"com.example.Svc"}

    def test_alpha_override_in_retrieve(self):
        mock_embedder = MagicMock()
        mock_embedder.embed_single.return_value = [0.1] * 384

        result = _make_result(class_name="Repo", vector_score=0.8)
        mock_indexer = MagicMock()
        mock_indexer.query.return_value = [result]

        r = HybridRetriever(
            indexer=mock_indexer,
            embedder=mock_embedder,
            alpha=0.5,
            ast_dependencies={"Repo"},
        )
        results = r.retrieve("test", alpha=1.0)
        assert results[0].source == "vector"
        assert results[0].hybrid_score == pytest.approx(0.8)

    def test_build_context_empty(self):
        r = HybridRetriever()
        assert r.build_context([]) == ""

    def test_build_context_respects_token_limit(self):
        chunks = [
            RetrievalResult(
                chunk=CodeChunk(
                    chunk_id=f"c{i}",
                    content="x" * 1000,
                    chunk_type="method",
                    file_path="f.java",
                    class_name="C",
                    method_name=f"m{i}",
                ),
                vector_score=0.9,
            )
            for i in range(10)
        ]
        r = HybridRetriever()
        ctx = r.build_context(chunks, max_tokens=100)
        assert len(ctx) <= 100 * 4

    def test_build_context_includes_headers(self):
        chunks = [
            RetrievalResult(
                chunk=CodeChunk(
                    chunk_id="c1",
                    content="code here",
                    chunk_type="method",
                    file_path="f.java",
                    class_name="Svc",
                    method_name="getData",
                ),
                vector_score=0.9,
            )
        ]
        r = HybridRetriever()
        ctx = r.build_context(chunks)
        assert "Svc" in ctx
        assert "getData" in ctx

    def test_empty_vector_results(self):
        mock_embedder = MagicMock()
        mock_embedder.embed_single.return_value = [0.1] * 384

        mock_indexer = MagicMock()
        mock_indexer.query.return_value = []

        r = HybridRetriever(indexer=mock_indexer, embedder=mock_embedder, alpha=0.5)
        assert r.retrieve("test") == []
