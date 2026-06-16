"""Benchmark evaluator: executes compile/test/coverage commands and parses metrics."""

from __future__ import annotations

import re
import shutil
import tempfile
from pathlib import Path
from typing import TYPE_CHECKING

from benchmark.schemas import EvaluationConfig
from benchmark.types import EvalMetrics, PipelineTimings, RunResult
from postproc._runner import run_command, stage_test_file
from postproc.pit_parser import parse_pit_report
from postproc.quality import assess_test_quality
from postproc.validator import parse_coverage

if TYPE_CHECKING:
    from orchestration.generator import GenerationResult


def evaluate_run(
    run_dir: Path | str,
    eval_config: EvaluationConfig,
    project_root: Path | str,
    generation_result: GenerationResult | None = None,
) -> EvalMetrics:
    """
    Execute evaluation commands for a generated test run.

    Args:
        run_dir: Directory containing the generated test file
        eval_config: Evaluation commands from manifest
        project_root: Root directory of the Java project
        generation_result: Optional generation result with per-step timings

    Returns:
        EvalMetrics with compile_pass, test_pass, coverage_pct, and failure info
    """
    run_path = Path(run_dir)

    test_files = list(run_path.glob("*.java"))
    if not test_files:
        return EvalMetrics(
            compile_pass=False,
            test_pass=False,
            failure_type="error",
            failure_message="No test file found in run directory",
        )

    test_file = test_files[0]
    test_content = test_file.read_text(encoding="utf-8")

    quality = assess_test_quality(test_content)
    quality_score = quality.score / 100.0

    timings = _extract_timings(generation_result)
    generation_time_ms = (
        sum(generation_result.timings.values())
        if generation_result is not None and hasattr(generation_result, "timings")
        else 0
    )

    temp_test_dir: tempfile.TemporaryDirectory | None = None
    try:
        temp_test_dir = tempfile.TemporaryDirectory(prefix="eval_tests_")
        temp_path = Path(temp_test_dir.name)

        project_root_path = Path(project_root)
        if (project_root_path / "pom.xml").exists():
            shutil.copytree(project_root_path, temp_path, dirs_exist_ok=True)

        stage_test_file(test_file, temp_path)

        compile_result = run_command(eval_config.compile_cmd, temp_path)
        if not compile_result.success:
            stderr_text = compile_result.stderr or ""
            return EvalMetrics(
                compile_pass=False,
                test_pass=False,
                failure_type="compile_failed",
                failure_message=stderr_text[:500] if stderr_text else "Compilation failed",
                assertion_count=quality.assertion_count,
                test_count=quality.test_count,
                trivial_flag=quality.trivial_flag,
                quality_score=quality_score,
                timings=timings,
                generation_time_ms=generation_time_ms,
                compile_errors=[line for line in stderr_text.split("\n") if line.strip()],
            )

        test_result = run_command(eval_config.test_cmd, temp_path)
        if not test_result.success:
            stderr_text = test_result.stderr or ""
            return EvalMetrics(
                compile_pass=True,
                test_pass=False,
                failure_type="test_failed",
                failure_message=stderr_text[:500] if stderr_text else "Tests failed",
                assertion_count=quality.assertion_count,
                test_count=quality.test_count,
                trivial_flag=quality.trivial_flag,
                quality_score=quality_score,
                timings=timings,
                generation_time_ms=generation_time_ms,
                compile_errors=[line for line in stderr_text.split("\n") if line.strip()],
            )

        coverage_pct: float | None = None
        branch_coverage_pct: float | None = None
        coverage_source: str | None = None
        coverage_reason: str | None = None

        if eval_config.coverage_cmd:
            coverage_result = run_command(eval_config.coverage_cmd, temp_path)
            coverage_pct, branch_coverage_pct, coverage_source, coverage_reason = _extract_coverage(
                project_path=temp_path,
                coverage_stdout=coverage_result.stdout,
                jacoco_path=eval_config.jacoco_path,
            )
        else:
            coverage_reason = "coverage_cmd_not_configured"

        mutation_score_pct: float | None = None
        killed_mutations: int | None = None
        total_mutations: int | None = None

        if eval_config.run_pit or eval_config.pit_cmd:
            if eval_config.pit_cmd:
                run_command(eval_config.pit_cmd, temp_path)
            pit_report_dir = temp_path / (eval_config.pit_path or "target/pit-reports")
            pit_data = parse_pit_report(str(pit_report_dir))
            if pit_data is not None:
                mutation_score_pct = pit_data.get("mutation_score_pct")
                killed_mutations = pit_data.get("killed_mutations")
                total_mutations = pit_data.get("total_mutations")

        return EvalMetrics(
            compile_pass=True,
            test_pass=True,
            coverage_pct=coverage_pct,
            branch_coverage_pct=branch_coverage_pct,
            coverage_source=coverage_source,
            coverage_reason=coverage_reason,
            mutation_score_pct=mutation_score_pct,
            killed_mutations=killed_mutations,
            total_mutations=total_mutations,
            assertion_count=quality.assertion_count,
            test_count=quality.test_count,
            trivial_flag=quality.trivial_flag,
            quality_score=quality_score,
            timings=timings,
            generation_time_ms=generation_time_ms,
            compile_errors=[],
        )
    finally:
        if temp_test_dir is not None:
            try:
                temp_test_dir.cleanup()
            except Exception:
                pass


def _extract_timings(generation_result: GenerationResult | None) -> PipelineTimings:
    if generation_result is None or not hasattr(generation_result, "timings"):
        return PipelineTimings()
    t = generation_result.timings
    return PipelineTimings(
        parse_ms=t.get("parse_ms", 0),
        retrieval_ms=t.get("retrieval_ms", 0),
        prompt_ms=t.get("prompt_ms", 0),
        llm_ms=t.get("llm_ms", 0),
        postproc_ms=t.get("postproc_ms", 0),
    )


def _extract_coverage(
    project_path: Path,
    coverage_stdout: str,
    jacoco_path: str | None = None,
) -> tuple[float | None, float | None, str | None, str | None]:
    jacoco_xml_path = _find_jacoco_xml(project_path, jacoco_path)
    if jacoco_xml_path is not None:
        cov = parse_coverage(jacoco_xml_path)
        if cov.line_pct is not None:
            return cov.line_pct, cov.branch_pct, "jacoco_xml", None

    fallback = _extract_coverage_from_stdout(coverage_stdout)
    if fallback is not None:
        return fallback, None, "stdout_regex", None

    return None, None, None, "coverage_unavailable"


def _find_jacoco_xml(
    project_path: Path,
    jacoco_path: str | None = None,
) -> Path | None:
    if jacoco_path is None:
        jacoco_path = "target/site/jacoco/jacoco.xml"

    candidates = [
        project_path / jacoco_path,
        project_path / "target" / "site" / "jacoco" / "jacoco.xml",
        project_path / "jacoco.xml",
        project_path / "target" / "jacoco.xml",
    ]
    for path in candidates:
        if path.exists():
            return path
    return None


def _extract_coverage_from_stdout(output: str) -> float | None:
    output_lower = output.lower()

    labeled_match = re.search(
        r"(?:instruction|line|branch|class)?\s*coverage[:\s]+(\d+(?:\.\d+)?)\s*%?",
        output_lower,
    )
    if labeled_match:
        return _bounded_percent(float(labeled_match.group(1)))

    simple_pct_match = re.search(r"(\d+(?:\.\d+)?)\s*%", output)
    if simple_pct_match:
        return _bounded_percent(float(simple_pct_match.group(1)))

    return None


def _bounded_percent(value: float) -> float | None:
    if 0.0 <= value <= 100.0:
        return value
    return None


def merge_run_metrics(
    run_result: RunResult,
    eval_metrics: EvalMetrics,
) -> RunResult:
    run_result.metrics = eval_metrics
    return run_result
