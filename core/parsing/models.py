"""
Data models for Java parsing.

This module provides the data structures used throughout the parsing layer.
"""

from __future__ import annotations

from dataclasses import dataclass, field


@dataclass
class JavaDependency:
    """Represents a Java dependency (imported class or package)."""

    name: str
    type: str  # "class", "interface", "import", "field", "method"
    package: str | None = None  # Package name if type is "class"
    file_path: str | None = None  # Path to the dependency file, if known


@dataclass
class MethodSignature:
    """Represents a Java method signature."""

    name: str
    visibility: str  # "public", "private", "protected", "package-private"
    return_type: str
    parameters: list[tuple[str, str]] = field(default_factory=list)
    is_static: bool = False
    is_abstract: bool = False
    is_private: bool = False  # Convenience flag (mirrors visibility == "private")
    is_in_inner_class: bool = False  # True if method belongs to inner/anonymous class
    effective_loc: int = 0  # Effective lines of code


@dataclass
class FieldDeclaration:
    """Represents a Java field declaration."""

    name: str
    type: str
    visibility: str  # "public", "private", "protected", "package-private"
    is_static: bool = False
    is_final: bool = False


def _filter_reftest_methods(methods: list[MethodSignature]) -> list[MethodSignature]:
    """Filter methods per RefTest eligibility criteria.

    Excludes private methods, trivial methods (<=1 effective LOC),
    and methods inside inner/anonymous classes.
    """
    return [
        m for m in methods if not m.is_private and m.effective_loc > 1 and not m.is_in_inner_class
    ]


@dataclass
class ParsedJavaClass:
    """Represents a parsed Java class."""

    name: str
    package: str | None = None
    imports: list[str] = field(default_factory=list)
    dependencies: list[JavaDependency] = field(default_factory=list)
    fields: list[FieldDeclaration] = field(default_factory=list)
    methods: list[MethodSignature] = field(default_factory=list)
    content: str = ""
    is_interface: bool = False
    is_abstract: bool = False
    file_path: str = ""

    @property
    def reftest_eligible_methods(self) -> list[MethodSignature]:
        """Return methods meeting RefTest dataset eligibility criteria.

        Filters out private methods, trivial methods (1 or fewer effective
        lines of code), and methods inside inner/anonymous classes.
        """
        return _filter_reftest_methods(self.methods)
