"""
Core module for AST parsing of Java files.
Uses Tree-sitter to extract dependencies, imports, and method signatures.
"""

from pathlib import Path
from typing import List, Dict, Optional
from dataclasses import dataclass

try:
    from tree_sitter import Language, Parser
except ImportError:
    raise ImportError("Tree-sitter is not installed. Install with: pip install tree-sitter")


@dataclass
class JavaDependency:
    """Represents a dependency extracted from Java code."""
    name: str
    type: str  # "class", "interface", "import", "field", "method"
    package: Optional[str] = None
    file_path: Optional[str] = None


@dataclass
class MethodSignature:
    """Represents a method signature extracted from a Java class/interface."""
    visibility: str
    return_type: str
    name: str
    parameters: List[str]
    is_static: bool = False


@dataclass
class ParsedJavaClass:
    """Represents a parsed Java class/interface with all its details."""
    name: str
    package: Optional[str]
    imports: List[str]
    dependencies: List[JavaDependency]
    methods: List[MethodSignature]
    fields: List[Dict]
    file_path: str
    content: str


class JavaParser:
    """Parser for Java files using Tree-sitter."""

    def __init__(self):
        self.parser = Parser()
        self._load_java_language()

    def _load_java_language(self):
        """Load the Java language grammar for Tree-sitter."""
        try:
            # Try to load from tree-sitter-java package
            import tree_sitter_java as tsjava
            self.java_lang = Language(tsjava.language())
        except Exception as e:
            raise ImportError(
                f"Could not load Java language parser: {e}\n"
                "Make sure tree-sitter-java is installed: pip install tree-sitter-java"
            )

        # Set language using the correct API
        self.parser.language = self.java_lang

    def parse_file(self, file_path: str) -> ParsedJavaClass:
        """
        Parse a Java file and extract all relevant information.

        Args:
            file_path: Path to the Java file to parse

        Returns:
            ParsedJavaClass with all extracted information
        """
        path = Path(file_path)
        if not path.exists():
            raise FileNotFoundError(f"File not found: {file_path}")

        content = path.read_text(encoding="utf-8")
        tree = self.parser.parse(bytes(content, "utf8"))

        return self._extract_class_info(tree, str(path), content)

    def _extract_class_info(
        self, tree, file_path: str, content: str
    ) -> ParsedJavaClass:
        """Extract class information from the parsed tree."""
        root = tree.root_node

        package = self._extract_package(root)
        imports = self._extract_imports(root)
        dependencies = self._extract_dependencies(root, imports)
        methods = self._extract_methods(root)
        fields = self._extract_fields(root)
        class_name = self._extract_class_name(root)

        return ParsedJavaClass(
            name=class_name,
            package=package,
            imports=imports,
            dependencies=dependencies,
            methods=methods,
            fields=fields,
            file_path=file_path,
            content=content,
        )

    def _extract_package(self, root) -> Optional[str]:
        """Extract the package declaration."""
        for child in root.children:
            if child.type == "package_declaration":
                # Get the package identifier
                for child2 in child.children:
                    if child2.type == "scoped_identifier":
                        return child2.text.decode("utf-8")
                    elif child2.type == "identifier":
                        return child2.text.decode("utf-8")
        return None

    def _extract_imports(self, root) -> List[str]:
        """Extract all import statements."""
        imports = []
        for child in root.children:
            if child.type == "import_declaration":
                # Get the imported identifier
                for child2 in child.children:
                    if child2.type == "scoped_identifier":
                        import_text = child2.text.decode("utf-8")
                        imports.append(import_text)
                        break
                    elif child2.type == "identifier":
                        # Simple import
                        import_text = child2.text.decode("utf-8")
                        imports.append(import_text)
                        break
                    # Handle star imports
                    elif child2.type == "asterisk":
                        # Get the scoped identifier before asterisk
                        for child3 in child.children:
                            if child3.type == "scoped_identifier":
                                imports.append(child3.text.decode("utf-8") + ".*")
                                break
        return imports

    def _extract_class_name(self, root) -> str:
        """Extract the main class/interface name."""
        for child in root.children:
            if child.type in ("class_declaration", "interface_declaration"):
                for child2 in child.children:
                    if child2.type == "identifier":
                        return child2.text.decode("utf-8")
        return "Unknown"

    def _extract_dependencies(
        self, root, imports: List[str]
    ) -> List[JavaDependency]:
        """
        Extract dependencies from imports and field declarations.

        Returns list of JavaDependency objects.
        """
        dependencies = []

        for imp in imports:
            if not imp.startswith("java.") and not imp.startswith("javax.") and not imp.startswith("org.springframework"):
                deps = self._parse_import_to_dependencies(imp)
                dependencies.extend(deps)

        for child in root.children:
            if child.type in ("class_declaration", "interface_declaration"):
                for child2 in child.children:
                    if child2.type in ("class_body", "interface_body"):
                        for child3 in child2.children:
                            if child3.type == "field_declaration":
                                dep = self._extract_field_dependency(child3)
                                if dep:
                                    dependencies.append(dep)

        return dependencies

    def _parse_import_to_dependencies(self, import_str: str) -> List[JavaDependency]:
        """Parse an import string into dependency objects."""
        deps = []
        if import_str.endswith(".*"):
            # Package import - can't determine exact classes
            return deps

        # Specific class import
        parts = import_str.split(".")
        if len(parts) > 1:
            class_name = parts[-1]
            package = ".".join(parts[:-1])
            deps.append(
                JavaDependency(
                    name=class_name,
                    type="import",
                    package=package,
                )
            )
        return deps

    def _extract_field_dependency(self, field_node) -> Optional[JavaDependency]:
        """Extract dependency from a field declaration."""
        for child in field_node.children:
            if child.type == "type_identifier":
                field_type = child.text.decode("utf-8")
                return JavaDependency(
                    name=field_type,
                    type="field",
                )
        return None

    def _extract_methods(self, root) -> List[MethodSignature]:
        """
        Extract all method signatures from the class/interface.

        Returns list of MethodSignature objects.
        """
        methods = []

        for child in root.children:
            if child.type in ("class_declaration", "interface_declaration"):
                for child2 in child.children:
                    if child2.type in ("class_body", "interface_body"):
                        for child3 in child2.children:
                            if child3.type == "method_declaration":
                                method = self._parse_method_signature(child3)
                                if method:
                                    methods.append(method)

        return methods

    def _parse_method_signature(self, method_node) -> Optional[MethodSignature]:
        """Parse a method declaration node into a MethodSignature."""
        visibility = "package-private"
        return_type = "void"
        name = ""
        parameters = []
        is_static = False

        for child in method_node.children:
            if child.type == "modifiers":
                visibility = self._extract_visibility(child)
                for mod in child.children:
                    if mod.type == "static":
                        is_static = True
            elif child.type == "type_identifier" or child.type == "void_type":
                return_type = child.text.decode("utf-8")
            elif child.type == "identifier":
                name = child.text.decode("utf-8")
            elif child.type == "formal_parameters":
                parameters = self._extract_parameters(child)

        if name:
            return MethodSignature(
                visibility=visibility,
                return_type=return_type,
                name=name,
                parameters=parameters,
                is_static=is_static,
            )
        return None

    def _extract_visibility(self, modifiers_node) -> str:
        """Extract visibility modifier from modifiers node."""
        for child in modifiers_node.children:
            if child.type in ("public", "private", "protected"):
                return child.type
        return "package-private"

    def _extract_parameters(self, params_node) -> List[str]:
        """Extract parameter types from formal parameters node."""
        params = []
        for child in params_node.children:
            if child.type == "formal_parameter":
                param_type = ""
                for param_child in child.children:
                    if param_child.type in ("type_identifier", "identifier"):
                        param_type = param_child.text.decode("utf-8")
                if param_type:
                    params.append(param_type)
        return params

    def _extract_fields(self, root) -> List[Dict]:
        """Extract field declarations with types and names."""
        fields = []

        for child in root.children:
            if child.type in ("class_declaration", "interface_declaration"):
                for child2 in child.children:
                    if child2.type in ("class_body", "interface_body"):
                        for child3 in child2.children:
                            if child3.type == "field_declaration":
                                field = self._parse_field(child3)
                                if field:
                                    fields.append(field)

        return fields

    def _parse_field(self, field_node) -> Optional[Dict]:
        """Parse a field declaration node."""
        field_type = ""
        field_name = ""

        for child in field_node.children:
            if child.type == "type_identifier":
                field_type = child.text.decode("utf-8")
            elif child.type == "variable_declarator":
                for declarator_child in child.children:
                    if declarator_child.type == "identifier":
                        field_name = declarator_child.text.decode("utf-8")

        if field_type and field_name:
            return {"type": field_type, "name": field_name}
        return None


def extract_dependencies_from_file(java_file_path: str) -> ParsedJavaClass:
    """
    Convenience function to parse a Java file and extract all information.

    Args:
        java_file_path: Path to the Java file

    Returns:
        ParsedJavaClass with all extracted information
    """
    parser = JavaParser()
    return parser.parse_file(java_file_path)


if __name__ == "__main__":
    # Example usage
    import sys

    if len(sys.argv) < 2:
        print("Usage: python parser.py <java_file>")
        sys.exit(1)

    java_file = sys.argv[1]
    result = extract_dependencies_from_file(java_file)

    print(f"Class: {result.name}")
    print(f"Package: {result.package}")
    print(f"\nImports: {len(result.imports)}")
    for imp in result.imports:
        print(f"  - {imp}")
    print(f"\nDependencies: {len(result.dependencies)}")
    for dep in result.dependencies:
        print(f"  - {dep.type}: {dep.name}")
    print(f"\nMethods: {len(result.methods)}")
    for method in result.methods:
        params = ", ".join(method.parameters)
        static = "static " if method.is_static else ""
        print(f"  - {method.visibility} {static}{method.return_type} {method.name}({params})")
