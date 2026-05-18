"""
Java parser using Tree-sitter.

This module provides the Tree-sitter based Java parser.
"""

from __future__ import annotations

from typing import Any

try:
    from tree_sitter import Language, Parser

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

        self.parser = Parser()
        self.parser.language = Language(tree_sitter_java.language())

    @staticmethod
    def _children_by_type(node: Any, child_type: str) -> list[Any]:
        """Find child nodes by type using compatible tree-sitter API."""
        return [c for c in node.children if c.type == child_type]

    @staticmethod
    def _child_text(node: Any, child_type: str) -> str | None:
        """Get text of first child matching a type."""
        for c in node.children:
            if c.type == child_type:
                return JavaParser._node_text(c)
        return None

    @staticmethod
    def _node_text(node: Any) -> str:
        """Get text from a tree-sitter node."""
        raw = node.text if hasattr(node, "text") else b""
        if isinstance(raw, bytes):
            return raw.decode("utf-8")
        return str(raw) if raw else ""

    @staticmethod
    def _find_type_name(node: Any) -> str | None:
        """Find a type name from a node, handling all Java type forms."""
        _TYPE_KINDS = ("type_identifier", "void_type", "primitive_type",
                       "integral_type", "floating_point_type", "boolean_type")
        for kind in _TYPE_KINDS:
            name = JavaParser._child_text(node, kind)
            if name:
                return name
        # Check for generic_type (e.g., Optional<Usuario>)
        for c in node.children:
            if c.type == "generic_type":
                return JavaParser._node_text(c)
        return None

    def parse_file(self, file_path: str) -> ParsedJavaClass:
        """Parse a Java file and extract its structure."""
        with open(file_path, encoding="utf-8") as f:
            content = f.read()
        return self.parse_content(content, file_path)

    def parse_content(self, content: str, file_path: str = "") -> ParsedJavaClass:
        """Parse Java content and extract its structure."""
        tree = self.parser.parse(bytes(content, "utf8"))

        package_name = None
        imports = []
        fields = []
        methods = []
        class_name = None
        is_interface = False
        is_abstract = False

        # Extract package name
        for child in tree.root_node.children:
            if child.type == "package_declaration":
                pkg_text = self._child_text(child, "scoped_identifier")
                if pkg_text:
                    package_name = pkg_text
                break

        # Extract imports
        for child in tree.root_node.children:
            if child.type == "import_declaration":
                imp_text = self._child_text(child, "scoped_identifier")
                if imp_text:
                    imports.append(imp_text)

        # Build dependencies from imports (filter stdlib)
        dependencies = []
        imported_names = set()
        for imp in imports:
            if not imp.startswith("java.") and not imp.startswith("javax.") and not imp.startswith("org.springframework"):
                parts = imp.split(".")
                if len(parts) > 1 and not imp.endswith("*") and not imp.endswith(".*"):
                    class_name = parts[-1]
                    package = ".".join(parts[:-1])
                    dependencies.append(JavaDependency(name=class_name, type="class", package=package))
                    imported_names.add(class_name)
                elif imp.endswith(".*"):
                    package = imp[:-2]
                    dependencies.append(JavaDependency(name=package, type="package"))

        # Extract class/interface info
        for child in tree.root_node.children:
            if child.type in ("class_declaration", "interface_declaration"):
                class_node = child
                is_interface = child.type == "interface_declaration"

                # Get class name
                class_name = self._child_text(class_node, "identifier")

                # Check modifiers for abstract
                for grandchild in class_node.children:
                    if grandchild.type == "modifiers":
                        for mod in grandchild.named_children:
                            if mod.type == "abstract":
                                is_abstract = True
                            # Also check text
                            mod_text = self._node_text(mod)
                            if mod_text == "abstract":
                                is_abstract = True

                # Extract fields and methods from class/interface body
                for grandchild in class_node.children:
                    if grandchild.type in ("class_body", "interface_body"):
                        for item in grandchild.children:
                            if item.type == "field_declaration":
                                field = JavaParser._extract_field(item)
                                if field:
                                    fields.append(field)
                            elif item.type == "method_declaration":
                                method = JavaParser._extract_method(item)
                                if method:
                                    methods.append(method)
                            elif item.type == "class_declaration":
                                # Recursively extract inner class methods
                                inner_methods, inner_fields = JavaParser._extract_class_body(item, is_inner=True)
                                methods.extend(inner_methods)

        # Add dependencies from field types (same-package refs)
        for field in fields:
            if field.type and field.type not in imported_names:
                dependencies.append(JavaDependency(name=field.type, type="class"))

        return ParsedJavaClass(
            name=class_name or "Unknown",
            package=package_name,
            imports=imports,
            dependencies=dependencies,
            fields=fields,
            methods=methods,
            content=content,
            is_interface=is_interface,
            is_abstract=is_abstract,
            file_path=file_path,
        )

    @staticmethod
    def _extract_class_body(class_node: Any, is_inner: bool = False) -> tuple[list[MethodSignature], list[FieldDeclaration]]:
        """Extract methods and fields from a class/interface node."""
        inner_methods: list[MethodSignature] = []
        inner_fields: list[FieldDeclaration] = []
        for cc in class_node.children:
            if cc.type in ("class_body", "interface_body"):
                for item in cc.children:
                    if item.type == "method_declaration":
                        m = JavaParser._extract_method(item)
                        if m:
                            if is_inner:
                                m.is_in_inner_class = True
                            inner_methods.append(m)
                    elif item.type == "class_declaration":
                        sub_methods, _ = JavaParser._extract_class_body(item, is_inner=True)
                        inner_methods.extend(sub_methods)
        return inner_methods, inner_fields

    @staticmethod
    def _extract_field(field_node: Any) -> FieldDeclaration | None:
        """Extract field information from a field node."""
        try:
            field_type = JavaParser._find_type_name(field_node)
            if not field_type:
                return None

            field_name = JavaParser._child_text(field_node, "variable_declarator")
            if not field_name:
                return None

            visibility = "package-private"
            is_static = False
            is_final = False

            for child in field_node.children:
                if child.type == "modifiers":
                    for mod in child.children:
                        mod_text = JavaParser._node_text(mod)
                        if mod_text in ("public", "private", "protected"):
                            visibility = mod_text
                        elif mod_text == "static":
                            is_static = True
                        elif mod_text == "final":
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

    @staticmethod
    def _extract_method(method_node: Any) -> MethodSignature | None:
        """Extract method information from a method node."""
        try:
            method_name = JavaParser._child_text(method_node, "identifier")
            if not method_name:
                return None

            # Extract return type
            return_type = JavaParser._find_type_name(method_node)
            if not return_type:
                return None

            # Extract parameters
            parameters = []
            for child in method_node.children:
                if child.type == "formal_parameters":
                    for param_item in child.children:
                        if param_item.type == "formal_parameter":
                            param = JavaParser._extract_parameter(param_item)
                            if param:
                                parameters.append(param)

            # Extract modifiers
            visibility = "package-private"
            is_static = False
            is_abstract = False

            for child in method_node.children:
                if child.type == "modifiers":
                    for mod in child.children:
                        mod_text = JavaParser._node_text(mod)
                        if mod_text in ("public", "private", "protected"):
                            visibility = mod_text
                        elif mod_text == "static":
                            is_static = True
                        elif mod_text == "abstract":
                            is_abstract = True

            # Calculate effective LOC
            effective_loc = JavaParser._calculate_effective_loc(method_node)

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

            # Extract return type (handle simple type, void, primitives, generics)
            return_type = self._child_text(method_node, "type_identifier")
            if not return_type:
                return_type = self._child_text(method_node, "void_type")
            if not return_type:
                return_type = self._child_text(method_node, "primitive_type")
            if not return_type:
                # Check for generic_type (e.g., Optional<Usuario>)
                for c in method_node.children:
                    if c.type == "generic_type":
                        return_type = self._node_text(c)
                        break
            if not return_type:
                return None

            # Extract parameters
            parameters = []
            for child in method_node.children:
                if child.type == "formal_parameters":
                    for param_item in child.children:
                        if param_item.type == "formal_parameter":
                            param = self._extract_parameter(param_item)
                            if param:
                                parameters.append(param)

            # Extract modifiers
            visibility = "package-private"
            is_static = False
            is_abstract = False

            for child in method_node.children:
                if child.type == "modifiers":
                    for mod in child.children:
                        mod_text = self._node_text(mod)
                        if mod_text in ("public", "private", "protected"):
                            visibility = mod_text
                        elif mod_text == "static":
                            is_static = True
                        elif mod_text == "abstract":
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

    @staticmethod
    def _extract_parameter(param_node: Any) -> tuple[str, str] | None:
        """Extract parameter (type, name) from a parameter node."""
        try:
            param_type = JavaParser._find_type_name(param_node)
            if not param_type:
                return None
            param_name = JavaParser._child_text(param_node, "identifier")
            if not param_name:
                return None
            return (param_type, param_name)
        except Exception:
            return None

    @staticmethod
    def _calculate_effective_loc(method_node: Any) -> int:
        """Calculate effective lines of code for a method.

        Counts non-empty, non-comment, non-brace lines in the method body.
        """
        try:
            for child in method_node.children:
                if child.type == "block":
                    content_lines = JavaParser._node_text(child).split("\n")
                    effective_loc = 0
                    for line in content_lines:
                        stripped = line.strip()
                        if stripped and not stripped.startswith("//") and not stripped.startswith("/*") and stripped not in ("{", "}", "{}"):
                            effective_loc += 1
                    return effective_loc
            return 0
        except Exception:
            return 0
