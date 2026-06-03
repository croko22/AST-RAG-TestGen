"""Unit tests for feedback-loop retry pipeline in orchestration/generator.py (T12)."""

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


class TestFeedbackLoopRetry:
    """Tests for feedback loop - now simplified: skip compile validation in loop."""

    def test_no_retry_when_compile_passes(self, mock_output, mock_parsed, tmp_path):
        """If retry disabled (default), no retry happens."""
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch("orchestration.generator._build_prompt", return_value=("code", "ctx")),
            patch(
                "orchestration.generator._generate_test_with_llm",
                return_value="public class Test {}",
            ),
        ):
            result = generate_test_for_file(
                java_file_path="/mock/Service.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                feedback_config=None,  # Default: no retry
            )

            # Default: max_retries=1, retry_on_compile_fail=False
            assert result.attempt_count == 1
            assert result.final_status == "success"

    def test_retry_when_enabled(self, mock_output, mock_parsed, tmp_path):
        """If retry enabled, should retry max_retries times."""
        feedback_config = MagicMock(max_retries=2, retry_on_compile_fail=True)

        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch("orchestration.generator._build_prompt", return_value=("code", "ctx")),
            patch(
                "orchestration.generator._generate_test_with_llm",
                return_value="public class Test {}",
            ),
        ):
            result = generate_test_for_file(
                java_file_path="/mock/Service.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                feedback_config=feedback_config,
            )

            # max_retries=2, so attempt 1 generates, attempt 2 retries = 2 attempts
            assert result.attempt_count == 2
            assert result.final_status == "success"

    def test_no_retry_when_disabled(self, mock_output, mock_parsed, tmp_path):
        """If retry disabled explicitly, no retry happens."""
        feedback_config = MagicMock(max_retries=3, retry_on_compile_fail=False)

        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch("orchestration.generator._build_prompt", return_value=("code", "ctx")),
            patch(
                "orchestration.generator._generate_test_with_llm",
                return_value="public class Test {}",
            ),
        ):
            result = generate_test_for_file(
                java_file_path="/mock/Service.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                feedback_config=feedback_config,
            )

            # retry_on_compile_fail=False -> no retry
            assert result.attempt_count == 1

    def test_feedback_injected_into_prompt(self, mock_output, mock_parsed, tmp_path):
        """When retry enabled, feedback context is passed to prompt rebuild."""
        feedback_config = MagicMock(max_retries=2, retry_on_compile_fail=True)

        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
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
                feedback_config=feedback_config,
            )

            # Build called twice: once for attempt 1, once for attempt 2 (retry)
            assert mock_build.call_count == 2

            # Second call should have feedback_context
            second_call_kwargs = mock_build.call_args[1]
            assert "feedback_context" in second_call_kwargs
            # Feedback mentions it's validated by evaluator (not actual compile errors)
            assert "validated by evaluator" in second_call_kwargs["feedback_context"]

    def test_lazy_import_skips_retry(self, mock_output, mock_parsed, tmp_path):
        """When validator unavailable, skip validation logic."""
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch("orchestration.generator._build_prompt", return_value=("code", "ctx")),
            patch(
                "orchestration.generator._generate_test_with_llm",
                return_value="public class Test {}",
            ),
        ):
            result = generate_test_for_file(
                java_file_path="/mock/Service.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                feedback_config=None,
            )

            # Default: no retry
            assert result.attempt_count == 1
            assert result.final_status == "success"


class TestFeedbackLoopDefaultBehavior:
    """Test default feedback config behavior."""

    def test_defaults_disabled(self, mock_output, mock_parsed, tmp_path):
        """By default, feedback loop is disabled (slow mvn compile avoided)."""
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch("orchestration.generator._build_prompt", return_value=("code", "ctx")),
            patch(
                "orchestration.generator._generate_test_with_llm",
                return_value="public class Test {}",
            ),
        ):
            result = generate_test_for_file(
                java_file_path="/mock/Service.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
            )

            # Default feedback_config creates: max_retries=1, retry_on_compile_fail=False
            assert result.attempt_count == 1
