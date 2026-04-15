"""Internal benchmark runtime types."""

from __future__ import annotations

from dataclasses import dataclass, field
from typing import Any, Literal

PreflightSeverity = Literal["error", "warning"]
ProvenanceStatus = Literal["ok", "unavailable"]


@dataclass(slots=True)
class EvalMetrics:
    """Evaluation outcomes collected after generation."""

    compile_pass: bool
    test_pass: bool
    coverage_pct: float | None = None
    coverage_source: str | None = None
    coverage_reason: str | None = None
    failure_type: str | None = None
    failure_message: str | None = None


@dataclass(slots=True)
class PreflightFinding:
    """Structured manifest preflight finding."""

    code: str
    severity: PreflightSeverity
    manifest_path: str
    message: str
    remediation: str


@dataclass(slots=True)
class ProvenanceRecord:
    """Captured git provenance metadata for a dataset target."""

    repo_id: str
    repo_path: str
    requested_ref: str | None
    resolved_commit: str | None
    branch: str | None
    dirty: bool | None
    status: ProvenanceStatus
    reason: str | None
    captured_at: str


@dataclass(slots=True)
class RunPlan:
    """One planned benchmark run from matrix expansion."""

    run_id: str
    dataset_id: str
    java_file: str
    provider: str
    model: str
    trial: int
    seed: int
    max_dependencies: int
    timeout_seconds: int
    retry_count: int
    project_root: str


@dataclass(slots=True)
class RunResult:
    """Result envelope produced by a benchmark run."""

    run_id: str
    status: str
    latency_ms: int
    metrics: EvalMetrics
    output_path: str
    provider: str
    model: str
    dataset_id: str
    trial: int
    config_snapshot: dict[str, Any] = field(default_factory=dict)
