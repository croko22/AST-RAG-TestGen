"""Pydantic schemas for benchmark manifest v1."""

from __future__ import annotations

from typing import Literal

from pydantic import BaseModel, ConfigDict, Field, model_validator


class StrictModel(BaseModel):
    """Base model with strict benchmark schema behavior."""

    model_config = ConfigDict(extra="forbid", str_strip_whitespace=True)


class DatasetEntry(StrictModel):
    """One Java target included in benchmark execution."""

    id: str = Field(..., min_length=1)
    java_file: str = Field(..., min_length=1)
    expected_test_path: str | None = None


class ProviderEntry(StrictModel):
    """One provider/model pair in benchmark matrix."""

    name: str = Field(..., min_length=1)
    model: str = Field(..., min_length=1)


class MatrixConfig(StrictModel):
    """Execution matrix configuration."""

    providers: list[ProviderEntry] = Field(..., min_length=1)


class RunConfig(StrictModel):
    """Run controls for benchmark execution."""

    trials: int = Field(default=1, ge=1)
    seed: int = Field(default=0)
    max_dependencies: int = Field(default=10, ge=1)
    timeout_seconds: int = Field(default=300, ge=1)
    retry_count: int = Field(default=0, ge=0)
    concurrency: int = Field(default=1, ge=1)


class EvaluationConfig(StrictModel):
    """Post-generation command configuration."""

    compile_cmd: str = Field(..., min_length=1)
    test_cmd: str = Field(..., min_length=1)
    coverage_cmd: str | None = None
    jacoco_path: str | None = Field(
        default="target/site/jacoco/jacoco.xml",
        description="Path to JaCoCo XML report relative to project root"
    )


class ScoringWeights(StrictModel):
    """Weights used to rank benchmark entries in summary."""

    success: float = Field(default=0.5, ge=0.0)
    coverage: float = Field(default=0.3, ge=0.0)
    latency: float = Field(default=0.2, ge=0.0)

    @model_validator(mode="after")
    def validate_total_positive(self) -> ScoringWeights:
        """Require at least one positive weight."""
        total = self.success + self.coverage + self.latency
        if total <= 0:
            raise ValueError("scoring.weights must have a positive total")
        return self


class ScoringConfig(StrictModel):
    """Scoring wrapper section for manifest."""

    weights: ScoringWeights = Field(default_factory=ScoringWeights)


class ToolchainVersions(StrictModel):
    """Captured toolchain versions for reproducibility."""

    python_version: str = Field(..., min_length=1)
    java_version: str = Field(..., min_length=1)
    maven_version: str = Field(..., min_length=1)
    ast_rag_version: str = Field(default="unknown")


class BenchmarkManifest(StrictModel):
    """Top-level benchmark manifest v1 contract."""

    manifest_version: Literal[1]
    project_root: str = Field(..., min_length=1)
    dataset: list[DatasetEntry] = Field(..., min_length=1)
    matrix: MatrixConfig
    run: RunConfig = Field(default_factory=RunConfig)
    evaluation: EvaluationConfig
    scoring: ScoringConfig = Field(default_factory=ScoringConfig)
    toolchain_versions: ToolchainVersions | None = None
