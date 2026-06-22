from __future__ import annotations

from unittest.mock import MagicMock

import pytest

from benchmark.evaluator import (
    _extract_coverage,
    _extract_timings,
    _find_jacoco_xml,
    evaluate_run,
)
from benchmark.schemas import EvaluationConfig
from benchmark.types import PipelineTimings
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


def _fake_commands(*results: CommandResult):
    return iter(results)


def _setup_run_dir(tmp_path, test_code="package com.example;\n\npublic class Test {}"):
    run_dir = tmp_path / "run"
    run_dir.mkdir(parents=True)
    (run_dir / "Test.java").write_text(test_code, encoding="utf-8")
    return run_dir


class TestExtractCoverageUsesPostproc:
    def test_branch_coverage_from_jacoco_xml(self, tmp_path):
        jacoco_dir = tmp_path / "target" / "site" / "jacoco"
        jacoco_dir.mkdir(parents=True)
        (jacoco_dir / "jacoco.xml").write_text(
            '<report><counter type="LINE" missed="10" covered="90"/>'
            '<counter type="BRANCH" missed="5" covered="15"/></report>',
            encoding="utf-8",
        )

        line_pct, branch_pct, source, reason = _extract_coverage(tmp_path, "")

        assert line_pct == pytest.approx(90.0)
        assert branch_pct == pytest.approx(75.0)
        assert source == "jacoco_xml"
        assert reason is None

    def test_branch_coverage_none_when_missing(self, tmp_path):
        jacoco_dir = tmp_path / "target" / "site" / "jacoco"
        jacoco_dir.mkdir(parents=True)
        (jacoco_dir / "jacoco.xml").write_text(
            '<report><counter type="LINE" missed="10" covered="90"/></report>',
            encoding="utf-8",
        )

        _, branch_pct, _, _ = _extract_coverage(tmp_path, "")

        assert branch_pct is None

    def test_stdout_fallback_no_branch(self, tmp_path):
        line_pct, branch_pct, source, _ = _extract_coverage(tmp_path, "Coverage: 65.3%")

        assert line_pct == pytest.approx(65.3)
        assert branch_pct is None
        assert source == "stdout_regex"

    def test_no_coverage_available(self, tmp_path):
        line_pct, branch_pct, _, reason = _extract_coverage(tmp_path, "")

        assert line_pct is None
        assert branch_pct is None
        assert reason == "coverage_unavailable"


class TestFindJacocoXml:
    def test_default_path(self, tmp_path):
        jacoco_dir = tmp_path / "target" / "site" / "jacoco"
        jacoco_dir.mkdir(parents=True)
        xml = jacoco_dir / "jacoco.xml"
        xml.write_text("<r/>", encoding="utf-8")

        assert _find_jacoco_xml(tmp_path) == xml

    def test_custom_path(self, tmp_path):
        custom = tmp_path / "reports" / "cov.xml"
        custom.parent.mkdir(parents=True)
        custom.write_text("<r/>", encoding="utf-8")

        assert _find_jacoco_xml(tmp_path, "reports/cov.xml") == custom

    def test_not_found(self, tmp_path):
        assert _find_jacoco_xml(tmp_path) is None


class TestEvaluateRunQualityMetrics:
    def test_quality_fields_populated_on_success(self, tmp_path, monkeypatch):
        run_dir = _setup_run_dir(tmp_path)
        project_root = tmp_path / "project"

        commands = _fake_commands(
            CommandResult(True, "", "", 0),
            CommandResult(True, "", "", 0),
            CommandResult(True, "", "", 0),
        )
        monkeypatch.setattr(
            "benchmark.evaluator.run_command",
            lambda *a, **kw: next(commands),
        )

        metrics = evaluate_run(run_dir, _eval_config(), project_root)

        assert isinstance(metrics.assertion_count, int)
        assert isinstance(metrics.test_count, int)
        assert isinstance(metrics.trivial_flag, bool)
        assert isinstance(metrics.timings, PipelineTimings)

    def test_quality_fields_on_compile_failure(self, tmp_path, monkeypatch):
        run_dir = _setup_run_dir(tmp_path)
        project_root = tmp_path / "project"

        monkeypatch.setattr(
            "benchmark.evaluator.run_command",
            lambda *a, **kw: CommandResult(False, "", "compilation error", 1),
        )

        metrics = evaluate_run(run_dir, _eval_config(), project_root)

        assert metrics.compile_pass is False
        assert metrics.trivial_flag is True
        assert metrics.test_count == 0
        assert metrics.assertion_count == 0

    def test_quality_fields_on_test_failure(self, tmp_path, monkeypatch):
        run_dir = _setup_run_dir(tmp_path)
        project_root = tmp_path / "project"

        commands = _fake_commands(
            CommandResult(True, "", "", 0),
            CommandResult(False, "", "test failed", 1),
        )
        monkeypatch.setattr(
            "benchmark.evaluator.run_command",
            lambda *a, **kw: next(commands),
        )

        metrics = evaluate_run(run_dir, _eval_config(), project_root)

        assert metrics.compile_pass is True
        assert metrics.test_pass is False
        assert isinstance(metrics.assertion_count, int)

    def test_branch_coverage_populated(self, tmp_path, monkeypatch):
        run_dir = _setup_run_dir(tmp_path)
        project_root = tmp_path / "project"

        def fake_run(cmd, cwd, **kwargs):
            if "jacoco" in cmd.lower():
                jacoco_dir = cwd / "target" / "site" / "jacoco"
                jacoco_dir.mkdir(parents=True, exist_ok=True)
                (jacoco_dir / "jacoco.xml").write_text(
                    '<report><counter type="LINE" missed="20" covered="80"/>'
                    '<counter type="BRANCH" missed="8" covered="12"/></report>',
                    encoding="utf-8",
                )
            return CommandResult(True, "", "", 0)

        monkeypatch.setattr("benchmark.evaluator.run_command", fake_run)

        metrics = evaluate_run(run_dir, _eval_config(), project_root)

        assert metrics.coverage_pct == pytest.approx(80.0)
        assert metrics.branch_coverage_pct == pytest.approx(60.0)
        assert metrics.coverage_source == "jacoco_xml"


class TestExtractTimings:
    def test_none_generation_result(self):
        timings = _extract_timings(None)
        assert timings == PipelineTimings()

    def test_with_generation_result(self):
        gen_result = MagicMock()
        gen_result.timings = {
            "parse_ms": 100,
            "retrieval_ms": 200,
            "prompt_ms": 50,
            "llm_ms": 3000,
            "postproc_ms": 150,
        }

        timings = _extract_timings(gen_result)

        assert timings.parse_ms == 100
        assert timings.retrieval_ms == 200
        assert timings.prompt_ms == 50
        assert timings.llm_ms == 3000
        assert timings.postproc_ms == 150

    def test_partial_timings(self):
        gen_result = MagicMock()
        gen_result.timings = {"parse_ms": 100, "llm_ms": 500}

        timings = _extract_timings(gen_result)

        assert timings.parse_ms == 100
        assert timings.llm_ms == 500
        assert timings.retrieval_ms == 0


class TestEvaluateRunWithGenerationResult:
    def test_generation_time_ms_populated(self, tmp_path, monkeypatch):
        run_dir = _setup_run_dir(tmp_path)
        project_root = tmp_path / "project"

        monkeypatch.setattr(
            "benchmark.evaluator.run_command",
            lambda *a, **kw: CommandResult(True, "", "", 0),
        )

        gen_result = MagicMock()
        gen_result.timings = {
            "parse_ms": 50,
            "retrieval_ms": 30,
            "prompt_ms": 10,
            "llm_ms": 2000,
            "postproc_ms": 100,
        }

        metrics = evaluate_run(
            run_dir,
            _eval_config(),
            project_root,
            generation_result=gen_result,
        )

        assert metrics.generation_time_ms == 2190
        assert metrics.timings.parse_ms == 50
        assert metrics.timings.llm_ms == 2000

    def test_backward_compat_without_generation_result(self, tmp_path, monkeypatch):
        run_dir = _setup_run_dir(tmp_path)
        project_root = tmp_path / "project"

        monkeypatch.setattr(
            "benchmark.evaluator.run_command",
            lambda *a, **kw: CommandResult(True, "", "", 0),
        )

        metrics = evaluate_run(run_dir, _eval_config(), project_root)

        assert metrics.generation_time_ms == 0
        assert metrics.timings == PipelineTimings()


class TestEvaluateRunMeaningfulTestCode:
    def test_meaningful_assertions_detected(self, tmp_path, monkeypatch):
        test_code = (
            "package com.example;\n"
            "import org.junit.jupiter.api.Test;\n"
            "import static org.junit.jupiter.api.Assertions.*;\n"
            "public class UsuarioServiceTest {\n"
            "    @Test\n"
            "    void testCrearUsuario() {\n"
            "        assertEquals(1, 1);\n"
            "        assertTrue(true);\n"
            "    }\n"
            "}\n"
        )
        run_dir = _setup_run_dir(tmp_path, test_code)
        project_root = tmp_path / "project"

        monkeypatch.setattr(
            "benchmark.evaluator.run_command",
            lambda *a, **kw: CommandResult(True, "", "", 0),
        )

        metrics = evaluate_run(run_dir, _eval_config(), project_root)

        assert metrics.test_count == 1
        assert metrics.assertion_count >= 2
        assert metrics.trivial_flag is False

    def test_no_test_file_returns_early(self, tmp_path):
        run_dir = tmp_path / "empty_run"
        run_dir.mkdir()

        metrics = evaluate_run(run_dir, _eval_config(), tmp_path / "project")

        assert metrics.compile_pass is False
        assert metrics.failure_type == "error"
        assert "No test file" in metrics.failure_message
