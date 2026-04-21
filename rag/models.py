"""Data models for the RAG pipeline."""

from __future__ import annotations

from dataclasses import dataclass, field


@dataclass(slots=True)
class CodeChunk:
    chunk_id: str
    content: str
    chunk_type: str
    file_path: str
    package: str | None = None
    class_name: str = ""
    method_name: str | None = None
    start_line: int = 0
    end_line: int = 0
    metadata: dict[str, str] = field(default_factory=dict)
    embedding: list[float] | None = None


@dataclass(slots=True)
class RetrievalResult:
    chunk: CodeChunk
    vector_score: float = 0.0
    ast_score: float = 0.0
    hybrid_score: float = 0.0
    source: str = "hybrid"


@dataclass(slots=True)
class IndexStats:
    total_chunks: int
    total_files: int
    indexed_at: str
    collection_name: str
