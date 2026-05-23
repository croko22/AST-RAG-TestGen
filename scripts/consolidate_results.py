"""Consolidate benchmark results into CSV + report."""
import json, csv, sys
from pathlib import Path

RESULTS_DIR = Path("results/nvidia")

def load_results(project_dir: Path) -> dict | None:
    summary_file = project_dir / "summary.json"
    if not summary_file.exists():
        return None
    try:
        return json.loads(summary_file.read_text())
    except (json.JSONDecodeError, OSError):
        return None

def main():
    rows = []
    for project_dir in sorted(RESULTS_DIR.iterdir()):
        if not project_dir.is_dir():
            continue
        data = load_results(project_dir)
        if data is None:
            continue

        stats = data.get("statistics", {})
        rows.append({
            "project": project_dir.name,
            "total_runs": stats.get("total_runs", 0),
            "success_count": stats.get("success_count", 0),
            "success_rate": stats.get("success_rate", 0),
            "avg_latency_ms": stats.get("avg_latency_ms", 0),
            "timeout_count": stats.get("timeout_count", 0),
            "error_count": stats.get("error_count", 0),
            "files": len(list(project_dir.glob("run_*/result.json"))),
        })

    if not rows:
        print("No se encontraron resultados en results/nvidia/")
        sys.exit(1)

    # Write CSV
    csv_path = RESULTS_DIR / "consolidated.csv"
    with open(csv_path, "w", newline="") as f:
        w = csv.DictWriter(f, fieldnames=rows[0].keys())
        w.writeheader()
        w.writerows(rows)
    print(f"CSV: {csv_path} ({len(rows)} projects)")

    # Write report
    report_path = RESULTS_DIR / "consolidated_report.md"
    with open(report_path, "w") as f:
        f.write("# Consolidated Benchmark Report\n\n")
        f.write(f"| {' | '.join(rows[0].keys())} |\n")
        f.write(f"|{'|'.join('---' for _ in rows[0].keys())}|\n")
        for r in rows:
            f.write(f"| {' | '.join(str(r[k]) for k in rows[0].keys())} |\n")
    print(f"Report: {report_path}")

    # Summary
    total_runs = sum(r["total_runs"] for r in rows)
    total_success = sum(r["success_count"] for r in rows)
    print(f"\nTotal runs: {total_runs}, Success: {total_success} ({total_success/total_runs*100:.1f}%)" if total_runs else "No data")

if __name__ == "__main__":
    main()
