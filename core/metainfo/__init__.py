"""Metainfo module for persistent code metadata storage.

Based on RefTest's Metainfo Database approach for structured
code entity storage and efficient retrieval.
"""

from .builder import BuildResult, MetainfoBuilder
from .database import MetainfoDatabase
from .schemas import (
    ClassInfo,
    FieldInfo,
    MethodInfo,
    PackageInfo,
    ReferenceMethodSet,
    ReferencePhase,
    ReferenceRelationship,
    ScopeGraph,
    TestBundle,
    TestInfo,
)

__all__ = [
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
    "MetainfoBuilder",
    "BuildResult",
]
