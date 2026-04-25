"""
CLI argument parsing for AST-RAG TestGen.

This module provides argument parsing logic for both legacy and modern CLI.
"""

from __future__ import annotations

import argparse

# Configuration management
try:
    from config import get_config

    CONFIG_AVAILABLE = True
except ImportError:
    CONFIG_AVAILABLE = False


def build_arg_parser() -> argparse.ArgumentParser:
    """Build command-line argument parser.

    Returns:
        Configured ArgumentParser instance.
    """
    try:
        from llm import client as llm_client_module

        get_available_providers = llm_client_module.get_available_providers
        available_providers = get_available_providers()
    except (ModuleNotFoundError, AttributeError):
        available_providers = ["anthropic", "openai", "glm", "gemini", "openrouter", "nvidia"]

    # Load configuration for default values (keep as strings for CLI)
    default_provider = "anthropic"
    default_model = "claude-3-5-sonnet-20241022"
    default_output = "./tests_generados"
    default_max_deps = 10
    default_benchmark_output = "./benchmark_results"

    if CONFIG_AVAILABLE:
        try:
            config = get_config()
            default_provider = config.llm.provider
            default_model = config.llm.model
            # Keep paths as strings for CLI
            default_output = str(config.java.test_output_dir)
            default_max_deps = config.java.max_dependencies
            default_benchmark_output = str(config.benchmark.output_dir)
        except Exception:
            # Fall back to hardcoded defaults
            pass

    parser = argparse.ArgumentParser(
        description="AST-RAG TestGen: Generate unit tests using AST-based RAG",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  # Generate test for a specific Java file
  python main.py /path/to/UsuarioService.java /path/to/java/project

  # Use OpenAI instead of Anthropic
  python main.py service.java project/ --provider openai --model gpt-4-turbo

  # Use GLM (Zhipu AI)
  python main.py service.java project/ --provider glm --model glm-5-turbo

  # Use Gemini
  python main.py service.java project/ --provider gemini --model gemini-2.0-flash-exp

  # Use OpenRouter
  python main.py service.java project/ --provider openrouter --model anthropic/claude-3.5-sonnet

  # Specify custom output directory
  python main.py service.java project/ --output ./my_tests

  # Run benchmark mode
  python main.py --benchmark-manifest benchmark.yaml --benchmark-output ./benchmark_results
        """,
    )

    # Benchmark mode arguments
    parser.add_argument(
        "--benchmark-manifest",
        dest="benchmark_manifest",
        metavar="PATH",
        help="Path to benchmark manifest (JSON or TOML). Enables benchmark mode.",
    )
    parser.add_argument(
        "--benchmark-output",
        dest="benchmark_output",
        metavar="DIR",
        default=default_benchmark_output,
        help=f"Output directory for benchmark results (default: {default_benchmark_output})",
    )
    parser.add_argument(
        "--benchmark-dry-run",
        dest="benchmark_dry_run",
        action="store_true",
        help="Run benchmark without actual generation (for testing)",
    )

    # Legacy mode arguments
    parser.add_argument(
        "java_file",
        nargs="?",
        help="Path to the Java file to generate tests for",
    )
    parser.add_argument(
        "project_path",
        nargs="?",
        help="Root path of the Java project (for dependency resolution)",
    )
    parser.add_argument(
        "--provider",
        default=default_provider,
        choices=available_providers,
        help=f"LLM provider. Available: {', '.join(available_providers)} (default: {default_provider})",
    )
    parser.add_argument(
        "--model",
        default=default_model,
        help=f"LLM model to use (default: {default_model})",
    )
    parser.add_argument(
        "--output",
        default=default_output,
        help=f"Output directory for generated tests (default: {default_output})",
    )
    parser.add_argument(
        "--max-deps",
        type=int,
        default=default_max_deps,
        help=f"Maximum number of dependencies to include (default: {default_max_deps})",
    )
    parser.add_argument(
        "--print",
        action="store_true",
        help="Print the generated test to stdout",
    )
    parser.add_argument(
        "--enable-metainfo-db",
        action="store_true",
        help="Enable experimental metainfo DB flow (default: disabled)",
    )
    parser.add_argument(
        "--enable-reftest-parity",
        action="store_true",
        help="Enable experimental reftest parity flow (default: disabled)",
    )

    return parser
