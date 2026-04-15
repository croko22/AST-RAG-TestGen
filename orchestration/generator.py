"""
Orchestration layer for test generation.

This module provides the orchestration logic for generating tests,
separating business logic from CLI concerns.
"""

from __future__ import annotations

from pathlib import Path

from output import get_output


def generate_test_for_file(
    java_file_path: str,
    java_project_path: str,
    output_dir: str,
    max_dependencies: int = 10,
    llm_provider: str = "anthropic",
    llm_model: str = "claude-3-5-sonnet-20241022",
    enable_metainfo_db: bool = False,
    enable_reftest_parity: bool = False,
) -> str:
    """
    Generate a unit test for a Java file.

    This function orchestrates the 4-step pipeline:
    1. Extractor (Tree-sitter): Parse Java file and extract dependencies
    2. RAG (Retriever): Find dependency files in the Java project
    3. Slicer: Extract method signatures from dependencies
    4. Prompt Builder: Assemble dynamic prompt and send to LLM

    Args:
        java_file_path: Path to the Java file to generate tests for
        java_project_path: Root path of the Java project
        output_dir: Directory to save generated tests
        max_dependencies: Maximum number of dependencies to include
        llm_provider: LLM provider to use
        llm_model: LLM model to use
        enable_metainfo_db: Enable experimental metainfo DB flow
        enable_reftest_parity: Enable experimental reftest parity flow

    Returns:
        Generated test code.
    """
    output = get_output()

    # Step 1: Parse Java file
    output.print_info("[1/4] 📄 Parsing Java file...")
    parsed_class = _parse_java_file(java_file_path)

    # Step 2: Resolve dependencies
    output.print_info("[2/4] 🔍 Resolving dependencies...")
    dependency_context = _resolve_dependencies(
        parsed_class=parsed_class,
        project_path=Path(java_project_path),
        max_depth=max_dependencies,
    )

    # Step 3: Build prompt
    output.print_info("[3/4] 📝 Building prompt...")
    code_under_test, dependency_signatures = _build_prompt(
        parsed_class=parsed_class,
        dependency_context=dependency_context,
        max_dependencies=max_dependencies,
    )

    output.print_info(f"    Context length: {len(dependency_context)} chars")

    # Step 4: Generate test with LLM
    output.print_info("[4/4] 🤖 Generating test with LLM...")
    test_code = _generate_test_with_llm(
        code_under_test=code_under_test,
        dependency_context=dependency_signatures,
        provider=llm_provider,
        model=llm_model,
    )

    # Save the generated test
    output_path = Path(output_dir)
    output_path.mkdir(parents=True, exist_ok=True)

    test_class_name = f"{parsed_class.name}Test.java"
    test_file_path = output_path / test_class_name

    # Clean up test code (remove markdown code blocks if present)
    test_code = test_code.strip()
    if test_code.startswith("```java"):
        test_code = test_code[7:]
    if test_code.startswith("```"):
        test_code = test_code[3:]
    if test_code.endswith("```"):
        test_code = test_code[:-3]
    test_code = test_code.strip()

    test_file_path.write_text(test_code, encoding="utf-8")
    output.print_success(f"Test generated: {test_file_path}")

    return test_code


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
    resolver = DependencyResolver(retriever, max_depth=max_depth)

    dependencies = []
    for dep in parsed_class.imports:
        resolved = resolver.resolve_dependencies(dep, project_path)
        dependencies.extend(resolved)

    return dependencies


def _build_prompt(parsed_class, dependency_context, max_dependencies):
    """Build the prompt for LLM generation.

    Args:
        parsed_class: Parsed Java class.
        dependency_context: Resolved dependencies.
        max_dependencies: Maximum number of dependencies to include.

    Returns:
        Tuple of (code under test, dependency signatures).
    """
    from core.prompt_builder import PromptBuilder

    builder = PromptBuilder()
    return builder.build_prompt(
        parsed_class=parsed_class,
        dependencies=dependency_context,
        max_dependencies=max_dependencies,
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
    from llm import client as llm_client_module

    LLMConfig = llm_client_module.LLMConfig
    LLMClient = llm_client_module.LLMClient

    llm_config = LLMConfig(
        provider=provider,
        model=model,
        temperature=0.3,
        max_tokens=4096,
    )
    llm_client = LLMClient(llm_config)
    test_code = llm_client.generate_test(code_under_test, dependency_context)

    return test_code
