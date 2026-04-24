"""Unit tests for PIT mutation columns in benchmark reporter."""

from __future__ import annotations

from benchmark.reporter import _build_markdown_report, _serialize_metrics
from benchmark.schemas import BenchmarkManifest
from benchmark.types import EvalMetrics, PipelineTimings, RunResult


def _make_result(
    mutation_score_pct=None,
    killed_mutations=None,
    total_mutations=None,
) -> RunResult:
    metrics = EvalMetrics(
        compile_pass=True,
        test_pass=True,
        coverage_pct=80.0,
        branch_coverage_pct=60.0,
        mutation_score_pct=mutation_score_pct,
        killed_mutations=killed_mutations,
        total_mutations=total_mutations,
    )
    return RunResult(
        run_id="r1",
        status="ok",
        latency_ms=1000,
        metrics=metrics,
        output_path="/tmp/r1",
        provider="test",
        model="m1",
        dataset_id="ds1",
        trial=1,
        config_snapshot={},
    )


def _minimal_manifest() -> BenchmarkManifest:
    return BenchmarkManifest(
        manifest_version=1,
        project_root="/test",
        dataset=[{"id": "test", "java_file": "src/Test.java"}],
        matrix={"providers": [{"name": "test", "model": "test"}]},
        evaluation={"compile_cmd": "mvn compile", "test_cmd": "mvn test"},
    )


class TestReporterMutationColumns:
    def test_reporter_includes_mutation_columns_when_data_present(self):
        results = [
            _make_result(mutation_score_pct=75.0, killed_mutations=6, total_mutations=8)
        ]
        manifest = _minimal_manifest()
        summary = {"statistics": {}, "rankings": []}

        md = _build_markdown_report(results, manifest, summary)

        assert "Mut%" in md
        assert "75.0" in md

    def test_reporter_excludes_mutation_columns_when_data_absent(self):
        results = [_make_result()]
        manifest = _minimal_manifest()
        summary = {"statistics": {}, "rankings": []}

        md = _build_markdown_report(results, manifest, summary)

        assert "Mut%" not in md


class TestSerializeMutationMetrics:
    def test_serialize_metrics_includes_mutation_fields(self):
        result = _make_result(mutation_score_pct=75.0, killed_mutations=6, total_mutations=8)
        serialized = _serialize_metrics(result.metrics)

        assert serialized["mutation_score_pct"] == 75.0
        assert serialized["killed_mutations"] == 6
        assert serialized["total_mutations"] == 8

    def test_serialize_metrics_missing_mutation_fields(self):
        result = _make_result()
        serialized = _serialize_metrics(result.metrics)

        assert "mutation_score_pct" in serialized
        assert serialized["mutation_score_pct"] is None
        assert "killed_mutations" in serialized
        assert serialized["killed_mutations"] is None
        assert "total_mutations" in serialized
        assert serialized["total_mutations"] is None
