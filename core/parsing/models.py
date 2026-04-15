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
    type: str  # "class" or "package"
    package: str | None = None  # Package name if type is "class"


@dataclass
class MethodSignature:
    """Represents a Java method signature."""

    name: str
    visibility: str  # "public", "private", "protected", "package-private"
    return_type: str
    parameters: list[tuple[str, str]] = field(default_factory=list)
    is_static: bool = False
    is_abstract: bool = False
    effective_loc: int = 0  # Effective lines of code


@dataclass
class FieldDeclaration:
    """Represents a Java field declaration."""

    name: str
    type: str
    visibility: str  # "public", "private", "protected", "package-private"
    is_static: bool = False
    is_final: bool = False


@dataclass
class ParsedJavaClass:
    """Represents a parsed Java class."""

    name: str
    package: str | None = None
    imports: list[str] = field(default_factory=list)
    fields: list[FieldDeclaration] = field(default_factory=list)
    methods: list[MethodSignature] = field(default_factory=list)
    content: str = ""
    is_interface: bool = False
    is_abstract: bool = False
    file_path: str = ""
