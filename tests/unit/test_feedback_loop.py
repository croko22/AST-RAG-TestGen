"""Unit tests for feedback-loop retry pipeline in orchestration/generator.py (T12)."""

from __future__ import annotations

from dataclasses import dataclass, field
from pathlib import Path
from unittest.mock import MagicMock, patch

import pytest

from orchestration.generator import GenerationResult, generate_test_for_file


@dataclass(slots=True)
class _MockCompileResult:
    success: bool
    errors: list[str] = field(default_factory=list)
    warnings: list[str] = field(default_factory=list)
    returncode: int = -1


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


@pytest.fixture
def feedback_config():
    """Default feedback config with retry enabled."""
    return MagicMock(max_retries=3, retry_on_compile_fail=True)


class TestFeedbackLoopRetry:
    def test_no_retry_when_compile_passes(self, mock_output, mock_parsed, feedback_config, tmp_path):
        """If compilation succeeds on first attempt, no retry should happen."""
        with patch("orchestration.generator._parse_java_file", return_value=mock_parsed), \
             patch("orchestration.generator._resolve_dependencies", return_value=[]), \
             patch("orchestration.generator._build_prompt", return_value=("code", "ctx")), \
             patch("orchestration.generator._generate_test_with_llm", return_value="public class Test {}"), \
             patch("orchestration.generator.validate_compilation", return_value=_MockCompileResult(success=True)) as mock_compile:

            result = generate_test_for_file(
                java_file_path="/mock/Service.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                feedback_config=feedback_config,
            )

        assert isinstance(result, GenerationResult)
        assert result.attempt_count == 1
        assert result.compile_errors == []
        assert result.final_status == "success"
        mock_compile.assert_called_once()

    def test_retry_once_when_compile_fails(self, mock_output, mock_parsed, feedback_config, tmp_path):
        """If compilation fails first then succeeds, should retry once."""
        compile_results = [
            _MockCompileResult(success=False, errors=["missing symbol Foo"]),
            _MockCompileResult(success=True),
        ]
        with patch("orchestration.generator._parse_java_file", return_value=mock_parsed), \
             patch("orchestration.generator._resolve_dependencies", return_value=[]), \
             patch("orchestration.generator._build_prompt", return_value=("code", "ctx")) as mock_build, \
             patch("orchestration.generator._generate_test_with_llm", return_value="public class Test {}"), \
             patch("orchestration.generator.validate_compilation", side_effect=compile_results) as mock_compile:

            result = generate_test_for_file(
                java_file_path="/mock/Service.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                feedback_config=feedback_config,
            )

        assert result.attempt_count == 2
        assert len(result.compile_errors) == 1
        assert "missing symbol Foo" in result.compile_errors[0]
        assert result.final_status == "success"
        assert mock_compile.call_count == 2
        # Prompt should have been rebuilt with feedback on the second call
        assert mock_build.call_count == 2
        second_call_kwargs = mock_build.call_args_list[1]
        assert "feedback_context" in second_call_kwargs.kwargs
        assert "missing symbol Foo" in second_call_kwargs.kwargs["feedback_context"]

    def test_max_retries_reached(self, mock_output, mock_parsed, feedback_config, tmp_path):
        """If compilation always fails, should stop after max_retries."""
        fail_result = _MockCompileResult(success=False, errors=["always fails"])
        with patch("orchestration.generator._parse_java_file", return_value=mock_parsed), \
             patch("orchestration.generator._resolve_dependencies", return_value=[]), \
             patch("orchestration.generator._build_prompt", return_value=("code", "ctx")) as mock_build, \
             patch("orchestration.generator._generate_test_with_llm", return_value="public class Test {}"), \
             patch("orchestration.generator.validate_compilation", return_value=fail_result) as mock_compile:

            result = generate_test_for_file(
                java_file_path="/mock/Service.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                feedback_config=feedback_config,
            )

        assert result.attempt_count == feedback_config.max_retries
        assert len(result.compile_errors) == feedback_config.max_retries
        assert result.final_status == "compile_failed_after_retries"
        assert mock_compile.call_count == feedback_config.max_retries
        # Prompt should be rebuilt max_retries - 1 times (not on the last failure)
        assert mock_build.call_count == feedback_config.max_retries

    def test_no_retry_when_disabled(self, mock_output, mock_parsed, tmp_path):
        """If retry_on_compile_fail is False, should stop after first failure."""
        disabled_config = MagicMock(max_retries=3, retry_on_compile_fail=False)
        fail_result = _MockCompileResult(success=False, errors=["compile error"])
        with patch("orchestration.generator._parse_java_file", return_value=mock_parsed), \
             patch("orchestration.generator._resolve_dependencies", return_value=[]), \
             patch("orchestration.generator._build_prompt", return_value=("code", "ctx")), \
             patch("orchestration.generator._generate_test_with_llm", return_value="public class Test {}"), \
             patch("orchestration.generator.validate_compilation", return_value=fail_result) as mock_compile:

            result = generate_test_for_file(
                java_file_path="/mock/Service.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                feedback_config=disabled_config,
            )

        assert result.attempt_count == 1
        assert result.compile_errors == ["compile error"]
        assert result.final_status == "compile_failed"
        mock_compile.assert_called_once()

    def test_feedback_injected_into_prompt(self, mock_output, mock_parsed, tmp_path):
        """When compile fails, feedback_context with error message should be injected into prompt."""
        # Use max_retries=2 so there is exactly one rebuild (total 2 _build_prompt calls)
        config = MagicMock(max_retries=2, retry_on_compile_fail=True)
        fail_result = _MockCompileResult(success=False, errors=["cannot find symbol Bar"])
        with patch("orchestration.generator._parse_java_file", return_value=mock_parsed), \
             patch("orchestration.generator._resolve_dependencies", return_value=[]), \
             patch("orchestration.generator._build_prompt", return_value=("code", "ctx")) as mock_build, \
             patch("orchestration.generator._generate_test_with_llm", return_value="public class Test {}"), \
             patch("orchestration.generator.validate_compilation", return_value=fail_result):

            generate_test_for_file(
                java_file_path="/mock/Service.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                feedback_config=config,
            )

        # Initial call + one rebuild after first failure = 2 calls
        assert mock_build.call_count == 2
        first_call_kwargs = mock_build.call_args_list[0].kwargs
        second_call_kwargs = mock_build.call_args_list[1].kwargs

        assert first_call_kwargs.get("feedback_context") is None
        feedback_ctx = second_call_kwargs.get("feedback_context", "")
        assert "FEEDBACK" in feedback_ctx or "Compilación" in feedback_ctx or "cannot find symbol Bar" in feedback_ctx
        assert "cannot find symbol Bar" in feedback_ctx

    def test_lazy_import_skips_retry(self, mock_output, mock_parsed, feedback_config, tmp_path):
        """If postproc.validator is not available, no retry should be attempted."""
        with patch("orchestration.generator._parse_java_file", return_value=mock_parsed), \
             patch("orchestration.generator._resolve_dependencies", return_value=[]), \
             patch("orchestration.generator._build_prompt", return_value=("code", "ctx")) as mock_build, \
             patch("orchestration.generator._generate_test_with_llm", return_value="public class Test {}"), \
             patch("orchestration.generator._VALIDATOR_AVAILABLE", False):

            result = generate_test_for_file(
                java_file_path="/mock/Service.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                feedback_config=feedback_config,
            )

        assert result.attempt_count == 1
        assert result.compile_errors == []
        assert result.final_status == "success"
        # _build_prompt should only be called once because no retry loop
        mock_build.assert_called_once()
