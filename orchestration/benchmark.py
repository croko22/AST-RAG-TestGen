"""
Orchestration layer for benchmark mode.

This module provides the orchestration logic for running benchmarks,
separating business logic from CLI concerns.
"""

from __future__ import annotations

from output import get_output


def run_benchmark_mode(
    manifest_path: str,
    output_dir: str,
    dry_run: bool = False,
) -> int:
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

    output = get_output()

    output.print_header("AST-RAG Benchmark Mode")

    console = output.console
    if console:
        console.print(f"📋 Manifest: [cyan]{manifest_path}[/cyan]")
        console.print(f"📁 Output: [cyan]{output_dir}[/cyan]")
    else:
        print(f"\n📋 Manifest: {manifest_path}")
        print(f"📁 Output: {output_dir}")

    try:
        manifest = load_manifest(manifest_path)
        output.print_success(f"Manifest loaded (version {manifest.manifest_version})")
    except ManifestValidationError as e:
        output.print_error(f"Error loading manifest: {e}")
        return 1

    preflight_findings = validate_manifest_preflight(manifest)
    preflight_errors = [finding for finding in preflight_findings if finding.severity == "error"]
    preflight_warnings = [finding for finding in preflight_findings if finding.severity == "warning"]

    if preflight_warnings:
        output.print_info("\n⚠️ Preflight warnings:")
        for warning in preflight_warnings:
            output.print_info(f"    - [{warning.code}] {warning.message}")

    if preflight_errors:
        output.print_error("Preflight errors:")
        for error in preflight_errors:
            output.print_error(f"    - [{error.code}] {error.message}")
            output.print_error(f"      remediation: {error.remediation}")
        return 1

    output.print_info("\n[1/3] 📊 Planning runs...")
    plans = plan_runs(manifest)

    if console:
        console.print(f"    Planned [yellow]{len(plans)}[/yellow] runs")
    else:
        print(f"    Planned {len(plans)} runs")

    output.print_info("\n[2/3] ⚙️ Executing runs...")

    # Use progress bar if rich is available
    if output.use_rich and output.console:
        from rich.progress import (
            BarColumn,
            Progress,
            SpinnerColumn,
            TextColumn,
            TimeRemainingColumn,
        )

        with Progress(
            SpinnerColumn(),
            TextColumn("[progress.description]{task.description}"),
            BarColumn(),
            TextColumn("[progress.percentage]{task.percentage:>3.0f}%"),
            TimeRemainingColumn(),
        ) as progress:
            task = progress.add_task("Running benchmarks...", total=len(plans))

            results = []
            for plan in plans:
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

    output.print_info("\n[3/3] 📝 Generating reports...")
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
