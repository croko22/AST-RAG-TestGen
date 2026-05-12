# Thesis Results Package — Design & Implementation Spec

**Date:** 2026-05-12
**Status:** Design Approved

## Overview

Add a `python main.py thesis-report` command that automatically aggregates all existing `benchmark_reftest_*_results/` directories and generates a thesis-ready markdown report at `docs/thesis/results-package-v1.md`. No new dependencies, no cost estimation, no per-project splitting — one command, one file.

## CLI Interface

```bash
python main.py thesis-report                           # default: docs/thesis/results-package-v1.md
python main.py thesis-report --output path/to/report.md
python main.py thesis-report --latex                   # also emit LaTeX table fragments
```

- Integrates into existing Typer-based CLI (`cli/modern.py`)
- No flags for provider filtering, dataset scoping, or cost estimation
- Fails fast if no benchmark result directories are found

## Architecture

### Module: `benchmark/thesis_report.py`

Four internal components, no external API exposure:

```
ResultsLoader
  └─> scan root for benchmark_reftest_*_results/ directories
  └─> load & parse each results.json
  └─> deduplicate by run_id
  └─> return list[RunResult]

MetricsAggregator
  └─> compute global stats:
  │     total_projects, total_runs, compile_rate, test_pass_rate
  │     avg_quality_score, avg_latency, total_assertions
  └─> compute per-project stats:
  │     compile status, test count, assertion count, quality, latency
  └─> return AggregatedMetrics

FailureAnalyzer
  └─> scan failure_message fields across all runs
  └─> classify into categories:
  │     void_mock, api_signature, type_inheritance, missing_import, runtime
  └─> count per category, extract representative examples
  └─> return FailureReport

ReportRenderer
  └─> generate sections:
  │     1. Executive Summary (table)
  │     2. Per-Project Results (table)
  │     3. Failure Analysis (grouped + examples)
  │     4. Quality Metrics (quality scores, assertions)
  │     5. Timing Breakdown (per-run timing)
  └─> render to markdown string
  └─> [optional] render LaTeX table fragments
  └─> write to output file
```

### Data structures (internal dataclasses):

```python
@dataclass
class RunResult:
    run_id: str
    status: str                    # ok | failed | timeout | error
    compile_pass: bool
    test_pass: bool | None
    coverage_pct: float | None
    quality_score: float
    assertion_count: int
    test_count: int
    trivial_flag: bool
    latency_ms: int
    generation_time_ms: int
    failure_type: str | None
    failure_message: str | None
    dataset_id: str
    provider: str
    model: str
    timings: dict                  # parse_ms, retrieval_ms, prompt_ms, llm_ms, postproc_ms

@dataclass
class FailureCategory:
    name: str
    count: int
    pct: float
    examples: list[str]
    recommendation: str
```

## Output Sections (Markdown)

### 1. Executive Summary
One table with top-level metrics: projects evaluated, compile rate, test pass rate, avg quality score, avg generation time, total tests, total assertions.

### 2. Per-Project Results
Table with one row per dataset entry:
| Project | Class | Status | Tests | Assertions | Quality | Time(s) |
Where Status uses emoji shorthand: ✅compiled / ❌compile / ⏱timeout

### 3. Failure Analysis
Each failure category gets:
- **Name** and **% of total failures**
- **Description** of the error pattern
- **Affected projects** list
- **Example** from actual failure_message
- **Recommendation** for prompt improvement

If there are zero failures (100% pass), this section reads: "All projects compiled and passed — no failures to analyze."

### 4. Quality Metrics
- Quality score distribution (table: score range -> count)
- Top/bottom projects by quality
- Trivial test detection summary

### 5. Timing Breakdown
Average parse/retrieval/prompt/LLM/postproc timing across all runs. Table per project showing LLM time vs total time.

## Error Handling

| Scenario | Behavior |
|----------|----------|
| No results dirs found | Exit 1, print: "No benchmark results found. Run a benchmark first." |
| Corrupt results.json | Skip directory with warning, continue |
| Empty results dir | Skip silently |
| Partial data (null fields) | Omit from derived metrics, continue |
| Output path not writable | Exit 1 with OS error |

## Files Changed

| File | Action |
|------|--------|
| `benchmark/thesis_report.py` | Create — new module |
| `benchmark/__init__.py` | Modify — add import |
| `cli/modern.py` | Modify — add `thesis-report` command |
| `docs/thesis/results-package-v1.md` | Create — generated output (gitignored? no, committed) |

## Non-Goals
- Cost estimation (NVIDIA pricing, token counting)
- HTML/PDF generation
- Multi-provider comparison in a single run
- Per-project separate files
- Interactive dashboards or charts
- Mutation score integration (PIT results not available in current runs)
