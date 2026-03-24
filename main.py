#!/usr/bin/env python3
"""
AST-RAG TestGen: Main orchestrator

This is the main entry point for the AST-based RAG test generation system.
It orchestrates the 4-step pipeline:
1. Extractor (Tree-sitter): Parse Java file and extract dependencies
2. RAG (Retriever): Find dependency files in the Java project
3. Slicer: Extract method signatures from dependencies
4. Prompt Builder: Assemble dynamic prompt and send to LLM
"""

import sys
import argparse
from pathlib import Path
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

from core import JavaFileRetriever, DependencyResolver, PromptBuilder
from llm import LLMClient, LLMConfig, get_available_providers, get_default_model


def generate_test_for_file(
    java_file_path: str,
    java_project_path: str,
    output_dir: str = "./tests_generados",
    max_dependencies: int = 10,
    llm_provider: str = "anthropic",
    llm_model: str = "claude-3-5-sonnet-20241022",
) -> str:
    """
    Generate a unit test for a given Java file.

    Args:
        java_file_path: Path to the Java file to test
        java_project_path: Root path of the Java project
        output_dir: Directory to save generated tests
        max_dependencies: Maximum number of dependencies to include
        llm_provider: LLM provider (anthropic, openai, glm, gemini, openrouter)
        llm_model: Model to use

    Returns:
        Generated test code as string
    """
    print(f"\n{'='*60}")
    print(f"AST-RAG TestGen")
    print(f"{'='*60}")
    print(f"\n📁 File: {java_file_path}")
    print(f"📦 Project: {java_project_path}")
    print(f"🤖 LLM: {llm_provider}/{llm_model}")

    # Step 1: Initialize retriever and resolver
    print(f"\n[1/4] 📥 Initializing retriever...")
    retriever = JavaFileRetriever(java_project_path)
    resolver = DependencyResolver(retriever)
    prompt_builder = PromptBuilder(retriever, resolver)

    # Step 2: Parse the file and extract dependencies
    print(f"[2/4] 🔍 Parsing Java file and extracting dependencies...")
    parsed = retriever.parse_file(java_file_path)
    if not parsed:
        raise ValueError(f"Could not parse file: {java_file_path}")
    print(f"    Class: {parsed.name}")
    print(f"    Package: {parsed.package}")
    print(f"    Methods: {len(parsed.methods)}")
    print(f"    Dependencies: {len(parsed.dependencies)}")

    # Step 3: Build prompt with context
    print(f"[3/4] 🧩 Building prompt with {max_dependencies} max dependencies...")
    code_under_test, dependency_context = prompt_builder.build_prompt(
        java_file_path, max_dependencies
    )
    print(f"    Code length: {len(code_under_test)} chars")
    print(f"    Context length: {len(dependency_context)} chars")

    # Step 4: Generate test with LLM
    print(f"[4/4] 🤖 Generating test with LLM...")
    llm_config = LLMConfig(
        provider=llm_provider,
        model=llm_model,
        temperature=0.3,
        max_tokens=4096,
    )
    llm_client = LLMClient(llm_config)
    test_code = llm_client.generate_test(code_under_test, dependency_context)

    # Save the generated test
    output_path = Path(output_dir)
    output_path.mkdir(parents=True, exist_ok=True)

    test_class_name = f"{parsed.name}Test.java"
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
    print(f"\n✅ Test generated: {test_file_path}")

    return test_code


def main():
    """Main entry point."""
    parser = argparse.ArgumentParser(
        description="AST-RAG TestGen: Generate unit tests using AST-based RAG",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  # Generate test for a specific Java file
  python main.py /path/to/UsuarioService.java /path/to/java/project

  # Use OpenAI instead of Anthropic
  python main.py service.java project/ --provider openai --model gpt-4-turbo

  # Use GLM (Zhipu AI)
  python main.py service.java project/ --provider glm --model glm-4-plus

  # Use Gemini
  python main.py service.java project/ --provider gemini --model gemini-2.0-flash-exp

  # Use OpenRouter
  python main.py service.java project/ --provider openrouter --model anthropic/claude-3.5-sonnet

  # Specify custom output directory
  python main.py service.java project/ --output ./my_tests
        """,
    )
    parser.add_argument(
        "java_file",
        help="Path to the Java file to generate tests for",
    )
    parser.add_argument(
        "project_path",
        help="Root path of the Java project (for dependency resolution)",
    )
    parser.add_argument(
        "--provider",
        default="anthropic",
        choices=get_available_providers(),
        help=f"LLM provider. Available: {', '.join(get_available_providers())} (default: anthropic)",
    )
    parser.add_argument(
        "--model",
        default="claude-3-5-sonnet-20241022",
        help="LLM model to use (default: claude-3-5-sonnet-20241022)",
    )
    parser.add_argument(
        "--output",
        default="./tests_generados",
        help="Output directory for generated tests (default: ./tests_generados)",
    )
    parser.add_argument(
        "--max-deps",
        type=int,
        default=10,
        help="Maximum number of dependencies to include (default: 10)",
    )
    parser.add_argument(
        "--print",
        action="store_true",
        help="Print the generated test to stdout",
    )

    args = parser.parse_args()

    # Check if files exist
    if not Path(args.java_file).exists():
        print(f"Error: Java file not found: {args.java_file}", file=sys.stderr)
        sys.exit(1)

    if not Path(args.project_path).exists():
        print(f"Error: Project path not found: {args.project_path}", file=sys.stderr)
        sys.exit(1)

    try:
        test_code = generate_test_for_file(
            java_file_path=args.java_file,
            java_project_path=args.project_path,
            output_dir=args.output,
            max_dependencies=args.max_deps,
            llm_provider=args.provider,
            llm_model=args.model,
        )

        if args.print:
            print("\n" + "="*60)
            print("GENERATED TEST:")
            print("="*60)
            print(test_code)

        print(f"\n✨ Done! Test saved to {args.output}/{Path(args.java_file).stem}Test.java")

    except Exception as e:
        print(f"\n❌ Error: {e}", file=sys.stderr)
        import traceback
        traceback.print_exc()
        sys.exit(1)


if __name__ == "__main__":
    main()
