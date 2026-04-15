"""Benchmark evaluator: executes compile/test/coverage commands and parses metrics."""

from __future__ import annotations

import re
import subprocess
import xml.etree.ElementTree as ET
from pathlib import Path

from benchmark.schemas import EvaluationConfig
from benchmark.types import EvalMetrics, RunResult

_JACOCO_XML_PATH = Path("target/site/jacoco/jacoco.xml")


def evaluate_run(
    run_dir: Path | str,
    eval_config: EvaluationConfig,
    project_root: Path | str,
) -> EvalMetrics:
    """
    Execute evaluation commands for a generated test run.

    Args:
        run_dir: Directory containing the generated test file
        eval_config: Evaluation commands from manifest
        project_root: Root directory of the Java project

    Returns:
        EvalMetrics with compile_pass, test_pass, coverage_pct, and failure info
    """
    run_path = Path(run_dir)
    project_path = Path(project_root)

    compile_pass = False
    test_pass = False
    coverage_pct: float | None = None
    failure_type: str | None = None
    failure_message: str | None = None

    # Find the generated test file
    test_files = list(run_path.glob("*.java"))
    if not test_files:
        failure_type = "error"
        failure_message = "No test file found in run directory"
        return EvalMetrics(
            compile_pass=False,
            test_pass=False,
            coverage_pct=None,
            failure_type=failure_type,
            failure_message=failure_message,
        )

    test_file = test_files[0]

    # Copy test file to the project's test directory
    # Determine the target path based on the package
    test_content = test_file.read_text(encoding="utf-8")
    package_match = re.search(r"package\s+([\w.]+);", test_content)
    if package_match:
        package = package_match.group(1)
        package_path = package.replace(".", "/")
        target_dir = project_path / "src" / "test" / "java" / package_path
    else:
        # Default package
        target_dir = project_path / "src" / "test" / "java"

    target_dir.mkdir(parents=True, exist_ok=True)
    target_file = target_dir / test_file.name

    # Copy the test file
    import shutil

    shutil.copy2(test_file, target_file)

    compile_result = _execute_command(
        eval_config.compile_cmd,
        project_path,
        run_path,
    )

    if not compile_result.success:
        failure_type = "compile_failed"
        failure_message = (
            compile_result.stderr[:500] if compile_result.stderr else "Compilation failed"
        )
        return EvalMetrics(
            compile_pass=False,
            test_pass=False,
            coverage_pct=None,
            failure_type=failure_type,
            failure_message=failure_message,
        )

    compile_pass = True

    test_result = _execute_command(
        eval_config.test_cmd,
        project_path,
        run_path,
    )

    if not test_result.success:
        failure_type = "test_failed"
        failure_message = test_result.stderr[:500] if test_result.stderr else "Tests failed"
        return EvalMetrics(
            compile_pass=True,
            test_pass=False,
            coverage_pct=None,
            failure_type=failure_type,
            failure_message=failure_message,
        )

    test_pass = True

    if eval_config.coverage_cmd:
        coverage_result = _execute_command(
            eval_config.coverage_cmd,
            project_path,
            run_path,
        )
        coverage_pct, coverage_source, coverage_reason = _extract_coverage_pct(
            project_path=project_path,
            coverage_stdout=coverage_result.stdout,
        )
    else:
        coverage_pct = None
        coverage_source = None
        coverage_reason = "coverage_cmd_not_configured"

    return EvalMetrics(
        compile_pass=compile_pass,
        test_pass=test_pass,
        coverage_pct=coverage_pct,
        coverage_source=coverage_source,
        coverage_reason=coverage_reason,
        failure_type=None,
        failure_message=None,
    )


class CommandResult:
    """Result of a subprocess command execution."""

    def __init__(
        self,
        success: bool,
        stdout: str,
        stderr: str,
        returncode: int,
    ) -> None:
        self.success = success
        self.stdout = stdout
        self.stderr = stderr
        self.returncode = returncode


def _execute_command(
    cmd: str,
    project_root: Path,
    run_dir: Path,
) -> CommandResult:
    """
    Execute a shell command in the project directory.

    Args:
        cmd: Command to execute
        project_root: Project directory for execution
        run_dir: Run-specific directory

    Returns:
        CommandResult with success status and output
    """
    try:
        result = subprocess.run(
            cmd,
            shell=True,
            cwd=str(project_root),
            capture_output=True,
            text=True,
            timeout=600,
        )
        return CommandResult(
            success=result.returncode == 0,
            stdout=result.stdout,
            stderr=result.stderr,
            returncode=result.returncode,
        )
    except subprocess.TimeoutExpired:
        return CommandResult(
            success=False,
            stdout="",
            stderr="Command timed out after 600 seconds",
            returncode=-1,
        )
    except Exception as e:
        return CommandResult(
            success=False,
            stdout="",
            stderr=str(e),
            returncode=-1,
        )


def _extract_coverage_pct(
    project_path: Path,
    coverage_stdout: str,
) -> tuple[float | None, str | None, str | None]:
    """Extract coverage with artifact-first strategy and bounded fallback."""
    artifact_result = _extract_coverage_from_jacoco_xml(project_path)
    if artifact_result is not None:
        return artifact_result, "jacoco_xml", None

    fallback_result = _extract_coverage_from_stdout(coverage_stdout)
    if fallback_result is not None:
        return fallback_result, "stdout_regex", None

    return None, None, "coverage_unavailable"


def _extract_coverage_from_jacoco_xml(project_path: Path) -> float | None:
    """Parse LINE coverage from target/site/jacoco/jacoco.xml when available."""
    jacoco_path = project_path / _JACOCO_XML_PATH
    if not jacoco_path.exists():
        return None

    try:
        root = ET.fromstring(jacoco_path.read_text(encoding="utf-8"))
    except (ET.ParseError, OSError, UnicodeDecodeError):
        return None

    line_counter = None
    for counter in root.iter("counter"):
        if counter.attrib.get("type") == "LINE":
            line_counter = counter
            break

    if line_counter is None:
        return None

    try:
        missed = int(line_counter.attrib.get("missed", ""))
        covered = int(line_counter.attrib.get("covered", ""))
    except ValueError:
        return None

    total = missed + covered
    if total <= 0:
        return None

    return (covered / total) * 100.0


def _extract_coverage_from_stdout(output: str) -> float | None:
    """Parse bounded percentage value from coverage command stdout."""
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
    """Return percent only when in [0, 100] bounds."""
    if 0.0 <= value <= 100.0:
        return value
    return None


def merge_run_metrics(
    run_result: RunResult,
    eval_metrics: EvalMetrics,
) -> RunResult:
    """
    Merge evaluation metrics into an existing run result.

    This is used to update a run result with evaluation outcomes
    after the evaluation phase completes.

    Args:
        run_result: Existing run result from generation
        eval_metrics: Evaluation metrics from compile/test/coverage

    Returns:
        Updated RunResult with merged metrics
    """
    run_result.metrics = eval_metrics
    return run_result
