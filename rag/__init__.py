"""RAG pipeline module for AST-aware retrieval-augmented generation."""

from rag.models import CodeChunk, IndexStats, RetrievalResult

__all__ = [
    "ASTChunker",
    "ChromaIndexer",
    "CodeChunk",
    "EmbeddingService",
    "HybridRetriever",
    "IndexStats",
    "RAGConfig",
    "RAGPipeline",
    "RAGResult",
    "RetrievalResult",
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
    if name == "RAGConfig":
        from rag.pipeline import RAGConfig

        return RAGConfig
    if name == "RAGPipeline":
        from rag.pipeline import RAGPipeline

        return RAGPipeline
    if name == "RAGResult":
        from rag.pipeline import RAGResult

        return RAGResult
    raise AttributeError(f"module {__name__!r} has no attribute {name!r}")
