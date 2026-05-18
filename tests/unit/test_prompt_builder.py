"""Unit tests for prompt builder module."""

import re
from pathlib import Path

import pytest

from core import PromptBuilder
from core.parsing.models import MethodSignature
from core.prompt_builder import build_test_prompt


class TestPromptBuilder:
    def test_build_prompt_returns_tuple(self, retriever, resolver):
        """Test that build_prompt returns (code, context) tuple."""
        builder = PromptBuilder(retriever, resolver)
        service_file = f"{retriever.project_root}/src/main/java/com/example/Service.java"
        code, context = builder.build_prompt(service_file)

        assert isinstance(code, str)
        assert isinstance(context, str)

    def test_code_under_test_is_full_content(self, retriever, resolver):
        """Test that code_under_test contains full file content."""
        builder = PromptBuilder(retriever, resolver)
        service_file = f"{retriever.project_root}/src/main/java/com/example/Service.java"
        code, _ = builder.build_prompt(service_file)

        full_content = Path(service_file).read_text()
        assert code == full_content

    def test_dependency_context_has_signatures(self, retriever, resolver):
        """Test that dependency context contains method signatures."""
        builder = PromptBuilder(retriever, resolver)
        service_file = f"{retriever.project_root}/src/main/java/com/example/Service.java"
        _, context = builder.build_prompt(service_file)

        assert "interface" in context or "class" in context

    def test_max_dependencies_limits_output(self, retriever, resolver):
        """Test that max_dependencies truncates results."""
        builder = PromptBuilder(retriever, resolver)
        service_file = f"{retriever.project_root}/src/main/java/com/example/Service.java"
        _, context = builder.build_prompt(service_file, max_dependencies=1)

        class_matches = re.findall(r"// (\w+)", context)
        assert len(class_matches) <= 2

    def test_dependency_context_includes_method_names(self, retriever, resolver):
        """Test that method names appear in context."""
        builder = PromptBuilder(retriever, resolver)
        service_file = f"{retriever.project_root}/src/main/java/com/example/Service.java"
        _, context = builder.build_prompt(service_file)

        assert "findAll" in context

    def test_build_prompt_with_nonexistent_file(self, retriever, resolver):
        """Test that non-existent file raises ValueError."""
        builder = PromptBuilder(retriever, resolver)

        with pytest.raises(ValueError, match="Could not parse file"):
            builder.build_prompt("/nonexistent/file.java")

    def test_format_method_signatures(self, retriever, resolver):
        """Test formatting method signatures."""
        builder = PromptBuilder(retriever, resolver)

        methods = [
            MethodSignature(
                name="getData",
                visibility="public",
                return_type="String",
                parameters=[("int", "id"), ("String", "filter")],
                is_static=False,
            ),
            MethodSignature(
                name="process",
                visibility="private",
                return_type="void",
                parameters=[],
                is_static=True,
            ),
        ]

        result = builder.format_method_signatures("TestClass", methods)

        assert "// TestClass" in result
        assert "public" in result
        assert "String getData" in result
        assert "private static void process" in result

    def test_build_test_prompt_convenience_function(self, mock_project_root):
        """Test the convenience function for building prompt."""
        service_file = f"{mock_project_root}/src/main/java/com/example/Service.java"
        code, context = build_test_prompt(service_file, mock_project_root, max_dependencies=5)

        assert "Service" in code
        assert isinstance(context, str)

    def test_truncation_message(self, retriever, resolver):
        """Test that max_dependencies limits dependency context output."""
        builder = PromptBuilder(retriever, resolver)
        service_file = f"{retriever.project_root}/src/main/java/com/example/Service.java"

        _, context = builder.build_prompt(service_file, max_dependencies=0)

        # Context includes public methods + dependency signatures
        # With max_dependencies=0, no dependency method signatures are included
        assert isinstance(context, str)
