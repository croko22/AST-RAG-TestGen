"""Integration tests for benchmark runner scaffold behavior."""

from __future__ import annotations

import pytest

pytestmark = pytest.mark.slow

import json
from pathlib import Path

from benchmark import runner
from benchmark.planner import plan_runs
from benchmark.runner import (
    _create_run_workspace,
    _write_run_result,
    execute_run,
    execute_runs,
)
from benchmark.schemas import BenchmarkManifest, EvaluationConfig
from benchmark.types import EvalMetrics, RunPlan, RunResult
from orchestration.generator import GenerationResult


def _minimal_manifest() -> BenchmarkManifest:
    return BenchmarkManifest(
        manifest_version=1,
        project_root="mock-java-project",
        dataset=[
            {"id": "svc", "java_file": "src/Service.java"},
        ],
        matrix={"providers": [{"name": "test", "model": "test-model"}]},
        run={"trials": 1, "seed": 0, "max_dependencies": 5},
        evaluation={"compile_cmd": "mvn compile", "test_cmd": "mvn test"},
    )


def test_create_run_workspace_creates_directory(tmp_path):
    run_dir = _create_run_workspace(tmp_path, "run_abc123")
    assert run_dir.exists()
    assert run_dir.is_dir()
    assert run_dir.name == "run_abc123"
    assert run_dir.parent == tmp_path


def test_create_run_workspace_isolated_per_run(tmp_path):
    run_dir1 = _create_run_workspace(tmp_path, "run_001")
    run_dir2 = _create_run_workspace(tmp_path, "run_002")
    assert run_dir1 != run_dir2
    assert run_dir1.exists()
    assert run_dir2.exists()


def test_write_run_result_creates_json(tmp_path):
    run_dir = tmp_path / "run_test"
    run_dir.mkdir()

    result = RunResult(
        run_id="run_test",
        status="ok",
        latency_ms=1500,
        metrics=EvalMetrics(compile_pass=True, test_pass=True, coverage_pct=85.5),
        output_path=str(run_dir / "Test.java"),
        provider="test",
        model="model",
        dataset_id="svc",
        trial=1,
        config_snapshot={"seed": 0},
    )

    _write_run_result(run_dir, result)

    result_path = run_dir / "result.json"
    assert result_path.exists()

    data = json.loads(result_path.read_text())
    assert data["run_id"] == "run_test"
    assert data["status"] == "ok"
    assert data["latency_ms"] == 1500
    assert data["metrics"]["compile_pass"] is True
    assert data["metrics"]["test_pass"] is True
    assert data["metrics"]["coverage_pct"] == 85.5


def test_execute_run_dry_run_creates_workspace(tmp_path):
    manifest = _minimal_manifest()
    plans = plan_runs(manifest)
    plan = plans[0]

    result = execute_run(plan, tmp_path, dry_run=True)

    assert result.status == "dry_run"
    assert result.run_id == plan.run_id

    run_dir = tmp_path / plan.run_id
    assert run_dir.exists()
    assert run_dir.is_dir()


def test_execute_run_dry_run_does_not_call_llm(tmp_path):
    manifest = _minimal_manifest()
    plans = plan_runs(manifest)
    plan = plans[0]

    result = execute_run(plan, tmp_path, dry_run=True)

    assert result.latency_ms == 0


def test_execute_runs_collects_all_results(tmp_path):
    manifest = BenchmarkManifest(
        manifest_version=1,
        project_root="mock-java-project",
        dataset=[{"id": "svc1", "java_file": "src/Svc1.java"}],
        matrix={"providers": [{"name": "test", "model": "m1"}]},
        run={"trials": 2, "seed": 0},
        evaluation={"compile_cmd": "mvn compile", "test_cmd": "mvn test"},
    )
    plans = plan_runs(manifest)

    results = execute_runs(plans, tmp_path, dry_run=True)

    assert len(results) == len(plans)
    assert all(r.status == "dry_run" for r in results)


def test_execute_runs_continues_after_failure(tmp_path):
    class FailingPlan:
        run_id = "fail-run"
        dataset_id = "svc"
        java_file = "src/Service.java"
        provider = "test"
        model = "model"
        trial = 1
        seed = 0
        max_dependencies = 5
        timeout_seconds = 60
        retry_count = 0
        project_root = "project"

    failing_plan = FailingPlan()

    results = execute_runs([failing_plan], tmp_path, dry_run=True)

    assert len(results) == 1
    assert results[0].run_id == "fail-run"


def test_execute_run_includes_config_snapshot(tmp_path):
    manifest = _minimal_manifest()
    plans = plan_runs(manifest)
    plan = plans[0]

    result = execute_run(plan, tmp_path, dry_run=True)

    assert "run_id" in result.config_snapshot
    assert result.config_snapshot["run_id"] == plan.run_id
    assert result.config_snapshot["dataset_id"] == plan.dataset_id
    assert result.config_snapshot["provider"] == plan.provider
    assert result.config_snapshot["seed"] == plan.seed


def test_execute_run_with_multiple_providers(tmp_path):
    manifest = BenchmarkManifest(
        manifest_version=1,
        project_root="mock-java-project",
        dataset=[{"id": "svc", "java_file": "src/Service.java"}],
        matrix={
            "providers": [
                {"name": "provider-a", "model": "model-a"},
                {"name": "provider-b", "model": "model-b"},
            ]
        },
        run={"trials": 1, "seed": 0},
        evaluation={"compile_cmd": "mvn compile", "test_cmd": "mvn test"},
    )
    plans = plan_runs(manifest)

    results = execute_runs(plans, tmp_path, dry_run=True)

    assert len(results) == 2

    run_dirs = list(tmp_path.iterdir())
    assert len(run_dirs) == 2

    provider_ids = {r.provider for r in results}
    assert provider_ids == {"provider-a", "provider-b"}


def test_execute_runs_threads_eval_config_to_each_plan(tmp_path, monkeypatch):
    manifest = _minimal_manifest()
    plans = plan_runs(manifest)

    captured_eval_configs = []

    def fake_execute_run(plan, base_output_dir, dry_run=False, eval_config=None):
        del base_output_dir, dry_run
        captured_eval_configs.append((plan.run_id, eval_config))
        return RunResult(
            run_id=plan.run_id,
            status="dry_run",
            latency_ms=0,
            metrics=EvalMetrics(compile_pass=False, test_pass=False),
            output_path="",
            provider=plan.provider,
            model=plan.model,
            dataset_id=plan.dataset_id,
            trial=plan.trial,
            config_snapshot={},
        )

    monkeypatch.setattr("benchmark.runner.execute_run", fake_execute_run)

    results = execute_runs(
        plans,
        tmp_path,
        dry_run=True,
        eval_config=manifest.evaluation,
    )

    assert len(results) == len(plans)
    assert len(captured_eval_configs) == len(plans)
    assert all(eval_cfg == manifest.evaluation for _, eval_cfg in captured_eval_configs)


# --- Repair loop (T1) -------------------------------------------------------


def _eval_config() -> EvaluationConfig:
    return EvaluationConfig(compile_cmd="mvn -q test-compile", test_cmd="mvn -q test")


def _repair_plan(tmp_path, repair_attempts: int = 2) -> RunPlan:
    return RunPlan(
        run_id="run_repair",
        dataset_id="svc",
        java_file="src/Service.java",
        provider="test",
        model="m",
        trial=1,
        seed=0,
        max_dependencies=5,
        timeout_seconds=10,
        retry_count=0,
        project_root=str(tmp_path),
        repair_attempts=repair_attempts,
    )


def _write_fake_generation(output_dir, code="class ServiceTest {}"):
    target = Path(output_dir) / "ServiceTest.java"
    target.write_text(code, encoding="utf-8")
    return GenerationResult(test_code=code, output_path=str(target))


def test_attempt_rank_prefers_compiling_attempt():
    compiling = EvalMetrics(compile_pass=True, test_pass=False, compile_errors=[])
    failing_few = EvalMetrics(compile_pass=False, test_pass=False, compile_errors=["a"])
    failing_many = EvalMetrics(compile_pass=False, test_pass=False, compile_errors=["a", "b"])

    assert runner._attempt_rank(compiling) > runner._attempt_rank(failing_few)
    assert runner._attempt_rank(failing_few) > runner._attempt_rank(failing_many)


def test_error_signature_none_when_empty():
    assert runner._error_signature(EvalMetrics(compile_pass=False, test_pass=False)) is None


def test_error_signature_is_order_independent():
    a = EvalMetrics(compile_pass=False, test_pass=False, compile_errors=["b", "a"])
    b = EvalMetrics(compile_pass=False, test_pass=False, compile_errors=["a", "b"])

    assert runner._error_signature(a) == runner._error_signature(b)


def test_execute_with_repair_retries_until_compile_passes(tmp_path, monkeypatch):
    plan = _repair_plan(tmp_path)
    calls = {"generation": 0, "feedback": []}

    def fake_generation(plan, output_dir, feedback_context=None):
        calls["generation"] += 1
        calls["feedback"].append(feedback_context)
        return str(output_dir), 7, _write_fake_generation(output_dir)

    def fake_eval(run_dir, eval_config, project_root, generation_result=None):
        if calls["generation"] == 1:
            return EvalMetrics(
                compile_pass=False,
                test_pass=False,
                failure_type="compile_failed",
                failure_message="boom",
                compile_errors=["ServiceTest.java:3: cannot find symbol"],
            )
        return EvalMetrics(compile_pass=True, test_pass=False)

    monkeypatch.setattr(runner, "_execute_generation", fake_generation)
    monkeypatch.setattr(runner, "_run_with_timeout", lambda func, timeout, *a, **k: func(*a, **k))
    monkeypatch.setattr("benchmark.evaluator.evaluate_run", fake_eval)

    metrics, _, latency_ms = runner._execute_with_repair(
        plan, tmp_path, _eval_config(), max_attempts=3
    )

    assert metrics.compile_pass is True
    assert metrics.attempt_count == 2
    assert calls["generation"] == 2
    assert calls["feedback"][0] is None
    assert "cannot find symbol" in calls["feedback"][1]
    assert latency_ms == 14


def test_execute_with_repair_stops_when_errors_do_not_change(tmp_path, monkeypatch):
    plan = _repair_plan(tmp_path)
    calls = {"generation": 0}

    def fake_generation(plan, output_dir, feedback_context=None):
        calls["generation"] += 1
        return str(output_dir), 5, _write_fake_generation(output_dir)

    def fake_eval(run_dir, eval_config, project_root, generation_result=None):
        return EvalMetrics(
            compile_pass=False,
            test_pass=False,
            failure_type="compile_failed",
            compile_errors=["ServiceTest.java:3: cannot find symbol"],
        )

    monkeypatch.setattr(runner, "_execute_generation", fake_generation)
    monkeypatch.setattr(runner, "_run_with_timeout", lambda func, timeout, *a, **k: func(*a, **k))
    monkeypatch.setattr("benchmark.evaluator.evaluate_run", fake_eval)

    metrics, _, _ = runner._execute_with_repair(plan, tmp_path, _eval_config(), max_attempts=5)

    # Attempt 1 -> attempt 2 with identical errors -> stop (no progress).
    assert calls["generation"] == 2
    assert metrics.compile_pass is False


def test_execute_with_repair_without_eval_config_is_single_success(tmp_path, monkeypatch):
    plan = _repair_plan(tmp_path)
    calls = {"generation": 0}

    def fake_generation(plan, output_dir, feedback_context=None):
        calls["generation"] += 1
        return str(output_dir), 1, _write_fake_generation(output_dir)

    monkeypatch.setattr(runner, "_execute_generation", fake_generation)
    monkeypatch.setattr(runner, "_run_with_timeout", lambda func, timeout, *a, **k: func(*a, **k))

    metrics, _, _ = runner._execute_with_repair(plan, tmp_path, None, max_attempts=3)

    assert metrics.compile_pass is True
    assert calls["generation"] == 1
