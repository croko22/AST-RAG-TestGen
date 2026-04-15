"""
Legacy CLI for AST-RAG TestGen using argparse.

This module provides the legacy CLI interface for backward compatibility.
"""

from __future__ import annotations

import sys
from pathlib import Path

from cli.parser import build_arg_parser
from output import get_output


def legacy_main(args) -> int:
    """Execute the legacy single-file generation mode.

    This preserves the original behavior for backward compatibility.

    Args:
        args: Parsed arguments from legacy CLI.

    Returns:
        Exit code (0 on success, 1 on failure).
    """
    output = get_output()

    if not Path(args.java_file).exists():
        output.print_error(f"Java file not found: {args.java_file}")
        return 1

    if not Path(args.project_path).exists():
        output.print_error(f"Project path not found: {args.project_path}")
        return 1

    try:
        # Import orchestration modules
        from orchestration.generator import generate_test_for_file

        test_code = generate_test_for_file(
            java_file_path=args.java_file,
            java_project_path=args.project_path,
            output_dir=args.output,
            max_dependencies=args.max_deps,
            llm_provider=args.provider,
            llm_model=args.model,
            enable_metainfo_db=args.enable_metainfo_db,
            enable_reftest_parity=args.enable_reftest_parity,
        )

        if args.print:
            output.print_code(test_code)

        output.print_success(f"Test saved to {args.output}/{Path(args.java_file).stem}Test.java")
        return 0

    except Exception as e:
        output.print_error(f"Error: {e}")
        import traceback

        traceback.print_exc()
        return 1


def run_legacy_cli() -> int:
    """Run the legacy CLI and return exit code.

    Returns:
        Exit code (0 on success, 1 on failure).
    """
    parser = build_arg_parser()
    args = parser.parse_args()

    # Check if benchmark mode is enabled
    if getattr(args, "benchmark_manifest", None):
        # Import benchmark orchestration
        from orchestration.benchmark import run_benchmark_mode

        return run_benchmark_mode(
            manifest_path=args.benchmark_manifest,
            output_dir=args.benchmark_output,
            dry_run=getattr(args, "benchmark_dry_run", False),
        )

    # Otherwise, run legacy mode
    return legacy_main(args)
