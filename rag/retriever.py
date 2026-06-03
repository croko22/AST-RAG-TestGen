"""Hybrid retriever combining vector search with AST-based relevance."""

from __future__ import annotations

from rag.models import RetrievalResult


def _compute_ast_scores(
    query_class: str,
    results: list[RetrievalResult],
    ast_dependencies: set[str] | None = None,
) -> list[float]:
    if ast_dependencies is None:
        return [0.0] * len(results)

    scores = []
    for r in results:
        class_name = r.chunk.class_name
        method_name = r.chunk.method_name
        package = r.chunk.package or ""

        full_ref = f"{package}.{class_name}" if package else class_name

        if full_ref in ast_dependencies or class_name in ast_dependencies:
            scores.append(1.0)
        elif method_name:
            matched = any(
                dep.endswith(f".{method_name}") or dep.endswith(f".{class_name}.{method_name}")
                for dep in ast_dependencies
            )
            scores.append(0.5 if matched else 0.0)
        else:
            scores.append(0.0)

    return scores


class HybridRetriever:
    def __init__(
        self,
        indexer=None,
        embedder=None,
        alpha: float = 0.5,
        ast_dependencies: set[str] | None = None,
        collection_name: str | None = None,
    ):
        self._indexer = indexer
        self._embedder = embedder
        self._alpha = alpha
        self._ast_dependencies = ast_dependencies or set()
        self._collection_name = collection_name

    def set_ast_dependencies(self, deps: set[str]) -> None:
        self._ast_dependencies = deps

    def retrieve(
        self,
        query: str,
        k: int = 10,
        alpha: float | None = None,
    ) -> list[RetrievalResult]:
        if alpha is None:
            alpha = self._alpha

        if not query.strip():
            return []

        if self._embedder is None or self._indexer is None:
            return []

        embedding = self._embedder.embed_single(query)
        vector_results = self._indexer.query(
            embedding, collection_name=self._collection_name, k=k * 2
        )

        if not vector_results:
            return []

        ast_scores = _compute_ast_scores(query, vector_results, self._ast_dependencies)

        for i, r in enumerate(vector_results):
            r.ast_score = ast_scores[i]
            r.hybrid_score = alpha * r.vector_score + (1 - alpha) * r.ast_score
            if alpha == 1.0:
                r.source = "vector"
            elif alpha == 0.0:
                r.source = "ast"
            else:
                r.source = "hybrid"

        vector_results.sort(key=lambda r: r.hybrid_score, reverse=True)
        return vector_results[:k]

    def build_context(
        self,
        results: list[RetrievalResult],
        max_tokens: int = 4000,
    ) -> str:
        if not results:
            return ""

        char_budget = max_tokens * 4
        parts: list[str] = []
        used = 0

        for r in results:
            chunk_text = r.chunk.content
            header = f"// {r.chunk.chunk_type}"
            if r.chunk.class_name:
                header += f" from {r.chunk.class_name}"
            if r.chunk.method_name:
                header += f".{r.chunk.method_name}"
            section = f"{header}\n{chunk_text}\n\n"
            if used + len(section) > char_budget:
                break
            parts.append(section)
            used += len(section)

        return "".join(parts)
