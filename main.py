#!/usr/bin/env python3
"""
AST-RAG TestGen: Main orchestrator

This is the main entry point for the AST-based RAG test generation system.
It orchestrates the 4-step pipeline:
1. Extractor (Tree-sitter): Parse Java file and extract dependencies
2. RAG (Retriever): Find dependency files in the Java project
3. Slicer: Extract method signatures from dependencies
4. Prompt Builder: Assemble dynamic prompt and send to LLM

Supports two modes:
- Legacy mode: Generate test for a single Java file
- Benchmark mode: Run benchmarks using a manifest file
"""

import argparse
import sys
from collections.abc import Callable
from pathlib import Path
from typing import cast

# Rich for beautiful terminal output
try:
    from rich.console import Console
    from rich.progress import Progress, SpinnerColumn, TextColumn, BarColumn, TimeRemainingColumn
    from rich.syntax import Syntax
    from rich.text import Text
    RICH_AVAILABLE = True
except ImportError:
    RICH_AVAILABLE = False
    Console = None  # type: ignore

# Typer for modern CLI
try:
    import typer
    from typer import Option as Opt
    TYPER_AVAILABLE = True
except ImportError:
    TYPER_AVAILABLE = False
    typer = None  # type: ignore
    Opt = None  # type: ignore

# Backward-compatible patch targets for tests and external callers.
# Keep these lazy to avoid hard failures when optional LLM deps are missing.
LLMClient = None
LLMConfig = None
get_available_providers = None
get_default_model = None


def _ensure_llm_symbols() -> None:
    """Lazily populate main-module LLM symbols if available."""
    global LLMClient, LLMConfig, get_available_providers, get_default_model

    if LLMConfig is not None and get_available_providers is not None and get_default_model is not None:
        return

    from llm import client as llm_client_module

    if LLMClient is None:
        LLMClient = llm_client_module.LLMClient
    if LLMConfig is None:
        LLMConfig = llm_client_module.LLMConfig
    if get_available_providers is None:
        get_available_providers = llm_client_module.get_available_providers
    if get_default_model is None:
        get_default_model = llm_client_module.get_default_model


def get_console() -> "Console | None":
    """Get Rich console if available, otherwise None."""
    if RICH_AVAILABLE and Console is not None:
        return Console()
    return None


def print_header(title: str) -> None:
    """Print a formatted header."""
    console = get_console()
    if console:
        console.print(f"\n{'=' * 60}")
        console.print(f"[bold cyan]{title}[/bold cyan]")
        console.print(f"{'=' * 60}\n")
    else:
        print(f"\n{'=' * 60}")
        print(title)
        print(f"{'=' * 60}\n")


def print_success(message: str) -> None:
    """Print a success message."""
    console = get_console()
    if console:
        console.print(Text(f"✓ {message}", style="bold green"))
    else:
        print(f"✓ {message}")


def print_error(message: str) -> None:
    """Print an error message."""
    console = get_console()
    if console:
        # Rich Console doesn't support stderr parameter
        # Print to stderr manually if needed
        console.print(Text(f"✗ {message}", style="bold red"))
    else:
        print(f"✗ {message}", file=sys.stderr)


def print_info(message: str) -> None:
    """Print an info message."""
    console = get_console()
    if console:
        console.print(message)
    else:
        print(message)


def generate_test_for_file(
    java_file_path: str,
    java_project_path: str,
    output_dir: str = "./tests_generados",
    max_dependencies: int = 10,
    llm_provider: str = "anthropic",
    llm_model: str = "claude-3-5-sonnet-20241022",
    enable_metainfo_db: bool = False,
    enable_reftest_parity: bool = False,
) -> str:
    """
    Generate a unit test for a given Java file.

    Args:
        java_file_path: Path to the Java file to test
        java_project_path: Root path of the Java project
        output_dir: Directory to save generated tests
        max_dependencies: Maximum number of dependencies to include
        llm_provider: LLM provider (anthropic, openai, glm, gemini, openrouter)
        llm_model: Model to use
        enable_metainfo_db: Reserved feature flag for metainfo DB-backed flow
        enable_reftest_parity: Reserved feature flag for reftest parity flow

    Returns:
        Generated test code as string
    """
    print_header("AST-RAG TestGen")

    console = get_console()
    if console:
        console.print(f"📁 File: [cyan]{java_file_path}[/cyan]")
        console.print(f"📦 Project: [cyan]{java_project_path}[/cyan]")
        console.print(f"🤖 LLM: [cyan]{llm_provider}/{llm_model}[/cyan]")
    else:
        print(f"\n📁 File: {java_file_path}")
        print(f"📦 Project: {java_project_path}")
        print(f"🤖 LLM: {llm_provider}/{llm_model}")

    # Reserved feature flags for incremental rollout. Disabled by default and
    # intentionally no-op until their respective phases are implemented.
    _ = (enable_metainfo_db, enable_reftest_parity)

    # Step 1: Initialize retriever and resolver
    print_info("\n[1/4] 📥 Initializing retriever...")
    from core.prompt_builder import PromptBuilder
    from core.retriever import DependencyResolver, JavaFileRetriever

    retriever = JavaFileRetriever(java_project_path)
    resolver = DependencyResolver(retriever)
    prompt_builder = PromptBuilder(retriever, resolver)

    # Step 2: Parse the file and extract dependencies
    print_info("[2/4] 🔍 Parsing Java file and extracting dependencies...")
    parsed = retriever.parse_file(java_file_path)
    if not parsed:
        raise ValueError(f"Could not parse file: {java_file_path}")

    console = get_console()
    if console:
        console.print(f"    Class: [green]{parsed.name}[/green]")
        console.print(f"    Package: [green]{parsed.package}[/green]")
        console.print(f"    Methods: [yellow]{len(parsed.methods)}[/yellow]")
        console.print(f"    Dependencies: [yellow]{len(parsed.dependencies)}[/yellow]")
    else:
        print(f"    Class: {parsed.name}")
        print(f"    Package: {parsed.package}")
        print(f"    Methods: {len(parsed.methods)}")
        print(f"    Dependencies: {len(parsed.dependencies)}")

    # Step 3: Build prompt with context
    print_info(f"[3/4] 🧩 Building prompt with {max_dependencies} max dependencies...")
    code_under_test, dependency_context = prompt_builder.build_prompt(
        java_file_path, max_dependencies
    )

    if console:
        console.print(f"    Code length: [yellow]{len(code_under_test)}[/yellow] chars")
        console.print(f"    Context length: [yellow]{len(dependency_context)}[/yellow] chars")
    else:
        print(f"    Code length: {len(code_under_test)} chars")
        print(f"    Context length: {len(dependency_context)} chars")

    # Step 4: Generate test with LLM
    print_info("[4/4] 🤖 Generating test with LLM...")
    _ensure_llm_symbols()
    assert LLMConfig is not None
    assert LLMClient is not None

    llm_config = LLMConfig(
        provider=llm_provider,
        model=llm_model,
        temperature=0.3,
        max_tokens=4096,
    )
    llm_client = LLMClient(llm_config)
    test_code = llm_client.generate_test(code_under_test, dependency_context)

    # Save the generated test
    output_path = Path(output_dir)
    output_path.mkdir(parents=True, exist_ok=True)

    test_class_name = f"{parsed.name}Test.java"
    test_file_path = output_path / test_class_name

    # Clean up test code (remove markdown code blocks if present)
    test_code = test_code.strip()
    if test_code.startswith("```java"):
        test_code = test_code[7:]
    if test_code.startswith("```"):
        test_code = test_code[3:]
    if test_code.endswith("```"):
        test_code = test_code[:-3]
    test_code = test_code.strip()

    test_file_path.write_text(test_code, encoding="utf-8")
    print_success(f"Test generated: {test_file_path}")

    return test_code


def run_benchmark_mode(manifest_path: str, output_dir: str, dry_run: bool = False) -> int:
    """
    Run benchmark mode using a manifest file.

    Args:
        manifest_path: Path to benchmark manifest (JSON or TOML)
        output_dir: Directory for benchmark outputs
        dry_run: If True, skip actual generation

    Returns:
        Exit code (0 on success, 1 on failure)
    """
    from benchmark.manifest import (
        ManifestValidationError,
        load_manifest,
        validate_manifest_preflight,
    )
    from benchmark.planner import plan_runs
    from benchmark.reporter import build_report, export_thesis_metrics_csv
    from benchmark.runner import execute_runs

    print_header("AST-RAG Benchmark Mode")

    console = get_console()
    if console:
        console.print(f"📋 Manifest: [cyan]{manifest_path}[/cyan]")
        console.print(f"📁 Output: [cyan]{output_dir}[/cyan]")
    else:
        print(f"\n📋 Manifest: {manifest_path}")
        print(f"📁 Output: {output_dir}")

    try:
        manifest = load_manifest(manifest_path)
        print_success(f"Manifest loaded (version {manifest.manifest_version})")
    except ManifestValidationError as e:
        print_error(f"Error loading manifest: {e}")
        return 1

    preflight_findings = validate_manifest_preflight(manifest)
    preflight_errors = [finding for finding in preflight_findings if finding.severity == "error"]
    preflight_warnings = [finding for finding in preflight_findings if finding.severity == "warning"]

    if preflight_warnings:
        print_info("\n⚠️ Preflight warnings:")
        for warning in preflight_warnings:
            print_info(f"    - [{warning.code}] {warning.message}")

    if preflight_errors:
        print_error("Preflight errors:")
        for error in preflight_errors:
            print_error(f"    - [{error.code}] {error.message}")
            print_error(f"      remediation: {error.remediation}")
        return 1

    print_info("\n[1/3] 📊 Planning runs...")
    plans = plan_runs(manifest)

    if console:
        console.print(f"    Planned [yellow]{len(plans)}[/yellow] runs")
    else:
        print(f"    Planned {len(plans)} runs")

    print_info("\n[2/3] ⚙️ Executing runs...")

    # Use progress bar if rich is available
    if RICH_AVAILABLE and Progress is not None:
        with Progress(
            SpinnerColumn(),
            TextColumn("[progress.description]{task.description}"),
            BarColumn(),
            TextColumn("[progress.percentage]{task.percentage:>3.0f}%"),
            TimeRemainingColumn(),
        ) as progress:
            task = progress.add_task("Running benchmarks...", total=len(plans))

            results = []
            for i, plan in enumerate(plans):
                result = execute_runs(
                    [plan],
                    output_dir,
                    dry_run=dry_run,
                    eval_config=manifest.evaluation,
                )
                results.extend(result)
                progress.update(task, advance=1)
    else:
        results = execute_runs(
            plans,
            output_dir,
            dry_run=dry_run,
            eval_config=manifest.evaluation,
        )

    success_count = sum(1 for r in results if r.status == "ok")

    if console:
        console.print(f"    Completed: [green]{success_count}/{len(results)}[/green] successful")
    else:
        print(f"    Completed: {success_count}/{len(results)} successful")

    print_info("\n[3/3] 📝 Generating reports...")
    bundle = build_report(
        results,
        manifest,
        output_dir,
        preflight_findings=preflight_findings,
    )

    if console:
        console.print(f"    Results: [cyan]{bundle.results_path}[/cyan]")
        console.print(f"    Summary: [cyan]{bundle.summary_path}[/cyan]")
    else:
        print(f"    Results: {bundle.results_path}")
        print(f"    Summary: {bundle.summary_path}")
    print(f"    Report: {bundle.report_path}")
    print(f"    Provenance: {bundle.provenance_path}")

    csv_path = export_thesis_metrics_csv(results, output_dir)
    print(f"    Thesis Metrics: {csv_path}")

    print("\n✨ Benchmark complete!")
    return 0


def legacy_main(args: argparse.Namespace) -> None:
    """
    Execute the legacy single-file generation mode.

    This preserves the original behavior for backward compatibility.

    Args:
        args: Parsed arguments from legacy CLI
    """
    if not Path(args.java_file).exists():
        print(f"Error: Java file not found: {args.java_file}", file=sys.stderr)
        sys.exit(1)

    if not Path(args.project_path).exists():
        print(f"Error: Project path not found: {args.project_path}", file=sys.stderr)
        sys.exit(1)

    try:
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
            print("\n" + "=" * 60)
            print("GENERATED TEST:")
            print("=" * 60)
            print(test_code)

        print(f"\n✨ Done! Test saved to {args.output}/{Path(args.java_file).stem}Test.java")

    except Exception as e:
        print(f"\n❌ Error: {e}", file=sys.stderr)
        import traceback

        traceback.print_exc()
        sys.exit(1)


def main():
    """Main entry point."""
    parser = build_arg_parser()
    args = parser.parse_args()

    if getattr(args, "benchmark_manifest", None):
        return run_benchmark_mode(
            manifest_path=args.benchmark_manifest,
            output_dir=args.benchmark_output,
            dry_run=getattr(args, "benchmark_dry_run", False),
        )

    legacy_main(args)


def build_arg_parser() -> argparse.ArgumentParser:
    """Build command-line argument parser."""
    try:
        _ensure_llm_symbols()
        available_providers_fn = cast(Callable[[], list[str]], get_available_providers)
        available_providers = available_providers_fn()
    except ModuleNotFoundError:
        available_providers = ["anthropic", "openai", "glm", "gemini", "openrouter", "nvidia"]

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
  python main.py service.java project/ --provider glm --model glm-4-plus

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
        default="./benchmark_results",
        help="Output directory for benchmark results (default: ./benchmark_results)",
    )
    parser.add_argument(
        "--benchmark-dry-run",
        dest="benchmark_dry_run",
        action="store_true",
        help="Run benchmark without actual generation (for testing)",
    )

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
        default="anthropic",
        choices=available_providers,
        help=f"LLM provider. Available: {', '.join(available_providers)} (default: anthropic)",
    )
    parser.add_argument(
        "--model",
        default="claude-3-5-sonnet-20241022",
        help="LLM model to use (default: claude-3-5-sonnet-20241022)",
    )
    parser.add_argument(
        "--output",
        default="./tests_generados",
        help="Output directory for generated tests (default: ./tests_generados)",
    )
    parser.add_argument(
        "--max-deps",
        type=int,
        default=10,
        help="Maximum number of dependencies to include (default: 10)",
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


# ============================================================================
# Modern CLI with Typer
# ============================================================================

if TYPER_AVAILABLE and typer is not None:
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
        try:
            test_code = generate_test_for_file(
                java_file_path=java_file,
                java_project_path=project_path,
                output_dir=output,
                max_dependencies=max_deps,
                llm_provider=provider,
                llm_model=model,
            )

            if print_code:
                console = get_console()
                if console and Syntax is not None:
                    syntax = Syntax(test_code, "java", theme="monokai", line_numbers=True)
                    console.print(syntax)
                else:
                    print(test_code)

        except Exception as e:
            console = get_console()
            if console:
                console.print(f"Error: {e}", style="bold red")
            else:
                print(f"Error: {e}", file=sys.stderr)
            sys.exit(1)

    @app.command()
    def benchmark(
        manifest: str = typer.Argument(..., help="Path to benchmark manifest (JSON or TOML)"),
        output: str = Opt("./benchmark_results", "--output", "-o", help="Output directory for benchmark results"),
        dry_run: bool = Opt(False, "--dry-run", help="Run benchmark without actual generation"),
    ) -> None:
        """Run benchmarks using a manifest file."""
        exit_code = run_benchmark_mode(manifest, output, dry_run)
        sys.exit(exit_code)

    @app.command()
    def providers() -> None:
        """List available LLM providers and their default models."""
        _ensure_llm_symbols()
        if get_available_providers is None:
            print("LLM symbols not available")
            return

        available_providers = cast(Callable[[], list[str]], get_available_providers)()

        console = get_console()
        if console:
            from rich.table import Table

            table = Table(title="Available LLM Providers")
            table.add_column("Provider", style="cyan")
            table.add_column("Default Model", style="green")

            for provider in available_providers:
                default_model = get_default_model(provider) if get_default_model else "N/A"
                table.add_row(provider, default_model)

            console.print(table)
        else:
            print("Available LLM Providers:")
            for provider in available_providers:
                default_model = get_default_model(provider) if get_default_model else "N/A"
                print(f"  {provider}: {default_model}")


if __name__ == "__main__":
    # Use typer if available, otherwise fall back to argparse
    if TYPER_AVAILABLE and typer is not None:
        app()
    else:
        main()
