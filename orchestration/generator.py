"""
Orchestration layer for test generation.

This module provides the orchestration logic for generating tests,
separating business logic from CLI concerns.
"""

from __future__ import annotations

import time
from dataclasses import dataclass, field
from pathlib import Path
from typing import Any, Literal

from output import get_output

try:
    from postproc.validator import validate_compilation
    _VALIDATOR_AVAILABLE = True
except ImportError:
    _VALIDATOR_AVAILABLE = False


@dataclass(slots=True)
class GenerationResult:
    test_code: str
    output_path: str
    timings: dict[str, int] = field(default_factory=dict)
    retrieval_strategy: str = "ast"
    context_sources: dict[str, Any] = field(default_factory=dict)
    attempt_count: int = 0
    compile_errors: list[str] = field(default_factory=list)
    final_status: str = "unknown"


def generate_test_for_file(
    java_file_path: str,
    java_project_path: str,
    output_dir: str,
    max_dependencies: int = 10,
    llm_provider: str = "anthropic",
    llm_model: str = "claude-3-5-sonnet-20241022",
    enable_metainfo_db: bool = False,
    enable_reftest_parity: bool = False,
    rag_enabled: bool = False,
    rag_config: dict[str, Any] | None = None,
    retrieval_strategy: Literal["ast", "rag", "hybrid"] = "ast",
    feedback_config: Any = None,
) -> GenerationResult:
    output = get_output()
    timings: dict[str, int] = {}

    t0 = time.perf_counter()
    output.print_info("[1/4] 📄 Parsing Java file...")
    parsed_class = _parse_java_file(java_file_path)
    timings["parse_ms"] = int((time.perf_counter() - t0) * 1000)

    t0 = time.perf_counter()
    output.print_info("[2/4] 🔍 Resolving dependencies...")
    dependency_context = _resolve_dependencies(
        parsed_class=parsed_class,
        project_path=Path(java_project_path),
        max_depth=max_dependencies,
    )
    timings["retrieval_ms"] = int((time.perf_counter() - t0) * 1000)

    rag_context: str | None = None
    context_sources: dict[str, Any] = {}

    if rag_enabled:
        t_rag = time.perf_counter()
        output.print_info("    📦 Running RAG retrieval...")
        rag_context, context_sources = _run_rag_retrieval(
            parsed_class=parsed_class,
            project_path=Path(java_project_path),
            strategy=retrieval_strategy,
            config=rag_config or {},
        )
        rag_ms = int((time.perf_counter() - t_rag) * 1000)
        timings["retrieval_ms"] = timings["retrieval_ms"] + rag_ms

    t0 = time.perf_counter()
    output.print_info("[3/4] 📝 Building prompt...")
    max_ctx_tokens = (rag_config or {}).get("max_context_tokens", 4000)
    code_under_test, dependency_signatures = _build_prompt(
        parsed_class=parsed_class,
        project_path=Path(java_project_path),
        dependency_context=dependency_context,
        max_dependencies=max_dependencies,
        rag_context=rag_context,
        max_context_tokens=max_ctx_tokens,
    )
    timings["prompt_ms"] = int((time.perf_counter() - t0) * 1000)

    output.print_info(f"    Context length: {len(dependency_signatures)} chars")

    # Default feedback config - disabled by default (mvn compile is slow)
    # Users can enable via FeedbackLoopConfig in main.py CLI
    if feedback_config is None:
        class DefaultFeedback:
            max_retries = 1
            retry_on_compile_fail = False
        feedback_config = DefaultFeedback()
    
    max_retries = getattr(feedback_config, "max_retries", 1)
    retry_on_compile_fail = getattr(feedback_config, "retry_on_compile_fail", False)

    attempt = 1
    compile_errors: list[str] = []
    final_status = "success"
    test_code = ""

    t_llm_total = time.perf_counter()
    while attempt <= max_retries:
        t0 = time.perf_counter()
        output.print_info(f"[4/4] 🤖 Generating test with LLM... (attempt {attempt}/{max_retries})")
        test_code = _generate_test_with_llm(
            code_under_test=code_under_test,
            dependency_context=dependency_signatures,
            provider=llm_provider,
            model=llm_model,
        )
        timings[f"llm_attempt_{attempt}_ms"] = int((time.perf_counter() - t0) * 1000)

        if test_code is None:
            test_code = ""
            output.print_warning("LLM returned None, using empty string")
        cleaned_code = test_code.strip()
        if cleaned_code.startswith("```java"):
            cleaned_code = cleaned_code[7:]
        if cleaned_code.startswith("```"):
            cleaned_code = cleaned_code[3:]
        if cleaned_code.endswith("```"):
            cleaned_code = cleaned_code[:-3]
        cleaned_code = cleaned_code.strip()

        # Skip compile validation in feedback loop - evaluator handles it
        # Validation via mvn compile is too slow for retry loop
        if retry_on_compile_fail and attempt < max_retries:
            # Add feedback for next attempt
            feedback = f"Attempt {attempt} generated - will be validated by evaluator"
            t_prompt = time.perf_counter()
            code_under_test, dependency_signatures = _build_prompt(
                parsed_class=parsed_class,
                project_path=Path(java_project_path),
                dependency_context=dependency_context,
                max_dependencies=max_dependencies,
                rag_context=rag_context,
                max_context_tokens=max_ctx_tokens,
                feedback_context=feedback,
            )
            timings[f"prompt_rebuild_attempt_{attempt}_ms"] = int((time.perf_counter() - t_prompt) * 1000)
            attempt += 1
        else:
            final_status = "success"
            break

    timings["llm_ms"] = int((time.perf_counter() - t_llm_total) * 1000)

    output_path = Path(output_dir)
    output_path.mkdir(parents=True, exist_ok=True)

    test_class_name = f"{parsed_class.name}Test.java"
    test_file_path = output_path / test_class_name

    test_file_path.write_text(cleaned_code, encoding="utf-8")
    output.print_success(f"Test generated: {test_file_path}")

    return GenerationResult(
        test_code=cleaned_code,
        output_path=str(test_file_path),
        timings=timings,
        retrieval_strategy=retrieval_strategy,
        context_sources=context_sources,
        attempt_count=attempt,
        compile_errors=compile_errors,
        final_status=final_status,
    )


def _run_rag_retrieval(
    parsed_class,
    project_path: Path,
    strategy: str,
    config: dict[str, Any],
) -> tuple[str | None, dict[str, Any]]:
    from rag.chunker import ASTChunker
    from rag.embedder import EmbeddingService
    from rag.indexer import ChromaIndexer
    from rag.retriever import HybridRetriever

    alpha = config.get("alpha", 0.5)
    top_k = config.get("retrieval_top_k", 10)
    max_tokens = config.get("max_context_tokens", 4000)
    persist_dir = config.get("chroma_persist_dir", ".chroma_db")
    model_name = config.get("embedding_model", "all-MiniLM-L6-v2")

    embedder = EmbeddingService(model_name=model_name)
    indexer = ChromaIndexer(persist_dir=persist_dir, embedder=embedder)
    chunker = ASTChunker()

    import hashlib
    collection_name = f"project_{hashlib.md5(str(project_path).encode()).hexdigest()[:12]}"

    chunks = chunker.chunk(parsed_class)
    from core.retriever import JavaFileRetriever

    java_files = JavaFileRetriever(str(project_path)).get_all_java_files()
    for f in java_files:
        try:
            file_chunks = chunker.chunk_file(f)
            chunks.extend(file_chunks)
        except Exception:
            pass

    indexer.index(chunks, collection_name)

    ast_deps: set[str] = set()
    if strategy in ("ast", "hybrid"):
        retriever = JavaFileRetriever(str(project_path))
        for dep in parsed_class.dependencies:
            if dep.type in ("class", "interface", "import", "field"):
                ast_deps.add(dep.name)
        for imp in parsed_class.imports:
            parts = imp.split(".")
            if len(parts) > 1 and not imp.startswith(("java.", "javax.", "org.")):
                dep_name = parts[-1]
                if not dep_name.endswith("*"):
                    ast_deps.add(dep_name)

    effective_alpha = alpha
    if strategy == "ast":
        effective_alpha = 0.0
    elif strategy == "rag":
        effective_alpha = 1.0

    hybrid = HybridRetriever(
        indexer=indexer,
        embedder=embedder,
        alpha=effective_alpha,
        ast_dependencies=ast_deps,
    )

    query = parsed_class.content[:2000]
    results = hybrid.retrieve(query, k=top_k)
    rag_context = hybrid.build_context(results, max_tokens=max_tokens)

    context_sources = {
        "strategy": strategy,
        "chunks_indexed": len(chunks),
        "results_retrieved": len(results),
        "top_scores": [r.hybrid_score for r in results[:5]],
    }

    return rag_context or None, context_sources


def _parse_java_file(java_file_path: str):
    """Parse a Java file and extract its structure.

    Args:
        java_file_path: Path to the Java file.

    Returns:
        Parsed Java class.
    """
    from core.parser import JavaParser

    parser = JavaParser()
    return parser.parse_file(java_file_path)


def _resolve_dependencies(parsed_class, project_path: Path, max_depth: int):
    """Resolve dependencies for a parsed Java class.

    Args:
        parsed_class: Parsed Java class.
        project_path: Root path of the Java project.
        max_depth: Maximum dependency resolution depth.

    Returns:
        List of resolved dependencies.
    """
    from core.retriever import DependencyResolver, JavaFileRetriever

    retriever = JavaFileRetriever(project_path)
    resolver = DependencyResolver(retriever)

    dependencies = []
    for dep in parsed_class.imports:
        resolved = resolver.resolve_dependencies(dep, max_depth=max_depth)
        dependencies.extend(resolved)

    return dependencies


def _build_prompt(parsed_class, project_path, dependency_context, max_dependencies,
                  rag_context=None, max_context_tokens=4000, feedback_context=None):
    from core.prompt_builder import PromptBuilder
    from core.retriever import DependencyResolver, JavaFileRetriever

    retriever = JavaFileRetriever(project_path)
    dependency_resolver = DependencyResolver(retriever)
    builder = PromptBuilder(retriever, dependency_resolver)

    java_file_path = parsed_class.file_path
    return builder.build_prompt(
        java_file_path=java_file_path,
        max_dependencies=max_dependencies,
        rag_context=rag_context,
        max_context_tokens=max_context_tokens,
        feedback_context=feedback_context,
    )


def _generate_test_with_llm(
    code_under_test: str,
    dependency_context: str,
    provider: str,
    model: str,
) -> str:
    """Generate test code using LLM.

    Args:
        code_under_test: Code under test.
        dependency_context: Dependency context.
        provider: LLM provider.
        model: LLM model.

    Returns:
        Generated test code.
    """
    from llm.client_new import LLMClient, LLMConfig

    llm_config = LLMConfig(
        provider=provider,
        model=model,
        temperature=0.3,
        max_tokens=4096,
    )
    llm_client = LLMClient(llm_config)
    test_code = llm_client.generate_test(code_under_test, dependency_context)

    return test_code
