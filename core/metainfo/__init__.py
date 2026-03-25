"""Metainfo module for persistent code metadata storage.

Based on RefTest's Metainfo Database approach for structured
code entity storage and efficient retrieval.
"""

from .schemas import (
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
)
from .database import MetainfoDatabase

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
]
