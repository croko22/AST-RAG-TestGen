"""ChromaDB-based vector indexer for code chunks."""

from __future__ import annotations

import hashlib
from datetime import UTC, datetime

from rag.models import CodeChunk, IndexStats, RetrievalResult


def _require_chromadb():
    try:
        import chromadb  # noqa: F401

        return True
    except ImportError as err:
        raise ImportError(
            "chromadb is required for indexing. Install with: pip install chromadb"
        ) from err


def _collection_name_for(path: str) -> str:
    h = hashlib.md5(path.encode()).hexdigest()[:12]
    return f"project_{h}"


class ChromaIndexer:
    def __init__(self, persist_dir: str = ".chroma_db", embedder=None):
        self._persist_dir = persist_dir
        self._embedder = embedder
        self._client = None

    def _get_client(self):
        if self._client is None:
            _require_chromadb()
            import chromadb

            self._client = chromadb.PersistentClient(path=self._persist_dir)
        return self._client

    def _get_collection(self, collection_name: str):
        client = self._get_client()
        return client.get_or_create_collection(
            name=collection_name,
            metadata={"hnsw:space": "cosine"},
        )

    def index(self, chunks: list[CodeChunk], collection_name: str) -> None:
        if not chunks:
            return
        collection = self._get_collection(collection_name)

        if self._embedder is None:
            raise ValueError("Embedder is required for indexing")

        texts = [c.content for c in chunks]
        embeddings = self._embedder.embed(texts)

        ids = [c.chunk_id for c in chunks]
        metadatas = []
        for c in chunks:
            meta = {
                "file_path": c.file_path,
                "chunk_type": c.chunk_type,
                "class_name": c.class_name,
            }
            if c.package:
                meta["package"] = c.package
            if c.method_name:
                meta["method_name"] = c.method_name
            meta.update(c.metadata)
            metadatas.append(meta)

        documents = texts
        collection.upsert(ids=ids, embeddings=embeddings, metadatas=metadatas, documents=documents)

    def query(
        self,
        embedding: list[float],
        collection_name: str,
        k: int = 10,
    ) -> list[RetrievalResult]:
        if not embedding:
            return []
        try:
            collection = self._get_collection(collection_name)
        except Exception:
            return []

        count = collection.count()
        if count == 0:
            return []

        actual_k = min(k, count)
        results = collection.query(query_embeddings=[embedding], n_results=actual_k)

        retrieval_results: list[RetrievalResult] = []
        if results and results["ids"] and results["ids"][0]:
            for i, chunk_id in enumerate(results["ids"][0]):
                distance = results["distances"][0][i] if results["distances"] else 0.0
                similarity = 1.0 - distance
                doc = results["documents"][0][i] if results["documents"] else ""
                meta = results["metadatas"][0][i] if results["metadatas"] else {}

                chunk = CodeChunk(
                    chunk_id=chunk_id,
                    content=doc or "",
                    chunk_type=meta.get("chunk_type", ""),
                    file_path=meta.get("file_path", ""),
                    package=meta.get("package"),
                    class_name=meta.get("class_name", ""),
                    method_name=meta.get("method_name"),
                    metadata={
                        k: v
                        for k, v in meta.items()
                        if k
                        not in ("file_path", "chunk_type", "class_name", "package", "method_name")
                    },
                )
                retrieval_results.append(
                    RetrievalResult(
                        chunk=chunk,
                        vector_score=similarity,
                        source="vector",
                    )
                )

        return retrieval_results

    def get_stats(self, collection_name: str) -> IndexStats:
        try:
            collection = self._get_collection(collection_name)
        except Exception:
            return IndexStats(
                total_chunks=0,
                total_files=0,
                indexed_at="",
                collection_name=collection_name,
            )

        count = collection.count()
        metas = collection.get(include=["metadatas"]) if count > 0 else {"metadatas": [[]]}
        files = set()
        if metas and metas.get("metadatas"):
            for m in metas["metadatas"]:
                if m and "file_path" in m:
                    files.add(m["file_path"])

        return IndexStats(
            total_chunks=count,
            total_files=len(files),
            indexed_at=datetime.now(UTC).isoformat(),
            collection_name=collection_name,
        )

    def delete_collection(self, collection_name: str) -> None:
        try:
            client = self._get_client()
            client.delete_collection(name=collection_name)
        except Exception:
            pass
