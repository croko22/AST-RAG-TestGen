"""Unit tests for the multi-provider benchmark setup.

Validates manifest-multi.json structure, schema compliance, and run-multi.sh
script contents so that the multi-provider campaign is reproducible.
"""

from __future__ import annotations

import json
import os

import pytest
pytestmark = pytest.mark.slow
import stat

import pytest

from benchmark.schemas import BenchmarkManifest

# ---------------------------------------------------------------------------
# Path constants
# ---------------------------------------------------------------------------
CAMPAIGN_DIR = os.path.join(os.path.dirname(__file__), "..", "..", "campaigns", "spring-boot-v1")
MANIFEST_MULTI_PATH = os.path.join(CAMPAIGN_DIR, "manifest-multi.json")
MANIFEST_SINGLE_PATH = os.path.join(CAMPAIGN_DIR, "manifest.json")
RUN_MULTI_PATH = os.path.join(CAMPAIGN_DIR, "run-multi.sh")

EXPECTED_PROVIDERS = ["nvidia", "openai", "anthropic", "gemini", "openrouter"]


# ---------------------------------------------------------------------------
# Fixtures
# ---------------------------------------------------------------------------
@pytest.fixture()
def manifest_multi_data() -> dict:
    """Parsed contents of manifest-multi.json."""
    with open(MANIFEST_MULTI_PATH, encoding="utf-8") as fh:
        return json.load(fh)


@pytest.fixture()
def manifest_single_data() -> dict:
    """Parsed contents of manifest.json (single-provider baseline)."""
    with open(MANIFEST_SINGLE_PATH, encoding="utf-8") as fh:
        return json.load(fh)


@pytest.fixture()
def run_multi_script() -> str:
    """Contents of run-multi.sh."""
    with open(RUN_MULTI_PATH, encoding="utf-8") as fh:
        return fh.read()


# ---------------------------------------------------------------------------
# Tests — manifest-multi.json
# ---------------------------------------------------------------------------
def test_manifest_multi_loads(manifest_multi_data):
    """manifest-multi.json parses as valid JSON and has 5 providers in matrix."""
    providers = manifest_multi_data["matrix"]["providers"]
    assert len(providers) == 5, f"Expected 5 providers in matrix, found {len(providers)}"


def test_manifest_multi_same_datasets(manifest_multi_data, manifest_single_data):
    """manifest-multi.json has the same 9 datasets as manifest.json."""
    multi_ids = [entry["id"] for entry in manifest_multi_data["dataset"]]
    single_ids = [entry["id"] for entry in manifest_single_data["dataset"]]

    assert multi_ids == single_ids, f"Dataset mismatch: multi={multi_ids}, single={single_ids}"
    assert len(multi_ids) == 9, f"Expected 9 datasets, found {len(multi_ids)}"


def test_manifest_multi_valid_schema(manifest_multi_data):
    """manifest-multi.json validates against BenchmarkManifest schema."""
    manifest = BenchmarkManifest.model_validate(manifest_multi_data)

    assert manifest.manifest_version == 1
    assert len(manifest.dataset) == 9
    assert len(manifest.matrix.providers) == 5
    assert manifest.run.trials == 1
    assert manifest.run.seed == 42
    assert manifest.run.max_dependencies == 10
    assert manifest.scoring.weights.success == pytest.approx(0.5)
    assert manifest.scoring.weights.coverage == pytest.approx(0.3)
    assert manifest.scoring.weights.latency == pytest.approx(0.2)


def test_manifest_multi_provider_names(manifest_multi_data):
    """All expected provider names are present in manifest-multi.json."""
    provider_names = {entry["name"] for entry in manifest_multi_data["matrix"]["providers"]}
    expected = set(EXPECTED_PROVIDERS)

    assert provider_names == expected, (
        f"Provider name mismatch: got {sorted(provider_names)}, expected {sorted(expected)}"
    )


# ---------------------------------------------------------------------------
# Tests — run-multi.sh
# ---------------------------------------------------------------------------
def test_run_multi_script_exists():
    """run-multi.sh exists and is executable."""
    assert os.path.isfile(RUN_MULTI_PATH), "run-multi.sh does not exist"

    st = os.stat(RUN_MULTI_PATH)
    is_executable = bool(st.st_mode & (stat.S_IXUSR | stat.S_IXGRP | stat.S_IXOTH))
    assert is_executable, "run-multi.sh is not executable"


def test_run_multi_script_has_all_providers(run_multi_script):
    """run-multi.sh contains entries for all 5 providers."""
    for provider in EXPECTED_PROVIDERS:
        assert provider in run_multi_script, f"Provider '{provider}' not found in run-multi.sh"


def test_run_multi_script_has_dry_run(run_multi_script):
    """run-multi.sh supports the --dry-run flag."""
    assert "--dry-run" in run_multi_script, "run-multi.sh does not reference --dry-run"


def test_run_multi_script_has_api_key_checks(run_multi_script):
    """run-multi.sh checks for each provider's API key environment variable."""
    expected_keys = {
        "nvidia": "NVIDIA_API_KEY",
        "openai": "OPENAI_API_KEY",
        "anthropic": "ANTHROPIC_API_KEY",
        "gemini": "GEMINI_API_KEY",
        "openrouter": "OPENROUTER_API_KEY",
    }

    for provider, env_key in expected_keys.items():
        assert env_key in run_multi_script, (
            f"API key env var '{env_key}' for provider '{provider}' not found in run-multi.sh"
        )
