"""RAG pipeline module for AST-aware retrieval-augmented generation."""

from rag.models import CodeChunk, IndexStats, RetrievalResult

__all__ = [
    "CodeChunk",
    "IndexStats",
    "RetrievalResult",
    "ASTChunker",
    "EmbeddingService",
    "ChromaIndexer",
    "HybridRetriever",
]


def __getattr__(name):
    if name == "ASTChunker":
        from rag.chunker import ASTChunker

        return ASTChunker
    if name == "EmbeddingService":
        from rag.embedder import EmbeddingService

        return EmbeddingService
    if name == "ChromaIndexer":
        from rag.indexer import ChromaIndexer

        return ChromaIndexer
    if name == "HybridRetriever":
        from rag.retriever import HybridRetriever

        return HybridRetriever
    raise AttributeError(f"module {__name__!r} has no attribute {name!r}")
