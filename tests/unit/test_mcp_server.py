"""Unit tests for mcp_server/server.py (T13-T14)."""

from __future__ import annotations

import importlib
import sys
from types import ModuleType
from unittest.mock import MagicMock, patch

import pytest


def _make_fake_fastmcp():
    recorded_tools: list[str] = []

    class FakeMCP:
        def __init__(self, name="", instructions=""):
            self.name = name
            self.instructions = instructions

        def tool(self):
            def decorator(fn):
                recorded_tools.append(fn.__name__)
                return fn

            return decorator

        def run(self, transport="stdio", host="127.0.0.1", port=8000):
            pass

    return FakeMCP, recorded_tools


@pytest.fixture(autouse=True)
def _mock_fastmcp_import(monkeypatch):
    fake_cls, tools = _make_fake_fastmcp()
    fake_module = ModuleType("fastmcp")
    fake_module.FastMCP = fake_cls
    monkeypatch.setitem(sys.modules, "fastmcp", fake_module)
    yield tools


class TestMCPServer:
    def test_create_server_registers_three_tools(self, _mock_fastmcp_import):
        from mcp_server.server import MCPServer

        MCPServer()
        assert "generate_tests" in _mock_fastmcp_import
        assert "analyze_code" in _mock_fastmcp_import
        assert "coverage_suggestions" in _mock_fastmcp_import

    def test_get_mcp_returns_underlying_instance(self, _mock_fastmcp_import):
        from mcp_server.server import MCPServer

        server = MCPServer()
        mcp = server.get_mcp()
        assert mcp is not None
        assert mcp.name == "ast-rag-testgen"


class TestGenerateTestsHandler:
    def test_returns_error_on_missing_file(self):
        from mcp_server.server import _handle_generate_tests

        with patch(
            "orchestration.generator.generate_test_for_file", side_effect=FileNotFoundError("nope")
        ):
            result = _handle_generate_tests(
                "NonExistent.java", "/tmp/project", None, None, False, 0.5, None
            )
        assert result["status"] == "error"
        assert "nope" in result["error"]
        assert "total_ms" in result["timings"]

    def test_returns_ok_on_success(self):
        from mcp_server.server import _handle_generate_tests

        mock_result = MagicMock()
        mock_result.test_code = "class FooTest {}"
        mock_result.output_path = "/tmp/FooTest.java"
        mock_result.timings = {"parse_ms": 10}
        mock_result.retrieval_strategy = "ast"
        mock_result.context_sources = {}

        mock_config = MagicMock()
        mock_config.llm.provider = "anthropic"
        mock_config.llm.model = "claude-3"
        mock_config.java.test_output_dir = "/tmp"
        mock_config.java.max_dependencies = 10
        mock_config.rag.embedding_model = "all-MiniLM-L6-v2"
        mock_config.rag.chroma_persist_dir = ".chroma_db"
        mock_config.rag.max_context_tokens = 4000
        mock_config.rag.retrieval_top_k = 10
        mock_config.rag.retrieval_strategy = "hybrid"

        with patch("orchestration.generator.generate_test_for_file", return_value=mock_result):
            result = _handle_generate_tests(
                "Service.java", "/project", "anthropic", "claude-3", False, 0.5, mock_config
            )

        assert result["status"] == "ok"
        assert result["test_code"] == "class FooTest {}"
        assert result["output_path"] == "/tmp/FooTest.java"
        assert "total_ms" in result["timings"]

    def test_rag_disabled_uses_ast_strategy(self):
        from mcp_server.server import _handle_generate_tests

        mock_result = MagicMock()
        mock_result.test_code = "test"
        mock_result.output_path = "/tmp"
        mock_result.timings = {}
        mock_result.retrieval_strategy = "ast"
        mock_result.context_sources = {}

        mock_config = MagicMock()
        mock_config.llm.provider = "openai"
        mock_config.llm.model = "gpt-4"
        mock_config.java.test_output_dir = "/tmp"
        mock_config.java.max_dependencies = 5
        mock_config.rag.embedding_model = "all-MiniLM-L6-v2"
        mock_config.rag.chroma_persist_dir = ".chroma_db"
        mock_config.rag.max_context_tokens = 4000
        mock_config.rag.retrieval_top_k = 10
        mock_config.rag.retrieval_strategy = "hybrid"

        with patch(
            "orchestration.generator.generate_test_for_file", return_value=mock_result
        ) as mock_gen:
            result = _handle_generate_tests(
                "Svc.java", "/proj", None, None, False, 0.5, mock_config
            )

        _, kwargs = mock_gen.call_args
        assert kwargs["retrieval_strategy"] == "ast"
        assert result["status"] == "ok"


class TestAnalyzeCodeHandler:
    def test_returns_error_on_missing_file(self):
        from mcp_server.server import _handle_analyze_code

        result = _handle_analyze_code("/nonexistent/File.java", "/project")
        assert result["status"] == "error"
        assert "not found" in result["error"].lower() or "error" in result["error"].lower()

    def test_returns_error_when_parser_fails(self):
        from mcp_server.server import _handle_analyze_code

        with patch("core.parsing.parser.JavaParser", side_effect=ImportError("no tree-sitter")):
            result = _handle_analyze_code("/some/File.java", "/project")
        assert result["status"] == "error"


class TestCoverageSuggestionsHandler:
    def test_returns_error_on_missing_file(self):
        from mcp_server.server import _handle_coverage_suggestions

        result = _handle_coverage_suggestions("/nonexistent/Test.java", "/project", None)
        assert result["status"] == "error"
        assert "not found" in result["error"].lower()

    def test_returns_quality_and_suggestions(self, tmp_path):
        from mcp_server.server import _handle_coverage_suggestions

        test_file = tmp_path / "ServiceTest.java"
        test_file.write_text(
            "@Test\npublic void testFoo() {\n  assertEquals(1, 1);\n  assertTrue(true);\n}\n"
        )

        mock_config = MagicMock()
        mock_config.postproc.quality_threshold = 0.5
        mock_config.postproc.auto_compile = False
        mock_config.postproc.auto_run = False

        result = _handle_coverage_suggestions(str(test_file), "/project", mock_config)

        assert result["status"] == "ok"
        assert "quality" in result
        assert result["quality"]["test_count"] == 1
        assert result["quality"]["assertion_count"] >= 1
        assert isinstance(result["suggestions"], list)
        assert "total_ms" in result["timings"]

    def test_suggests_more_assertions_when_trivial(self, tmp_path):
        from mcp_server.server import _handle_coverage_suggestions

        test_file = tmp_path / "EmptyTest.java"
        test_file.write_text("@Test\npublic void testNothing() {\n}\n")

        mock_config = MagicMock()
        mock_config.postproc.quality_threshold = 0.5
        mock_config.postproc.auto_compile = False
        mock_config.postproc.auto_run = False

        result = _handle_coverage_suggestions(str(test_file), "/project", mock_config)

        assert result["status"] == "ok"
        assert result["quality"]["trivial_flag"] is True
        assert len(result["suggestions"]) > 0


class TestMCPServerImport:
    def test_missing_fastmcp_raises_helpful_error(self, monkeypatch):
        monkeypatch.delitem(sys.modules, "fastmcp", raising=False)
        monkeypatch.setitem(sys.modules, "mcp", None)

        import mcp_server.server as srv

        importlib.reload(srv)

        with pytest.raises(ImportError, match="fastmcp"):
            srv._import_fastmcp()


class TestMCPCli:
    def test_run_serve_stdio(self, _mock_fastmcp_import):
        from mcp_server.cli import run_serve

        with patch("mcp_server.server.MCPServer") as MockServer:
            instance = MagicMock()
            MockServer.return_value = instance
            result = run_serve(transport="stdio")

        assert result == 0
        instance.run_stdio.assert_called_once()

    def test_run_serve_http(self, _mock_fastmcp_import):
        from mcp_server.cli import run_serve

        with patch("mcp_server.server.MCPServer") as MockServer:
            instance = MagicMock()
            MockServer.return_value = instance
            result = run_serve(host="0.0.0.0", port=9000, transport="http")

        assert result == 0
        instance.run_http.assert_called_once_with(host="0.0.0.0", port=9000)

    def test_run_serve_invalid_transport(self, _mock_fastmcp_import):
        from mcp_server.cli import run_serve

        with patch("mcp_server.server.MCPServer") as MockServer:
            instance = MagicMock()
            MockServer.return_value = instance
            result = run_serve(transport="ws")

        assert result == 1
