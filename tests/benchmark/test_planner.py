"""Unit tests for benchmark planner deterministic expansion."""

from __future__ import annotations

import json

import pytest

from benchmark.manifest import load_manifest
from benchmark.planner import plan_runs, _generate_run_id


def _manifest_for_planner() -> dict:
    return {
        "manifest_version": 1,
        "project_root": "mock-java-project",
        "dataset": [
            {"id": "z-dataset", "java_file": "src/ZService.java"},
            {"id": "a-dataset", "java_file": "src/AService.java"},
            {"id": "m-dataset", "java_file": "src/MService.java"},
        ],
        "matrix": {
            "providers": [
                {"name": "z-provider", "model": "z-model"},
                {"name": "a-provider", "model": "a-model"},
            ]
        },
        "run": {
            "trials": 2,
            "seed": 42,
            "max_dependencies": 5,
            "timeout_seconds": 120,
            "retry_count": 1,
        },
        "evaluation": {
            "compile_cmd": "mvn compile",
            "test_cmd": "mvn test",
        },
    }


def test_plan_runs_generates_correct_count(tmp_path):
    manifest_path = tmp_path / "benchmark.json"
    manifest_path.write_text(json.dumps(_manifest_for_planner()), encoding="utf-8")

    manifest = load_manifest(manifest_path)
    plans = plan_runs(manifest)

    assert len(plans) == 12


def test_plan_runs_deterministic_ordering(tmp_path):
    manifest_path = tmp_path / "benchmark.json"
    manifest_path.write_text(json.dumps(_manifest_for_planner()), encoding="utf-8")

    manifest = load_manifest(manifest_path)
    plans1 = plan_runs(manifest)
    plans2 = plan_runs(manifest)

    assert len(plans1) == len(plans2)
    for p1, p2 in zip(plans1, plans2):
        assert p1.run_id == p2.run_id
        assert p1.dataset_id == p2.dataset_id
        assert p1.provider == p2.provider
        assert p1.model == p2.model
        assert p1.trial == p2.trial


def test_plan_runs_sorted_by_dataset_then_provider_then_trial(tmp_path):
    manifest_path = tmp_path / "benchmark.json"
    manifest_path.write_text(json.dumps(_manifest_for_planner()), encoding="utf-8")

    manifest = load_manifest(manifest_path)
    plans = plan_runs(manifest)

    dataset_ids = [p.dataset_id for p in plans]
    assert dataset_ids == sorted(dataset_ids)

    for dataset_id in set(dataset_ids):
        dataset_plans = [p for p in plans if p.dataset_id == dataset_id]
        for provider_name in {"a-provider", "z-provider"}:
            provider_plans = [
                p for p in dataset_plans if p.provider == provider_name
            ]
            trials = [p.trial for p in provider_plans]
            assert trials == [1, 2]


def test_plan_runs_run_ids_are_unique(tmp_path):
    manifest_path = tmp_path / "benchmark.json"
    manifest_path.write_text(json.dumps(_manifest_for_planner()), encoding="utf-8")

    manifest = load_manifest(manifest_path)
    plans = plan_runs(manifest)

    run_ids = [p.run_id for p in plans]
    assert len(run_ids) == len(set(run_ids))


def test_plan_runs_seed_included_in_config_snapshot(tmp_path):
    base_manifest = _manifest_for_planner()
    base_manifest["run"]["seed"] = 42
    manifest_path = tmp_path / "benchmark1.json"
    manifest_path.write_text(json.dumps(base_manifest), encoding="utf-8")

    manifest1 = load_manifest(manifest_path)
    plans1 = plan_runs(manifest1)

    base_manifest["run"]["seed"] = 99
    manifest_path2 = tmp_path / "benchmark2.json"
    manifest_path2.write_text(json.dumps(base_manifest), encoding="utf-8")

    manifest2 = load_manifest(manifest_path2)
    plans2 = plan_runs(manifest2)

    assert plans1[0].seed == 42
    assert plans2[0].seed == 99


def test_plan_runs_single_provider_single_trial(tmp_path):
    manifest = {
        "manifest_version": 1,
        "project_root": "project",
        "dataset": [{"id": "svc", "java_file": "src/Service.java"}],
        "matrix": {"providers": [{"name": "anthropic", "model": "claude-3"}]},
        "evaluation": {"compile_cmd": "mvn compile", "test_cmd": "mvn test"},
    }
    manifest_path = tmp_path / "benchmark.json"
    manifest_path.write_text(json.dumps(manifest), encoding="utf-8")

    loaded = load_manifest(manifest_path)
    plans = plan_runs(loaded)

    assert len(plans) == 1
    plan = plans[0]
    assert plan.dataset_id == "svc"
    assert plan.provider == "anthropic"
    assert plan.model == "claude-3"
    assert plan.trial == 1


def test_plan_runs_includes_all_config(tmp_path):
    manifest = {
        "manifest_version": 1,
        "project_root": "/full/path",
        "dataset": [{"id": "svc", "java_file": "src/Service.java"}],
        "matrix": {"providers": [{"name": "openai", "model": "gpt-4"}]},
        "run": {
            "trials": 3,
            "seed": 12345,
            "max_dependencies": 7,
            "timeout_seconds": 600,
            "retry_count": 2,
            "concurrency": 4,
        },
        "evaluation": {"compile_cmd": "mvn compile", "test_cmd": "mvn test"},
    }
    manifest_path = tmp_path / "benchmark.json"
    manifest_path.write_text(json.dumps(manifest), encoding="utf-8")

    loaded = load_manifest(manifest_path)
    plans = plan_runs(loaded)

    assert len(plans) == 3
    for i, plan in enumerate(plans, 1):
        assert plan.project_root == "/full/path"
        assert plan.seed == 12345
        assert plan.max_dependencies == 7
        assert plan.timeout_seconds == 600
        assert plan.retry_count == 2
        assert plan.trial == i


def test_generate_run_id_format():
    run_id = _generate_run_id("dataset1", "provider1", "model1", 1)
    assert run_id.startswith("run_")
    assert len(run_id) == 16


def test_generate_run_id_deterministic():
    id1 = _generate_run_id("svc", "anthropic", "claude", 1)
    id2 = _generate_run_id("svc", "anthropic", "claude", 1)
    assert id1 == id2


def test_generate_run_id_differs_by_inputs():
    id1 = _generate_run_id("svc", "anthropic", "claude", 1)
    id2 = _generate_run_id("svc", "anthropic", "claude", 2)
    assert id1 != id2
