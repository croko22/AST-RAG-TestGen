"""FastMCP server exposing AST-RAG TestGen pipeline as MCP tools."""

from __future__ import annotations

import time
from pathlib import Path
from typing import Any


def _import_fastmcp():
    try:
        from fastmcp import FastMCP

        return FastMCP
    except ImportError:
        pass

    try:
        from mcp.server.fastmcp import FastMCP

        return FastMCP
    except ImportError:
        raise ImportError(
            "fastmcp is not installed. Install with: pip install 'ast-rag-testgen[mcp]'"
        ) from None


class MCPServer:
    def __init__(self, config=None):
        self._config = config
        mcp_class = _import_fastmcp()
        self._mcp = mcp_class(
            name="ast-rag-testgen",
            instructions="Java unit test generation using AST-based RAG pipeline",
        )
        self._register_tools()

    def _register_tools(self):
        @self._mcp.tool()
        def generate_tests(
            java_file: str,
            project_path: str,
            provider: str | None = None,
            model: str | None = None,
            rag_enabled: bool = True,
            alpha: float = 0.7,
        ) -> dict[str, Any]:
            return _handle_generate_tests(
                java_file, project_path, provider, model, rag_enabled, alpha, self._config
            )

        @self._mcp.tool()
        def analyze_code(
            java_file: str,
            project_path: str,
        ) -> dict[str, Any]:
            return _handle_analyze_code(java_file, project_path)

        @self._mcp.tool()
        def coverage_suggestions(
            test_file: str,
            project_path: str,
        ) -> dict[str, Any]:
            return _handle_coverage_suggestions(test_file, project_path, self._config)

    def run_stdio(self):
        self._mcp.run(transport="stdio")

    def run_http(self, host: str = "127.0.0.1", port: int = 8000):
        self._mcp.run(transport="sse", host=host, port=port)

    def get_mcp(self):
        return self._mcp


def _pipeline_handler(handler_fn):
    """Wrap an MCP handler with timing + error envelope.

    Returns a function that captures timing and wraps errors into
    a consistent {status, data, timings} envelope.
    """

    def wrapped(*args, **kwargs):
        t_start = time.perf_counter()
        try:
            result = handler_fn(*args, **kwargs)
            elapsed_ms = int((time.perf_counter() - t_start) * 1000)
            result["timings"] = {"total_ms": elapsed_ms}
            return result
        except Exception as e:
            elapsed_ms = int((time.perf_counter() - t_start) * 1000)
            return {
                "status": "error",
                "error": str(e),
                "timings": {"total_ms": elapsed_ms},
            }

    return wrapped


@_pipeline_handler
def _handle_generate_tests(
    java_file: str,
    project_path: str,
    provider: str | None,
    model: str | None,
    rag_enabled: bool,
    alpha: float,
    config: Any,
) -> dict[str, Any]:
    from config import get_config

    app_config = config or get_config()
    effective_provider = provider or app_config.llm.provider
    effective_model = model or app_config.llm.model

    from orchestration.generator import generate_test_for_file

    result = generate_test_for_file(
        java_file_path=java_file,
        java_project_path=project_path,
        output_dir=str(app_config.java.test_output_dir),
        max_dependencies=app_config.java.max_dependencies,
        llm_provider=effective_provider,
        llm_model=effective_model,
        rag_enabled=rag_enabled,
        rag_config={
            "alpha": alpha,
            "embedding_model": app_config.rag.embedding_model,
            "chroma_persist_dir": app_config.rag.chroma_persist_dir,
            "max_context_tokens": app_config.rag.max_context_tokens,
            "retrieval_top_k": app_config.rag.retrieval_top_k,
        },
        retrieval_strategy=app_config.rag.retrieval_strategy if rag_enabled else "ast",
    )

    return {
        "status": "ok",
        "test_code": result.test_code,
        "output_path": result.output_path,
        "timings": result.timings,
        "retrieval_strategy": result.retrieval_strategy,
        "context_sources": result.context_sources,
    }


@_pipeline_handler
def _handle_analyze_code(java_file: str, project_path: str) -> dict[str, Any]:
    file_path = Path(java_file)
    if not file_path.exists():
        return {
            "status": "error",
            "error": f"File not found: {java_file}",
        }

    from core.parsing.parser import JavaParser

    parser = JavaParser()
    parsed = parser.parse_file(str(file_path))

    from rag.retriever import DependencyResolver, JavaFileRetriever

    retriever = JavaFileRetriever(project_path)
    resolver = DependencyResolver(retriever)

    dep_graph: list[dict[str, str]] = []
    for imp in parsed.imports:
        resolved = resolver.resolve_dependencies(imp, max_depth=2)
        for dep in resolved:
            dep_graph.append(
                {
                    "name": dep.name,
                    "type": dep.type,
                    "package": dep.package or "",
                }
            )

    methods_info = [
        {
            "name": m.name,
            "visibility": m.visibility,
            "return_type": m.return_type,
            "parameters": [{"type": p[0], "name": p[1]} for p in m.parameters],
            "is_static": m.is_static,
            "is_abstract": m.is_abstract,
            "effective_loc": m.effective_loc,
        }
        for m in parsed.methods
    ]

    fields_info = [
        {
            "name": f.name,
            "type": f.type,
            "visibility": f.visibility,
            "is_static": f.is_static,
            "is_final": f.is_final,
        }
        for f in parsed.fields
    ]

    return {
        "status": "ok",
        "class_name": parsed.name,
        "package": parsed.package,
        "is_interface": parsed.is_interface,
        "is_abstract": parsed.is_abstract,
        "imports": parsed.imports,
        "fields": fields_info,
        "methods": methods_info,
        "dependency_count": len(dep_graph),
        "dependencies": dep_graph[:50],
        "file_path": str(parsed.file_path),
    }


@_pipeline_handler
def _handle_coverage_suggestions(
    test_file: str,
    project_path: str,
    config: Any,
) -> dict[str, Any]:
    test_path = Path(test_file)
    if not test_path.exists():
        return {
            "status": "error",
            "error": f"Test file not found: {test_file}",
        }

    from config import get_config

    app_config = config or get_config()

    from postproc.quality import assess_test_quality

    test_code = test_path.read_text(encoding="utf-8")
    quality = assess_test_quality(test_code, app_config.postproc.quality_threshold)

    compile_result = None
    test_result = None
    coverage_result = None

    if app_config.postproc.auto_compile and app_config.postproc.compile_cmd:
        from postproc.validator import validate_compilation

        cr = validate_compilation(
            test_path,
            Path(project_path),
            app_config.postproc.compile_cmd,
        )
        compile_result = {
            "success": cr.success,
            "errors": cr.errors,
            "warnings": cr.warnings,
        }

    if app_config.postproc.auto_run and app_config.postproc.test_cmd:
        from postproc.validator import run_tests

        tr = run_tests(
            test_path,
            Path(project_path),
            app_config.postproc.test_cmd,
        )
        test_result = {
            "success": tr.success,
            "passed": tr.passed,
            "failed": tr.failed,
            "errors": tr.errors,
            "test_count": tr.test_count,
        }

    suggestions: list[str] = []

    if quality.trivial_flag:
        suggestions.append("Generated tests are below quality threshold — consider re-generating")

    if quality.assertion_count == 0:
        suggestions.append("No assertions found — add meaningful assertions")
    elif quality.assertion_density < 50:
        suggestions.append("Low assertion density — add more assertions per test")

    if quality.test_count == 0:
        suggestions.append("No @Test methods found — verify test class structure")
    elif quality.test_count < 3:
        suggestions.append(f"Only {quality.test_count} test(s) — consider adding more test cases")

    for issue in quality.issues:
        suggestions.append(issue)

    return {
        "status": "ok",
        "quality": {
            "score": quality.score,
            "test_count": quality.test_count,
            "assertion_count": quality.assertion_count,
            "assertion_density": quality.assertion_density,
            "meaningfulness": quality.meaningfulness,
            "diversity": quality.diversity,
            "trivial_flag": quality.trivial_flag,
        },
        "compile": compile_result,
        "test_run": test_result,
        "coverage": coverage_result,
        "suggestions": suggestions,
    }
