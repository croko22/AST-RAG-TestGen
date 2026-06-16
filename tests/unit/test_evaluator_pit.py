"""Unit tests for PIT integration in benchmark evaluator."""

from __future__ import annotations

import pytest

from benchmark.evaluator import evaluate_run
from benchmark.schemas import EvaluationConfig
from postproc._runner import CommandResult


def _eval_config(**overrides) -> EvaluationConfig:
    defaults = dict(
        compile_cmd="mvn -q -DskipTests compile",
        test_cmd="mvn -q test",
        coverage_cmd="mvn -q jacoco:report",
        jacoco_path="target/site/jacoco/jacoco.xml",
    )
    defaults.update(overrides)
    return EvaluationConfig(**defaults)


def _setup_run_dir(tmp_path, test_code="package com.example;\n\npublic class Test {}"):
    run_dir = tmp_path / "run"
    run_dir.mkdir(parents=True)
    (run_dir / "Test.java").write_text(test_code, encoding="utf-8")
    return run_dir


class TestEvaluateRunPitIntegration:
    def test_mutation_fields_populated_when_pit_enabled(self, tmp_path, monkeypatch):
        run_dir = _setup_run_dir(tmp_path)
        project_root = tmp_path / "project"

        monkeypatch.setattr(
            "benchmark.evaluator.run_command",
            lambda *a, **kw: CommandResult(True, "", "", 0),
        )
        monkeypatch.setattr(
            "benchmark.evaluator.parse_pit_report",
            lambda path: {
                "mutation_score_pct": 75.0,
                "killed_mutations": 6,
                "total_mutations": 8,
            },
        )

        metrics = evaluate_run(run_dir, _eval_config(run_pit=True), project_root)

        assert metrics.mutation_score_pct == pytest.approx(75.0)
        assert metrics.killed_mutations == 6
        assert metrics.total_mutations == 8
        assert metrics.compile_pass is True
        assert metrics.test_pass is True

    def test_mutation_fields_none_when_pit_parser_returns_none(self, tmp_path, monkeypatch):
        run_dir = _setup_run_dir(tmp_path)
        project_root = tmp_path / "project"

        monkeypatch.setattr(
            "benchmark.evaluator.run_command",
            lambda *a, **kw: CommandResult(True, "", "", 0),
        )
        monkeypatch.setattr(
            "benchmark.evaluator.parse_pit_report",
            lambda path: None,
        )

        metrics = evaluate_run(run_dir, _eval_config(run_pit=True), project_root)

        assert metrics.mutation_score_pct is None
        assert metrics.killed_mutations is None
        assert metrics.total_mutations is None
        assert metrics.compile_pass is True
        assert metrics.test_pass is True

    def test_pit_command_executed_when_pit_cmd_set(self, tmp_path, monkeypatch):
        run_dir = _setup_run_dir(tmp_path)
        project_root = tmp_path / "project"

        executed_commands = []

        def fake_run(cmd, cwd, **kwargs):
            executed_commands.append(cmd)
            return CommandResult(True, "", "", 0)

        monkeypatch.setattr("benchmark.evaluator.run_command", fake_run)
        monkeypatch.setattr(
            "benchmark.evaluator.parse_pit_report",
            lambda path: {
                "mutation_score_pct": 50.0,
                "killed_mutations": 1,
                "total_mutations": 2,
            },
        )

        metrics = evaluate_run(
            run_dir,
            _eval_config(pit_cmd="mvn pitest:mutationCoverage"),
            project_root,
        )

        assert any("pitest" in cmd for cmd in executed_commands)
        assert metrics.mutation_score_pct == pytest.approx(50.0)
        assert metrics.killed_mutations == 1
        assert metrics.total_mutations == 2

    def test_mutation_fields_none_when_pit_disabled(self, tmp_path, monkeypatch):
        run_dir = _setup_run_dir(tmp_path)
        project_root = tmp_path / "project"

        monkeypatch.setattr(
            "benchmark.evaluator.run_command",
            lambda *a, **kw: CommandResult(True, "", "", 0),
        )

        metrics = evaluate_run(run_dir, _eval_config(), project_root)

        assert metrics.mutation_score_pct is None
        assert metrics.killed_mutations is None
        assert metrics.total_mutations is None
