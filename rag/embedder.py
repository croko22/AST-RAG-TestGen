"""Embedding service wrapping sentence-transformers (optional)."""

from __future__ import annotations


def _require_sentence_transformers():
    try:
        from sentence_transformers import SentenceTransformer  # noqa: F401

        return True
    except ImportError as err:
        raise ImportError(
            "sentence-transformers is required for embedding. "
            "Install with: pip install sentence-transformers"
        ) from err


class EmbeddingService:
    def __init__(self, model_name: str = "all-MiniLM-L6-v2"):
        self._model_name = model_name
        self._model = None
        self._cache: dict[str, list[float]] = {}

    def _get_model(self):
        if self._model is None:
            _require_sentence_transformers()
            from sentence_transformers import SentenceTransformer

            self._model = SentenceTransformer(self._model_name)
        return self._model

    def embed(self, texts: list[str]) -> list[list[float]]:
        if not texts:
            return []
        results = []
        uncached = []
        uncached_indices = []
        for i, text in enumerate(texts):
            if not text or not text.strip():
                model = self._get_model()
                dim = model.get_sentence_embedding_dimension()
                results.append([0.0] * dim)
                continue
            truncated = text[:8192]
            if truncated in self._cache:
                results.append(self._cache[truncated])
            else:
                results.append(None)
                uncached.append(truncated)
                uncached_indices.append(i)

        if uncached:
            model = self._get_model()
            vectors = model.encode(uncached, convert_to_numpy=True)
            for idx, text, vec in zip(uncached_indices, uncached, vectors, strict=True):
                embedding = vec.tolist()
                self._cache[text] = embedding
                results[idx] = embedding

        return results

    def embed_single(self, text: str) -> list[float]:
        if not text or not text.strip():
            model = self._get_model()
            return [0.0] * model.get_sentence_embedding_dimension()
        truncated = text[:8192]
        if truncated in self._cache:
            return self._cache[truncated]
        model = self._get_model()
        vec = model.encode([truncated], convert_to_numpy=True)[0]
        embedding = vec.tolist()
        self._cache[truncated] = embedding
        return embedding

    @property
    def embedding_dim(self) -> int:
        model = self._get_model()
        return model.get_sentence_embedding_dimension()
