"""Unit tests for core/prompt_builder.py RAG integration (T09)."""

from __future__ import annotations

from unittest.mock import MagicMock, patch

import pytest

from core.prompt_builder import PromptBuilder, build_test_prompt


@pytest.fixture
def mock_retriever():
    r = MagicMock()
    r.project_root = "/mock"
    parsed = MagicMock()
    parsed.name = "Service"
    parsed.content = "public class Service {}"
    parsed.file_path = "/mock/Service.java"
    parsed.dependencies = []
    parsed.imports = []
    parsed.fields = []
    parsed.methods = [
        MagicMock(
            visibility="public",
            return_type="String",
            name="getData",
            parameters=[],
            is_static=False,
        )
    ]
    r.parse_file.return_value = parsed
    return r


@pytest.fixture
def mock_resolver():
    r = MagicMock()
    r.get_method_signatures.return_value = "// SomeClass\npublic void doStuff();"
    return r


class TestBuildPromptWithoutRag:
    def test_no_rag_context_identical_output(self, mock_retriever, mock_resolver):
        builder = PromptBuilder(mock_retriever, mock_resolver)
        code, ctx = builder.build_prompt("/mock/Service.java")

        code_with_rag, ctx_with_rag = builder.build_prompt("/mock/Service.java", rag_context=None)

        assert code == code_with_rag
        assert ctx == ctx_with_rag

    def test_no_rag_no_header_in_context(self, mock_retriever, mock_resolver):
        builder = PromptBuilder(mock_retriever, mock_resolver)
        _, ctx = builder.build_prompt("/mock/Service.java")
        assert "CONTEXTO ADICIONAL" not in ctx


class TestBuildPromptWithRag:
    def test_rag_context_appended(self, mock_retriever, mock_resolver):
        builder = PromptBuilder(mock_retriever, mock_resolver)
        rag = "// retrieved code snippet"
        _, ctx = builder.build_prompt("/mock/Service.java", rag_context=rag)

        assert "CONTEXTO ADICIONAL (RAG" in ctx
        assert "// retrieved code snippet" in ctx

    def test_rag_header_is_spanish(self, mock_retriever, mock_resolver):
        builder = PromptBuilder(mock_retriever, mock_resolver)
        _, ctx = builder.build_prompt("/mock/Service.java", rag_context="some code")

        assert "Recuperación Semántica" in ctx

    def test_rag_context_after_ast_context(self, mock_retriever, mock_resolver):
        builder = PromptBuilder(mock_retriever, mock_resolver)
        _, ctx = builder.build_prompt("/mock/Service.java", rag_context="rag stuff")

        ast_pos = ctx.find("MÉTODOS PÚBLICOS")
        rag_pos = ctx.find("CONTEXTO ADICIONAL")
        assert ast_pos < rag_pos

    def test_rag_context_truncated_to_budget(self, mock_retriever, mock_resolver):
        builder = PromptBuilder(mock_retriever, mock_resolver)
        long_rag = "x" * 10000
        _, ctx = builder.build_prompt(
            "/mock/Service.java",
            rag_context=long_rag,
            max_context_tokens=50,
        )

        assert len(ctx) < 50 * 4 + 500

    def test_empty_rag_context_not_appended(self, mock_retriever, mock_resolver):
        builder = PromptBuilder(mock_retriever, mock_resolver)
        _, ctx = builder.build_prompt("/mock/Service.java", rag_context="")
        assert "CONTEXTO ADICIONAL" not in ctx

    def test_default_max_context_tokens(self, mock_retriever, mock_resolver):
        builder = PromptBuilder(mock_retriever, mock_resolver)
        rag = "y" * 20000
        _, ctx = builder.build_prompt("/mock/Service.java", rag_context=rag)

        assert len(ctx) <= 4000 * 4 + 500


class TestBuildTestPromptConvenience:
    def test_convenience_passes_rag_context(self):
        with (
            patch("core.prompt_builder.JavaFileRetriever") as MockRet,
            patch("core.prompt_builder.DependencyResolver") as MockRes,
        ):
            mock_retriever = MagicMock()
            mock_resolver = MagicMock()
            MockRet.return_value = mock_retriever
            MockRes.return_value = mock_resolver

            parsed = MagicMock()
            parsed.name = "Svc"
            parsed.content = "code"
            parsed.file_path = "/test/Svc.java"
            parsed.dependencies = []
            parsed.imports = []
            parsed.fields = []
            parsed.methods = []
            mock_retriever.parse_file.return_value = parsed

            _, ctx = build_test_prompt(
                "/test/Svc.java",
                "/project",
                rag_context="rag data",
            )
            assert "rag data" in ctx
