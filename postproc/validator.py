"""Test validator: compile, run, and coverage extraction for generated Java tests."""

from __future__ import annotations

import re
import shutil
import subprocess
import tempfile
import xml.etree.ElementTree as ET
from dataclasses import dataclass, field
from pathlib import Path

_DEFAULT_TIMEOUT = 600


@dataclass(slots=True)
class CompileResult:
    success: bool
    errors: list[str] = field(default_factory=list)
    warnings: list[str] = field(default_factory=list)
    returncode: int = -1


@dataclass(slots=True)
class TestRunResult:
    __test__ = False
    success: bool
    passed: int = 0
    failed: int = 0
    errors: int = 0
    test_count: int = 0
    output: str = ""
    returncode: int = -1


@dataclass(slots=True)
class CoverageResult:
    line_pct: float | None = None
    branch_pct: float | None = None
    missed_lines: int = 0
    missed_branches: int = 0
    covered_lines: int = 0
    covered_branches: int = 0


def validate_compilation(
    test_file: Path,
    project_path: Path,
    compile_cmd: str | None = None,
    timeout: int = _DEFAULT_TIMEOUT,
) -> CompileResult:
    if not test_file.exists():
        return CompileResult(success=False, errors=[f"Test file not found: {test_file}"])

    if compile_cmd is None:
        return CompileResult(success=False, errors=["No compile command configured"])

    temp_dir: tempfile.TemporaryDirectory | None = None
    try:
        temp_dir = tempfile.TemporaryDirectory(prefix="compile_")
        temp_path = Path(temp_dir.name)
        _stage_test_file(test_file, temp_path)

        result = _run_shell_command(compile_cmd, temp_path, timeout)

        errors, warnings = _classify_output(result.stderr or result.stdout)
        return CompileResult(
            success=result.returncode == 0,
            errors=errors,
            warnings=warnings,
            returncode=result.returncode,
        )
    except Exception as e:
        return CompileResult(success=False, errors=[str(e)])
    finally:
        if temp_dir is not None:
            try:
                temp_dir.cleanup()
            except Exception:
                pass


def run_tests(
    test_file: Path,
    project_path: Path,
    test_cmd: str | None = None,
    timeout: int = _DEFAULT_TIMEOUT,
) -> TestRunResult:
    if not test_file.exists():
        return TestRunResult(success=False, output=f"Test file not found: {test_file}")

    if test_cmd is None:
        return TestRunResult(success=False, output="No test command configured")

    temp_dir: tempfile.TemporaryDirectory | None = None
    try:
        temp_dir = tempfile.TemporaryDirectory(prefix="runtest_")
        temp_path = Path(temp_dir.name)
        _stage_test_file(test_file, temp_path)

        result = _run_shell_command(test_cmd, temp_path, timeout)
        combined_output = (result.stdout or "") + "\n" + (result.stderr or "")
        passed, failed, errors = _parse_test_totals(combined_output)
        test_count = passed + failed + errors

        return TestRunResult(
            success=result.returncode == 0,
            passed=passed,
            failed=failed,
            errors=errors,
            test_count=test_count,
            output=combined_output.strip(),
            returncode=result.returncode,
        )
    except Exception as e:
        return TestRunResult(success=False, output=str(e))
    finally:
        if temp_dir is not None:
            try:
                temp_dir.cleanup()
            except Exception:
                pass


def parse_coverage(jacoco_xml: Path) -> CoverageResult:
    if not jacoco_xml.exists():
        return CoverageResult()

    try:
        root = ET.fromstring(jacoco_xml.read_text(encoding="utf-8"))
    except (ET.ParseError, OSError, UnicodeDecodeError):
        return CoverageResult()

    line_pct, missed_lines, covered_lines = _extract_counter(root, "LINE")
    branch_pct, missed_branches, covered_branches = _extract_counter(root, "BRANCH")

    return CoverageResult(
        line_pct=line_pct,
        branch_pct=branch_pct,
        missed_lines=missed_lines,
        missed_branches=missed_branches,
        covered_lines=covered_lines,
        covered_branches=covered_branches,
    )


def _stage_test_file(test_file: Path, target_base: Path) -> Path:
    content = test_file.read_text(encoding="utf-8")
    package_match = re.search(r"package\s+([\w.]+);", content)

    if package_match:
        package_path = package_match.group(1).replace(".", "/")
        target_dir = target_base / "src" / "test" / "java" / package_path
    else:
        target_dir = target_base / "src" / "test" / "java"

    target_dir.mkdir(parents=True, exist_ok=True)
    target = target_dir / test_file.name
    shutil.copy2(test_file, target)
    return target


def _run_shell_command(
    cmd: str,
    cwd: Path,
    timeout: int,
) -> subprocess.CompletedProcess:
    shell_ops = ["|", ">", "<", "&&", "||", ";", "$", "`", "(", ")"]
    use_shell = any(op in cmd for op in shell_ops)

    if use_shell:
        return subprocess.run(
            cmd,
            shell=True,
            cwd=str(cwd),
            capture_output=True,
            text=True,
            timeout=timeout,
        )
    else:
        return subprocess.run(
            cmd.split(),
            cwd=str(cwd),
            capture_output=True,
            text=True,
            timeout=timeout,
        )


def _classify_output(output: str) -> tuple[list[str], list[str]]:
    errors: list[str] = []
    warnings: list[str] = []
    if not output:
        return errors, warnings

    for line in output.splitlines():
        line_stripped = line.strip()
        if not line_stripped:
            continue
        lower = line_stripped.lower()
        if "error" in lower and "warning" not in lower:
            errors.append(line_stripped)
        elif "warning" in lower:
            warnings.append(line_stripped)

    return errors, warnings


def _parse_test_totals(output: str) -> tuple[int, int, int]:
    passed = failed = errors = 0

    surefire_match = re.search(
        r"Tests run:\s*(\d+),\s*Failures:\s*(\d+),\s*Errors:\s*(\d+)",
        output,
    )
    if surefire_match:
        total = int(surefire_match.group(1))
        failed = int(surefire_match.group(2))
        errors = int(surefire_match.group(3))
        passed = total - failed - errors
        return passed, failed, errors

    gradle_match = re.search(
        r"(\d+)\s+tests\s+(?:completed|found).*?(\d+)\s+failures?",
        output,
        re.IGNORECASE,
    )
    if gradle_match:
        passed = int(gradle_match.group(1))
        failed = int(gradle_match.group(2))
        return passed, failed, errors

    return passed, failed, errors


def _extract_counter(root: ET.Element, counter_type: str) -> tuple[float | None, int, int]:
    for counter in root.iter("counter"):
        if counter.attrib.get("type") == counter_type:
            try:
                missed = int(counter.attrib.get("missed", "0"))
                covered = int(counter.attrib.get("covered", "0"))
            except ValueError:
                return None, 0, 0

            total = missed + covered
            if total <= 0:
                return None, missed, covered

            pct = (covered / total) * 100.0
            return pct, missed, covered

    return None, 0, 0
