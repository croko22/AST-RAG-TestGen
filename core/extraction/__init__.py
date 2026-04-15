"""Extraction layer for AST-RAG TestGen."""

from core.extraction.extractor import (
    extract_dependencies_from_content,
    extract_dependencies_from_file,
    extract_test_methods,
    map_test_to_focal_method,
)

__all__ = [
    "extract_dependencies_from_file",
    "extract_dependencies_from_content",
    "extract_test_methods",
    "map_test_to_focal_method",
]
