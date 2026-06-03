"""CLI layer for AST-RAG TestGen."""

from cli.legacy import legacy_main, run_legacy_cli
from cli.modern import create_modern_cli, run_modern_cli
from cli.parser import build_arg_parser

__all__ = [
    "build_arg_parser",
    "create_modern_cli",
    "legacy_main",
    "run_legacy_cli",
    "run_modern_cli",
]
