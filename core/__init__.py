"""Core module for AST-RAG TestGen."""

# Lazy imports to avoid tree-sitter requirement for metainfo module
_parser = None
_retriever = None
_prompt_builder = None
_metainfo = None
_extraction = None

_METAINFO_EXPORTS = {
    "BuildResult",
    "ClassInfo",
    "FieldInfo",
    "MetainfoBuilder",
    "MetainfoDatabase",
    "MethodInfo",
    "PackageInfo",
    "ReferenceMethodSet",
    "ReferencePhase",
    "ReferenceRelationship",
    "ScopeGraph",
    "TestBundle",
    "TestInfo",
}


def get_parser():
    """Lazy import for parser module."""
    global _parser
    if _parser is None:
        from core.parsing import parser as _parsing_mod

        _parser = _parsing_mod
    return _parser


def get_retriever():
    """Lazy import for retriever module."""
    global _retriever
    if _retriever is None:
        from rag import retriever

        _retriever = retriever
    return _retriever


def get_prompt_builder():
    """Lazy import for prompt builder module."""
    global _prompt_builder
    if _prompt_builder is None:
        from . import prompt_builder

        _prompt_builder = prompt_builder
    return _prompt_builder


def get_extraction():
    """Lazy import for extraction module."""
    global _extraction
    if _extraction is None:
        from core.extraction import extractor

        _extraction = extractor
    return _extraction


def get_metainfo():
    """Lazy import for metainfo module."""
    global _metainfo
    if _metainfo is None:
        from . import metainfo

        _metainfo = metainfo
    return _metainfo


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
        return get_extraction().extract_dependencies_from_file
    elif name == "JavaFileRetriever":
        return get_retriever().JavaFileRetriever
    elif name == "DependencyResolver":
        return get_retriever().DependencyResolver
    elif name == "PromptBuilder":
        return get_prompt_builder().PromptBuilder
    elif name == "build_test_prompt":
        return get_prompt_builder().build_test_prompt
    elif name in _METAINFO_EXPORTS:
        return getattr(get_metainfo(), name)

    raise AttributeError(f"module {__name__} has no attribute {name}")


__all__ = [
    "BuildResult",
    "ClassInfo",
    "DependencyResolver",
    "FieldInfo",
    "JavaDependency",
    "JavaFileRetriever",
    "JavaParser",
    "MetainfoBuilder",
    "MetainfoDatabase",
    "MethodInfo",
    "MethodSignature",
    "PackageInfo",
    "ParsedJavaClass",
    "PromptBuilder",
    "ReferenceMethodSet",
    "ReferencePhase",
    "ReferenceRelationship",
    "ScopeGraph",
    "TestBundle",
    "TestInfo",
    "build_test_prompt",
    "extract_dependencies_from_file",
]
