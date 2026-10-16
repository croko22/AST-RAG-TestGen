"""
Modern CLI for AST-RAG TestGen using Typer.

This module provides the modern CLI interface with subcommands and auto-completion.
"""

from __future__ import annotations

import sys
from collections.abc import Callable
from typing import cast

# Typer for modern CLI
try:
    import typer
    from typer import Option as Opt

    TYPER_AVAILABLE = True
except ImportError:
    TYPER_AVAILABLE = False
    typer = None  # type: ignore
    Opt = None  # type: ignore

from output import get_output

# Hardcoded fallbacks, mirroring cli.parser.build_arg_parser.
_DEFAULT_PROVIDER = "anthropic"
_DEFAULT_MODEL = "claude-3-5-sonnet-20241022"
_DEFAULT_OUTPUT = "./tests_generados"
_DEFAULT_MAX_DEPS = 10
_DEFAULT_BENCHMARK_OUTPUT = "./benchmark_results"


def _config_defaults():
    """Resolve CLI defaults from the .env-backed config.

    Falls back to the hardcoded defaults when the config is unavailable, so a
    broken .env never breaks the CLI.

    Returns:
        Tuple of (provider, model, output, max_deps, benchmark_output).
    """
    provider = _DEFAULT_PROVIDER
    model = _DEFAULT_MODEL
    output = _DEFAULT_OUTPUT
    max_deps = _DEFAULT_MAX_DEPS
    benchmark_output = _DEFAULT_BENCHMARK_OUTPUT

    try:
        from config import get_config

        config = get_config()
        provider = config.llm.provider
        model = config.llm.model
        output = str(config.java.test_output_dir)
        max_deps = config.java.max_dependencies
        benchmark_output = str(config.benchmark.output_dir)
    except Exception:
        pass

    return provider, model, output, max_deps, benchmark_output


def create_modern_cli():
    """Create and return the modern Typer CLI application.

    Returns:
        Typer application instance or None if Typer is not available.
    """
    if not TYPER_AVAILABLE or typer is None:
        return None

    app = typer.Typer(
        name="ast-rag-testgen",
        help="Generate unit tests for Java using AST-based Retrieval-Augmented Generation",
        add_completion=True,
    )

    default_provider, default_model, default_output, default_max_deps, default_bench = (
        _config_defaults()
    )

    @app.command()
    def generate(
        java_file: str = typer.Argument(..., help="Path to the Java file to generate tests for"),
        project_path: str = typer.Argument(..., help="Root path of the Java project"),
        provider: str = Opt(
            default_provider,
            "--provider",
            "-p",
            help="LLM provider (anthropic, openai, glm, gemini, nvidia, openrouter)",
        ),
        model: str = Opt(default_model, "--model", "-m", help="LLM model to use"),
        output: str = Opt(
            default_output, "--output", "-o", help="Output directory for generated tests"
        ),
        max_deps: int = Opt(
            default_max_deps, "--max-deps", "-d", help="Maximum number of dependencies to include"
        ),
        print_code: bool = Opt(False, "--print", help="Print the generated test to stdout"),
    ) -> None:
        """Generate a unit test for a Java file."""

        output_manager = get_output()

        try:
            # Import orchestration modules
            from orchestration.generator import generate_test_for_file

            result = generate_test_for_file(
                java_file_path=java_file,
                java_project_path=project_path,
                output_dir=output,
                max_dependencies=max_deps,
                llm_provider=provider,
                llm_model=model,
            )

            if print_code:
                output_manager.print_code(result.test_code)

        except Exception as e:
            output_manager.print_error(f"Error: {e}")
            sys.exit(1)

    @app.command()
    def benchmark(
        manifest: str = typer.Argument(..., help="Path to benchmark manifest (JSON or TOML)"),
        output: str = Opt(
            default_bench, "--output", "-o", help="Output directory for benchmark results"
        ),
        dry_run: bool = Opt(False, "--dry-run", help="Run benchmark without actual generation"),
    ) -> None:
        """Run benchmarks using a manifest file."""
        from orchestration.benchmark import run_benchmark_mode

        exit_code = run_benchmark_mode(manifest, output, dry_run)
        sys.exit(exit_code)

    @app.command()
    def providers() -> None:
        """List available LLM providers and their default models."""
        try:
            from llm.client_new import get_available_providers, get_default_model
        except (ModuleNotFoundError, AttributeError):
            print("LLM symbols not available")
            return

        available_providers = cast("Callable[[], list[str]]", get_available_providers)()

        output_manager = get_output()

        output_manager.print_header("Available LLM Providers")

        for provider in available_providers:
            default_model = get_default_model(provider)
            output_manager.print_info(f"{provider}: {default_model}")

    @app.command()
    def thesis_report(
        output: str = Opt(
            "",
            "--output",
            "-o",
            help="Output path for the report (default: docs/thesis/results-package-v1.md)",
        ),
        latex: bool = Opt(False, "--latex", help="Also emit LaTeX table fragments"),
    ) -> None:
        """Generate thesis-ready results report from benchmark data."""
        try:
            from benchmark.thesis_report import generate_thesis_report

            out = generate_thesis_report(
                output_path=output if output else None,
                latex=latex,
            )
            print(f"\nReport written to: {out}")
        except SystemExit:
            raise
        except Exception as e:
            print(f"Error generating thesis report: {e}")
            sys.exit(1)

    @app.command()
    def serve(
        host: str = Opt("127.0.0.1", "--host", "-H", help="Host for HTTP transport"),
        port: int = Opt(8000, "--port", "-P", help="Port for HTTP transport"),
        transport: str = Opt("stdio", "--transport", "-t", help="Transport: stdio or http"),
    ) -> None:
        """Start the MCP server for editor/IDE integration."""
        from mcp_server.cli import run_serve

        sys.exit(run_serve(host=host, port=port, transport=transport))

    return app


def run_modern_cli() -> int:
    """Run the modern CLI and return exit code.

    Returns:
        Exit code (0 on success, 1 on failure).
    """
    app = create_modern_cli()
    if app is None:
        print("Error: Typer is not installed. Install with: pip install typer")
        return 1

    app()
    return 0
