"""Integration tests for generator with RAG integration (T18)."""

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
    p.name = "UsuarioService"
    p.content = "public class UsuarioService { private Repository repo; public String getData() { return repo.findAll(); } public void save(String data) { repo.save(data); } }"
    p.file_path = "/mock/UsuarioService.java"
    p.dependencies = []
    p.imports = ["com.example.Repository"]
    p.fields = []
    p.methods = []
    return p


def _make_rag_mock(context: str, sources: dict | None = None):
    m = MagicMock()
    m.context = context
    m.sources = sources or {}
    return m


def _patch_pipeline(
    mock_parsed, dep_return=None, rag_return=None, prompt_return=None, llm_return=None
):
    dep_return = dep_return or []
    prompt_return = prompt_return or ("code", "ctx")
    llm_return = llm_return or "public class UsuarioServiceTest { @Test void test() {} }"

    patches = [
        patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
        patch("orchestration.generator._resolve_dependencies", return_value=dep_return),
        patch("orchestration.generator._build_prompt", return_value=prompt_return),
        patch("orchestration.generator._generate_test_with_llm", return_value=llm_return),
    ]
    if rag_return is not None:
        mock_result = _make_rag_mock(rag_return[0], rag_return[1])
        patches.insert(2, patch("rag.pipeline.RAGPipeline.retrieve", return_value=mock_result))
    return patches


class TestGeneratorRagVsAst:
    def test_ast_only_no_rag(self, mock_output, mock_parsed, tmp_path):
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch("orchestration.generator._build_prompt", return_value=("code", "ctx")),
            patch("orchestration.generator._generate_test_with_llm", return_value="test code"),
        ):
            result = generate_test_for_file(
                java_file_path="/mock/UsuarioService.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                rag_enabled=False,
            )

        assert isinstance(result, GenerationResult)
        assert result.retrieval_strategy == "ast"
        assert result.context_sources == {}

    def test_rag_enabled_uses_rag(self, mock_output, mock_parsed, tmp_path):
        rag_result = _make_rag_mock(
            "rag ctx",
            {
                "strategy": "hybrid",
                "chunks_indexed": 10,
                "results_retrieved": 3,
                "top_scores": [0.9],
            },
        )
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch("rag.pipeline.RAGPipeline.retrieve", return_value=rag_result),
            patch("orchestration.generator._build_prompt", return_value=("code", "full ctx")),
            patch("orchestration.generator._generate_test_with_llm", return_value="test code"),
        ):
            result = generate_test_for_file(
                java_file_path="/mock/UsuarioService.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                rag_enabled=True,
                retrieval_strategy="hybrid",
            )

        assert result.retrieval_strategy == "hybrid"
        assert result.context_sources["strategy"] == "hybrid"
        assert result.context_sources["chunks_indexed"] == 10


class TestGenerationResultTimings:
    def test_timings_present_with_rag(self, mock_output, mock_parsed, tmp_path):
        rag_result = _make_rag_mock("rag ctx", {})
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch("rag.pipeline.RAGPipeline.retrieve", return_value=rag_result),
            patch("orchestration.generator._build_prompt", return_value=("code", "ctx")),
            patch("orchestration.generator._generate_test_with_llm", return_value="test"),
        ):
            result = generate_test_for_file(
                java_file_path="/mock/UsuarioService.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                rag_enabled=True,
            )

        assert "parse_ms" in result.timings
        assert "retrieval_ms" in result.timings
        assert "prompt_ms" in result.timings
        assert "llm_ms" in result.timings

    def test_timings_present_without_rag(self, mock_output, mock_parsed, tmp_path):
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch("orchestration.generator._build_prompt", return_value=("code", "ctx")),
            patch("orchestration.generator._generate_test_with_llm", return_value="test"),
        ):
            result = generate_test_for_file(
                java_file_path="/mock/UsuarioService.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                rag_enabled=False,
            )

        assert "parse_ms" in result.timings
        assert "retrieval_ms" in result.timings
        assert "prompt_ms" in result.timings
        assert "llm_ms" in result.timings


class TestPromptBuilderReceivesRagContext:
    def test_rag_context_passed_to_build_prompt(self, mock_output, mock_parsed, tmp_path):
        rag_result = _make_rag_mock("rag data here", {})
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch("rag.pipeline.RAGPipeline.retrieve", return_value=rag_result),
            patch(
                "orchestration.generator._build_prompt", return_value=("code", "ctx")
            ) as mock_build,
            patch("orchestration.generator._generate_test_with_llm", return_value="test"),
        ):
            generate_test_for_file(
                java_file_path="/mock/UsuarioService.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                rag_enabled=True,
            )

        call_kwargs = mock_build.call_args
        passed_rag = call_kwargs.kwargs.get("rag_context")
        assert passed_rag == "rag data here"

    def test_no_rag_context_when_disabled(self, mock_output, mock_parsed, tmp_path):
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch(
                "orchestration.generator._build_prompt", return_value=("code", "ctx")
            ) as mock_build,
            patch("orchestration.generator._generate_test_with_llm", return_value="test"),
        ):
            generate_test_for_file(
                java_file_path="/mock/UsuarioService.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                rag_enabled=False,
            )

        call_kwargs = mock_build.call_args
        assert call_kwargs.kwargs.get("rag_context") is None


class TestBackwardCompatibility:
    def test_default_params_work(self, mock_output, mock_parsed, tmp_path):
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch("orchestration.generator._build_prompt", return_value=("code", "ctx")),
            patch("orchestration.generator._generate_test_with_llm", return_value="test"),
        ):
            result = generate_test_for_file(
                java_file_path="/mock/UsuarioService.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
            )

        assert isinstance(result, GenerationResult)
        assert result.retrieval_strategy == "ast"
        assert result.context_sources == {}

    def test_generation_result_defaults(self):
        r = GenerationResult(test_code="code", output_path="/out/Test.java")
        assert r.timings == {}
        assert r.retrieval_strategy == "ast"
        assert r.context_sources == {}


class TestRetrievalStrategySelection:
    def test_ast_strategy(self, mock_output, mock_parsed, tmp_path):
        rag_result = _make_rag_mock("ctx", {})
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch("rag.pipeline.RAGPipeline.retrieve", return_value=rag_result) as mock_rag,
            patch("orchestration.generator._build_prompt", return_value=("code", "ctx")),
            patch("orchestration.generator._generate_test_with_llm", return_value="test"),
        ):
            generate_test_for_file(
                java_file_path="/mock/UsuarioService.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                rag_enabled=True,
                retrieval_strategy="ast",
            )

        mock_rag.assert_called_once()

    def test_rag_strategy(self, mock_output, mock_parsed, tmp_path):
        rag_result = _make_rag_mock("ctx", {})
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch("rag.pipeline.RAGPipeline.retrieve", return_value=rag_result) as mock_rag,
            patch("orchestration.generator._build_prompt", return_value=("code", "ctx")),
            patch("orchestration.generator._generate_test_with_llm", return_value="test"),
        ):
            generate_test_for_file(
                java_file_path="/mock/UsuarioService.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                rag_enabled=True,
                retrieval_strategy="rag",
            )

        mock_rag.assert_called_once()
        call_kwargs = mock_rag.call_args
        assert call_kwargs.kwargs.get("strategy") == "rag" or (
            len(call_kwargs.args) > 2 and call_kwargs.args[2] == "rag"
        )

    def test_hybrid_strategy(self, mock_output, mock_parsed, tmp_path):
        rag_result = _make_rag_mock(
            "ctx",
            {
                "strategy": "hybrid",
                "chunks_indexed": 5,
                "results_retrieved": 2,
                "top_scores": [0.8],
            },
        )
        with (
            patch("orchestration.generator._parse_java_file", return_value=mock_parsed),
            patch("orchestration.generator._resolve_dependencies", return_value=[]),
            patch("rag.pipeline.RAGPipeline.retrieve", return_value=rag_result),
            patch("orchestration.generator._build_prompt", return_value=("code", "ctx")),
            patch("orchestration.generator._generate_test_with_llm", return_value="test"),
        ):
            result = generate_test_for_file(
                java_file_path="/mock/UsuarioService.java",
                java_project_path=str(tmp_path),
                output_dir=str(tmp_path / "out"),
                rag_enabled=True,
                retrieval_strategy="hybrid",
            )

        assert result.retrieval_strategy == "hybrid"
        assert result.context_sources["strategy"] == "hybrid"
