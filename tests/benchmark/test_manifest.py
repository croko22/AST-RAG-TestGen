"""Unit tests for benchmark manifest loading and validation."""

from __future__ import annotations

import json

import pytest

from benchmark.manifest import ManifestValidationError, load_manifest


def _base_manifest() -> dict[str, object]:
    return {
        "manifest_version": 1,
        "project_root": "mock-java-project",
        "dataset": [
            {
                "id": "usuario-service",
                "java_file": "src/main/java/com/example/demo/service/UsuarioService.java",
            }
        ],
        "matrix": {
            "providers": [
                {"name": "anthropic", "model": "claude-3-5-sonnet-20241022"},
            ]
        },
        "evaluation": {
            "compile_cmd": "mvn -q -DskipTests compile",
            "test_cmd": "mvn -q test",
        },
    }


def test_load_manifest_valid_json_with_defaults(tmp_path):
    manifest_path = tmp_path / "benchmark.json"
    manifest_path.write_text(json.dumps(_base_manifest()), encoding="utf-8")

    manifest = load_manifest(manifest_path)

    assert manifest.manifest_version == 1
    assert manifest.run.trials == 1
    assert manifest.run.max_dependencies == 10
    assert manifest.scoring.weights.success == pytest.approx(0.5)
    assert manifest.scoring.weights.coverage == pytest.approx(0.3)
    assert manifest.scoring.weights.latency == pytest.approx(0.2)


def test_load_manifest_valid_toml(tmp_path):
    manifest_path = tmp_path / "benchmark.toml"
    manifest_path.write_text(
        """
manifest_version = 1
project_root = "mock-java-project"

[[dataset]]
id = "pedido-service"
java_file = "src/main/java/com/example/demo/service/PedidoService.java"

[matrix]

[[matrix.providers]]
name = "openai"
model = "gpt-4-turbo"

[evaluation]
compile_cmd = "mvn -q -DskipTests compile"
test_cmd = "mvn -q test"
""".strip(),
        encoding="utf-8",
    )

    manifest = load_manifest(str(manifest_path))

    assert manifest.dataset[0].id == "pedido-service"
    assert manifest.matrix.providers[0].name == "openai"


def test_load_manifest_missing_required_field_reports_path(tmp_path):
    data = _base_manifest()
    del data["dataset"]
    manifest_path = tmp_path / "invalid.json"
    manifest_path.write_text(json.dumps(data), encoding="utf-8")

    with pytest.raises(ManifestValidationError) as exc_info:
        load_manifest(manifest_path)

    message = str(exc_info.value)
    assert "dataset" in message
    assert str(manifest_path) in message


def test_load_manifest_wrong_type_reports_field_path(tmp_path):
    data = _base_manifest()
    data["run"] = {"trials": "three"}
    manifest_path = tmp_path / "wrong-type.json"
    manifest_path.write_text(json.dumps(data), encoding="utf-8")

    with pytest.raises(ManifestValidationError) as exc_info:
        load_manifest(manifest_path)

    message = str(exc_info.value)
    assert "run.trials" in message
    assert "Input should be a valid integer" in message
