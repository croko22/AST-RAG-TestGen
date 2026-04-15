"""
Method filtering utilities for AST-RAG TestGen.

This module provides filtering logic for selecting which methods to generate tests for.
"""

from __future__ import annotations

from core.parsing.models import MethodSignature


def is_private(method: MethodSignature) -> bool:
    """Check if a method is private.

    Args:
        method: Method signature to check.

    Returns:
        True if method is private, False otherwise.
    """
    return method.visibility == "private"


def is_single_line(method: MethodSignature) -> bool:
    """Check if a method is a single line (likely a getter/setter).

    Args:
        method: Method signature to check.

    Returns:
        True if method is single line, False otherwise.
    """
    return method.effective_loc <= 1


def is_in_inner_class(method: MethodSignature) -> bool:
    """Check if a method belongs to an inner class.

    Args:
        method: Method signature to check.

    Returns:
        True if method is in an inner class, False otherwise.
    """
    # This is a placeholder - actual implementation would need context
    # about whether the method is in an inner class
    return False


def filter_reftest_methods(methods: list[MethodSignature]) -> list[MethodSignature]:
    """
    Filter methods according to RefTest eligibility criteria.

    RefTest excludes:
    - Private methods
    - Single-line methods (likely getters/setters)
    - Methods in inner classes

    Args:
        methods: List of method signatures to filter.

    Returns:
        Filtered list of eligible methods.
    """
    eligible = []
    for method in methods:
        if is_private(method):
            continue
        if is_single_line(method):
            continue
        if is_in_inner_class(method):
            continue
        eligible.append(method)

    return eligible


def get_reftest_eligible_property(methods: list[MethodSignature]) -> list[MethodSignature]:
    """
    Get the reftest_eligible property for a list of methods.

    This is a convenience function that returns the same result as
    filter_reftest_methods but can be used as a property.

    Args:
        methods: List of method signatures.

    Returns:
        List of reftest-eligible methods.
    """
    return filter_reftest_methods(methods)
