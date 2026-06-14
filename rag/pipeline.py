"""Single-interface RAG pipeline: chunk -> embed -> index -> retrieve -> context."""

from __future__ import annotations

import hashlib
from dataclasses import dataclass, field
from pathlib import Path
from typing import Any, TYPE_CHECKING

if TYPE_CHECKING:
    from core.parsing.models import ParsedJavaClass


@dataclass(slots=True)
class RAGConfig:
    """Configuration for the RAG pipeline."""
    alpha: float = 0.5
    retrieval_top_k: int = 10
    max_context_tokens: int = 4000
    chroma_persist_dir: str = ".chroma_db"
    embedding_model: str = "all-MiniLM-L6-v2"


@dataclass(slots=True)
class RAGResult:
    """Result from RAG retrieval."""
    context: str | None
    sources: dict[str, Any] = field(default_factory=dict)


class RAGPipeline:
    """Deep module: one interface for the entire RAG retrieval flow.

    Callers pass a parsed Java class and project path, get back context
    and source metadata. All wiring (chunker -> embedder -> indexer ->
    retriever) is internal to this module.
    """

    def __init__(self, config: RAGConfig | None = None):
        self.config = config or RAGConfig()
        self._chunker = None
        self._embedder = None
        self._indexer = None

    def retrieve(
        self,
        parsed_class: ParsedJavaClass,
        project_path: str | Path,
        strategy: str = "hybrid",
    ) -> RAGResult:
        """Retrieve RAG context for a parsed Java class.

        Args:
            parsed_class: Parsed Java class to generate context for.
            project_path: Root path of the Java project.
            strategy: Retrieval strategy -- 'ast', 'rag', or 'hybrid'.

        Returns:
            RAGResult with context string and source metadata.
        """
        from rag.chunker import ASTChunker
        from rag.embedder import EmbeddingService
        from rag.indexer import ChromaIndexer
        from rag.retriever import HybridRetriever, JavaFileRetriever

        project_path = Path(project_path)

        if self._chunker is None:
            self._chunker = ASTChunker()
        if self._embedder is None:
            self._embedder = EmbeddingService(model_name=self.config.embedding_model)
        if self._indexer is None:
            self._indexer = ChromaIndexer(
                persist_dir=self.config.chroma_persist_dir,
                embedder=self._embedder,
            )

        collection_name = f"project_{hashlib.md5(str(project_path).encode()).hexdigest()[:12]}"

        chunks = self._chunker.chunk(parsed_class)
        java_retriever = JavaFileRetriever(str(project_path))
        for f in java_retriever.get_all_java_files():
            try:
                file_chunks = self._chunker.chunk_file(f)
                chunks.extend(file_chunks)
            except Exception:
                pass

        self._indexer.index(chunks, collection_name)

        ast_deps: set[str] = set()
        if strategy in ("ast", "hybrid"):
            for dep in parsed_class.dependencies:
                if dep.type in ("class", "interface", "import", "field"):
                    ast_deps.add(dep.name)
            for imp in parsed_class.imports:
                parts = imp.split(".")
                if len(parts) > 1 and not imp.startswith(("java.", "javax.", "org.")):
                    dep_name = parts[-1]
                    if not dep_name.endswith("*"):
                        ast_deps.add(dep_name)

        effective_alpha = self.config.alpha
        if strategy == "ast":
            effective_alpha = 0.0
        elif strategy == "rag":
            effective_alpha = 1.0

        hybrid = HybridRetriever(
            indexer=self._indexer,
            embedder=self._embedder,
            alpha=effective_alpha,
            ast_dependencies=ast_deps,
        )

        query = parsed_class.content[:2000]
        results = hybrid.retrieve(query, k=self.config.retrieval_top_k)
        context = hybrid.build_context(results, max_tokens=self.config.max_context_tokens)

        sources = {
            "strategy": strategy,
            "chunks_indexed": len(chunks),
            "results_retrieved": len(results),
            "top_scores": [r.hybrid_score for r in results[:5]],
        }

        return RAGResult(context=context or None, sources=sources)
