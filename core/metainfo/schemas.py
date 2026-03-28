"""Pydantic schemas for Metainfo Database.

Based on RefTest paper's structured metadata representation.
"""

from enum import StrEnum
from typing import Any, ClassVar

from pydantic import BaseModel, ConfigDict, Field, field_validator, model_validator


class ReferencePhase(StrEnum):
    """Reference relationship phases: Given, When, Then."""

    GIVEN = "given"
    WHEN = "when"
    THEN = "then"


class ClassInfo(BaseModel):
    """Metadata for a Java class."""

    uri: str = Field(..., description="Unique identifier (e.g., com.example.TestService)")
    name: str = Field(..., description="Class name without package")
    file_path: str = Field(..., description="Absolute path to source file")
    package: str | None = Field(None, description="Package name (e.g., com.example)")
    superclasses: list[str] = Field(default_factory=list, description="List of parent class URIs")
    super_interfaces: list[str] = Field(
        default_factory=list, description="List of implemented interface URIs"
    )
    class_docstring: str | None = Field(None, description="Class-level Javadoc")
    original_string: str | None = Field(None, description="Original source code snippet")
    is_abstract: bool = Field(default=False, description="Whether the class is abstract")
    is_interface: bool = Field(default=False, description="Whether this is an interface")
    is_record: bool = Field(default=False, description="Whether this is a Java record")

    @field_validator("uri")
    @classmethod
    def validate_uri(cls, v):
        """Ensure URI is not empty."""
        if not v or not v.strip():
            raise ValueError("URI cannot be empty")
        return v.strip()


class MethodInfo(BaseModel):
    """Metadata for a Java method."""

    uri: str = Field(..., description="Unique identifier (e.g., com.example.TestService.getData)")
    name: str = Field(..., description="Method name")
    class_uri: str = Field(..., description="URI of the declaring class")
    visibility: str = Field(
        ..., description="Access modifier (public, private, protected, package-private)"
    )
    return_type: str | None = Field(None, description="Return type")
    parameters: list[dict[str, str]] = Field(
        default_factory=list, description="List of {name, type} parameter definitions"
    )
    modifiers: list[str] = Field(
        default_factory=list, description="List of modifiers (static, final, synchronized, etc.)"
    )
    is_static: bool = Field(default=False, description="Whether the method is static")
    docstring: str | None = Field(None, description="Method-level Javadoc")
    original_string: str | None = Field(None, description="Original source code snippet")
    throws: list[str] = Field(default_factory=list, description="List of thrown exceptions")
    is_constructor: bool = Field(default=False, description="Whether this is a constructor")
    signature: str | None = Field(
        None,
        description="Normalized method signature (e.g., (), (String,int))",
    )
    legacy_uri: str | None = Field(
        None,
        description="Legacy URI without signature (e.g., com.example.Service.doWork)",
    )

    @field_validator("visibility")
    @classmethod
    def validate_visibility(cls, v):
        """Ensure visibility is valid."""
        v = v.strip().lower()
        valid_visibilities = {"public", "private", "protected", "package-private"}
        if v not in valid_visibilities:
            raise ValueError(f"Invalid visibility: {v}. Must be one of {valid_visibilities}")
        return v


class FieldInfo(BaseModel):
    """Metadata for a Java field."""

    uri: str = Field(..., description="Unique identifier (e.g., com.example.TestService.repo)")
    name: str = Field(..., description="Field name")
    class_uri: str = Field(..., description="URI of the declaring class")
    type: str = Field(..., description="Field type (e.g., String, int, custom types)")
    modifiers: list[str] = Field(
        default_factory=list,
        description="List of modifiers (private, protected, static, final, etc.)",
    )
    docstring: str | None = Field(None, description="Field-level Javadoc")
    is_static: bool = Field(default=False, description="Whether the field is static")
    is_final: bool = Field(default=False, description="Whether the field is final")


class PackageInfo(BaseModel):
    """Metadata for a Java package."""

    uri: str = Field(..., description="Package identifier (e.g., com.example)")
    name: str = Field(..., description="Package name")
    file_path: str | None = Field(None, description="Path to package-info.java if exists")
    classes: list[str] = Field(
        default_factory=list, description="List of class URIs in this package"
    )


class TestInfo(BaseModel):
    """Metadata for a test class."""

    uri: str = Field(..., description="Unique identifier (e.g., com.example.TestServiceTest)")
    name: str = Field(..., description="Test class name")
    file_path: str = Field(..., description="Absolute path to test file")
    target_class: str = Field(..., description="URI of the class being tested")
    test_cases: list[str] = Field(..., description="List of test method names")
    fixtures: list[str] = Field(
        default_factory=list,
        description="List of fixture/setup method names (beforeEach, setUp, etc.)",
    )
    imports: list[str] = Field(default_factory=list, description="List of imported class names")
    class_members: dict[str, Any] = Field(
        default_factory=dict, description="Class-level variables, nested classes used by tests"
    )


class TestBundle(BaseModel):
    """Complete context for a single test case."""

    test_uri: str = Field(
        ..., description="Unique identifier (e.g., com.example.TestServiceTest.testGetData)"
    )
    test_name: str = Field(..., description="Test method name")
    test_class_uri: str = Field(..., description="URI of the test class")
    target_method: str = Field(..., description="URI of the method under test")
    fixtures_used: list[str] = Field(
        default_factory=list, description="List of fixtures this test uses"
    )
    external_dependencies: dict[str, Any] = Field(
        default_factory=dict, description="External dependencies: {modules: [], class_members: {}}"
    )
    project_specific_resources: list[str] = Field(
        default_factory=list, description="Project-specific resources/utilities used"
    )
    assertions: list[str] = Field(
        default_factory=list, description="Key assertions made in this test"
    )
    given_phase: dict[str, Any] | None = Field(
        None, description="Test context for Given phase (setup, preconditions)"
    )
    when_phase: dict[str, Any] | None = Field(
        None, description="Test context for When phase (invocation, parameters)"
    )
    then_phase: dict[str, Any] | None = Field(
        None, description="Test context for Then phase (assertions, expected results)"
    )


class ReferenceRelationship(BaseModel):
    """Represents a reference relationship between two methods.

    Based on RefTest's Given-When-Then paradigm.
    """

    source_method: str = Field(..., description="URI of the method using the reference")
    target_method: str = Field(..., description="URI of the method providing the reference")
    phase: ReferencePhase = Field(..., description="Primary phase of this relationship")
    phases: set[ReferencePhase] | None = Field(
        None, description="All phases where this reference applies (for complete relationships)"
    )
    description: str = Field(..., description="Description of why this reference is relevant")
    confidence: float = Field(..., ge=0.0, le=1.0, description="Confidence score (0-1)")
    is_external: bool = Field(
        default=False, description="Whether the reference is from another class"
    )

    @model_validator(mode="after")
    def normalize_phases(self):
        """Keep `phase` and `phases` consistent."""
        if self.phases is None:
            self.phases = {self.phase}
        elif self.phase not in self.phases:
            self.phases.add(self.phase)
        return self

    @property
    def is_complete(self) -> bool:
        """Check if this is a complete relationship (all three phases)."""
        return bool(self.phases) and self.phases == {
            ReferencePhase.GIVEN,
            ReferencePhase.WHEN,
            ReferencePhase.THEN,
        }


class ReferenceMethodSet(BaseModel):
    """Set of reference methods for a focal method.

    From RefTest: R(m) = {given, when, then, complete} sets.
    """

    focal_method_uri: str = Field(..., description="URI of the method being tested")
    complete: list[str] = Field(
        default_factory=list, description="Methods with complete relationships (all GWT phases)"
    )
    given: list[tuple[str, str]] = Field(
        default_factory=list, description="Methods with Given phase: [(method_uri, description)]"
    )
    when: list[tuple[str, str]] = Field(
        default_factory=list, description="Methods with When phase: [(method_uri, description)]"
    )
    then: list[tuple[str, str]] = Field(
        default_factory=list, description="Methods with Then phase: [(method_uri, description)]"
    )

    def all_methods(self) -> list[str]:
        """Get all referenced method URIs."""
        all_set: set[str] = set(self.complete)
        for uri, _ in self.given:
            all_set.add(uri)
        for uri, _ in self.when:
            all_set.add(uri)
        for uri, _ in self.then:
            all_set.add(uri)
        return list(all_set)

    def rank(self, max_count: int = 3) -> list[tuple[str, str, float]]:
        """Rank methods by priority: complete > given > when > then.

        Returns: [(method_uri, description, priority_score)]
        """
        scored: list[tuple[str, str, float]] = []

        # Complete methods have highest priority
        for uri in self.complete:
            scored.append((uri, "Complete relationship", 1.0))

        # Given phase (preconditions, setup)
        for uri, desc in self.given:
            if uri not in self.complete:
                scored.append((uri, desc, 0.8))

        # When phase (invocation patterns)
        for uri, desc in self.when:
            if uri not in self.complete and not any(uri == g[0] for g in self.given):
                scored.append((uri, desc, 0.6))

        # Then phase (assertions)
        for uri, desc in self.then:
            if uri not in self.complete and not any(uri == g[0] for g in self.given + self.when):
                scored.append((uri, desc, 0.4))

        # Sort by score (descending), then return top N
        scored.sort(key=lambda x: x[2], reverse=True)
        return scored[:max_count]


class ScopeGraph(BaseModel):
    """Lightweight scope graph for reference resolution.

    From RefTest: Extends Scope Graphs with Metainfo DB integration.
    """

    nodes: dict[str, dict] = Field(
        default_factory=dict, description="All nodes in the graph: {node_id: node_data}"
    )
    edges: list[tuple[str, str, str]] = Field(
        default_factory=list, description="Edges as (from_node, to_node, edge_type)"
    )

    # Node types
    NODE_SCOPE: ClassVar[str] = "LocalScope"
    NODE_DEF: ClassVar[str] = "LocalDef"
    NODE_IMPORT: ClassVar[str] = "LocalImport"
    NODE_REF: ClassVar[str] = "Reference"

    # Edge types
    EDGE_SCOPE_TO_SCOPE: ClassVar[str] = "ScopeToScope"
    EDGE_DEF_TO_SCOPE: ClassVar[str] = "DefToScope"
    EDGE_IMPORT_TO_SCOPE: ClassVar[str] = "ImportToScope"
    EDGE_REF_TO_SCOPE: ClassVar[str] = "RefToScope"
    EDGE_REF_TO_DEF: ClassVar[str] = "RefToDef"
    EDGE_REF_TO_IMPORT: ClassVar[str] = "RefToImport"

    def add_scope(self, scope_id: str, parent_id: str | None = None) -> None:
        """Add a lexical scope to the graph."""
        self.nodes[scope_id] = {"type": self.NODE_SCOPE}
        if parent_id and parent_id in self.nodes:
            self.edges.append((scope_id, parent_id, self.EDGE_SCOPE_TO_SCOPE))

    def add_definition(self, def_id: str, scope_id: str, symbol: str | None = None) -> None:
        """Add a definition (variable, method, class) to the graph."""
        self.nodes[def_id] = {"type": self.NODE_DEF, "symbol": symbol or def_id}
        if scope_id in self.nodes:
            self.edges.append((def_id, scope_id, self.EDGE_DEF_TO_SCOPE))

    def add_import(self, import_id: str, scope_id: str) -> None:
        """Add an import statement to the graph."""
        self.nodes[import_id] = {"type": self.NODE_IMPORT}
        if scope_id in self.nodes:
            self.edges.append((import_id, scope_id, self.EDGE_IMPORT_TO_SCOPE))

    def add_reference(self, ref_id: str, scope_id: str, symbol: str | None = None) -> None:
        """Add a reference usage to the graph."""
        self.nodes[ref_id] = {"type": self.NODE_REF, "symbol": symbol or ref_id}
        if scope_id in self.nodes:
            self.edges.append((ref_id, scope_id, self.EDGE_REF_TO_SCOPE))

    def resolve_local(self, ref_id: str) -> str | None:
        """Resolve a reference to a definition using local scope traversal."""
        if ref_id not in self.nodes:
            return None

        node = self.nodes[ref_id]
        if node["type"] != self.NODE_REF:
            return None

        # Get the scope this reference is in
        ref_scope = self._find_containing_scope(ref_id)
        if not ref_scope:
            return None

        ref_symbol = self.nodes[ref_id].get("symbol", ref_id)

        # Search for definition in current scope and parent scopes
        current_scope: str | None = ref_scope
        while current_scope:
            def_candidates = [
                edge[0]
                for edge in self.edges
                if edge[2] == self.EDGE_DEF_TO_SCOPE and edge[1] == current_scope
            ]

            for def_id in def_candidates:
                def_symbol = self.nodes.get(def_id, {}).get("symbol", def_id)
                if def_symbol == ref_symbol:
                    return def_id

            # Move to parent scope
            current_scope = self._find_parent_scope(current_scope)

        return None  # Not found locally

    def _find_containing_scope(self, node_id: str) -> str | None:
        """Find the scope containing a given node."""
        for edge in self.edges:
            if edge[2] == self.EDGE_REF_TO_SCOPE and edge[0] == node_id:
                return edge[1]
        return None

    def _find_parent_scope(self, scope_id: str) -> str | None:
        """Find parent scope using ScopeToScope edges."""
        for edge in self.edges:
            if edge[2] == self.EDGE_SCOPE_TO_SCOPE and edge[0] == scope_id:
                return edge[1]
        return None

    model_config = ConfigDict(extra="allow")
