"""
Thesis Results Report Generator.

Aggregates all benchmark_reftest_*_results/ directories and generates
a thesis-ready markdown report at docs/thesis/results-package-v1.md.

Usage:
    python main.py thesis-report
    python main.py thesis-report --output path/to/report.md
    python main.py thesis-report --latex
"""

from __future__ import annotations

import json
import re
from collections import Counter
from dataclasses import dataclass, field
from datetime import UTC, datetime
from pathlib import Path
from typing import Any

# ── Forward compat: import compile_errors if available ──────────────────────
try:
    from benchmark.types import EvalMetrics as BMEvalMetrics

    _HAS_COMPILE_ERRORS = hasattr(BMEvalMetrics, "compile_errors") and hasattr(
        BMEvalMetrics, "final_status"
    )
except ImportError:
    _HAS_COMPILE_ERRORS = False

PROJECT_ROOT = Path(__file__).resolve().parent.parent


# ── Data structures ──────────────────────────────────────────────────────────


@dataclass
class RunData:
    """Flattened run data extracted from results.json."""

    run_id: str
    dataset_id: str
    provider: str
    model: str
    status: str
    compile_pass: bool
    test_pass: bool | None
    coverage_pct: float | None
    branch_coverage_pct: float | None
    quality_score: float
    assertion_count: int
    test_count: int
    trivial_flag: bool
    latency_ms: int
    generation_time_ms: int
    failure_type: str | None
    failure_message: str | None
    parse_ms: int = 0
    retrieval_ms: int = 0
    prompt_ms: int = 0
    llm_ms: int = 0
    postproc_ms: int = 0
    compile_errors: list[str] = field(default_factory=list)


@dataclass
class FailureCategory:
    name: str
    count: int
    pct: float
    examples: list[str]
    recommendation: str


# ── Loader ───────────────────────────────────────────────────────────────────


def discover_results_dirs(root: Path) -> list[Path]:
    """Find all benchmark_reftest_*_results/ directories."""
    pattern = re.compile(r"^benchmark_reftest_.+_results$")
    return sorted(p for p in root.iterdir() if p.is_dir() and pattern.match(p.name))


def load_run_from_dict(d: dict[str, Any]) -> RunData:
    """Deserialize one run entry from results.json."""
    metrics = d.get("metrics", {})
    timings = metrics.get("timings", {})
    compile_errs = metrics.get("compile_errors", [])
    if isinstance(compile_errs, list):
        compile_errs = [str(e) for e in compile_errs]
    else:
        compile_errs = [str(compile_errs)] if compile_errs else []
    return RunData(
        run_id=d["run_id"],
        dataset_id=d.get("dataset_id", "unknown"),
        provider=d.get("provider", "unknown"),
        model=d.get("model", "unknown"),
        status=d.get("status", "unknown"),
        compile_pass=metrics.get("compile_pass", False),
        test_pass=metrics.get("test_pass"),
        coverage_pct=metrics.get("coverage_pct"),
        branch_coverage_pct=metrics.get("branch_coverage_pct"),
        quality_score=metrics.get("quality_score", 0.0),
        assertion_count=metrics.get("assertion_count", 0),
        test_count=metrics.get("test_count", 0),
        trivial_flag=metrics.get("trivial_flag", False),
        latency_ms=d.get("latency_ms", 0),
        generation_time_ms=metrics.get("generation_time_ms", 0),
        failure_type=metrics.get("failure_type"),
        failure_message=metrics.get("failure_message"),
        parse_ms=timings.get("parse_ms", 0),
        retrieval_ms=timings.get("retrieval_ms", 0),
        prompt_ms=timings.get("prompt_ms", 0),
        llm_ms=timings.get("llm_ms", 0),
        postproc_ms=timings.get("postproc_ms", 0),
        compile_errors=compile_errs,
    )


def load_all_results(results_dirs: list[Path]) -> list[RunData]:
    """Load and deduplicate all runs from results directories."""
    seen: set[str] = set()
    runs: list[RunData] = []
    for d in results_dirs:
        results_file = d / "results.json"
        if not results_file.exists():
            continue
        try:
            data = json.loads(results_file.read_text(encoding="utf-8"))
        except (json.JSONDecodeError, OSError):
            continue
        for entry in data.get("runs", []):
            rid = entry.get("run_id", "")
            if rid and rid not in seen:
                seen.add(rid)
                runs.append(load_run_from_dict(entry))
    return runs


# ── Aggregator ───────────────────────────────────────────────────────────────


@dataclass
class AggregatedMetrics:
    total_projects: int = 0
    total_runs: int = 0
    compile_count: int = 0
    test_pass_count: int = 0
    timeout_count: int = 0
    total_assertions: int = 0
    total_tests: int = 0
    quality_scores: list[float] = field(default_factory=list)
    latencies_ms: list[int] = field(default_factory=list)
    gen_times_ms: list[int] = field(default_factory=list)
    compile_errs: list[str] = field(default_factory=list)

    @property
    def compile_rate(self) -> float:
        return self.compile_count / self.total_runs if self.total_runs else 0.0

    @property
    def test_pass_rate(self) -> float:
        return self.test_pass_count / self.total_runs if self.total_runs else 0.0

    @property
    def avg_quality(self) -> float:
        if not self.quality_scores:
            return 0.0
        return sum(self.quality_scores) / len(self.quality_scores)

    @property
    def avg_quality_nonzero(self) -> float:
        scores = [s for s in self.quality_scores if s > 0]
        return sum(scores) / len(scores) if scores else 0.0

    @property
    def avg_latency_sec(self) -> float:
        if not self.latencies_ms:
            return 0.0
        return sum(self.latencies_ms) / len(self.latencies_ms) / 1000.0

    @property
    def avg_gen_time_sec(self) -> float:
        if not self.gen_times_ms:
            return 0.0
        return sum(self.gen_times_ms) / len(self.gen_times_ms) / 1000.0

    @property
    def trivial_count(self) -> int:
        return sum(1 for s in self.quality_scores if s == 0.0)


def aggregate(runs: list[RunData]) -> AggregatedMetrics:
    m = AggregatedMetrics(total_projects=len({r.dataset_id for r in runs}))
    for r in runs:
        m.total_runs += 1
        if r.compile_pass:
            m.compile_count += 1
        if r.test_pass:
            m.test_pass_count += 1
        if r.status == "timeout":
            m.timeout_count += 1
        m.total_assertions += r.assertion_count
        m.total_tests += r.test_count
        m.quality_scores.append(r.quality_score)
        m.latencies_ms.append(r.latency_ms)
        m.gen_times_ms.append(r.generation_time_ms)
        if r.failure_type == "compile_failed" and r.failure_message:
            m.compile_errs.append(r.failure_message)
    return m


# ── Failure Analyzer ─────────────────────────────────────────────────────────

FAILURE_PATTERNS: list[tuple[str, str, str]] = [
    (
        "void_method_mock",
        r"when\(.*\)\.thenReturn\(.*void|void.*method.*mock|doNothing",
        "Use doNothing() instead of when().thenReturn() for void methods",
    ),
    (
        "api_signature",
        r"no suitable method|cannot find symbol|csv\(|method.*not found",
        "Verify method signatures match the actual API before generating calls",
    ),
    (
        "type_inheritance",
        r"incompatible types|cannot convert|cannot be cast",
        "Check class hierarchy: use correct parent/child types",
    ),
    (
        "missing_import",
        r"cannot find symbol|import.*not found|package.*does not exist",
        "Ensure all used classes are properly imported",
    ),
    (
        "runtime_error",
        r"Exception|Error|NullPointer|IllegalArgument|ArrayIndexOutOf",
        "Review test logic: runtime errors indicate incorrect usage or missing setup",
    ),
]


def classify_failure(msg: str) -> str:
    for cat, pattern, _ in FAILURE_PATTERNS:
        if re.search(pattern, msg, re.IGNORECASE):
            return cat
    return "other"


RECOMMENDATIONS: dict[str, str] = {cat: rec for cat, _, rec in FAILURE_PATTERNS}
RECOMMENDATIONS["other"] = "Inspect failure message and adjust test logic"

FAILURE_LABELS: dict[str, str] = {
    "void_method_mock": "Void method mocking",
    "api_signature": "API signature mismatch",
    "type_inheritance": "Type inheritance confusion",
    "missing_import": "Missing import statements",
    "runtime_error": "Runtime exception",
    "other": "Other",
}


def _get_failure_text(r: RunData) -> str:
    """Get best available failure description for a run."""
    if r.compile_errors:
        return "\n".join(r.compile_errors)
    if r.failure_message and r.failure_message not in ("Compilation failed", "None", ""):
        return r.failure_message
    if not r.compile_pass:
        return f"Compilation failed for {r.dataset_id}"
    if r.status in ("error", "timeout"):
        return f"Run {r.status}: {r.failure_message or 'no details'}"
    if not r.test_pass:
        return f"Tests failed: {r.failure_message or 'no details'}"
    return "(no failure details captured)"


def analyze_failures(runs: list[RunData]) -> list[FailureCategory]:
    """Analyze all runs that didn't fully pass (compile + test)."""
    failed = [
        r
        for r in runs
        if not r.compile_pass
        or r.status in ("error", "timeout")
        or (r.compile_pass and r.test_pass is False)
    ]
    if not failed:
        return []

    examples: dict[str, list[str]] = {}
    cat_counts: Counter = Counter()
    for r in failed:
        msg = _get_failure_text(r)
        cat = classify_failure(msg)
        cat_counts[cat] += 1
        if cat not in examples and msg and msg != f"Compilation failed for {r.dataset_id}":
            examples[cat] = [msg[:300]]

    total_fail = len(failed)
    result = []
    for cat_name, count in cat_counts.most_common():
        result.append(
            FailureCategory(
                name=FAILURE_LABELS.get(cat_name, cat_name),
                count=count,
                pct=count / total_fail * 100,
                examples=examples.get(cat_name, ["(no error details captured in benchmark run)"]),
                recommendation=RECOMMENDATIONS.get(cat_name, "Review failure"),
            )
        )
    return result


# ── Quality analysis ─────────────────────────────────────────────────────────


@dataclass
class QualityDistribution:
    excellent: int = 0  # >= 0.8
    good: int = 0  # >= 0.5
    fair: int = 0  # >= 0.3
    poor: int = 0  # < 0.3, > 0
    trivial: int = 0  # == 0


def quality_distribution(runs: list[RunData]) -> QualityDistribution:
    d = QualityDistribution()
    for r in runs:
        q = r.quality_score
        if q >= 0.8:
            d.excellent += 1
        elif q >= 0.5:
            d.good += 1
        elif q >= 0.3:
            d.fair += 1
        elif q > 0:
            d.poor += 1
        else:
            d.trivial += 1
    return d


# ── Timing analysis ──────────────────────────────────────────────────────────


@dataclass
class TimingSummary:
    parse_avg: float = 0.0
    retrieval_avg: float = 0.0
    prompt_avg: float = 0.0
    llm_avg: float = 0.0
    postproc_avg: float = 0.0
    sample_count: int = 0


def timing_summary(runs: list[RunData]) -> TimingSummary:
    n = len(runs)
    if n == 0:
        return TimingSummary()
    return TimingSummary(
        parse_avg=sum(r.parse_ms for r in runs) / n,
        retrieval_avg=sum(r.retrieval_ms for r in runs) / n,
        prompt_avg=sum(r.prompt_ms for r in runs) / n,
        llm_avg=sum(r.llm_ms for r in runs) / n,
        postproc_avg=sum(r.postproc_ms for r in runs) / n,
        sample_count=n,
    )


# ── Renderer ─────────────────────────────────────────────────────────────────


def _fmt_sec(ms: int) -> str:
    return f"{ms / 1000:.1f}"


def _status_cell(r: RunData) -> str:
    if r.compile_pass and r.test_pass is None:
        return "✅ compiled"
    if r.compile_pass and r.test_pass:
        return "✅✅ pass"
    if r.compile_pass and not r.test_pass:
        return "✅comp ❌test"
    if r.status == "timeout":
        return "⏱ timeout"
    return "❌ compile"


def render_report(runs: list[RunData]) -> str:
    if not runs:
        return "# Thesis Results Package\n\nNo benchmark data found.\n"

    agg = aggregate(runs)
    failures = analyze_failures(runs)
    quality = quality_distribution(runs)
    timings = timing_summary(runs)

    # Deduplicate to unique dataset entries for per-project table
    seen_datasets: set[str] = set()
    per_project: list[RunData] = []
    for r in runs:
        if r.dataset_id not in seen_datasets:
            seen_datasets.add(r.dataset_id)
            per_project.append(r)

    # Unique provider/model info
    providers = sorted({(r.provider, r.model) for r in runs})
    provider_str = "; ".join(f"{p}/{m}" for p, m in providers) if providers else "N/A"

    lines: list[str] = []
    _w = lines.append

    _w("# Thesis Results Package")
    _w("")
    _w(f"**Generated:** {datetime.now(UTC).strftime('%Y-%m-%d %H:%M:%S UTC')}")
    _w(f"**Provider/Model:** {provider_str}")
    _w(f"**Dataset:** {agg.total_projects} projects, {agg.total_runs} total runs")
    _w("")

    # ── 1. Executive Summary ──
    _w("## 1. Executive Summary")
    _w("")
    _w("| Metric | Value |")
    _w("|--------|-------|")
    _w(f"| Projects evaluated | {agg.total_projects} |")
    _w(f"| Total benchmark runs | {agg.total_runs} |")
    _w(f"| Compilation success | {agg.compile_count} / {agg.total_runs} ({agg.compile_rate:.1%}) |")
    _w(
        f"| Test execution success | {agg.test_pass_count} / {agg.total_runs} ({agg.test_pass_rate:.1%}) |"
    )
    _w(f"| Timeouts | {agg.timeout_count} |")
    _w(f"| Average quality score | {agg.avg_quality:.3f} |")
    _w(f"| Average generation time | {agg.avg_gen_time_sec:.1f}s |")
    _w(f"| Total generated tests | {agg.total_tests} |")
    _w(f"| Total assertions | {agg.total_assertions} |")
    _w(f"| Average assertions per test | {agg.total_assertions / max(agg.total_tests, 1):.1f} |")
    _w(f"| Trivial tests | {quality.trivial} ({quality.trivial / max(agg.total_runs, 1):.1%}) |")
    _w("")

    # ── 2. Per-Project Results ──
    _w("## 2. Per-Project Results")
    _w("")
    _w("| Project | Class | Status | Tests | Assertions | Quality | Time(s) |")
    _w("|---------|-------|--------|-------|------------|---------|---------|")
    for r in per_project:
        pid = r.dataset_id.rsplit("-", 1)[0] if "-" in r.dataset_id else r.dataset_id
        cls = r.dataset_id.rsplit("-", 1)[-1] if "-" in r.dataset_id else ""
        _w(
            f"| {pid} | {cls} | {_status_cell(r)} "
            f"| {r.test_count} | {r.assertion_count} "
            f"| {r.quality_score:.3f} | {_fmt_sec(r.generation_time_ms)} |"
        )
    _w("")

    # ── 3. Failure Analysis ──
    _w("## 3. Failure Analysis")
    _w("")

    if not failures:
        _w("All projects compiled and passed — no failures to analyze.")
        _w("")
    else:
        total_fail = sum(f.count for f in failures)
        _w(f"**{total_fail} failed run(s)** categorized into {len(failures)} pattern(s):")
        _w("")

        for f in failures:
            _w(f"### 3.{failures.index(f) + 1} {f.name} ({f.count}/{total_fail}, {f.pct:.0f}%)")
            _w("")
            _w(f"- **Count:** {f.count}")
            _w(f"- **% of failures:** {f.pct:.0f}%")
            _w(f"- **Recommendation:** {f.recommendation}")
            _w("")
            for ex in f.examples:
                _w("```")
                _w(ex)
                _w("```")
                _w("")

    # ── 4. Quality Metrics ──
    _w("## 4. Quality Metrics")
    _w("")
    _w("| Range | Label | Count |")
    _w("|-------|-------|-------|")
    _w(f"| >= 0.8 | Excellent | {quality.excellent} |")
    _w(f"| 0.5 – 0.79 | Good | {quality.good} |")
    _w(f"| 0.3 – 0.49 | Fair | {quality.fair} |")
    _w(f"| > 0 – 0.29 | Poor | {quality.poor} |")
    _w(f"| 0 (trivial) | Trivial | {quality.trivial} |")
    _w("")
    _w(f"- **Average quality score:** {agg.avg_quality:.3f}")
    _w(f"- **Average (non-zero):** {agg.avg_quality_nonzero:.3f}")
    _w("")

    # Top/bottom by quality
    sorted_by_q = sorted(per_project, key=lambda r: r.quality_score)
    _w("**Highest quality:**")
    for r in sorted_by_q[-3:]:
        _w(
            f"- {r.dataset_id}: {r.quality_score:.3f} ({r.assertion_count} assertions, {r.test_count} tests)"
        )
    _w("")
    _w("**Lowest quality:**")
    for r in sorted_by_q[:3]:
        _w(
            f"- {r.dataset_id}: {r.quality_score:.3f} ({r.assertion_count} assertions, {r.test_count} tests)"
        )
    _w("")

    # ── 5. Timing Breakdown ──
    _w("## 5. Timing Breakdown")
    _w("")
    _w(f"Average across {timings.sample_count} runs:")
    _w("")
    _w("| Phase | Avg Time (ms) | % of Total |")
    _w("|-------|---------------|------------|")
    total = (
        timings.parse_avg
        + timings.retrieval_avg
        + timings.prompt_avg
        + timings.llm_avg
        + timings.postproc_avg
    )
    phases = [
        ("Parse (AST)", timings.parse_avg),
        ("Retrieval (deps)", timings.retrieval_avg),
        ("Prompt building", timings.prompt_avg),
        ("LLM generation", timings.llm_avg),
        ("Post-processing", timings.postproc_avg),
    ]
    for name, avg in phases:
        pct = avg / total * 100 if total > 0 else 0
        _w(f"| {name} | {avg:.0f} | {pct:.1f}% |")
    _w("")
    _w(f"- **Average total:** {total:.0f}ms ({total / 1000:.1f}s)")
    _w(
        f"- **Average LLM time:** {timings.llm_avg:.0f}ms ({timings.llm_avg / total * 100:.1f}% of total)"
        if total > 0
        else ""
    )
    _w("")

    # Per-project timing table
    _w("### Per-Project Timing")
    _w("")
    _w("| Project | Class | LLM (s) | Total (s) |")
    _w("|---------|-------|---------|-----------|")
    for r in per_project:
        pid = r.dataset_id.rsplit("-", 1)[0] if "-" in r.dataset_id else r.dataset_id
        cls = r.dataset_id.rsplit("-", 1)[-1] if "-" in r.dataset_id else ""
        _w(f"| {pid} | {cls} | {_fmt_sec(r.llm_ms)} | {_fmt_sec(r.generation_time_ms)} |")
    _w("")

    return "\n".join(lines)


# ── Public API ───────────────────────────────────────────────────────────────


def generate_thesis_report(
    output_path: str | Path | None = None,
    latex: bool = False,
) -> Path:
    """Generate the thesis results report.

    Args:
        output_path: Where to write the report. Defaults to docs/thesis/results-package-v1.md.
        latex: If True, also generate LaTeX table fragments (not yet implemented).

    Returns:
        Path to the generated report.
    """
    root = PROJECT_ROOT
    results_dirs = discover_results_dirs(root)

    if not results_dirs:
        print("✖ No benchmark result directories found.")
        print("  Run a benchmark first: python main.py benchmark <manifest>")
        raise SystemExit(1)

    runs = load_all_results(results_dirs)
    if not runs:
        print("✖ No run data found in benchmark result directories.")
        raise SystemExit(1)

    print(f"✓ Found {len(results_dirs)} result dir(s), {len(runs)} run(s)")

    report = render_report(runs)

    if output_path is None:
        output_path = root / "docs" / "thesis" / "results-package-v1.md"

    out = Path(output_path)
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text(report, encoding="utf-8")

    # Print summary
    agg = aggregate(runs)
    print()
    print("╭──────────────────────────────────────────────╮")
    print("│  📊 Thesis Results Package v1                │")
    print("│                                              │")
    print(f"│  Projects    : {agg.total_projects:<5}                      │")
    print(f"│  Total runs  : {agg.total_runs:<5}                      │")
    print(f"│  Compile rate: {agg.compile_rate:.1%}                           │")
    print(f"│  Test pass   : {agg.test_pass_rate:.1%}                           │")
    print(f"│  Avg quality : {agg.avg_quality:.3f}                        │")
    print("│                                              │")
    print(f"│  Output: {out} │")
    print("╰──────────────────────────────────────────────╯")
    print()

    return out
