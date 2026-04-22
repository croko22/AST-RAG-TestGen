"""Unit tests for benchmark reporter scoring and ranking."""

from __future__ import annotations

import json

import pytest

pytestmark = pytest.mark.slow

from benchmark.reporter import (
    _build_summary_json,
    _compute_diagnostics,
    _compute_entry_score,
    _compute_rankings,
    _compute_statistics,
    build_report,
    collect_provenance_records,
    load_results_from_dir,
)
from benchmark.schemas import BenchmarkManifest, ScoringConfig, ScoringWeights
from benchmark.types import EvalMetrics, PreflightFinding, RunResult


def _make_result(
    run_id: str,
    provider: str,
    model: str,
    dataset_id: str,
    trial: int,
    status: str = "ok",
    latency_ms: int = 50000,
    compile_pass: bool = True,
    test_pass: bool = True,
    coverage_pct: float | None = None,
) -> RunResult:
    """Helper to create RunResult for testing."""
    return RunResult(
        run_id=run_id,
        status=status,
        latency_ms=latency_ms,
        metrics=EvalMetrics(
            compile_pass=compile_pass,
            test_pass=test_pass,
            coverage_pct=coverage_pct,
            failure_type=None if status == "ok" else "error",
            failure_message=None,
        ),
        output_path=f"/output/{run_id}",
        provider=provider,
        model=model,
        dataset_id=dataset_id,
        trial=trial,
        config_snapshot={},
    )


def _minimal_manifest() -> BenchmarkManifest:
    """Create minimal manifest for testing."""
    return BenchmarkManifest(
        manifest_version=1,
        project_root="/test",
        dataset=[{"id": "test", "java_file": "src/Test.java"}],
        matrix={"providers": [{"name": "test", "model": "test"}]},
        evaluation={"compile_cmd": "mvn compile", "test_cmd": "mvn test"},
    )


def test_compute_statistics_empty():
    stats = _compute_statistics([])
    assert stats["total_runs"] == 0
    assert stats["success_rate"] == 0.0


def test_compute_statistics_all_success():
    results = [
        _make_result("r1", "p1", "m1", "d1", 1),
        _make_result("r2", "p1", "m1", "d1", 2),
    ]
    stats = _compute_statistics(results)
    assert stats["total_runs"] == 2
    assert stats["success_count"] == 2
    assert stats["success_rate"] == 1.0
    assert stats["timeout_count"] == 0
    assert stats["error_count"] == 0


def test_compute_statistics_mixed_results():
    results = [
        _make_result("r1", "p1", "m1", "d1", 1, status="ok"),
        _make_result("r2", "p1", "m1", "d1", 2, status="timeout"),
        _make_result("r3", "p1", "m1", "d1", 3, status="error"),
    ]
    stats = _compute_statistics(results)
    assert stats["total_runs"] == 3
    assert stats["success_count"] == 1
    assert stats["success_rate"] == pytest.approx(1 / 3)
    assert stats["timeout_count"] == 1
    assert stats["error_count"] == 1


def test_compute_statistics_avg_latency():
    results = [
        _make_result("r1", "p1", "m1", "d1", 1, latency_ms=50000),
        _make_result("r2", "p1", "m1", "d1", 2, latency_ms=100000),
    ]
    stats = _compute_statistics(results)
    assert stats["avg_latency_ms"] == 75000


def test_compute_entry_score_full_success():
    result = _make_result(
        "r1",
        "p1",
        "m1",
        "d1",
        1,
        compile_pass=True,
        test_pass=True,
        coverage_pct=80.0,
        latency_ms=60000,
    )
    weights = ScoringWeights(success=0.5, coverage=0.3, latency=0.2)
    score = _compute_entry_score(result, weights)

    assert 0.0 <= score <= 1.0
    success_comp = 1.0 * 0.5
    coverage_comp = 0.8 * 0.3
    latency_comp = (1.0 - 60 / 300) * 0.2
    expected = (success_comp + coverage_comp + latency_comp) / 1.0
    assert abs(score - expected) < 0.001


def test_compute_entry_score_partial_success():
    result = _make_result(
        "r1",
        "p1",
        "m1",
        "d1",
        1,
        compile_pass=True,
        test_pass=False,
        latency_ms=30000,
    )
    weights = ScoringWeights(success=0.5, coverage=0.3, latency=0.2)
    score = _compute_entry_score(result, weights)

    assert 0.0 <= score <= 1.0
    success_comp = 0.5 * 0.5
    latency_comp = (1.0 - 30 / 300) * 0.2
    expected = (success_comp + latency_comp) / 1.0
    assert abs(score - expected) < 0.001


def test_compute_entry_score_no_coverage():
    result = _make_result(
        "r1",
        "p1",
        "m1",
        "d1",
        1,
        compile_pass=True,
        test_pass=True,
        coverage_pct=None,
    )
    weights = ScoringWeights(success=0.5, coverage=0.3, latency=0.2)
    score = _compute_entry_score(result, weights)

    success_comp = 1.0 * 0.5
    latency_comp = (1.0 - 50 / 300) * 0.2
    expected = (success_comp + latency_comp) / 1.0
    assert abs(score - expected) < 0.001


def test_compute_rankings_empty():
    manifest = _minimal_manifest()
    rankings = _compute_rankings([], manifest.scoring)
    assert rankings == []


def test_compute_rankings_single_provider():
    results = [
        _make_result("r1", "anthropic", "claude-3", "d1", 1),
    ]
    manifest = _minimal_manifest()
    rankings = _compute_rankings(results, manifest.scoring)

    assert len(rankings) == 1
    assert rankings[0]["provider"] == "anthropic"
    assert rankings[0]["model"] == "claude-3"
    assert rankings[0]["rank"] == 1


def test_compute_rankings_multiple_providers():
    results = [
        _make_result("r1", "openai", "gpt-4", "d1", 1, coverage_pct=80.0),
        _make_result("r2", "anthropic", "claude-3", "d1", 1, coverage_pct=90.0),
    ]
    manifest = _minimal_manifest()
    rankings = _compute_rankings(results, manifest.scoring)

    assert len(rankings) == 2
    assert rankings[0]["rank"] == 1
    assert rankings[1]["rank"] == 2


def test_compute_rankings_respects_weights():
    results = [
        _make_result("r1", "p1", "m1", "d1", 1, coverage_pct=50.0),
        _make_result("r2", "p2", "m2", "d1", 1, coverage_pct=100.0),
    ]
    scoring = ScoringConfig(weights=ScoringWeights(success=0.0, coverage=1.0, latency=0.0))
    manifest = BenchmarkManifest(
        manifest_version=1,
        project_root="/test",
        dataset=[{"id": "test", "java_file": "src/Test.java"}],
        matrix={"providers": [{"name": "test", "model": "test"}]},
        evaluation={"compile_cmd": "mvn compile", "test_cmd": "mvn test"},
        scoring=scoring,
    )
    rankings = _compute_rankings(results, manifest.scoring)

    assert rankings[0]["provider"] == "p2"


def test_compute_rankings_excludes_failed_runs():
    results = [
        _make_result("r1", "p1", "m1", "d1", 1, status="ok"),
        _make_result("r2", "p1", "m1", "d1", 2, status="error"),
        _make_result("r3", "p1", "m1", "d1", 3, status="ok"),
    ]
    manifest = _minimal_manifest()
    rankings = _compute_rankings(results, manifest.scoring)

    assert len(rankings) == 1
    assert rankings[0]["run_count"] == 2
    assert rankings[0]["success_count"] == 2


def test_compute_rankings_tie_break_by_success_rate():
    results = [
        _make_result("r1", "p1", "m1", "d1", 1, latency_ms=30000),
        _make_result("r2", "p2", "m2", "d1", 1, latency_ms=30000),
    ]
    manifest = _minimal_manifest()
    rankings = _compute_rankings(results, manifest.scoring)

    assert rankings[0]["avg_score"] == rankings[1]["avg_score"]
    assert rankings[0]["rank"] == 1


def test_build_summary_json_structure():
    results = [
        _make_result("r1", "p1", "m1", "d1", 1),
    ]
    manifest = _minimal_manifest()
    summary = _build_summary_json(results, manifest)

    assert "generated_at" in summary
    assert "statistics" in summary
    assert "rankings" in summary
    assert "scoring_weights" in summary
    assert "diagnostics" in summary
    assert "provenance" in summary


def test_build_report_creates_files(tmp_path):
    results = [
        _make_result("r1", "anthropic", "claude-3", "d1", 1),
    ]
    manifest = _minimal_manifest()

    bundle = build_report(results, manifest, tmp_path)

    assert bundle.results_path.exists()
    assert bundle.summary_path.exists()
    assert bundle.report_path.exists()
    assert bundle.provenance_path.exists()


def test_build_report_results_json_content(tmp_path):
    results = [
        _make_result("r1", "p1", "m1", "d1", 1),
    ]
    manifest = _minimal_manifest()

    bundle = build_report(results, manifest, tmp_path)

    data = json.loads(bundle.results_path.read_text(encoding="utf-8"))
    assert data["total_runs"] == 1
    assert len(data["runs"]) == 1
    assert data["runs"][0]["run_id"] == "r1"
    assert data["generated_at"].endswith("Z")


def test_build_report_summary_json_content(tmp_path):
    results = [
        _make_result("r1", "p1", "m1", "d1", 1),
    ]
    manifest = _minimal_manifest()

    bundle = build_report(results, manifest, tmp_path)

    data = json.loads(bundle.summary_path.read_text(encoding="utf-8"))
    assert data["statistics"]["total_runs"] == 1
    assert len(data["rankings"]) == 1
    assert "diagnostics" in data
    assert data["generated_at"].endswith("Z")


def test_build_report_markdown_has_tables(tmp_path):
    results = [
        _make_result("r1", "p1", "m1", "d1", 1),
    ]
    manifest = _minimal_manifest()

    bundle = build_report(results, manifest, tmp_path)

    content = bundle.report_path.read_text(encoding="utf-8")
    assert "# Benchmark Report" in content
    assert " UTC" in content
    assert "| Rank |" in content
    assert "| Run ID |" in content


def test_load_results_from_dir_empty(tmp_path):
    results = load_results_from_dir(tmp_path)
    assert results == []


def test_load_results_from_dir_with_results(tmp_path):
    run_dir = tmp_path / "run_001"
    run_dir.mkdir()
    result_file = run_dir / "result.json"
    result_file.write_text(
        json.dumps(
            {
                "run_id": "run_001",
                "status": "ok",
                "latency_ms": 50000,
                "metrics": {
                    "compile_pass": True,
                    "test_pass": True,
                    "coverage_pct": None,
                    "failure_type": None,
                    "failure_message": None,
                },
                "output_path": "/output/run_001",
                "provider": "anthropic",
                "model": "claude-3",
                "dataset_id": "svc1",
                "trial": 1,
                "config_snapshot": {},
            }
        ),
        encoding="utf-8",
    )

    results = load_results_from_dir(tmp_path)

    assert len(results) == 1
    assert results[0].run_id == "run_001"
    assert results[0].provider == "anthropic"


def test_load_results_from_dir_skips_invalid(tmp_path):
    run_dir = tmp_path / "run_001"
    run_dir.mkdir()
    result_file = run_dir / "result.json"
    result_file.write_text("not valid json", encoding="utf-8")

    results = load_results_from_dir(tmp_path)
    assert results == []


def test_reporter_serializes_coverage_metadata_fields(tmp_path):
    results = [
        _make_result("r1", "p1", "m1", "d1", 1, coverage_pct=None),
    ]
    results[0].metrics.coverage_source = None
    results[0].metrics.coverage_reason = "coverage_unavailable"
    manifest = _minimal_manifest()

    bundle = build_report(results, manifest, tmp_path)

    results_data = json.loads(bundle.results_path.read_text(encoding="utf-8"))
    metrics = results_data["runs"][0]["metrics"]
    assert "coverage_source" in metrics
    assert "coverage_reason" in metrics
    assert metrics["coverage_pct"] is None
    assert metrics["coverage_reason"] == "coverage_unavailable"


def test_build_report_persists_provenance_and_metadata(tmp_path):
    project_root = tmp_path / "project"
    service_dir = project_root / "src"
    service_dir.mkdir(parents=True)
    (service_dir / "Service.java").write_text("class Service {}", encoding="utf-8")

    manifest = BenchmarkManifest(
        manifest_version=1,
        project_root=str(project_root),
        dataset=[{"id": "svc", "java_file": "src/Service.java"}],
        matrix={"providers": [{"name": "test", "model": "test"}]},
        evaluation={"compile_cmd": "echo compile", "test_cmd": "echo test"},
    )
    results = [_make_result("r1", "p1", "m1", "d1", 1, coverage_pct=40.0)]

    bundle = build_report(results, manifest, tmp_path)

    provenance = json.loads(bundle.provenance_path.read_text(encoding="utf-8"))
    assert provenance["generated_at"].endswith("Z")
    assert provenance["records"][0]["repo_id"] == "svc"
    assert provenance["records"][0]["status"] == "unavailable"
    assert provenance["records"][0]["captured_at"].endswith("Z")

    summary_data = json.loads(bundle.summary_path.read_text(encoding="utf-8"))
    assert "diagnostics" in summary_data
    assert "provenance" in summary_data
    assert summary_data["provenance"]["path"].endswith("provenance.json")


def test_collect_provenance_records_git_success(monkeypatch, tmp_path):
    project_root = tmp_path / "project"
    src_dir = project_root / "src"
    src_dir.mkdir(parents=True)
    (src_dir / "Service.java").write_text("class Service {}", encoding="utf-8")

    manifest = BenchmarkManifest(
        manifest_version=1,
        project_root=str(project_root),
        dataset=[{"id": "svc", "java_file": "src/Service.java"}],
        matrix={"providers": [{"name": "test", "model": "test"}]},
        evaluation={"compile_cmd": "echo compile", "test_cmd": "echo test"},
    )

    monkeypatch.setattr("benchmark.reporter._resolve_repo_root", lambda _: project_root)
    monkeypatch.setattr(
        "benchmark.reporter._git_value",
        lambda _root, args: "main" if args[-1] == "HEAD" else "feature",
    )
    monkeypatch.setattr("benchmark.reporter._git_is_dirty", lambda _root: False)

    records = collect_provenance_records(manifest)

    assert len(records) == 1
    assert records[0].status == "ok"
    assert records[0].resolved_commit == "main"
    assert records[0].dirty is False


def test_compute_diagnostics_includes_preflight_and_coverage_counters():
    results = [
        _make_result("r1", "p1", "m1", "d1", 1, coverage_pct=10.0),
        _make_result("r2", "p1", "m1", "d1", 2, coverage_pct=None),
    ]
    results[0].metrics.coverage_source = "stdout_regex"
    results[1].metrics.coverage_reason = "coverage_unavailable"

    findings = [
        PreflightFinding(
            code="MANIFEST_EXPECTED_TEST_PARENT_MISSING",
            severity="warning",
            manifest_path="dataset[0].expected_test_path",
            message="warn",
            remediation="fix",
        )
    ]

    diagnostics = _compute_diagnostics(results, findings)

    assert diagnostics["preflight_warning_count"] == 1
    assert diagnostics["coverage_fallback_count"] == 1
    assert diagnostics["coverage_unavailable_count"] == 1
