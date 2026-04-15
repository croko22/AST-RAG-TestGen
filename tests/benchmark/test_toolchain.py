"""Unit tests for toolchain capture and CSV export."""

from __future__ import annotations

from benchmark.manifest import capture_toolchain_versions, create_manifest_with_toolchain
from benchmark.reporter import export_thesis_metrics_csv
from benchmark.schemas import ToolchainVersions
from benchmark.types import EvalMetrics, RunResult


def test_toolchain_versions_dataclass():
    toolchain = ToolchainVersions(
        python_version="3.11.0",
        java_version="openjdk 17.0.2",
        maven_version="Apache Maven 3.9.0",
        ast_rag_version="1.0.0",
    )

    assert toolchain.python_version == "3.11.0"
    assert toolchain.java_version == "openjdk 17.0.2"
    assert toolchain.maven_version == "Apache Maven 3.9.0"
    assert toolchain.ast_rag_version == "1.0.0"


def test_toolchain_versions_default_ast_rag_version():
    toolchain = ToolchainVersions(
        python_version="3.11.0",
        java_version="17",
        maven_version="3.9",
    )

    assert toolchain.ast_rag_version == "unknown"


def test_capture_toolchain_versions_returns_valid_structure():
    toolchain = capture_toolchain_versions()

    assert toolchain.python_version is not None
    assert "." in toolchain.python_version
    assert toolchain.java_version is not None
    assert toolchain.maven_version is not None
    assert toolchain.ast_rag_version is not None


def test_create_manifest_with_toolchain(tmp_path):
    dataset = [
        {"id": "test-service", "java_file": "src/main/java/Test.java"}
    ]
    matrix = {
        "providers": [{"name": "anthropic", "model": "claude-3-5-sonnet-20241022"}]
    }
    evaluation = {
        "compile_cmd": "mvn compile",
        "test_cmd": "mvn test",
    }

    manifest = create_manifest_with_toolchain(
        project_root="test-project",
        dataset=dataset,
        matrix=matrix,
        evaluation=evaluation,
    )

    assert manifest.manifest_version == 1
    assert manifest.project_root == "test-project"
    assert len(manifest.dataset) == 1
    assert manifest.dataset[0].id == "test-service"
    assert manifest.toolchain_versions is not None
    assert manifest.toolchain_versions.python_version is not None


def test_create_manifest_with_toolchain_includes_toolchain_in_output(tmp_path):
    dataset = [{"id": "svc", "java_file": "Test.java"}]
    matrix = {"providers": [{"name": "test", "model": "test"}]}
    evaluation = {"compile_cmd": "echo compile", "test_cmd": "echo test"}

    manifest = create_manifest_with_toolchain(
        project_root="proj",
        dataset=dataset,
        matrix=matrix,
        evaluation=evaluation,
    )

    dumped = manifest.model_dump()
    assert "toolchain_versions" in dumped
    assert dumped["toolchain_versions"]["python_version"] is not None


def test_export_thesis_metrics_csv_empty_results(tmp_path):
    csv_path = export_thesis_metrics_csv(results=[], output_dir=tmp_path)

    assert csv_path.exists()
    content = csv_path.read_text()
    lines = content.strip().split("\n")
    assert len(lines) == 1
    assert "model,success_rate" in lines[0]


def test_export_thesis_metrics_csv_single_result(tmp_path):
    results = [
        RunResult(
            run_id="run-1",
            status="ok",
            latency_ms=5000,
            metrics=EvalMetrics(
                compile_pass=True,
                test_pass=True,
                coverage_pct=75.0,
            ),
            output_path="/tmp/output",
            provider="anthropic",
            model="claude-3-5-sonnet",
            dataset_id="test",
            trial=1,
            config_snapshot={},
        )
    ]

    csv_path = export_thesis_metrics_csv(results, output_dir=tmp_path)

    assert csv_path.exists()
    content = csv_path.read_text()
    lines = content.strip().split("\n")
    assert len(lines) == 2
    assert "anthropic/claude-3-5-sonnet" in lines[1]


def test_export_thesis_metrics_csv_multiple_results(tmp_path):
    results = [
        RunResult(
            run_id="run-1",
            status="ok",
            latency_ms=5000,
            metrics=EvalMetrics(
                compile_pass=True,
                test_pass=True,
                coverage_pct=80.0,
            ),
            output_path="/tmp/output",
            provider="anthropic",
            model="claude-3-5-sonnet",
            dataset_id="test",
            trial=1,
            config_snapshot={},
        ),
        RunResult(
            run_id="run-2",
            status="ok",
            latency_ms=6000,
            metrics=EvalMetrics(
                compile_pass=True,
                test_pass=False,
                coverage_pct=60.0,
            ),
            output_path="/tmp/output",
            provider="anthropic",
            model="claude-3-5-sonnet",
            dataset_id="test",
            trial=2,
            config_snapshot={},
        ),
    ]

    csv_path = export_thesis_metrics_csv(results, output_dir=tmp_path)

    content = csv_path.read_text()
    lines = content.strip().split("\n")
    assert len(lines) == 2


def test_export_thesis_metrics_csv_failure_counts(tmp_path):
    results = [
        RunResult(
            run_id="run-1",
            status="ok",
            latency_ms=5000,
            metrics=EvalMetrics(
                compile_pass=True,
                test_pass=True,
                coverage_pct=80.0,
            ),
            output_path="/tmp/output",
            provider="test",
            model="model",
            dataset_id="test",
            trial=1,
            config_snapshot={},
        ),
        RunResult(
            run_id="run-2",
            status="error",
            latency_ms=0,
            metrics=EvalMetrics(
                compile_pass=False,
                test_pass=False,
                coverage_pct=None,
            ),
            output_path="/tmp/output",
            provider="test",
            model="model",
            dataset_id="test",
            trial=2,
            config_snapshot={},
        ),
    ]

    csv_path = export_thesis_metrics_csv(results, output_dir=tmp_path)

    content = csv_path.read_text()
    lines = content.strip().split("\n")
    data_line = lines[1]
    fields = data_line.split(",")
    assert fields[1] == "0.5000"


def test_export_thesis_metrics_csv_custom_filename(tmp_path):
    results = []
    csv_path = export_thesis_metrics_csv(
        results,
        output_dir=tmp_path,
        filename="custom_metrics.csv",
    )

    assert csv_path.name == "custom_metrics.csv"
