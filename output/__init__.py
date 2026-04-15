"""Output and presentation layer for AST-RAG TestGen."""

from output.console import (
    OutputManager,
    ProgressContextManager,
    get_output,
    set_output,
)

__all__ = [
    "OutputManager",
    "ProgressContextManager",
    "get_output",
    "set_output",
]
