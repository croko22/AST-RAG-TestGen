"""Consolidate benchmark results from results/nvidia/ into CSV + markdown report.

Usage: python scripts/consolidate_results.py

Scans results/nvidia/<project>/ for summary.json (preferred) or aggregates
individual run_*/result.json files when no summary exists.
"""

import csv
import json
import sys
from pathlib import Path

RESULTS_DIR = Path("results/nvidia")

# Fields we care about in the output
FIELDS = [
    "project",
    "total_runs",
    "generation_ok",
    "generation_rate",
    "avg_latency_ms",
    "total_tests_generated",
    "total_assertions",
    "avg_tests_per_run",
    "avg_assertions_per_run",
]


def _safe_float(value, default=0.0):
    if value is None:
        return default
    try:
        return float(value)
    except (ValueError, TypeError):
        return default


def _safe_int(value, default=0):
    if value is None:
        return default
    try:
        return int(value)
    except (ValueError, TypeError):
        return default


def _get_nested(d, *keys, default=0):
    """Get a value from a nested dict, e.g. _get_nested(data, 'metrics', 'test_count')."""
    current = d
    for k in keys:
        if isinstance(current, dict):
            current = current.get(k)
        else:
            return default
    return current if current is not None else default


def load_summary(summary_file: Path) -> dict | None:
    """Load a pre-computed summary.json."""
    if not summary_file.exists():
        return None
    try:
        data = json.loads(summary_file.read_text())
    except (json.JSONDecodeError, OSError):
        return None

    stats = data.get("statistics", {})
    config = data.get("config", {})
    return {
        "project": data.get("project", summary_file.parent.name),
        "total_runs": _safe_int(stats.get("total_runs")),
        "generation_ok": _safe_int(stats.get("generation_ok")),
        "generation_rate": _safe_float(stats.get("generation_rate")),
        "avg_latency_ms": _safe_float(stats.get("avg_latency_ms")),
        "total_tests_generated": _safe_int(stats.get("total_tests_generated")),
        "total_assertions": _safe_int(stats.get("total_assertions")),
        "avg_tests_per_run": _safe_float(stats.get("avg_tests_per_run")),
        "avg_assertions_per_run": _safe_float(stats.get("avg_assertions_per_run")),
        "provider": config.get("provider", ""),
        "model": config.get("model", ""),
        "_source": "summary.json",
    }


def aggregate_from_runs(project_dir: Path) -> dict | None:
    """Aggregate results from individual run_*/result.json files."""
    result_files = sorted(project_dir.glob("run_*/result.json"))
    if not result_files:
        return None

    runs = []
    for rf in result_files:
        try:
            runs.append(json.loads(rf.read_text()))
        except (json.JSONDecodeError, OSError):
            continue

    if not runs:
        return None

    total_runs = len(runs)
    generation_ok = sum(1 for r in runs if r.get("status", "").lower() == "success")
    total_tests = sum(
        _safe_int(
            _get_nested(r, "test_count", default=0)
            or _get_nested(r, "metrics", "test_count", default=0)
        )
        for r in runs
    )
    total_assertions = sum(
        _safe_int(
            _get_nested(r, "assertion_count", default=0)
            or _get_nested(r, "metrics", "assertion_count", default=0)
        )
        for r in runs
    )
    latencies = [_safe_float(r.get("latency_ms")) for r in runs if r.get("latency_ms") is not None]
    avg_latency = sum(latencies) / len(latencies) if latencies else 0.0

    return {
        "project": project_dir.name,
        "total_runs": total_runs,
        "generation_ok": generation_ok,
        "generation_rate": generation_ok / total_runs if total_runs else 0.0,
        "avg_latency_ms": round(avg_latency, 1),
        "total_tests_generated": total_tests,
        "total_assertions": total_assertions,
        "avg_tests_per_run": round(total_tests / total_runs, 1) if total_runs else 0.0,
        "avg_assertions_per_run": round(total_assertions / total_runs, 1) if total_runs else 0.0,
        "provider": "",
        "model": "",
        "_source": "aggregated",
    }


def fmt(v, width=14):
    """Format a value for the markdown table (right-aligned)."""
    if isinstance(v, float):
        s = f"{v:.2f}" if v != int(v) else f"{v:.1f}"
    else:
        s = str(v)
    return s.rjust(width)


def main():
    rows = []
    for project_dir in sorted(RESULTS_DIR.iterdir()):
        if not project_dir.is_dir():
            continue

        row = load_summary(project_dir / "summary.json")
        if row is None:
            row = aggregate_from_runs(project_dir)

        if row is None:
            print(f"  [skip] {project_dir.name} — no summary.json or run_*/result.json")
            continue

        rows.append(row)

    if not rows:
        print("No se encontraron resultados en results/nvidia/")
        sys.exit(1)

    # ── Write CSV ──────────────────────────────────────────────
    csv_path = RESULTS_DIR / "consolidated.csv"
    with open(csv_path, "w", newline="") as f:
        w = csv.DictWriter(f, fieldnames=FIELDS, extrasaction="ignore")
        w.writeheader()
        w.writerows(rows)
    print(f"CSV: {csv_path} ({len(rows)} projects)")

    # ── Write markdown report ──────────────────────────────────
    report_path = RESULTS_DIR / "consolidated_report.md"
    header = ["Project"] + FIELDS[1:]
    sep = "|" + "|".join("---" for _ in header) + "|"
    with open(report_path, "w") as f:
        f.write("# Consolidated Benchmark Report\n\n")
        f.write("| " + " | ".join(header) + " |\n")
        f.write(sep + "\n")
        for r in rows:
            vals = [r["project"]] + [r[k] for k in FIELDS[1:]]
            f.write("| " + " | ".join(fmt(v) for v in vals) + " |\n")
    print(f"Report: {report_path}")

    # ── Stdout summary ─────────────────────────────────────────
    total_runs = sum(r["total_runs"] for r in rows)
    total_ok = sum(r["generation_ok"] for r in rows)
    total_tests = sum(r["total_tests_generated"] for r in rows)
    total_assertions = sum(r["total_assertions"] for r in rows)
    avg_lat = sum(r["avg_latency_ms"] for r in rows) / len(rows) if rows else 0

    print()
    print(f"Total projects:              {len(rows)}")
    print(f"Total runs:                  {total_runs}")
    print(
        f"Successful generations:      {total_ok} ({total_ok / total_runs * 100:.1f}%)"
        if total_runs
        else "N/A"
    )
    print(f"Total tests generated:       {total_tests}")
    print(f"Total assertions:            {total_assertions}")
    print(f"Average latency (all):       {avg_lat:.0f} ms")
    print()
    print("Per-project breakdown:")
    print(
        f"  {'Project':22s} {'Runs':>5s} {'OK':>5s} {'Rate':>6s} {'Tests':>6s} {'Assert':>6s} {'Lat(ms)':>8s} {'Src':>12s}"
    )
    print(f"  {'-' * 22} {'-' * 5} {'-' * 5} {'-' * 6} {'-' * 6} {'-' * 6} {'-' * 8} {'-' * 12}")
    for r in rows:
        src = r.get("_source", "")
        print(
            f"  {r['project']:22s} {r['total_runs']:5d} {r['generation_ok']:5d} "
            f"{r['generation_rate']:5.0%} {r['total_tests_generated']:6d} "
            f"{r['total_assertions']:6d} {r['avg_latency_ms']:>8.0f} {src:>12s}"
        )


if __name__ == "__main__":
    main()
