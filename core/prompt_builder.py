"""
Prompt builder module for assembling the master prompt
with code under test and extracted context.
"""

from core.parsing.models import MethodSignature, ParsedJavaClass
from rag.retriever import DependencyResolver, JavaFileRetriever


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
        rag_context: str | None = None,
        max_context_tokens: int = 4000,
        feedback_context: str | None = None,
    ) -> tuple[str, str]:
        """
        Build the complete prompt with code and context.

        Args:
            java_file_path: Path to the Java file to test
            max_dependencies: Maximum number of dependencies to include
            rag_context: Optional RAG-retrieved context to include
            max_context_tokens: Maximum token budget for combined context
            feedback_context: Optional feedback from a previous attempt to include

        Returns:
            Tuple of (code_under_test, dependency_context)
        """
        parsed = self.retriever.parse_file(java_file_path)
        if not parsed:
            raise ValueError(f"Could not parse file: {java_file_path}")

        code_under_test = parsed.content

        dependency_context = self._build_dependency_context(parsed, max_dependencies)

        public_methods_context = self._build_public_methods_context(parsed)
        dependency_context = public_methods_context + "\n" + dependency_context

        if rag_context:
            rag_section = self._format_rag_context(rag_context)
            char_budget = max_context_tokens * 4
            remaining = char_budget - len(dependency_context)
            if remaining < len(rag_section):
                rag_section = rag_section[: max(0, remaining)]
            dependency_context = dependency_context + "\n" + rag_section

        if feedback_context:
            feedback_section = self._format_feedback_context(feedback_context)
            dependency_context = dependency_context + "\n" + feedback_section

        return code_under_test, dependency_context

    def _format_rag_context(self, rag_context: str) -> str:
        header = "### CONTEXTO ADICIONAL (RAG - Recuperación Semántica)"
        return f"{header}\n{rag_context}"

    def _format_feedback_context(self, feedback_context: str) -> str:
        header = "### FEEDBACK DE INTENTO ANTERIOR"
        return f"{header}\n{feedback_context}"

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
            if (
                not imp.startswith("java.")
                and not imp.startswith("javax.")
                and not imp.startswith("org.")
            ):
                parts = imp.split(".")
                if len(parts) > 1:
                    dep_name = parts[-1]
                    if not dep_name.endswith("*"):
                        dependency_names.add(dep_name)

        # Resolve and format each dependency
        count = 0
        for dep_name in dependency_names:
            if count >= max_dependencies:
                lines.append(
                    f"\n// ... and {len(dependency_names) - max_dependencies} more dependencies (truncated)"
                )
                break

            # Try to find and format the dependency
            signature = self.dependency_resolver.get_method_signatures(dep_name)
            lines.append(signature)
            count += 1

        # Also add field type dependencies
        for field in parsed_class.fields:
            if field.type and field.type not in dependency_names:
                signature = self.dependency_resolver.get_method_signatures(field.type)
                if not signature.startswith(f"// Class {field.type} not found"):
                    lines.append(signature)

        return "\n".join(lines)

    def _build_public_methods_context(
        self,
        parsed_class: ParsedJavaClass,
    ) -> str:
        """
        Build context with public methods of the class under test.

        This helps the LLM know which methods it can safely call in tests.

        Args:
            parsed_class: Parsed Java class

        Returns:
            Formatted string with public methods
        """
        lines = []
        lines.append(f"// MÉTODOS PÚBLICOS DE {parsed_class.name} (usa SOLO estos en los tests):")
        lines.append("")

        # Filter for public methods only
        public_methods = [m for m in parsed_class.methods if m.visibility == "public"]

        for method in public_methods:
            params = ", ".join(f"{t} {n}" for t, n in method.parameters)
            static = "static " if method.is_static else ""
            lines.append(f"    public {static}{method.return_type} {method.name}({params});")

        lines.append("")
        return "\n".join(lines)

    def format_method_signatures(self, class_name: str, methods: list[MethodSignature]) -> str:
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
            params = ", ".join(f"{t} {n}" for t, n in method.parameters)
            visibility = method.visibility
            static = "static " if method.is_static else ""
            lines.append(f"    {visibility} {static}{method.return_type} {method.name}({params});")

        lines.append("")
        return "\n".join(lines)


def build_test_prompt(
    java_file_path: str,
    java_project_path: str,
    max_dependencies: int = 10,
    rag_context: str | None = None,
    max_context_tokens: int = 4000,
    feedback_context: str | None = None,
) -> tuple[str, str]:
    """
    Convenience function to build the test prompt.

    Args:
        java_file_path: Path to the Java file to test
        java_project_path: Root path of the Java project
        max_dependencies: Maximum dependencies to include
        rag_context: Optional RAG-retrieved context
        max_context_tokens: Maximum token budget for combined context
        feedback_context: Optional feedback from a previous attempt

    Returns:
        Tuple of (code_under_test, dependency_context)
    """
    retriever = JavaFileRetriever(java_project_path)
    resolver = DependencyResolver(retriever)
    builder = PromptBuilder(retriever, resolver)
    return builder.build_prompt(
        java_file_path,
        max_dependencies,
        rag_context=rag_context,
        max_context_tokens=max_context_tokens,
        feedback_context=feedback_context,
    )


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
