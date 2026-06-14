"""
Shared subprocess runner for post-processing and benchmark modules.

Consolidates duplicated shell execution, test staging, and output parsing
from postproc/validator.py and benchmark/evaluator.py into a single module.
"""

from __future__ import annotations

import re
import shutil
import subprocess
from dataclasses import dataclass
from pathlib import Path


@dataclass
class CommandResult:
    """Result of a subprocess command execution."""

    success: bool
    stdout: str
    stderr: str
    returncode: int


def run_command(cmd: str, cwd: Path, timeout: int = 600) -> CommandResult:
    """Execute a shell command or binary in a working directory.

    Args:
        cmd: Command string (supports shell operators like |, &&, etc.)
        cwd: Working directory for the process.
        timeout: Maximum execution time in seconds.

    Returns:
        CommandResult with success flag, output streams, and return code.
    """
    shell_ops = ["|", ">", "<", "&&", "||", ";", "$", "`", "(", ")"]
    use_shell = any(op in cmd for op in shell_ops)

    try:
        if use_shell:
            result = subprocess.run(
                cmd,
                shell=True,
                cwd=str(cwd),
                capture_output=True,
                text=True,
                timeout=timeout,
            )
        else:
            result = subprocess.run(
                cmd.split(),
                cwd=str(cwd),
                capture_output=True,
                text=True,
                timeout=timeout,
            )
        return CommandResult(
            success=result.returncode == 0,
            stdout=result.stdout or "",
            stderr=result.stderr or "",
            returncode=result.returncode,
        )
    except subprocess.TimeoutExpired:
        return CommandResult(
            success=False,
            stdout="",
            stderr=f"Command timed out after {timeout} seconds",
            returncode=-1,
        )
    except Exception as e:
        return CommandResult(
            success=False,
            stdout="",
            stderr=str(e),
            returncode=-1,
        )


def stage_test_file(test_file: Path, target_base: Path) -> Path:
    """Stage a Java test file into the correct package directory.

    Reads the package declaration and copies the file to the matching
    path under target_base/src/test/java/.

    Args:
        test_file: Source .java test file.
        target_base: Root directory for the staged project copy.

    Returns:
        Path to the staged test file.
    """
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


def run_git(*args: str, **kwargs) -> str:
    """Run a git command, returning stripped stdout on success or '' on failure.

    Wraps subprocess.run with capture_output=True and text=True by default.
    The 'git' command is prepended to args automatically.
    """
    kwargs.setdefault("capture_output", True)
    kwargs.setdefault("text", True)
    cmd = ["git", *args]
    try:
        result = subprocess.run(cmd, **kwargs)
        if result.returncode == 0:
            return result.stdout.strip()
        return ""
    except (OSError, subprocess.TimeoutExpired):
        return ""


def get_toolchain_version(tool: str, flag: str = "-version") -> str:
    """Get the first line of a tool's version output, or 'unknown' on failure."""
    try:
        result = subprocess.run(
            [tool, flag],
            capture_output=True,
            text=True,
            timeout=5,
        )
        output = result.stderr or result.stdout
        if output:
            return output.split("\n")[0].strip()
    except (FileNotFoundError, subprocess.TimeoutExpired):
        pass
    return "unknown"


def classify_output(output: str) -> tuple[list[str], list[str]]:
    """Classify compilation output into error and warning lines."""
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


def parse_test_totals(output: str) -> tuple[int, int, int]:
    """Parse test run output for passed, failed, error counts.

    Supports Maven Surefire and Gradle output formats.

    Returns:
        Tuple of (passed, failed, errors).
    """
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
