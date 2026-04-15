"""
Dependency extraction utilities for AST-RAG TestGen.

This module provides extraction logic for extracting dependencies from Java files.
"""

from __future__ import annotations

from core.parsing import JavaParser, ParsedJavaClass


def extract_dependencies_from_file(java_file_path: str) -> ParsedJavaClass:
    """
    Extract dependencies from a Java file.

    This function parses a Java file and returns a ParsedJavaClass
    with all extracted information including imports, fields, and methods.

    Args:
        java_file_path: Path to the Java file.

    Returns:
        ParsedJavaClass with extracted information.
    """
    parser = JavaParser()
    return parser.parse_file(java_file_path)


def extract_dependencies_from_content(
    content: str,
    file_path: str = "",
) -> ParsedJavaClass:
    """
    Extract dependencies from Java content.

    This function parses Java content and returns a ParsedJavaClass
    with all extracted information including imports, fields, and methods.

    Args:
        content: Java source code content.
        file_path: Optional file path for reference.

    Returns:
        ParsedJavaClass with extracted information.
    """
    parser = JavaParser()
    return parser.parse_content(content, file_path)


def extract_test_methods(parsed_class: ParsedJavaClass) -> list:
    """
    Extract test methods from a parsed class.

    This function filters methods to only include test methods
    (methods that start with "test" and are public).

    Args:
        parsed_class: Parsed Java class.

    Returns:
        List of test method signatures.
    """

    test_methods = []
    for method in parsed_class.methods:
        if method.name.startswith("test") and method.visibility == "public":
            test_methods.append(method)
    return test_methods


def map_test_to_focal_method(test_method_name: str) -> str:
    """
    Map a test method name to its corresponding focal method name.

    This function converts test method names (e.g., "testCalculateTotal")
    to focal method names (e.g., "calculateTotal").

    Args:
        test_method_name: Test method name.

    Returns:
        Focal method name.
    """
    # Remove "test" prefix and convert to camelCase
    if test_method_name.startswith("test"):
        focal_name = test_method_name[4:]  # Remove "test"
        # Convert first character to lowercase
        if focal_name:
            focal_name = focal_name[0].lower() + focal_name[1:]
        return focal_name
    return test_method_name
