"""Unit tests for benchmark coverage extraction fidelity behavior."""

from __future__ import annotations

from pathlib import Path

import pytest

from benchmark.evaluator import CommandResult, evaluate_run
from benchmark.schemas import EvaluationConfig


def _eval_config() -> EvaluationConfig:
    return EvaluationConfig(
        compile_cmd="mvn -q -DskipTests compile",
        test_cmd="mvn -q test",
        coverage_cmd="mvn -q jacoco:report",
    )


def test_evaluate_run_prefers_jacoco_xml_over_stdout_fallback(tmp_path, monkeypatch):
    project_root = tmp_path / "project"
    run_dir = tmp_path / "run"
    jacoco_dir = project_root / "target/site/jacoco"
    jacoco_dir.mkdir(parents=True)
    run_dir.mkdir(parents=True)

    (jacoco_dir / "jacoco.xml").write_text(
        """
<report name="test">
  <counter type="LINE" missed="20" covered="80"/>
</report>
""".strip(),
        encoding="utf-8",
    )

    command_results = iter(
        [
            CommandResult(True, "", "", 0),
            CommandResult(True, "", "", 0),
            CommandResult(True, "Coverage: 10%", "", 0),
        ]
    )

    def fake_execute_command(cmd: str, project_path: Path, run_path: Path) -> CommandResult:
        del cmd, project_path, run_path
        return next(command_results)

    monkeypatch.setattr("benchmark.evaluator._execute_command", fake_execute_command)

    metrics = evaluate_run(run_dir, _eval_config(), project_root)

    assert metrics.coverage_pct == pytest.approx(80.0)
    assert metrics.coverage_source == "jacoco_xml"
    assert metrics.coverage_reason is None


def test_evaluate_run_uses_stdout_fallback_when_artifact_missing(tmp_path, monkeypatch):
    project_root = tmp_path / "project"
    run_dir = tmp_path / "run"
    project_root.mkdir(parents=True)
    run_dir.mkdir(parents=True)

    command_results = iter(
        [
            CommandResult(True, "", "", 0),
            CommandResult(True, "", "", 0),
            CommandResult(True, "Total instruction coverage: 73.5%", "", 0),
        ]
    )

    def fake_execute_command(cmd: str, project_path: Path, run_path: Path) -> CommandResult:
        del cmd, project_path, run_path
        return next(command_results)

    monkeypatch.setattr("benchmark.evaluator._execute_command", fake_execute_command)

    metrics = evaluate_run(run_dir, _eval_config(), project_root)

    assert metrics.coverage_pct == pytest.approx(73.5)
    assert metrics.coverage_source == "stdout_regex"
    assert metrics.coverage_reason is None


def test_evaluate_run_sets_null_reason_for_out_of_range_fallback(tmp_path, monkeypatch):
    project_root = tmp_path / "project"
    run_dir = tmp_path / "run"
    project_root.mkdir(parents=True)
    run_dir.mkdir(parents=True)

    command_results = iter(
        [
            CommandResult(True, "", "", 0),
            CommandResult(True, "", "", 0),
            CommandResult(True, "Coverage: 140%", "", 0),
        ]
    )

    def fake_execute_command(cmd: str, project_path: Path, run_path: Path) -> CommandResult:
        del cmd, project_path, run_path
        return next(command_results)

    monkeypatch.setattr("benchmark.evaluator._execute_command", fake_execute_command)

    metrics = evaluate_run(run_dir, _eval_config(), project_root)

    assert metrics.coverage_pct is None
    assert metrics.coverage_source is None
    assert metrics.coverage_reason == "coverage_unavailable"
