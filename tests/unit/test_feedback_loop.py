"""Unit tests for feedback/repair plumbing in orchestration/generator.py.

The generator now performs a single LLM call. Compile-driven retries live in
the benchmark runner; the generator only needs to forward the feedback text it
receives into the prompt.
"""

from __future__ import annotations

from unittest.mock import MagicMock, patch

import pytest

from orchestration.generator import generate_test_for_file


@pytest.fixture
def mock_output():
    with patch("orchestration.generator.get_output") as mock_get:
        out = MagicMock()
        mock_get.return_value = out
        yield out


@pytest.fixture
def mock_parsed():
    p = MagicMock()
    p.name = "Service"
    p.content = "public class Service { public String getData() { return null; } }"
    p.file_path = "/mock/Service.java"
    p.dependencies = []
    p.imports = []
    p.fields = []
    p.methods = []
    return p


def _run(tmp_path, **kwargs):
    with (
        patch("orchestration.generator._parse_java_file", return_value=kwargs.pop("parsed")),
        patch("orchestration.generator._resolve_dependencies", return_value=[]),
        patch(
            "orchestration.generator._build_prompt", return_value=("code", "ctx")
        ) as mock_build,
        patch(
            "orchestration.generator._generate_test_with_llm",
            return_value="public class Test {}",
        ),
    ):
        result = generate_test_for_file(
            java_file_path="/mock/Service.java",
            java_project_path=str(tmp_path),
            output_dir=str(tmp_path / "out"),
            **kwargs,
        )
    return result, mock_build


class TestGeneratorSinglePass:
    def test_generates_once_without_feedback(self, mock_output, mock_parsed, tmp_path):
        result, mock_build = _run(tmp_path, parsed=mock_parsed)

        assert result.attempt_count == 1
        assert result.final_status == "success"
        assert mock_build.call_count == 1
        assert mock_build.call_args[1]["feedback_context"] is None

    def test_feedback_context_is_forwarded_to_prompt(self, mock_output, mock_parsed, tmp_path):
        feedback = "- Foo.java:12: cannot find symbol (symbol: class Bar)"

        _, mock_build = _run(tmp_path, parsed=mock_parsed, feedback_context=feedback)

        assert mock_build.call_args[1]["feedback_context"] == feedback

    def test_legacy_feedback_config_is_ignored(self, mock_output, mock_parsed, tmp_path):
        """The old FeedbackLoopConfig must not trigger extra LLM calls."""
        legacy = MagicMock(max_retries=5, retry_on_compile_fail=True)

        result, mock_build = _run(tmp_path, parsed=mock_parsed, feedback_config=legacy)

        assert result.attempt_count == 1
        assert mock_build.call_count == 1
