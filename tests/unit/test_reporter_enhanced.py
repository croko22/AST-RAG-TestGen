from __future__ import annotations

import json
from pathlib import Path

import pytest

from benchmark.reporter import (
    _build_markdown_report,
    _build_rag_comparison_section,
    _compute_statistics,
    _deserialize_run_result,
    _get_retrieval_strategy,
    _serialize_metrics,
    _serialize_run_result,
    export_thesis_metrics_csv,
)
from benchmark.types import EvalMetrics, PipelineTimings, RunResult


def _make_result(
    run_id="r1",
    status="ok",
    provider="test",
    model="model-a",
    coverage_pct=80.0,
    branch_coverage_pct=60.0,
    assertion_count=5,
    test_count=3,
    trivial_flag=False,
    generation_time_ms=1500,
    latency_ms=2000,
    dataset_id="ds1",
    trial=1,
    config_snapshot=None,
    timings=None,
) -> RunResult:
    t = timings or PipelineTimings(parse_ms=100, retrieval_ms=50, prompt_ms=20, llm_ms=1200, postproc_ms=130)
    metrics = EvalMetrics(
        compile_pass=True,
        test_pass=True,
        coverage_pct=coverage_pct,
        branch_coverage_pct=branch_coverage_pct,
        assertion_count=assertion_count,
        test_count=test_count,
        trivial_flag=trivial_flag,
        generation_time_ms=generation_time_ms,
        timings=t,
    )
    return RunResult(
        run_id=run_id,
        status=status,
        latency_ms=latency_ms,
        metrics=metrics,
        output_path=f"/tmp/{run_id}",
        provider=provider,
        model=model,
        dataset_id=dataset_id,
        trial=trial,
        config_snapshot=config_snapshot or {},
    )


class TestSerializeMetrics:
    def test_includes_new_fields(self):
        result = _make_result()
        serialized = _serialize_metrics(result.metrics)

        assert serialized["branch_coverage_pct"] == 60.0
        assert serialized["assertion_count"] == 5
        assert serialized["trivial_flag"] is False
        assert serialized["test_count"] == 3
        assert serialized["quality_score"] == result.metrics.quality_score
        assert serialized["generation_time_ms"] == 1500
        assert serialized["timings"]["parse_ms"] == 100
        assert serialized["timings"]["llm_ms"] == 1200

    def test_quality_score_computed(self):
        result = _make_result(assertion_count=10, test_count=5, trivial_flag=False)
        serialized = _serialize_metrics(result.metrics)

        assert serialized["quality_score"] > 0


class TestSerializeRunResult:
    def test_metrics_have_new_fields(self):
        result = _make_result()
        serialized = _serialize_run_result(result)

        assert "branch_coverage_pct" in serialized["metrics"]
        assert "timings" in serialized["metrics"]
        assert serialized["metrics"]["timings"]["retrieval_ms"] == 50


class TestDeserializeRunResult:
    def test_round_trip_with_new_fields(self):
        result = _make_result()
        serialized = _serialize_run_result(result)
        deserialized = _deserialize_run_result(serialized)

        assert deserialized.metrics.branch_coverage_pct == 60.0
        assert deserialized.metrics.assertion_count == 5
        assert deserialized.metrics.trivial_flag is False
        assert deserialized.metrics.test_count == 3
        assert deserialized.metrics.generation_time_ms == 1500
        assert deserialized.metrics.timings.parse_ms == 100
        assert deserialized.metrics.timings.llm_ms == 1200

    def test_backward_compat_missing_new_fields(self):
        legacy_data = {
            "run_id": "r1",
            "status": "ok",
            "latency_ms": 1000,
            "metrics": {
                "compile_pass": True,
                "test_pass": True,
                "coverage_pct": 75.0,
            },
            "output_path": "/tmp/r1",
            "provider": "test",
            "model": "m1",
            "dataset_id": "ds1",
            "trial": 1,
        }
        result = _deserialize_run_result(legacy_data)

        assert result.metrics.compile_pass is True
        assert result.metrics.coverage_pct == 75.0
        assert result.metrics.branch_coverage_pct is None
        assert result.metrics.timings == PipelineTimings()


class TestMarkdownReport:
    def test_run_details_has_new_columns(self):
        results = [_make_result()]
        from benchmark.schemas import BenchmarkManifest

        manifest = BenchmarkManifest(
            manifest_version=1,
            project_root="/tmp",
            dataset=[{"id": "ds1", "java_file": "Test.java"}],
            matrix={"providers": [{"name": "test", "model": "model-a"}]},
            evaluation={"compile_cmd": "echo ok", "test_cmd": "echo ok"},
        )
        summary = {"statistics": _compute_statistics(results), "rankings": []}

        md = _build_markdown_report(results, manifest, summary)

        assert "Cov%" in md
        assert "Branch%" in md
        assert "Quality" in md
        assert "Trivial" in md
        assert "Gen(ms)" in md

    def test_summary_statistics_include_new_metrics(self):
        results = [
            _make_result(coverage_pct=80.0, branch_coverage_pct=60.0),
            _make_result(run_id="r2", coverage_pct=70.0, branch_coverage_pct=50.0),
        ]
        stats = _compute_statistics(results)

        assert stats["avg_coverage_pct"] == 75.0
        assert stats["avg_branch_coverage_pct"] == 55.0
        assert "avg_quality_score" in stats
        assert "trivial_rate" in stats


class TestRagComparisonSection:
    def test_no_rag_returns_empty(self):
        results = [_make_result(config_snapshot={"retrieval_strategy": "ast"})]
        section = _build_rag_comparison_section(results)
        assert section == []

    def test_with_rag_results(self):
        results = [
            _make_result(
                run_id="ast1",
                provider="p1",
                coverage_pct=60.0,
                config_snapshot={"retrieval_strategy": "ast"},
            ),
            _make_result(
                run_id="rag1",
                provider="p1",
                coverage_pct=80.0,
                config_snapshot={"retrieval_strategy": "rag"},
            ),
        ]
        section = _build_rag_comparison_section(results)

        assert len(section) > 0
        assert any("AST vs RAG" in line for line in section)
        assert any("AST-only" in line for line in section)
        assert any("RAG/Hybrid" in line for line in section)


class TestGetRetrievalStrategy:
    def test_default_ast(self):
        result = _make_result(config_snapshot={})
        assert _get_retrieval_strategy(result) == "ast"

    def test_rag_strategy(self):
        result = _make_result(config_snapshot={"retrieval_strategy": "rag"})
        assert _get_retrieval_strategy(result) == "rag"

    def test_none_config(self):
        result = _make_result(config_snapshot=None)
        assert _get_retrieval_strategy(result) == "ast"


class TestThesisMetricsCsv:
    def test_new_columns_present(self, tmp_path):
        results = [
            _make_result(
                provider="openai",
                model="gpt-4",
                coverage_pct=80.0,
                branch_coverage_pct=60.0,
                assertion_count=10,
                test_count=5,
                trivial_flag=False,
                generation_time_ms=2000,
            ),
        ]
        csv_path = export_thesis_metrics_csv(results, tmp_path)
        content = csv_path.read_text(encoding="utf-8")

        header = content.split("\n")[0]
        assert "branch_coverage_pct" in header
        assert "mutation_score_pct" in header
        assert "quality_score" in header
        assert "trivial_pct" in header
        assert "avg_generation_time_ms" in header
        assert "avg_assertion_count" in header

    def test_csv_values(self, tmp_path):
        results = [
            _make_result(
                provider="openai",
                model="gpt-4",
                coverage_pct=80.0,
                branch_coverage_pct=60.0,
                assertion_count=10,
                test_count=5,
                trivial_flag=False,
                generation_time_ms=2000,
            ),
        ]
        csv_path = export_thesis_metrics_csv(results, tmp_path)
        lines = csv_path.read_text(encoding="utf-8").strip().split("\n")
        data_line = lines[1]
        fields = data_line.split(",")

        assert fields[4] == "60.00"
        assert fields[5] == "0.00"
        assert fields[8] == "10.0"
        assert fields[9] == "2000"

    def test_multiple_providers(self, tmp_path):
        results = [
            _make_result(run_id="r1", provider="p1", model="m1", coverage_pct=80.0),
            _make_result(run_id="r2", provider="p2", model="m2", coverage_pct=60.0),
        ]
        csv_path = export_thesis_metrics_csv(results, tmp_path)
        lines = csv_path.read_text(encoding="utf-8").strip().split("\n")

        assert len(lines) == 3
