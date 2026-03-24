"""
Prompt builder module for assembling the master prompt
with code under test and extracted context.
"""

from typing import List
from pathlib import Path

from .parser import ParsedJavaClass, MethodSignature
from .retriever import DependencyResolver, JavaFileRetriever


class PromptBuilder:
    """
    Builds the dynamic prompt for LLM test generation.
    Combines code under test with retrieved dependency context.
    """

    def __init__(
        self,
        retriever: JavaFileRetriever,
        dependency_resolver: DependencyResolver,
    ):
        """
        Initialize the prompt builder.

        Args:
            retriever: JavaFileRetriever instance
            dependency_resolver: DependencyResolver instance
        """
        self.retriever = retriever
        self.dependency_resolver = dependency_resolver

    def build_prompt(
        self,
        java_file_path: str,
        max_dependencies: int = 10,
    ) -> tuple[str, str]:
        """
        Build the complete prompt with code and context.

        Args:
            java_file_path: Path to the Java file to test
            max_dependencies: Maximum number of dependencies to include

        Returns:
            Tuple of (code_under_test, dependency_context)
        """
        # Parse the file under test
        parsed = self.retriever.parse_file(java_file_path)
        if not parsed:
            raise ValueError(f"Could not parse file: {java_file_path}")

        code_under_test = parsed.content

        # Extract and resolve dependencies
        dependency_context = self._build_dependency_context(
            parsed, max_dependencies
        )

        return code_under_test, dependency_context

    def _build_dependency_context(
        self,
        parsed_class: ParsedJavaClass,
        max_dependencies: int,
    ) -> str:
        """
        Build the dependency context string with method signatures.

        Args:
            parsed_class: Parsed Java class
            max_dependencies: Maximum dependencies to include

        Returns:
            Formatted dependency context string
        """
        lines = []

        # Get unique dependency names
        dependency_names = set()
        for dep in parsed_class.dependencies:
            if dep.type in ("class", "interface", "import", "field"):
                dependency_names.add(dep.name)

        # Also add dependencies from import statements
        for imp in parsed_class.imports:
            if not imp.startswith("java.") and not imp.startswith("javax.") and not imp.startswith("org."):
                parts = imp.split(".")
                if len(parts) > 1:
                    dep_name = parts[-1]
                    if not dep_name.endswith("*"):
                        dependency_names.add(dep_name)

        # Resolve and format each dependency
        count = 0
        for dep_name in dependency_names:
            if count >= max_dependencies:
                lines.append(f"\n// ... and {len(dependency_names) - max_dependencies} more dependencies (truncated)")
                break

            # Try to find and format the dependency
            signature = self.dependency_resolver.get_method_signatures(dep_name)
            lines.append(signature)
            count += 1

        # Also add field type dependencies
        for field in parsed_class.fields:
            field_type = field.get("type", "")
            if field_type and field_type not in dependency_names:
                signature = self.dependency_resolver.get_method_signatures(field_type)
                if not signature.startswith(f"// Class {field_type} not found"):
                    lines.append(signature)

        return "\n".join(lines)

    def format_method_signatures(
        self, class_name: str, methods: List[MethodSignature]
    ) -> str:
        """
        Format method signatures for a class.

        Args:
            class_name: Name of the class
            methods: List of method signatures

        Returns:
            Formatted string
        """
        lines = [f"// {class_name}"]
        lines.append("")

        for method in methods:
            params = ", ".join(method.parameters)
            visibility = method.visibility
            static = "static " if method.is_static else ""
            lines.append(f"    {visibility} {static}{method.return_type} {method.name}({params});")

        lines.append("")
        return "\n".join(lines)


def build_test_prompt(
    java_file_path: str,
    java_project_path: str,
    max_dependencies: int = 10,
) -> tuple[str, str]:
    """
    Convenience function to build the test prompt.

    Args:
        java_file_path: Path to the Java file to test
        java_project_path: Root path of the Java project
        max_dependencies: Maximum dependencies to include

    Returns:
        Tuple of (code_under_test, dependency_context)
    """
    retriever = JavaFileRetriever(java_project_path)
    resolver = DependencyResolver(retriever)
    builder = PromptBuilder(retriever, resolver)
    return builder.build_prompt(java_file_path, max_dependencies)


if __name__ == "__main__":
    # Example usage
    import sys

    if len(sys.argv) < 3:
        print("Usage: python prompt_builder.py <java_file> <project_path>")
        sys.exit(1)

    java_file = sys.argv[1]
    project_path = sys.argv[2]

    code, context = build_test_prompt(java_file, project_path)

    print("=" * 50)
    print("CODE UNDER TEST:")
    print("=" * 50)
    print(code)

    print("\n" + "=" * 50)
    print("DEPENDENCY CONTEXT:")
    print("=" * 50)
    print(context)
