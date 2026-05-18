"""
Java parser using Tree-sitter.

This module provides the Tree-sitter based Java parser.
"""

from __future__ import annotations

from typing import Any

try:
    import tree_sitter
    import tree_sitter_java

    TREE_SITTER_AVAILABLE = True
except ImportError:
    TREE_SITTER_AVAILABLE = False

from core.parsing.models import (
    FieldDeclaration,
    JavaDependency,
    MethodSignature,
    ParsedJavaClass,
)


class JavaParser:
    """Parser for Java files using Tree-sitter."""

    def __init__(self):
        """Initialize the Java parser."""
        if not TREE_SITTER_AVAILABLE:
            raise ImportError("tree-sitter or tree-sitter-languages is not installed")

        self.parser = tree_sitter.Parser()
        self.parser.set_language(tree_sitter_java)
        self.query = tree_sitter.Query(
            """
            (program
                (package_declaration
                    (scoped_identifier_name) @package_name)?)
            """
        )

    def parse_file(self, file_path: str) -> ParsedJavaClass:
        """
        Parse a Java file and extract its structure.

        Args:
            file_path: Path to the Java file.

        Returns:
            ParsedJavaClass with extracted information.
        """
        with open(file_path, encoding="utf-8") as f:
            content = f.read()

        return self.parse_content(content, file_path)

    def parse_content(self, content: str, file_path: str = "") -> ParsedJavaClass:
        """
        Parse Java content and extract its structure.

        Args:
            content: Java source code content.
            file_path: Optional file path for reference.

        Returns:
            ParsedJavaClass with extracted information.
        """
        tree = self.parser.parse(bytes(content, "utf8"))

        package_name = None
        imports = []
        fields = []
        methods = []
        class_name = None
        is_interface = False
        is_abstract = False

        # Extract package name
        package_nodes = tree.root_node.children_by_type_name("package_declaration")
        if package_nodes:
            package_node = package_nodes[0]
            package_name_nodes = package_node.children_by_type_name("scoped_identifier_name")
            if package_name_nodes:
                package_name = package_name_nodes[0].text

        # Extract imports
        import_nodes = tree.root_node.children_by_type_name("import_declaration")
        for import_node in import_nodes:
            import_name_nodes = import_node.children_by_type_name("scoped_identifier_name")
            if import_name_nodes:
                imports.append(import_name_nodes[0].text)

        # Extract class/interface information
        class_nodes = tree.root_node.children_by_type_name("class_declaration")
        if class_nodes:
            class_node = class_nodes[0]
            class_name_nodes = class_node.children_by_type_name("identifier")
            if class_name_nodes:
                class_name = class_name_nodes[0].text

            # Check if interface
            interface_nodes = class_node.children_by_type_name("interface")
            is_interface = len(interface_nodes) > 0

            # Check if abstract
            modifiers = class_node.children_by_type_name("modifiers")
            if modifiers:
                modifier_nodes = modifiers[0].children_by_type_name("modifier")
                for modifier in modifier_nodes:
                    if modifier.text == "abstract":
                        is_abstract = True
                        break

            # Extract fields and methods
            class_body_nodes = class_node.children_by_type_name("class_body")
            if class_body_nodes:
                class_body = class_body_nodes[0]

                # Extract fields
                field_nodes = class_body.children_by_type_name("field_declaration")
                for field_node in field_nodes:
                    field = self._extract_field(field_node)
                    if field:
                        fields.append(field)

                # Extract methods
                method_nodes = class_body.children_by_type_name("method_declaration")
                for method_node in method_nodes:
                    method = self._extract_method(method_node)
                    if method:
                        methods.append(method)

        return ParsedJavaClass(
            name=class_name or "Unknown",
            package=package_name,
            imports=imports,
            fields=fields,
            methods=methods,
            content=content,
            is_interface=is_interface,
            is_abstract=is_abstract,
            file_path=file_path,
        )

    def _extract_field(self, field_node: Any) -> FieldDeclaration | None:
        """Extract field information from a field node.

        Args:
            field_node: Tree-sitter field node.

        Returns:
            FieldDeclaration or None if extraction fails.
        """
        try:
            # Extract type
            type_nodes = field_node.children_by_type_name("type_identifier")
            if not type_nodes:
                return None
            field_type = type_nodes[0].text

            # Extract name
            declarator_nodes = field_node.children_by_type_name("variable_declarator")
            if not declarator_nodes:
                return None
            field_name = declarator_nodes[0].text

            # Extract modifiers
            modifiers = field_node.children_by_type_name("modifiers")
            visibility = "package-private"
            is_static = False
            is_final = False

            if modifiers:
                modifier_nodes = modifiers[0].children_by_type_name("modifier")
                for modifier in modifier_nodes:
                    if modifier.text in ("public", "private", "protected"):
                        visibility = modifier.text
                    elif modifier.text == "static":
                        is_static = True
                    elif modifier.text == "final":
                        is_final = True

            return FieldDeclaration(
                name=field_name,
                type=field_type,
                visibility=visibility,
                is_static=is_static,
                is_final=is_final,
            )
        except Exception:
            return None

    def _extract_method(self, method_node: Any) -> MethodSignature | None:
        """Extract method information from a method node.

        Args:
            method_node: Tree-sitter method node.

        Returns:
            MethodSignature or None if extraction fails.
        """
        try:
            # Extract name
            name_nodes = method_node.children_by_type_name("identifier")
            if not name_nodes:
                return None
            method_name = name_nodes[0].text

            # Extract return type
            type_nodes = method_node.children_by_type_name("type_identifier")
            if not type_nodes:
                return None
            return_type = type_nodes[0].text

            # Extract parameters
            parameters = []
            param_nodes = method_node.children_by_type_name("formal_parameters")
            if param_nodes:
                param_list_nodes = param_nodes[0].children_by_type_name("formal_parameter_list")
                if param_list_nodes:
                    param_nodes = param_list_nodes[0].children_by_type_name("formal_parameter")
                    for param_node in param_nodes:
                        param = self._extract_parameter(param_node)
                        if param:
                            parameters.append(param)

            # Extract modifiers
            modifiers = method_node.children_by_type_name("modifiers")
            visibility = "package-private"
            is_static = False
            is_abstract = False

            if modifiers:
                modifier_nodes = modifiers[0].children_by_type_name("modifier")
                for modifier in modifier_nodes:
                    if modifier.text in ("public", "private", "protected"):
                        visibility = modifier.text
                    elif modifier.text == "static":
                        is_static = True
                    elif modifier.text == "abstract":
                        is_abstract = True

            # Calculate effective LOC
            effective_loc = self._calculate_effective_loc(method_node)

            return MethodSignature(
                name=method_name,
                visibility=visibility,
                return_type=return_type,
                parameters=parameters,
                is_static=is_static,
                is_abstract=is_abstract,
                is_private=(visibility == "private"),
                effective_loc=effective_loc,
            )
        except Exception:
            return None

    def _extract_parameter(self, param_node: Any) -> tuple[str, str] | None:
        """Extract parameter information from a parameter node.

        Args:
            param_node: Tree-sitter parameter node.

        Returns:
            Tuple of (type, name) or None if extraction fails.
        """
        try:
            # Extract type
            type_nodes = param_node.children_by_type_name("type_identifier")
            if not type_nodes:
                return None
            param_type = type_nodes[0].text

            # Extract name
            name_nodes = param_node.children_by_type_name("identifier")
            if not name_nodes:
                return None
            param_name = name_nodes[0].text

            return (param_type, param_name)
        except Exception:
            return None

    def _calculate_effective_loc(self, method_node: Any) -> int:
        """Calculate effective lines of code for a method.

        Args:
            method_node: Tree-sitter method node.

        Returns:
            Effective LOC count.
        """
        try:
            # Get the method body
            body_nodes = method_node.children_by_type_name("block")
            if not body_nodes:
                return 0

            body_node = body_nodes[0]

            # Get the content lines
            content_lines = body_node.text.split("\n")

            # Count non-empty, non-comment lines
            effective_loc = 0
            for line in content_lines:
                stripped = line.strip()
                if stripped and not stripped.startswith("//") and not stripped.startswith("/*"):
                    effective_loc += 1

            return effective_loc
        except Exception:
            return 0
