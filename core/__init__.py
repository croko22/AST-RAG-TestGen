"""Core module for AST-RAG TestGen."""

# Lazy imports to avoid tree-sitter requirement for metainfo module
_parser = None
_retriever = None
_prompt_builder = None

def get_parser():
    """Lazy import for parser module."""
    global _parser
    if _parser is None:
        from . import parser
        _parser = parser
    return _parser

def get_retriever():
    """Lazy import for retriever module."""
    global _retriever
    if _retriever is None:
        from . import retriever
        _retriever = retriever
    return _retriever

def get_prompt_builder():
    """Lazy import for prompt builder module."""
    global _prompt_builder
    if _prompt_builder is None:
        from . import prompt_builder
        _prompt_builder = prompt_builder
    return _prompt_builder


def __getattr__(name):
    """Lazy attribute access for backwards compatibility."""
    if name == "JavaParser":
        return get_parser().JavaParser
    elif name == "ParsedJavaClass":
        return get_parser().ParsedJavaClass
    elif name == "JavaDependency":
        return get_parser().JavaDependency
    elif name == "MethodSignature":
        return get_parser().MethodSignature
    elif name == "extract_dependencies_from_file":
        return get_parser().extract_dependencies_from_file
    elif name == "JavaFileRetriever":
        return get_retriever().JavaFileRetriever
    elif name == "DependencyResolver":
        return get_retriever().DependencyResolver
    elif name == "PromptBuilder":
        return get_prompt_builder().PromptBuilder
    elif name == "build_test_prompt":
        return get_prompt_builder().build_test_prompt

    raise AttributeError(f"module {__name__} has no attribute {name}")


# Metainfo module (no tree-sitter dependency)
from .metainfo import (
    ClassInfo,
    MethodInfo,
    FieldInfo,
    PackageInfo,
    TestInfo,
    TestBundle,
    ReferenceRelationship,
    ReferencePhase,
    ReferenceMethodSet,
    ScopeGraph,
    MetainfoDatabase,
)

__all__ = [
    # Parser functions (lazy)
    "JavaParser",
    "ParsedJavaClass",
    "JavaDependency",
    "MethodSignature",
    "extract_dependencies_from_file",
    # Retriever functions (lazy)
    "JavaFileRetriever",
    "DependencyResolver",
    # Prompt builder functions (lazy)
    "PromptBuilder",
    "build_test_prompt",
    # Metainfo (direct import)
    "ClassInfo",
    "MethodInfo",
    "FieldInfo",
    "PackageInfo",
    "TestInfo",
    "TestBundle",
    "ReferenceRelationship",
    "ReferencePhase",
    "ReferenceMethodSet",
    "ScopeGraph",
    "MetainfoDatabase",
]
