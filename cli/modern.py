"""
Modern CLI for AST-RAG TestGen using Typer.

This module provides the modern CLI interface with subcommands and auto-completion.
"""

from __future__ import annotations

import sys
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

    @app.command()
    def generate(
        java_file: str = typer.Argument(..., help="Path to the Java file to generate tests for"),
        project_path: str = typer.Argument(..., help="Root path of the Java project"),
        provider: str = Opt("anthropic", "--provider", "-p", help="LLM provider (anthropic, openai, glm, gemini, nvidia, openrouter)"),
        model: str = Opt("claude-3-5-sonnet-20241022", "--model", "-m", help="LLM model to use"),
        output: str = Opt("./tests_generados", "--output", "-o", help="Output directory for generated tests"),
        max_deps: int = Opt(10, "--max-deps", "-d", help="Maximum number of dependencies to include"),
        print_code: bool = Opt(False, "--print", help="Print the generated test to stdout"),
    ) -> None:
        """Generate a unit test for a Java file."""
        from pathlib import Path

        output_manager = get_output()

        try:
            # Import orchestration modules
            from orchestration.generator import generate_test_for_file

            test_code = generate_test_for_file(
                java_file_path=java_file,
                java_project_path=project_path,
                output_dir=output,
                max_dependencies=max_deps,
                llm_provider=provider,
                llm_model=model,
            )

            if print_code:
                output_manager.print_code(test_code)

        except Exception as e:
            output_manager.print_error(f"Error: {e}")
            sys.exit(1)

    @app.command()
    def benchmark(
        manifest: str = typer.Argument(..., help="Path to benchmark manifest (JSON or TOML)"),
        output: str = Opt("./benchmark_results", "--output", "-o", help="Output directory for benchmark results"),
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
            from llm import client as llm_client_module

            get_available_providers = llm_client_module.get_available_providers
            get_default_model = llm_client_module.get_default_model
        except (ModuleNotFoundError, AttributeError):
            print("LLM symbols not available")
            return

        available_providers = cast(
            "Callable[[], list[str]]", get_available_providers
        )()

        output_manager = get_output()

        output_manager.print_header("Available LLM Providers")

        for provider in available_providers:
            default_model = get_default_model(provider)
            output_manager.print_info(f"{provider}: {default_model}")

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
