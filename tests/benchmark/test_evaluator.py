"""Unit tests for benchmark coverage extraction fidelity behavior."""

from __future__ import annotations

from pathlib import Path

import pytest

from benchmark.evaluator import CommandResult, evaluate_run
from benchmark.schemas import EvaluationConfig

pytestmark = pytest.mark.slow


def _eval_config() -> EvaluationConfig:
    return EvaluationConfig(
        compile_cmd="mvn -q -DskipTests compile",
        test_cmd="mvn -q test",
        coverage_cmd="mvn -q jacoco:report",
        jacoco_path="target/site/jacoco/jacoco.xml",
    )


def test_evaluate_run_prefers_jacoco_xml_over_stdout_fallback(tmp_path, monkeypatch):
    """Test that JaCoCo XML is preferred over stdout fallback.

    Note: With the temp-directory architecture, Maven runs in a temp directory,
    so JaCoCo report is generated at temp_path/target/site/jacoco/jacoco.xml.
    The test sets up the jacoco.xml at the correct location (temp_path) since
    that's where Maven will actually run.
    """
    project_root = tmp_path / "project"
    run_dir = tmp_path / "run"
    run_dir.mkdir(parents=True)

    # Create a dummy test file so evaluate_run() can proceed
    (run_dir / "Test.java").write_text(
        "package com.example;\n\npublic class Test {}",
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
        result = next(command_results)

        # Side effect: create jacoco.xml at the project_path location
        # This simulates Maven creating the report when coverage_cmd runs
        if "jacoco" in cmd.lower():
            jacoco_dir = project_path / "target" / "site" / "jacoco"
            jacoco_dir.mkdir(parents=True, exist_ok=True)
            (jacoco_dir / "jacoco.xml").write_text(
                '<report name="test"><counter type="LINE" missed="20" covered="80"/></report>',
                encoding="utf-8",
            )

        return result

    monkeypatch.setattr("benchmark.evaluator._execute_command", fake_execute_command)

    # The project_root is still passed but is only used for reference.
    # Commands run in temp_path created by evaluate_run.
    metrics = evaluate_run(run_dir, _eval_config(), project_root)

    assert metrics.coverage_pct == pytest.approx(80.0)
    assert metrics.coverage_source == "jacoco_xml"
    assert metrics.coverage_reason is None


def test_evaluate_run_uses_stdout_fallback_when_artifact_missing(tmp_path, monkeypatch):
    """Test fallback to stdout regex when jacoco.xml is not available."""
    project_root = tmp_path / "project"
    run_dir = tmp_path / "run"
    project_root.mkdir(parents=True)
    run_dir.mkdir(parents=True)

    # Create a dummy test file so evaluate_run() can proceed
    (run_dir / "Test.java").write_text(
        "package com.example;\n\npublic class Test {}",
        encoding="utf-8",
    )

    command_results = iter(
        [
            CommandResult(True, "", "", 0),
            CommandResult(True, "", "", 0),
            CommandResult(True, "Total instruction coverage: 73.5%", "", 0),
        ]
    )

    def fake_execute_command(cmd: str, project_path: Path, run_path: Path) -> CommandResult:
        return next(command_results)

    monkeypatch.setattr("benchmark.evaluator._execute_command", fake_execute_command)

    metrics = evaluate_run(run_dir, _eval_config(), project_root)

    assert metrics.coverage_pct == pytest.approx(73.5)
    assert metrics.coverage_source == "stdout_regex"
    assert metrics.coverage_reason is None


def test_evaluate_run_sets_null_reason_for_out_of_range_fallback(tmp_path, monkeypatch):
    """Test that out-of-range coverage values are rejected."""
    project_root = tmp_path / "project"
    run_dir = tmp_path / "run"
    project_root.mkdir(parents=True)
    run_dir.mkdir(parents=True)

    # Create a dummy test file so evaluate_run() can proceed
    (run_dir / "Test.java").write_text(
        "package com.example;\n\npublic class Test {}",
        encoding="utf-8",
    )

    command_results = iter(
        [
            CommandResult(True, "", "", 0),
            CommandResult(True, "", "", 0),
            CommandResult(True, "Coverage: 140%", "", 0),
        ]
    )

    def fake_execute_command(cmd: str, project_path: Path, run_path: Path) -> CommandResult:
        return next(command_results)

    monkeypatch.setattr("benchmark.evaluator._execute_command", fake_execute_command)

    metrics = evaluate_run(run_dir, _eval_config(), project_root)

    assert metrics.coverage_pct is None
    assert metrics.coverage_source is None
    assert metrics.coverage_reason == "coverage_unavailable"


def test_evaluate_run_no_project_modification(tmp_path, monkeypatch):
    """Test that evaluate_run doesn't modify the project under test."""
    project_root = tmp_path / "project"
    run_dir = tmp_path / "run"
    project_root.mkdir(parents=True)
    run_dir.mkdir(parents=True)

    # Create src/test/java structure in project to verify it's not modified
    test_java_dir = project_root / "src" / "test" / "java"
    test_java_dir.mkdir(parents=True, exist_ok=True)

    # Create a dummy test file so evaluate_run() can proceed
    (run_dir / "Test.java").write_text(
        "package com.example;\n\npublic class Test {}",
        encoding="utf-8",
    )

    # Track what files exist before
    files_before = set(project_root.rglob("*"))

    command_results = iter(
        [
            CommandResult(True, "", "", 0),
            CommandResult(True, "", "", 0),
            CommandResult(True, "Coverage: 50%", "", 0),
        ]
    )

    def fake_execute_command(cmd: str, project_path: Path, run_path: Path) -> CommandResult:
        return next(command_results)

    monkeypatch.setattr("benchmark.evaluator._execute_command", fake_execute_command)

    metrics = evaluate_run(run_dir, _eval_config(), project_root)

    # Check project was not modified
    files_after = set(project_root.rglob("*"))
    new_files = files_after - files_before

    # There should be no new files in project_root (temp dir is used instead)
    assert not new_files, f"Project was modified! New files: {new_files}"
    assert metrics.compile_pass is True
    assert metrics.test_pass is True


def test_evaluate_run_cleans_up_temp_directory(tmp_path, monkeypatch):
    """Test that temp directory is properly cleaned up after evaluation."""
    project_root = tmp_path / "project"
    run_dir = tmp_path / "run"
    project_root.mkdir(parents=True)
    run_dir.mkdir(parents=True)

    (run_dir / "Test.java").write_text(
        "package com.example;\n\npublic class Test {}",
        encoding="utf-8",
    )

    command_results = iter(
        [
            CommandResult(True, "", "", 0),
            CommandResult(True, "", "", 0),
            CommandResult(True, "Coverage: 50%", "", 0),
        ]
    )

    def fake_execute_command(cmd: str, project_path: Path, run_path: Path) -> CommandResult:
        return next(command_results)

    monkeypatch.setattr("benchmark.evaluator._execute_command", fake_execute_command)

    # List temp directories before
    temp_dirs_before = set(d for d in tmp_path.iterdir() if d.is_dir() and d.name.startswith("eval_tests_"))

    metrics = evaluate_run(run_dir, _eval_config(), project_root)

    # List temp directories after
    temp_dirs_after = set(d for d in tmp_path.iterdir() if d.is_dir() and d.name.startswith("eval_tests_"))

    # Temp directory should be cleaned up
    assert not temp_dirs_after, f"Temp directories not cleaned up: {temp_dirs_after}"
    assert metrics.compile_pass is True
