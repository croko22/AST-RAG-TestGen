"""Unit tests for RAG protocols (T04)."""

from rag.models import CodeChunk, RetrievalResult
from rag.protocols import Chunker, Embedder, Indexer, Retriever


class FakeChunker:
    def chunk(self, parsed_class: object) -> list[CodeChunk]:
        return [CodeChunk(chunk_id="test", content="x", chunk_type="method", file_path="x.java")]


class FakeEmbedder:
    def embed(self, texts: list[str]) -> list[list[float]]:
        return [[0.1] * 384 for _ in texts]

    def embed_single(self, text: str) -> list[float]:
        return [0.1] * 384


class FakeIndexer:
    def index(self, chunks: list[CodeChunk], collection: str) -> None:
        pass

    def query(self, embedding: list[float], k: int) -> list[RetrievalResult]:
        return []


class FakeRetriever:
    def retrieve(self, query: str, k: int, alpha: float) -> list[RetrievalResult]:
        return []


class TestChunkerProtocol:
    def test_duck_type_compliance(self):
        chunker = FakeChunker()
        assert isinstance(chunker, Chunker)

    def test_non_compliant(self):
        class NotAChunker:
            pass

        assert not isinstance(NotAChunker(), Chunker)


class TestEmbedderProtocol:
    def test_duck_type_compliance(self):
        embedder = FakeEmbedder()
        assert isinstance(embedder, Embedder)

    def test_embed_returns_vectors(self):
        embedder = FakeEmbedder()
        result = embedder.embed(["hello"])
        assert len(result) == 1
        assert len(result[0]) == 384

    def test_embed_single(self):
        embedder = FakeEmbedder()
        result = embedder.embed_single("hello")
        assert len(result) == 384


class TestIndexerProtocol:
    def test_duck_type_compliance(self):
        indexer = FakeIndexer()
        assert isinstance(indexer, Indexer)

    def test_non_compliant_missing_method(self):
        class NotAnIndexer:
            def index(self, chunks, collection):
                pass

        assert not isinstance(NotAnIndexer(), Indexer)


class TestRetrieverProtocol:
    def test_duck_type_compliance(self):
        retriever = FakeRetriever()
        assert isinstance(retriever, Retriever)

    def test_non_compliant(self):
        class NotARetriever:
            def fetch(self, query):
                pass

        assert not isinstance(NotARetriever(), Retriever)
