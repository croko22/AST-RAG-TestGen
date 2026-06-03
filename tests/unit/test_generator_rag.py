"""Unit tests for orchestration/generator.py RAG integration (T10)."""

from __future__ import annotations

from unittest.mock import MagicMock, patch

import pytest

from orchestration.generator import GenerationResult, generate_test_for_file


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


class TestGenerateTestForFileAstOnly:
    def test_returns_generation_result(self, mock_output, mock_parsed, tmp_path):
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

        assert isinstance(result, GenerationResult)
        assert result.test_code == "public class Test {}"
        assert result.retrieval_strategy == "ast"
        assert "parse_ms" in result.timings
        assert "retrieval_ms" in result.timings
        assert "prompt_ms" in result.timings
        assert "llm_ms" in result.timings

    def test_ast_only_no_rag(self, mock_output, mock_parsed, tmp_path):
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch("orchestration.generator._build_prompt", return_value=("code", "ctx")),
            patch("orchestration.generator._generate_test_with_llm", return_value="test code"),
        ):
            result = generate_test_for_file(
                java_file_path="/mock/Service.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                rag_enabled=False,
            )

        assert result.context_sources == {}
        assert result.retrieval_strategy == "ast"

    def test_backward_compatible_default_params(self, mock_output, mock_parsed, tmp_path):
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch("orchestration.generator._build_prompt", return_value=("code", "ctx")),
            patch("orchestration.generator._generate_test_with_llm", return_value="test"),
        ):
            result = generate_test_for_file(
                java_file_path="/mock/Service.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
            )

        assert isinstance(result, GenerationResult)


class TestGenerateTestForFileWithRag:
    def test_rag_enabled_calls_rag_retrieval(self, mock_output, mock_parsed, tmp_path):
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch(
                "orchestration.generator._run_rag_retrieval",
                return_value=(
                    "rag ctx",
                    {
                        "strategy": "hybrid",
                        "chunks_indexed": 10,
                        "results_retrieved": 5,
                        "top_scores": [],
                    },
                ),
            ),
            patch("orchestration.generator._build_prompt", return_value=("code", "full ctx")),
            patch("orchestration.generator._generate_test_with_llm", return_value="test"),
        ):
            result = generate_test_for_file(
                java_file_path="/mock/Service.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                rag_enabled=True,
                retrieval_strategy="hybrid",
            )

        assert result.retrieval_strategy == "hybrid"
        assert result.context_sources["strategy"] == "hybrid"
        assert result.context_sources["chunks_indexed"] == 10

    def test_rag_context_passed_to_build_prompt(self, mock_output, mock_parsed, tmp_path):
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch("orchestration.generator._run_rag_retrieval", return_value=("rag data", {})),
            patch(
                "orchestration.generator._build_prompt", return_value=("code", "ctx")
            ) as mock_build,
            patch("orchestration.generator._generate_test_with_llm", return_value="test"),
        ):
            generate_test_for_file(
                java_file_path="/mock/Service.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                rag_enabled=True,
            )

        call_kwargs = mock_build.call_args
        assert (
            call_kwargs.kwargs.get("rag_context") == "rag data"
            or call_kwargs[1].get("rag_context") == "rag data"
        )

    def test_retrieval_strategy_ast(self, mock_output, mock_parsed, tmp_path):
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch(
                "orchestration.generator._run_rag_retrieval", return_value=("ctx", {})
            ) as mock_rag,
            patch("orchestration.generator._build_prompt", return_value=("code", "ctx")),
            patch("orchestration.generator._generate_test_with_llm", return_value="test"),
        ):
            generate_test_for_file(
                java_file_path="/mock/Service.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                rag_enabled=True,
                retrieval_strategy="ast",
            )

        mock_rag.assert_called_once()
        call_args = mock_rag.call_args
        assert call_args[1].get("strategy") == "ast" or (
            len(call_args[0]) > 2 and call_args[0][2] == "ast"
        )

    def test_retrieval_strategy_rag(self, mock_output, mock_parsed, tmp_path):
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch(
                "orchestration.generator._run_rag_retrieval", return_value=("ctx", {})
            ) as mock_rag,
            patch("orchestration.generator._build_prompt", return_value=("code", "ctx")),
            patch("orchestration.generator._generate_test_with_llm", return_value="test"),
        ):
            generate_test_for_file(
                java_file_path="/mock/Service.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                rag_enabled=True,
                retrieval_strategy="rag",
            )

        mock_rag.assert_called_once()


class TestGenerationResult:
    def test_dataclass_fields(self):
        r = GenerationResult(
            test_code="code",
            output_path="/out/Test.java",
            timings={"parse_ms": 10, "llm_ms": 500},
            retrieval_strategy="hybrid",
            context_sources={"chunks": 5},
        )
        assert r.test_code == "code"
        assert r.output_path == "/out/Test.java"
        assert r.timings["parse_ms"] == 10
        assert r.retrieval_strategy == "hybrid"

    def test_defaults(self):
        r = GenerationResult(test_code="c", output_path="/out")
        assert r.timings == {}
        assert r.retrieval_strategy == "ast"
        assert r.context_sources == {}
