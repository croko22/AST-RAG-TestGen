"""Benchmark reporter: generates results.json, summary.json, and report.md."""

from __future__ import annotations

import csv
import json
import subprocess
from datetime import UTC, datetime
from pathlib import Path
from typing import Any

from benchmark.schemas import BenchmarkManifest, ScoringConfig, ScoringWeights
from benchmark.types import (
    EvalMetrics,
    PipelineTimings,
    PreflightFinding,
    ProvenanceRecord,
    RunResult,
)


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
    output_path = Path(output_dir)
    output_path.mkdir(parents=True, exist_ok=True)

    findings = preflight_findings or []
    provenance_records = collect_provenance_records(manifest)
    provenance_data = _build_provenance_json(manifest, provenance_records)
    provenance_path = output_path / "provenance.json"
    provenance_path.write_text(json.dumps(provenance_data, indent=2), encoding="utf-8")

    results_data = _build_results_json(
        results, manifest, findings, provenance_path, provenance_records
    )
    results_path = output_path / "results.json"
    results_path.write_text(json.dumps(results_data, indent=2), encoding="utf-8")

    summary_data = _build_summary_json(
        results, manifest, findings, provenance_path, provenance_records
    )
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


def _serialize_metrics(metrics: EvalMetrics) -> dict[str, Any]:
    return {
        "compile_pass": metrics.compile_pass,
        "test_pass": metrics.test_pass,
        "coverage_pct": metrics.coverage_pct,
        "branch_coverage_pct": metrics.branch_coverage_pct,
        "coverage_source": metrics.coverage_source,
        "coverage_reason": metrics.coverage_reason,
        "failure_type": metrics.failure_type,
        "failure_message": metrics.failure_message,
        "generation_time_ms": metrics.generation_time_ms,
        "assertion_count": metrics.assertion_count,
        "trivial_flag": metrics.trivial_flag,
        "test_count": metrics.test_count,
        "quality_score": metrics.quality_score,
        "mutation_score_pct": metrics.mutation_score_pct,
        "killed_mutations": metrics.killed_mutations,
        "total_mutations": metrics.total_mutations,
        "timings": {
            "parse_ms": metrics.timings.parse_ms,
            "retrieval_ms": metrics.timings.retrieval_ms,
            "prompt_ms": metrics.timings.prompt_ms,
            "llm_ms": metrics.timings.llm_ms,
            "postproc_ms": metrics.timings.postproc_ms,
        },
    }


def _serialize_run_result(result: RunResult) -> dict[str, Any]:
    return {
        "run_id": result.run_id,
        "status": result.status,
        "latency_ms": result.latency_ms,
        "metrics": _serialize_metrics(result.metrics),
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
    return {
        "preflight_warning_count": sum(1 for f in preflight_findings if f.severity == "warning"),
        "coverage_fallback_count": sum(
            1 for r in results if r.metrics.coverage_source == "stdout_regex"
        ),
        "coverage_unavailable_count": sum(
            1 for r in results if r.metrics.coverage_reason == "coverage_unavailable"
        ),
        "trivial_test_count": sum(1 for r in results if r.metrics.trivial_flag),
    }


def _serialize_preflight(findings: list[PreflightFinding]) -> dict[str, Any]:
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
    current = path if path.is_dir() else path.parent
    for candidate in (current, *current.parents):
        if (candidate / ".git").exists():
            return candidate
    return None


def _git_value(repo_root: Path, args: list[str]) -> str | None:
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
    total = len(results)
    if total == 0:
        return {
            "total_runs": 0,
            "success_count": 0,
            "success_rate": 0.0,
            "timeout_count": 0,
            "error_count": 0,
            "avg_latency_ms": 0,
            "avg_coverage_pct": 0.0,
            "avg_branch_coverage_pct": 0.0,
            "avg_quality_score": 0.0,
            "trivial_rate": 0.0,
            "avg_mutation_score_pct": 0.0,
            "mutation_data_count": 0,
        }

    success_count = sum(1 for r in results if r.status == "ok")
    timeout_count = sum(1 for r in results if r.status == "timeout")
    error_count = sum(1 for r in results if r.status == "error")

    latencies = [r.latency_ms for r in results if r.status == "ok"]
    avg_latency = sum(latencies) / len(latencies) if latencies else 0

    coverages = [r.metrics.coverage_pct for r in results if r.metrics.coverage_pct is not None]
    avg_coverage = sum(coverages) / len(coverages) if coverages else 0.0

    branch_coverages = [
        r.metrics.branch_coverage_pct for r in results if r.metrics.branch_coverage_pct is not None
    ]
    avg_branch = sum(branch_coverages) / len(branch_coverages) if branch_coverages else 0.0

    quality_scores = [r.metrics.quality_score for r in results if r.status == "ok"]
    avg_quality = sum(quality_scores) / len(quality_scores) if quality_scores else 0.0

    trivial_count = sum(1 for r in results if r.metrics.trivial_flag)

    mutation_scores = [
        r.metrics.mutation_score_pct for r in results if r.metrics.mutation_score_pct is not None
    ]
    avg_mutation = sum(mutation_scores) / len(mutation_scores) if mutation_scores else 0.0
    mutation_count = len(mutation_scores)

    return {
        "total_runs": total,
        "success_count": success_count,
        "success_rate": success_count / total,
        "timeout_count": timeout_count,
        "error_count": error_count,
        "avg_latency_ms": int(avg_latency),
        "avg_coverage_pct": round(avg_coverage, 2),
        "avg_branch_coverage_pct": round(avg_branch, 2),
        "avg_quality_score": round(avg_quality, 4),
        "trivial_rate": trivial_count / total,
        "avg_mutation_score_pct": round(avg_mutation, 2),
        "mutation_data_count": mutation_count,
    }


def _compute_rankings(
    results: list[RunResult],
    scoring: ScoringConfig,
) -> list[dict[str, Any]]:
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
            1 for r in results if r.provider == provider and r.model == model and r.status == "ok"
        )

        provider_results = [
            r for r in results if r.provider == provider and r.model == model and r.status == "ok"
        ]
        avg_coverage = 0.0
        coverages = [
            r.metrics.coverage_pct for r in provider_results if r.metrics.coverage_pct is not None
        ]
        if coverages:
            avg_coverage = sum(coverages) / len(coverages)

        avg_quality = 0.0
        qualities = [r.metrics.quality_score for r in provider_results]
        if qualities:
            avg_quality = sum(qualities) / len(qualities)

        trivial_count = sum(1 for r in provider_results if r.metrics.trivial_flag)

        ranked_entries.append(
            {
                "provider": provider,
                "model": model,
                "avg_score": round(avg_score, 4),
                "run_count": run_count,
                "success_count": success_count,
                "success_rate": success_count / run_count if run_count > 0 else 0.0,
                "avg_coverage_pct": round(avg_coverage, 2),
                "avg_quality_score": round(avg_quality, 4),
                "trivial_count": trivial_count,
            }
        )

    ranked_entries.sort(key=lambda e: (-e["avg_score"], -e["success_rate"]))

    for i, entry in enumerate(ranked_entries, 1):
        entry["rank"] = i

    return ranked_entries


def _compute_entry_score(
    result: RunResult,
    weights: ScoringWeights,
) -> float:
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

    mutation_score = 0.0
    if result.metrics.mutation_score_pct is not None:
        mutation_score = result.metrics.mutation_score_pct / 100.0

    total_weight = weights.success + weights.coverage + weights.latency + weights.mutation
    if total_weight == 0:
        return success_score

    weighted = (
        (success_score * weights.success)
        + (coverage_score * weights.coverage)
        + (latency_score * weights.latency)
        + (mutation_score * weights.mutation)
    ) / total_weight

    return weighted


def _build_markdown_report(
    results: list[RunResult],
    manifest: BenchmarkManifest,
    summary: dict[str, Any],
) -> str:
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
    summary_lines = [
        f"- **Total Runs:** {stats.get('total_runs', 0)}",
        f"- **Successful:** {stats.get('success_count', 0)} ({stats.get('success_rate', 0):.1%})",
        f"- **Timeouts:** {stats.get('timeout_count', 0)}",
        f"- **Errors:** {stats.get('error_count', 0)}",
        f"- **Avg Latency:** {stats.get('avg_latency_ms', 0):,}ms",
        f"- **Avg Coverage:** {stats.get('avg_coverage_pct', 0):.1f}%",
        f"- **Avg Branch Coverage:** {stats.get('avg_branch_coverage_pct', 0):.1f}%",
        f"- **Avg Quality Score:** {stats.get('avg_quality_score', 0):.4f}",
        f"- **Trivial Rate:** {stats.get('trivial_rate', 0):.1%}",
    ]
    if stats.get("mutation_data_count", 0) > 0:
        summary_lines.append(f"- **Avg Mutation Score:** {stats.get('avg_mutation_score_pct', 0):.1f}%")
        summary_lines.append(f"- **Runs with Mutation Data:** {stats.get('mutation_data_count', 0)}")
    summary_lines.append("")
    lines.extend(summary_lines)

    rankings = summary.get("rankings", [])
    if rankings:
        lines.extend(
            [
                "## Rankings",
                "",
                "| Rank | Provider | Model | Avg Score | Success Rate | Avg Cov% | Quality | Trivial | Runs |",
                "|------|----------|-------|-----------|--------------|---------|---------|---------|------|",
            ]
        )
        for entry in rankings:
            lines.append(
                f"| {entry['rank']} | {entry['provider']} | "
                f"{entry['model']} | {entry['avg_score']:.4f} | "
                f"{entry['success_rate']:.1%} | {entry['avg_coverage_pct']:.1f} | "
                f"{entry['avg_quality_score']:.4f} | {entry['trivial_count']} | "
                f"{entry['run_count']} |"
            )
        lines.append("")

    rag_section = _build_rag_comparison_section(results)
    if rag_section:
        lines.extend(rag_section)

    has_mutation_data = any(r.metrics.mutation_score_pct is not None for r in results)

    if has_mutation_data:
        lines.extend(
            [
                "## Run Details",
                "",
                "| Run ID | Provider | Model | Dataset | Status | Cov% | Branch% | Mut% | Quality | Trivial | Gen(ms) |",
                "|--------|----------|-------|---------|--------|------|---------|------|---------|---------|---------|",
            ]
        )
    else:
        lines.extend(
            [
                "## Run Details",
                "",
                "| Run ID | Provider | Model | Dataset | Status | Cov% | Branch% | Quality | Trivial | Gen(ms) |",
                "|--------|----------|-------|---------|--------|------|---------|---------|---------|---------|",
            ]
        )
    for result in results:
        status_icon = {
            "ok": "+",
            "timeout": "T",
            "error": "X",
        }.get(result.status, "?")
        cov = (
            f"{result.metrics.coverage_pct:.1f}" if result.metrics.coverage_pct is not None else "-"
        )
        branch = (
            f"{result.metrics.branch_coverage_pct:.1f}"
            if result.metrics.branch_coverage_pct is not None
            else "-"
        )
        quality = f"{result.metrics.quality_score:.3f}"
        trivial = "Y" if result.metrics.trivial_flag else "-"
        gen_ms = (
            f"{result.metrics.generation_time_ms:,}" if result.metrics.generation_time_ms else "-"
        )
        if has_mutation_data:
            mut = (
                f"{result.metrics.mutation_score_pct:.1f}"
                if result.metrics.mutation_score_pct is not None
                else "-"
            )
            lines.append(
                f"| {result.run_id} | {result.provider} | {result.model} | "
                f"{result.dataset_id} | {status_icon} {result.status} | "
                f"{cov} | {branch} | {mut} | {quality} | {trivial} | {gen_ms} |"
            )
        else:
            lines.append(
                f"| {result.run_id} | {result.provider} | {result.model} | "
                f"{result.dataset_id} | {status_icon} {result.status} | "
                f"{cov} | {branch} | {quality} | {trivial} | {gen_ms} |"
            )
    lines.append("")

    return "\n".join(lines)


def _build_rag_comparison_section(results: list[RunResult]) -> list[str]:
    ast_results: list[RunResult] = []
    rag_results: list[RunResult] = []

    for r in results:
        strategy = _get_retrieval_strategy(r)
        if strategy in ("rag", "hybrid"):
            rag_results.append(r)
        else:
            ast_results.append(r)

    if not rag_results:
        return []

    lines = [
        "## AST vs RAG Comparison",
        "",
        "| Metric | AST-only | RAG/Hybrid | Delta |",
        "|--------|----------|------------|-------|",
    ]

    ast_ok = [r for r in ast_results if r.status == "ok"]
    rag_ok = [r for r in rag_results if r.status == "ok"]

    ast_success_rate = len(ast_ok) / len(ast_results) if ast_results else 0
    rag_success_rate = len(rag_ok) / len(rag_results) if rag_results else 0

    ast_cov = _avg_field(ast_ok, lambda r: r.metrics.coverage_pct)
    rag_cov = _avg_field(rag_ok, lambda r: r.metrics.coverage_pct)

    ast_quality = _avg_field(ast_ok, lambda r: r.metrics.quality_score)
    rag_quality = _avg_field(rag_ok, lambda r: r.metrics.quality_score)

    ast_latency = _avg_field(ast_ok, lambda r: float(r.latency_ms))
    rag_latency = _avg_field(rag_ok, lambda r: float(r.latency_ms))

    comparisons = [
        ("Success Rate", f"{ast_success_rate:.1%}", f"{rag_success_rate:.1%}"),
        ("Avg Coverage%", f"{ast_cov:.1f}", f"{rag_cov:.1f}"),
        ("Avg Quality", f"{ast_quality:.4f}", f"{rag_quality:.4f}"),
        ("Avg Latency(ms)", f"{ast_latency:.0f}", f"{rag_latency:.0f}"),
    ]

    for label, ast_val, rag_val in comparisons:
        try:
            delta = float(rag_val.replace("%", "").replace(",", "")) - float(
                ast_val.replace("%", "").replace(",", "")
            )
            delta_str = f"{'+' if delta >= 0 else ''}{delta:.2f}"
        except (ValueError, AttributeError):
            delta_str = "N/A"
        lines.append(f"| {label} | {ast_val} | {rag_val} | {delta_str} |")

    lines.append("")
    return lines


def _get_retrieval_strategy(result: RunResult) -> str:
    snapshot = result.config_snapshot or {}
    strategy = snapshot.get("retrieval_strategy", "ast")
    if isinstance(strategy, str):
        return strategy.lower()
    return "ast"


def _avg_field(results: list[RunResult], extractor) -> float:
    values = [extractor(r) for r in results if extractor(r) is not None]
    return sum(values) / len(values) if values else 0.0


def _utc_now() -> datetime:
    return datetime.now(UTC)


def _utc_iso_z() -> str:
    return _utc_now().isoformat().replace("+00:00", "Z")


def load_results_from_dir(
    output_dir: Path | str,
) -> list[RunResult]:
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
    from benchmark.types import EvalMetrics

    metrics_data = data.get("metrics", {})
    timings_data = metrics_data.get("timings", {})
    timings = PipelineTimings(
        parse_ms=timings_data.get("parse_ms", 0),
        retrieval_ms=timings_data.get("retrieval_ms", 0),
        prompt_ms=timings_data.get("prompt_ms", 0),
        llm_ms=timings_data.get("llm_ms", 0),
        postproc_ms=timings_data.get("postproc_ms", 0),
    )
    metrics = EvalMetrics(
        compile_pass=metrics_data.get("compile_pass", False),
        test_pass=metrics_data.get("test_pass", False),
        coverage_pct=metrics_data.get("coverage_pct"),
        branch_coverage_pct=metrics_data.get("branch_coverage_pct"),
        coverage_source=metrics_data.get("coverage_source"),
        coverage_reason=metrics_data.get("coverage_reason"),
        failure_type=metrics_data.get("failure_type"),
        failure_message=metrics_data.get("failure_message"),
        generation_time_ms=metrics_data.get("generation_time_ms", 0),
        assertion_count=metrics_data.get("assertion_count", 0),
        trivial_flag=metrics_data.get("trivial_flag", False),
        test_count=metrics_data.get("test_count", 0),
        timings=timings,
        mutation_score_pct=metrics_data.get("mutation_score_pct"),
        killed_mutations=metrics_data.get("killed_mutations"),
        total_mutations=metrics_data.get("total_mutations"),
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
                "branch_coverage_sum": 0.0,
                "branch_coverage_count": 0,
                "mutation_sum": 0.0,
                "mutation_count": 0,
                "quality_scores": [],
                "trivial_count": 0,
                "assertion_sum": 0,
                "generation_time_sum": 0,
                "generation_time_count": 0,
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

        if result.metrics.branch_coverage_pct is not None:
            stats["branch_coverage_sum"] += result.metrics.branch_coverage_pct
            stats["branch_coverage_count"] += 1

        if result.metrics.mutation_score_pct is not None:
            stats["mutation_sum"] += result.metrics.mutation_score_pct
            stats["mutation_count"] += 1

        stats["quality_scores"].append(result.metrics.quality_score)

        if result.metrics.trivial_flag:
            stats["trivial_count"] += 1

        stats["assertion_sum"] += result.metrics.assertion_count

        if result.metrics.generation_time_ms > 0:
            stats["generation_time_sum"] += result.metrics.generation_time_ms
            stats["generation_time_count"] += 1

    csv_path = output_path / filename

    with csv_path.open("w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerow(
            [
                "model",
                "success_rate",
                "avg_latency_sec",
                "coverage_pct",
                "branch_coverage_pct",
                "mutation_score_pct",
                "quality_score",
                "trivial_pct",
                "avg_assertion_count",
                "avg_generation_time_ms",
                "score",
            ]
        )

        for (provider, model), stats in entry_stats.items():
            model_str = f"{provider}/{model}"
            total = stats["total_count"]
            success_rate = stats["success_count"] / total if total > 0 else 0.0
            avg_latency_sec = (
                (stats["latency_sum"] / stats["latency_count"]) / 1000.0
                if stats["latency_count"] > 0
                else 0.0
            )
            coverage_pct = (
                stats["coverage_sum"] / stats["coverage_count"]
                if stats["coverage_count"] > 0
                else 0.0
            )
            branch_coverage_pct = (
                stats["branch_coverage_sum"] / stats["branch_coverage_count"]
                if stats["branch_coverage_count"] > 0
                else 0.0
            )
            mutation_score_pct = (
                stats["mutation_sum"] / stats["mutation_count"]
                if stats["mutation_count"] > 0
                else 0.0
            )
            quality_score = (
                sum(stats["quality_scores"]) / len(stats["quality_scores"])
                if stats["quality_scores"]
                else 0.0
            )
            trivial_pct = (stats["trivial_count"] / total * 100) if total > 0 else 0.0
            avg_assertion = stats["assertion_sum"] / total if total > 0 else 0.0
            avg_gen_ms = (
                stats["generation_time_sum"] / stats["generation_time_count"]
                if stats["generation_time_count"] > 0
                else 0.0
            )

            success_score = success_rate
            coverage_score = coverage_pct / 100.0
            latency_score = 1.0 - min(avg_latency_sec / 300.0, 1.0)
            score = (success_score * 0.5) + (coverage_score * 0.3) + (latency_score * 0.2)

            writer.writerow(
                [
                    model_str,
                    f"{success_rate:.4f}",
                    f"{avg_latency_sec:.2f}",
                    f"{coverage_pct:.2f}",
                    f"{branch_coverage_pct:.2f}",
                    f"{mutation_score_pct:.2f}",
                    f"{quality_score:.4f}",
                    f"{trivial_pct:.1f}",
                    f"{avg_assertion:.1f}",
                    f"{avg_gen_ms:.0f}",
                    f"{score:.4f}",
                ]
            )

    return csv_path
