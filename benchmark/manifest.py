"""Manifest loader and validation utilities for benchmark mode."""

from __future__ import annotations

import json
import os
import shutil
import subprocess
import sys
import tomllib
from pathlib import Path
from typing import Any

from pydantic import ValidationError

from benchmark.schemas import BenchmarkManifest, ToolchainVersions
from benchmark.types import PreflightFinding


class ManifestValidationError(ValueError):
    """Raised when manifest parsing or schema validation fails."""


_PROVIDER_ENV_KEYS: dict[str, str] = {
    "anthropic": "ANTHROPIC_API_KEY",
    "openai": "OPENAI_API_KEY",
    "glm": "GLM_API_KEY",
    "gemini": "GEMINI_API_KEY",
    "nvidia": "NVIDIA_API_KEY",
    "openrouter": "OPENROUTER_API_KEY",
}


def _read_manifest_data(path: Path) -> dict[str, Any]:
    suffix = path.suffix.lower()
    raw = path.read_bytes()

    if suffix == ".json":
        data = json.loads(raw.decode("utf-8"))
    elif suffix in {".toml", ".tml"}:
        data = tomllib.loads(raw.decode("utf-8"))
    else:
        raise ManifestValidationError(f"Unsupported manifest format at {path}. Use .json or .toml")

    if not isinstance(data, dict):
        raise ManifestValidationError(f"Manifest root must be an object: {path}")
    return data


def _format_pydantic_error(path: Path, exc: ValidationError) -> str:
    issues: list[str] = []
    for err in exc.errors():
        loc = ".".join(str(p) for p in err.get("loc", ()))
        err_path = loc or "<root>"
        msg = err.get("msg", "invalid value")
        issues.append(f"- {err_path}: {msg}")
    detail = "\n".join(issues)
    return f"Manifest validation failed for {path}:\n{detail}"


def load_manifest(path: str | Path) -> BenchmarkManifest:
    """Load and validate benchmark manifest from JSON or TOML file."""

    manifest_path = Path(path)
    if not manifest_path.exists():
        raise ManifestValidationError(f"Manifest file not found: {manifest_path}")

    try:
        payload = _read_manifest_data(manifest_path)
    except json.JSONDecodeError as exc:
        raise ManifestValidationError(
            f"Invalid JSON manifest at {manifest_path}: {exc.msg}"
        ) from exc
    except tomllib.TOMLDecodeError as exc:
        raise ManifestValidationError(f"Invalid TOML manifest at {manifest_path}: {exc}") from exc

    try:
        return BenchmarkManifest.model_validate(payload)
    except ValidationError as exc:
        raise ManifestValidationError(_format_pydantic_error(manifest_path, exc)) from exc


def capture_toolchain_versions() -> ToolchainVersions:
    """Capture current toolchain versions for reproducibility."""
    python_version = f"{sys.version_info.major}.{sys.version_info.minor}.{sys.version_info.micro}"

    java_version = "unknown"
    try:
        result = subprocess.run(
            ["java", "-version"],
            capture_output=True,
            text=True,
            timeout=5,
        )
        if result.stderr:
            first_line = result.stderr.split("\n")[0]
            java_version = first_line.strip()
        elif result.stdout:
            first_line = result.stdout.split("\n")[0]
            java_version = first_line.strip()
    except (FileNotFoundError, subprocess.TimeoutExpired):
        pass

    maven_version = "unknown"
    try:
        result = subprocess.run(
            ["mvn", "-version"],
            capture_output=True,
            text=True,
            timeout=5,
        )
        if result.stdout:
            first_line = result.stdout.split("\n")[0]
            maven_version = first_line.strip()
    except (FileNotFoundError, subprocess.TimeoutExpired):
        pass

    return ToolchainVersions(
        python_version=python_version,
        java_version=java_version,
        maven_version=maven_version,
        ast_rag_version="1.0.0",
    )


def create_manifest_with_toolchain(
    project_root: str,
    dataset: list[dict[str, Any]],
    matrix: dict[str, Any],
    evaluation: dict[str, Any],
    run: dict[str, Any] | None = None,
    scoring: dict[str, Any] | None = None,
) -> BenchmarkManifest:
    """Create a benchmark manifest with automatic toolchain capture."""
    toolchain = capture_toolchain_versions()

    manifest_data: dict[str, Any] = {
        "manifest_version": 1,
        "project_root": project_root,
        "dataset": dataset,
        "matrix": matrix,
        "evaluation": evaluation,
        "toolchain_versions": toolchain.model_dump(),
    }

    if run:
        manifest_data["run"] = run
    if scoring:
        manifest_data["scoring"] = scoring

    return BenchmarkManifest.model_validate(manifest_data)


def validate_manifest_preflight(manifest: BenchmarkManifest) -> list[PreflightFinding]:
    """Validate manifest/runtime prerequisites with fail-warn taxonomy."""
    findings: list[PreflightFinding] = []
    project_root = Path(manifest.project_root)

    if not project_root.exists():
        findings.append(
            PreflightFinding(
                code="MANIFEST_PATH_MISSING",
                severity="error",
                manifest_path="project_root",
                message=f"Project root does not exist: {project_root}",
                remediation="Set project_root to an existing repository path.",
            )
        )

    for index, dataset in enumerate(manifest.dataset):
        java_path = project_root / dataset.java_file
        if not java_path.exists():
            findings.append(
                PreflightFinding(
                    code="MANIFEST_DATASET_FILE_MISSING",
                    severity="error",
                    manifest_path=f"dataset[{index}].java_file",
                    message=f"Dataset Java file not found: {java_path}",
                    remediation="Fix dataset.java_file to point at an existing Java source file.",
                )
            )

        if dataset.expected_test_path:
            expected_test_parent = (project_root / dataset.expected_test_path).parent
            if not expected_test_parent.exists():
                findings.append(
                    PreflightFinding(
                        code="MANIFEST_EXPECTED_TEST_PARENT_MISSING",
                        severity="warning",
                        manifest_path=f"dataset[{index}].expected_test_path",
                        message=(
                            f"Expected test parent directory is missing: {expected_test_parent}"
                        ),
                        remediation="Create the parent folder or update expected_test_path.",
                    )
                )

    for index, provider in enumerate(manifest.matrix.providers):
        provider_name = provider.name.lower()
        env_key = _PROVIDER_ENV_KEYS.get(provider_name)
        if env_key is None:
            findings.append(
                PreflightFinding(
                    code="MANIFEST_PROVIDER_UNKNOWN",
                    severity="error",
                    manifest_path=f"matrix.providers[{index}].name",
                    message=f"Unknown provider '{provider.name}'.",
                    remediation=("Use one of: " + ", ".join(sorted(_PROVIDER_ENV_KEYS.keys()))),
                )
            )
            continue

        if not os.getenv(env_key):
            findings.append(
                PreflightFinding(
                    code="MANIFEST_PROVIDER_CREDENTIAL_MISSING",
                    severity="error",
                    manifest_path=f"matrix.providers[{index}].name",
                    message=f"Missing credential env var {env_key} for provider '{provider.name}'.",
                    remediation=f"Export {env_key} before running benchmark mode.",
                )
            )

    for tool in ("java", "mvn"):
        if shutil.which(tool) is None:
            findings.append(
                PreflightFinding(
                    code="MANIFEST_TOOLCHAIN_MISSING",
                    severity="error",
                    manifest_path="toolchain",
                    message=f"Required toolchain executable '{tool}' was not found in PATH.",
                    remediation=f"Install '{tool}' and ensure it is available in PATH.",
                )
            )

    return findings
