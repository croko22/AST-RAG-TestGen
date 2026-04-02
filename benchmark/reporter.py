"""Benchmark reporter: generates results.json, summary.json, and report.md."""

from __future__ import annotations

import csv
import json
import subprocess
from datetime import UTC, datetime
from pathlib import Path
from typing import Any

from benchmark.schemas import BenchmarkManifest, ScoringConfig
from benchmark.types import PreflightFinding, ProvenanceRecord, RunResult


class ReportBundle:
    """Container for all report outputs."""

    def __init__(
        self,
        results_path: Path,
        summary_path: Path,
        report_path: Path,
        provenance_path: Path,
    ) -> None:
        self.results_path = results_path
        self.summary_path = summary_path
        self.report_path = report_path
        self.provenance_path = provenance_path


def build_report(
    results: list[RunResult],
    manifest: BenchmarkManifest,
    output_dir: Path | str,
    preflight_findings: list[PreflightFinding] | None = None,
) -> ReportBundle:
    """
    Generate complete benchmark report bundle.

    Creates three files:
    - results.json: All run results in machine-readable format
    - summary.json: Aggregated statistics and ranked entries
    - report.md: Human-readable summary with tables

    Args:
        results: List of all run results
        manifest: Original benchmark manifest for config
        output_dir: Directory to write reports

    Returns:
        ReportBundle with paths to generated files
    """
    output_path = Path(output_dir)
    output_path.mkdir(parents=True, exist_ok=True)

    findings = preflight_findings or []
    provenance_records = collect_provenance_records(manifest)
    provenance_data = _build_provenance_json(manifest, provenance_records)
    provenance_path = output_path / "provenance.json"
    provenance_path.write_text(json.dumps(provenance_data, indent=2), encoding="utf-8")

    results_data = _build_results_json(results, manifest, findings, provenance_path, provenance_records)
    results_path = output_path / "results.json"
    results_path.write_text(json.dumps(results_data, indent=2), encoding="utf-8")

    summary_data = _build_summary_json(results, manifest, findings, provenance_path, provenance_records)
    summary_path = output_path / "summary.json"
    summary_path.write_text(json.dumps(summary_data, indent=2), encoding="utf-8")

    report_md = _build_markdown_report(results, manifest, summary_data)
    report_path = output_path / "report.md"
    report_path.write_text(report_md, encoding="utf-8")

    return ReportBundle(
        results_path=results_path,
        summary_path=summary_path,
        report_path=report_path,
        provenance_path=provenance_path,
    )


def _build_results_json(
    results: list[RunResult],
    manifest: BenchmarkManifest,
    preflight_findings: list[PreflightFinding],
    provenance_path: Path,
    provenance_records: list[ProvenanceRecord],
) -> dict[str, Any]:
    """Build machine-readable results JSON."""
    return {
        "generated_at": _utc_iso_z(),
        "manifest_version": manifest.manifest_version,
        "total_runs": len(results),
        "runs": [_serialize_run_result(r) for r in results],
        "metadata": {
            "preflight": _serialize_preflight(preflight_findings),
            "diagnostics": _compute_diagnostics(results, preflight_findings),
            "provenance": {
                "path": str(provenance_path),
                "status_counts": _count_provenance_statuses(provenance_records),
            },
        },
    }


def _serialize_run_result(result: RunResult) -> dict[str, Any]:
    """Serialize a single RunResult to JSON-compatible dict."""
    return {
        "run_id": result.run_id,
        "status": result.status,
        "latency_ms": result.latency_ms,
        "metrics": {
            "compile_pass": result.metrics.compile_pass,
            "test_pass": result.metrics.test_pass,
            "coverage_pct": result.metrics.coverage_pct,
            "coverage_source": result.metrics.coverage_source,
            "coverage_reason": result.metrics.coverage_reason,
            "failure_type": result.metrics.failure_type,
            "failure_message": result.metrics.failure_message,
        },
        "output_path": result.output_path,
        "provider": result.provider,
        "model": result.model,
        "dataset_id": result.dataset_id,
        "trial": result.trial,
        "config_snapshot": result.config_snapshot,
    }


def _build_summary_json(
    results: list[RunResult],
    manifest: BenchmarkManifest,
    preflight_findings: list[PreflightFinding] | None = None,
    provenance_path: Path | None = None,
    provenance_records: list[ProvenanceRecord] | None = None,
) -> dict[str, Any]:
    """Build summary JSON with aggregated statistics and rankings."""
    stats = _compute_statistics(results)
    rankings = _compute_rankings(results, manifest.scoring)

    findings = preflight_findings or []
    records = provenance_records or []

    return {
        "generated_at": _utc_iso_z(),
        "statistics": stats,
        "rankings": rankings,
        "scoring_weights": manifest.scoring.weights.model_dump(),
        "diagnostics": _compute_diagnostics(results, findings),
        "preflight": _serialize_preflight(findings),
        "provenance": {
            "path": str(provenance_path) if provenance_path else None,
            "status_counts": _count_provenance_statuses(records),
        },
    }


def _compute_diagnostics(
    results: list[RunResult],
    preflight_findings: list[PreflightFinding],
) -> dict[str, int]:
    """Compute campaign diagnostics for fidelity hardening surfacing."""
    return {
        "preflight_warning_count": sum(1 for f in preflight_findings if f.severity == "warning"),
        "coverage_fallback_count": sum(1 for r in results if r.metrics.coverage_source == "stdout_regex"),
        "coverage_unavailable_count": sum(1 for r in results if r.metrics.coverage_reason == "coverage_unavailable"),
    }


def _serialize_preflight(findings: list[PreflightFinding]) -> dict[str, Any]:
    """Serialize preflight findings with split severities."""
    warnings = [f for f in findings if f.severity == "warning"]
    errors = [f for f in findings if f.severity == "error"]
    return {
        "warning_count": len(warnings),
        "error_count": len(errors),
        "findings": [
            {
                "code": f.code,
                "severity": f.severity,
                "manifest_path": f.manifest_path,
                "message": f.message,
                "remediation": f.remediation,
            }
            for f in findings
        ],
    }


def _count_provenance_statuses(records: list[ProvenanceRecord]) -> dict[str, int]:
    """Count provenance record statuses."""
    ok_count = sum(1 for record in records if record.status == "ok")
    unavailable_count = sum(1 for record in records if record.status == "unavailable")
    return {
        "ok": ok_count,
        "unavailable": unavailable_count,
        "total": len(records),
    }


def _build_provenance_json(
    manifest: BenchmarkManifest,
    records: list[ProvenanceRecord],
) -> dict[str, Any]:
    """Build campaign-level provenance sidecar payload."""
    return {
        "generated_at": _utc_iso_z(),
        "manifest_version": manifest.manifest_version,
        "records": [
            {
                "repo_id": record.repo_id,
                "repo_path": record.repo_path,
                "requested_ref": record.requested_ref,
                "resolved_commit": record.resolved_commit,
                "branch": record.branch,
                "dirty": record.dirty,
                "status": record.status,
                "reason": record.reason,
                "captured_at": record.captured_at,
            }
            for record in records
        ],
    }


def collect_provenance_records(manifest: BenchmarkManifest) -> list[ProvenanceRecord]:
    """Capture git provenance for each dataset target repository path."""
    project_root = Path(manifest.project_root)
    records: list[ProvenanceRecord] = []

    for dataset in manifest.dataset:
        java_file_path = project_root / dataset.java_file
        repo_path = _resolve_repo_root(java_file_path)
        captured_at = _utc_iso_z()

        if repo_path is None:
            records.append(
                ProvenanceRecord(
                    repo_id=dataset.id,
                    repo_path=str(java_file_path.parent),
                    requested_ref=None,
                    resolved_commit=None,
                    branch=None,
                    dirty=None,
                    status="unavailable",
                    reason="PROVENANCE_NOT_GIT_REPO",
                    captured_at=captured_at,
                )
            )
            continue

        commit = _git_value(repo_path, ["rev-parse", "HEAD"])
        branch = _git_value(repo_path, ["rev-parse", "--abbrev-ref", "HEAD"])
        dirty = _git_is_dirty(repo_path)

        if commit is None:
            records.append(
                ProvenanceRecord(
                    repo_id=dataset.id,
                    repo_path=str(repo_path),
                    requested_ref=None,
                    resolved_commit=None,
                    branch=branch,
                    dirty=dirty,
                    status="unavailable",
                    reason="PROVENANCE_GIT_RESOLUTION_FAILED",
                    captured_at=captured_at,
                )
            )
            continue

        records.append(
            ProvenanceRecord(
                repo_id=dataset.id,
                repo_path=str(repo_path),
                requested_ref=None,
                resolved_commit=commit,
                branch=branch,
                dirty=dirty,
                status="ok",
                reason=None,
                captured_at=captured_at,
            )
        )

    return records


def _resolve_repo_root(path: Path) -> Path | None:
    """Resolve nearest git repository root for a path."""
    current = path if path.is_dir() else path.parent
    for candidate in (current, *current.parents):
        if (candidate / ".git").exists():
            return candidate
    return None


def _git_value(repo_root: Path, args: list[str]) -> str | None:
    """Return git command stdout value or None."""
    try:
        proc = subprocess.run(
            ["git", *args],
            cwd=str(repo_root),
            capture_output=True,
            text=True,
            timeout=5,
            check=False,
        )
    except (OSError, subprocess.TimeoutExpired):
        return None

    if proc.returncode != 0:
        return None
    value = proc.stdout.strip()
    return value or None


def _git_is_dirty(repo_root: Path) -> bool | None:
    """Return dirty state or None when git status cannot be resolved."""
    try:
        proc = subprocess.run(
            ["git", "status", "--porcelain"],
            cwd=str(repo_root),
            capture_output=True,
            text=True,
            timeout=5,
            check=False,
        )
    except (OSError, subprocess.TimeoutExpired):
        return None

    if proc.returncode != 0:
        return None
    return bool(proc.stdout.strip())


def _compute_statistics(results: list[RunResult]) -> dict[str, Any]:
    """Compute aggregate statistics from results."""
    total = len(results)
    if total == 0:
        return {
            "total_runs": 0,
            "success_count": 0,
            "success_rate": 0.0,
            "timeout_count": 0,
            "error_count": 0,
            "avg_latency_ms": 0,
        }

    success_count = sum(1 for r in results if r.status == "ok")
    timeout_count = sum(1 for r in results if r.status == "timeout")
    error_count = sum(1 for r in results if r.status == "error")

    latencies = [r.latency_ms for r in results if r.status == "ok"]
    avg_latency = sum(latencies) / len(latencies) if latencies else 0

    return {
        "total_runs": total,
        "success_count": success_count,
        "success_rate": success_count / total,
        "timeout_count": timeout_count,
        "error_count": error_count,
        "avg_latency_ms": int(avg_latency),
    }


def _compute_rankings(
    results: list[RunResult],
    scoring: ScoringConfig,
) -> list[dict[str, Any]]:
    """Compute ranked entries by provider/model combinations."""
    weights = scoring.weights

    entry_scores: dict[tuple[str, str], list[float]] = {}

    for result in results:
        if result.status != "ok":
            continue

        key = (result.provider, result.model)
        if key not in entry_scores:
            entry_scores[key] = []

        score = _compute_entry_score(result, weights)
        entry_scores[key].append(score)

    ranked_entries = []
    for (provider, model), scores in entry_scores.items():
        avg_score = sum(scores) / len(scores)
        run_count = len(scores)
        success_count = sum(
            1 for r in results
            if r.provider == provider and r.model == model and r.status == "ok"
        )

        ranked_entries.append({
            "provider": provider,
            "model": model,
            "avg_score": round(avg_score, 4),
            "run_count": run_count,
            "success_count": success_count,
            "success_rate": success_count / run_count if run_count > 0 else 0.0,
        })

    ranked_entries.sort(key=lambda e: (-e["avg_score"], -e["success_rate"]))

    for i, entry in enumerate(ranked_entries, 1):
        entry["rank"] = i

    return ranked_entries


def _compute_entry_score(
    result: RunResult,
    weights: "benchmark.schemas.ScoringWeights",
) -> float:
    """
    Compute weighted score for a single run result.

    Score formula:
    - success component: (compile_pass ? 0.5 : 0) + (test_pass ? 0.5 : 0)
    - coverage component: coverage_pct / 100.0 if available
    - latency component: normalized inverse (lower is better)

    Final score = success * w_success + coverage * w_coverage + latency * w_latency

    Args:
        result: Run result to score
        weights: Scoring weights from manifest

    Returns:
        Weighted score [0.0, 1.0]
    """
    success_score = 0.0
    if result.metrics.compile_pass:
        success_score += 0.5
    if result.metrics.test_pass:
        success_score += 0.5

    coverage_score = 0.0
    if result.metrics.coverage_pct is not None:
        coverage_score = result.metrics.coverage_pct / 100.0

    latency_score = 0.0
    if result.latency_ms > 0:
        max_latency = 300000
        latency_score = 1.0 - min(result.latency_ms / max_latency, 1.0)

    total_weight = weights.success + weights.coverage + weights.latency
    if total_weight == 0:
        return success_score

    weighted = (
        (success_score * weights.success)
        + (coverage_score * weights.coverage)
        + (latency_score * weights.latency)
    ) / total_weight

    return weighted


def _build_markdown_report(
    results: list[RunResult],
    manifest: BenchmarkManifest,
    summary: dict[str, Any],
) -> str:
    """Build human-readable Markdown report."""
    lines = [
        "# Benchmark Report",
        "",
        f"**Generated:** {_utc_now().strftime('%Y-%m-%d %H:%M:%S')} UTC",
        f"**Manifest Version:** {manifest.manifest_version}",
        "",
        "## Summary Statistics",
        "",
    ]

    stats = summary.get("statistics", {})
    lines.extend([
        f"- **Total Runs:** {stats.get('total_runs', 0)}",
        f"- **Successful:** {stats.get('success_count', 0)} ({stats.get('success_rate', 0):.1%})",
        f"- **Timeouts:** {stats.get('timeout_count', 0)}",
        f"- **Errors:** {stats.get('error_count', 0)}",
        f"- **Avg Latency:** {stats.get('avg_latency_ms', 0):,}ms",
        "",
    ])

    rankings = summary.get("rankings", [])
    if rankings:
        lines.extend([
            "## Rankings",
            "",
            "| Rank | Provider | Model | Avg Score | Success Rate | Runs |",
            "|------|----------|-------|-----------|--------------|------|",
        ])
        for entry in rankings:
            lines.append(
                f"| {entry['rank']} | {entry['provider']} | "
                f"{entry['model']} | {entry['avg_score']:.4f} | "
                f"{entry['success_rate']:.1%} | {entry['run_count']} |"
            )
        lines.append("")

    lines.extend([
        "## Run Details",
        "",
        "| Run ID | Provider | Model | Dataset | Status | Latency |",
        "|--------|----------|-------|---------|--------|---------|",
    ])
    for result in results:
        status_emoji = {
            "ok": "✓",
            "timeout": "⏱",
            "error": "✗",
        }.get(result.status, "?")
        lines.append(
            f"| {result.run_id} | {result.provider} | {result.model} | "
            f"{result.dataset_id} | {status_emoji} {result.status} | "
            f"{result.latency_ms:,}ms |"
        )
    lines.append("")

    return "\n".join(lines)


def _utc_now() -> datetime:
    """Return timezone-aware UTC datetime."""
    return datetime.now(UTC)


def _utc_iso_z() -> str:
    """Serialize current UTC datetime with a trailing Z suffix."""
    return _utc_now().isoformat().replace("+00:00", "Z")


def load_results_from_dir(
    output_dir: Path | str,
) -> list[RunResult]:
    """
    Load run results from a benchmark output directory.

    Reads individual run/result.json files and aggregates them.

    Args:
        output_dir: Directory containing run subdirectories

    Returns:
        List of RunResult objects
    """
    output_path = Path(output_dir)
    results: list[RunResult] = []

    if not output_path.exists():
        return results

    for run_dir in output_path.iterdir():
        if not run_dir.is_dir():
            continue

        result_file = run_dir / "result.json"
        if not result_file.exists():
            continue

        try:
            data = json.loads(result_file.read_text(encoding="utf-8"))
            result = _deserialize_run_result(data)
            results.append(result)
        except (json.JSONDecodeError, KeyError):
            continue

    return results


def _deserialize_run_result(data: dict[str, Any]) -> RunResult:
    """Deserialize a JSON dict back to RunResult."""
    from benchmark.types import EvalMetrics

    metrics_data = data.get("metrics", {})
    metrics = EvalMetrics(
        compile_pass=metrics_data.get("compile_pass", False),
        test_pass=metrics_data.get("test_pass", False),
        coverage_pct=metrics_data.get("coverage_pct"),
        coverage_source=metrics_data.get("coverage_source"),
        coverage_reason=metrics_data.get("coverage_reason"),
        failure_type=metrics_data.get("failure_type"),
        failure_message=metrics_data.get("failure_message"),
    )

    return RunResult(
        run_id=data["run_id"],
        status=data["status"],
        latency_ms=data["latency_ms"],
        metrics=metrics,
        output_path=data.get("output_path", ""),
        provider=data["provider"],
        model=data["model"],
        dataset_id=data["dataset_id"],
        trial=data["trial"],
        config_snapshot=data.get("config_snapshot", {}),
    )


def export_thesis_metrics_csv(
    results: list[RunResult],
    output_dir: Path | str,
    filename: str = "thesis_metrics.csv",
) -> Path:
    """
    Export thesis-ready metrics in CSV format.

    Creates a CSV file with columns:
    - model: provider/model combination
    - success_rate: proportion of successful runs
    - avg_latency_sec: average latency in seconds
    - coverage_pct: average coverage percentage
    - score: weighted average score

    Args:
        results: List of run results
        output_dir: Directory to write CSV file
        filename: Output filename (default: thesis_metrics.csv)

    Returns:
        Path to created CSV file
    """
    output_path = Path(output_dir)
    output_path.mkdir(parents=True, exist_ok=True)

    entry_stats: dict[tuple[str, str], dict[str, Any]] = {}

    for result in results:
        key = (result.provider, result.model)
        if key not in entry_stats:
            entry_stats[key] = {
                "success_count": 0,
                "total_count": 0,
                "latency_sum": 0,
                "latency_count": 0,
                "coverage_sum": 0.0,
                "coverage_count": 0,
                "scores": [],
            }

        stats = entry_stats[key]
        stats["total_count"] += 1

        if result.status == "ok":
            stats["success_count"] += 1

        if result.latency_ms > 0:
            stats["latency_sum"] += result.latency_ms
            stats["latency_count"] += 1

        if result.metrics.coverage_pct is not None:
            stats["coverage_sum"] += result.metrics.coverage_pct
            stats["coverage_count"] += 1

    csv_path = output_path / filename

    with csv_path.open("w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerow([
            "model",
            "success_rate",
            "avg_latency_sec",
            "coverage_pct",
            "score",
        ])

        for (provider, model), stats in entry_stats.items():
            model_str = f"{provider}/{model}"
            success_rate = (
                stats["success_count"] / stats["total_count"]
                if stats["total_count"] > 0 else 0.0
            )
            avg_latency_sec = (
                (stats["latency_sum"] / stats["latency_count"]) / 1000.0
                if stats["latency_count"] > 0 else 0.0
            )
            coverage_pct = (
                stats["coverage_sum"] / stats["coverage_count"]
                if stats["coverage_count"] > 0 else 0.0
            )

            success_score = stats["success_count"] / stats["total_count"] if stats["total_count"] > 0 else 0.0
            coverage_score = coverage_pct / 100.0
            latency_score = 1.0 - min(avg_latency_sec / 300.0, 1.0)
            score = (success_score * 0.5) + (coverage_score * 0.3) + (latency_score * 0.2)

            writer.writerow([
                model_str,
                f"{success_rate:.4f}",
                f"{avg_latency_sec:.2f}",
                f"{coverage_pct:.2f}",
                f"{score:.4f}",
            ])

    return csv_path
