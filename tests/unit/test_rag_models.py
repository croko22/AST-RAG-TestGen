"""Unit tests for RAG data models (T02)."""

from rag.models import CodeChunk, IndexStats, RetrievalResult


class TestCodeChunk:
    def test_construction(self):
        chunk = CodeChunk(
            chunk_id="svc.java:method:getData",
            content="public String getData() { return repo.findAll(); }",
            chunk_type="method",
            file_path="Service.java",
            package="com.example",
            class_name="Service",
            method_name="getData",
            start_line=10,
            end_line=12,
        )
        assert chunk.chunk_id == "svc.java:method:getData"
        assert chunk.chunk_type == "method"
        assert chunk.method_name == "getData"
        assert chunk.embedding is None
        assert chunk.metadata == {}

    def test_optional_fields_default(self):
        chunk = CodeChunk(
            chunk_id="a.java:class:Foo",
            content="class Foo {}",
            chunk_type="class",
            file_path="a.java",
        )
        assert chunk.package is None
        assert chunk.method_name is None
        assert chunk.start_line == 0
        assert chunk.end_line == 0
        assert chunk.class_name == ""
        assert chunk.metadata == {}
        assert chunk.embedding is None

    def test_with_embedding(self):
        chunk = CodeChunk(
            chunk_id="x",
            content="x",
            chunk_type="method",
            file_path="x.java",
            embedding=[0.1, 0.2, 0.3],
        )
        assert chunk.embedding == [0.1, 0.2, 0.3]

    def test_with_metadata(self):
        chunk = CodeChunk(
            chunk_id="y",
            content="y",
            chunk_type="class",
            file_path="y.java",
            metadata={"visibility": "public", "complexity": "low"},
        )
        assert chunk.metadata["visibility"] == "public"


class TestRetrievalResult:
    def test_construction(self):
        chunk = CodeChunk(
            chunk_id="z",
            content="z",
            chunk_type="method",
            file_path="z.java",
        )
        result = RetrievalResult(
            chunk=chunk,
            vector_score=0.9,
            ast_score=1.0,
            hybrid_score=0.95,
            source="hybrid",
        )
        assert result.vector_score == 0.9
        assert result.ast_score == 1.0
        assert result.hybrid_score == 0.95
        assert result.source == "hybrid"

    def test_defaults(self):
        chunk = CodeChunk(chunk_id="w", content="w", chunk_type="class", file_path="w.java")
        result = RetrievalResult(chunk=chunk)
        assert result.vector_score == 0.0
        assert result.ast_score == 0.0
        assert result.hybrid_score == 0.0
        assert result.source == "hybrid"


class TestIndexStats:
    def test_construction(self):
        stats = IndexStats(
            total_chunks=42,
            total_files=10,
            indexed_at="2026-04-21T00:00:00Z",
            collection_name="project_x",
        )
        assert stats.total_chunks == 42
        assert stats.total_files == 10
        assert stats.collection_name == "project_x"
