#!/usr/bin/env python3
"""
AST-RAG TestGen: Main entry point

This is the main entry point for the AST-based RAG test generation system.
It routes to the appropriate CLI (legacy or modern) and orchestrates the pipeline.

Supports two modes:
- Legacy mode: Generate test for a single Java file
- Benchmark mode: Run benchmarks using a manifest file
"""

from __future__ import annotations

import sys

# Try to use modern CLI first, fall back to legacy
try:
    from cli import run_modern_cli

    MODERN_CLI_AVAILABLE = True
except ImportError:
    MODERN_CLI_AVAILABLE = False

# Legacy CLI is always available
from cli import run_legacy_cli

# Backward compatibility exports
from cli.parser import build_arg_parser  # noqa: F401
from orchestration.benchmark import run_benchmark_mode  # noqa: F401
from orchestration.generator import generate_test_for_file  # noqa: F401


def main() -> int:
    """Main entry point.

    Returns:
        Exit code (0 on success, 1 on failure).
    """
    # Try modern CLI first
    if MODERN_CLI_AVAILABLE:
        # Check if we're being called with modern CLI arguments
        # (e.g., "generate", "benchmark", "providers" subcommands)
        if len(sys.argv) > 1 and sys.argv[1] in ("generate", "benchmark", "providers", "serve"):
            return run_modern_cli()

    # Fall back to legacy CLI
    return run_legacy_cli()


if __name__ == "__main__":
    sys.exit(main())
