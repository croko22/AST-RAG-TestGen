"""Abstract protocols for the RAG pipeline components."""

from __future__ import annotations

from typing import Protocol, runtime_checkable

from rag.models import CodeChunk, RetrievalResult


@runtime_checkable
class Chunker(Protocol):
    def chunk(self, parsed_class: object) -> list[CodeChunk]: ...


@runtime_checkable
class Embedder(Protocol):
    def embed(self, texts: list[str]) -> list[list[float]]: ...

    def embed_single(self, text: str) -> list[float]: ...


@runtime_checkable
class Indexer(Protocol):
    def index(self, chunks: list[CodeChunk], collection: str) -> None: ...

    def query(self, embedding: list[float], k: int) -> list[RetrievalResult]: ...


@runtime_checkable
class Retriever(Protocol):
    def retrieve(self, query: str, k: int, alpha: float) -> list[RetrievalResult]: ...
