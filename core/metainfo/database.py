"""Persistent Metainfo Database using SQLite.

Provides structured storage for code entities (Class, Method, Field, etc.)
with fast lookups via indexing.
"""

import json
import sqlite3
from contextlib import contextmanager
from pathlib import Path
from typing import Any

from .schemas import (
    ClassInfo,
    FieldInfo,
    MethodInfo,
    PackageInfo,
    ReferencePhase,
    ReferenceRelationship,
    TestBundle,
    TestInfo,
)


class MetainfoDatabase:
    """Persistent SQLite database for Java code metainfo.

    Stores structured metadata for classes, methods, fields, packages, tests,
    and reference relationships for efficient retrieval.
    """

    def __init__(self, db_path: str):
        """Initialize database and create schema if needed."""
        self.db_path = str(Path(db_path))
        self._conn = None
        Path(self.db_path).parent.mkdir(parents=True, exist_ok=True)
        self._create_schema()

    @contextmanager
    def _get_connection(self):
        """Context manager for database connections."""
        conn = sqlite3.connect(self.db_path)
        conn.row_factory = sqlite3.Row
        try:
            yield conn
        finally:
            conn.close()

    def _create_schema(self):
        """Create database tables if they don't exist."""
        with self._get_connection() as conn:
            cursor = conn.cursor()

            # Classes table
            cursor.execute("""
                CREATE TABLE IF NOT EXISTS classes (
                    uri TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    file_path TEXT NOT NULL,
                    package TEXT,
                    superclasses TEXT,
                    super_interfaces TEXT,
                    class_docstring TEXT,
                    original_string TEXT,
                    is_abstract INTEGER DEFAULT 0,
                    is_interface INTEGER DEFAULT 0,
                    is_record INTEGER DEFAULT 0
                )
            """)

            # Methods table
            cursor.execute("""
                CREATE TABLE IF NOT EXISTS methods (
                    uri TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    class_uri TEXT NOT NULL,
                    visibility TEXT NOT NULL,
                    return_type TEXT,
                    parameters TEXT,
                    modifiers TEXT,
                    is_static INTEGER DEFAULT 0,
                    docstring TEXT,
                    original_string TEXT,
                    throws TEXT,
                    is_constructor INTEGER DEFAULT 0,
                    FOREIGN KEY (class_uri) REFERENCES classes(uri) ON DELETE CASCADE
                )
            """)

            # Fields table
            cursor.execute("""
                CREATE TABLE IF NOT EXISTS fields (
                    uri TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    class_uri TEXT NOT NULL,
                    type TEXT NOT NULL,
                    modifiers TEXT,
                    docstring TEXT,
                    is_static INTEGER DEFAULT 0,
                    is_final INTEGER DEFAULT 0,
                    FOREIGN KEY (class_uri) REFERENCES classes(uri) ON DELETE CASCADE
                )
            """)

            # Packages table
            cursor.execute("""
                CREATE TABLE IF NOT EXISTS packages (
                    uri TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    file_path TEXT,
                    classes TEXT
                )
            """)

            # Tests table
            cursor.execute("""
                CREATE TABLE IF NOT EXISTS tests (
                    uri TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    file_path TEXT NOT NULL,
                    target_class TEXT NOT NULL,
                    test_cases TEXT,
                    fixtures TEXT,
                    imports TEXT,
                    class_members TEXT,
                    FOREIGN KEY (target_class) REFERENCES classes(uri) ON DELETE CASCADE
                )
            """)

            # Test bundles table
            cursor.execute("""
                CREATE TABLE IF NOT EXISTS test_bundles (
                    test_uri TEXT PRIMARY KEY,
                    test_name TEXT NOT NULL,
                    test_class_uri TEXT NOT NULL,
                    target_method TEXT NOT NULL,
                    fixtures_used TEXT,
                    external_dependencies TEXT,
                    project_specific_resources TEXT,
                    assertions TEXT,
                    given_phase TEXT,
                    when_phase TEXT,
                    then_phase TEXT,
                    FOREIGN KEY (test_class_uri) REFERENCES tests(uri) ON DELETE CASCADE,
                    FOREIGN KEY (target_method) REFERENCES methods(uri) ON DELETE CASCADE
                )
            """)

            # Reference relationships table
            cursor.execute("""
                CREATE TABLE IF NOT EXISTS reference_relationships (
                    source_method TEXT NOT NULL,
                    target_method TEXT NOT NULL,
                    phase TEXT NOT NULL,
                    phases TEXT,
                    description TEXT NOT NULL,
                    confidence REAL NOT NULL,
                    is_external INTEGER DEFAULT 0,
                    PRIMARY KEY (source_method, target_method),
                    FOREIGN KEY (source_method) REFERENCES methods(uri) ON DELETE CASCADE,
                    FOREIGN KEY (target_method) REFERENCES methods(uri) ON DELETE CASCADE
                )
            """)

            # Indexes for fast lookups
            cursor.execute("CREATE INDEX IF NOT EXISTS idx_methods_class ON methods(class_uri)")
            cursor.execute("CREATE INDEX IF NOT EXISTS idx_fields_class ON fields(class_uri)")
            cursor.execute("CREATE INDEX IF NOT EXISTS idx_tests_target ON tests(target_class)")
            cursor.execute(
                "CREATE INDEX IF NOT EXISTS idx_ref_source ON reference_relationships(source_method)"
            )
            cursor.execute(
                "CREATE INDEX IF NOT EXISTS idx_ref_target ON reference_relationships(target_method)"
            )
            cursor.execute(
                "CREATE INDEX IF NOT EXISTS idx_ref_phase ON reference_relationships(phase)"
            )

            conn.commit()

    # CRUD operations for Classes
    def save_class(self, class_info: ClassInfo) -> None:
        """Save or update a class."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute(
                """
                INSERT OR REPLACE INTO classes
                (uri, name, file_path, package, superclasses, super_interfaces,
                 class_docstring, original_string, is_abstract, is_interface, is_record)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """,
                (
                    class_info.uri,
                    class_info.name,
                    class_info.file_path,
                    class_info.package,
                    json.dumps(class_info.superclasses),
                    json.dumps(class_info.super_interfaces),
                    class_info.class_docstring,
                    class_info.original_string,
                    1 if class_info.is_abstract else 0,
                    1 if class_info.is_interface else 0,
                    1 if class_info.is_record else 0,
                ),
            )
            conn.commit()

    def get_class(self, uri: str) -> ClassInfo | None:
        """Retrieve a class by URI."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute("SELECT * FROM classes WHERE uri = ?", (uri,))
            row = cursor.fetchone()
            if row:
                return self._row_to_class(row)
            return None

    def get_classes_by_package(self, package_name: str) -> list[ClassInfo]:
        """Get all classes in a package."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute("SELECT * FROM classes WHERE package = ? ORDER BY name", (package_name,))
            return [self._row_to_class(row) for row in cursor.fetchall()]

    def query_classes_by_name(self, name_pattern: str) -> list[ClassInfo]:
        """Search classes by name (partial match)."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute(
                "SELECT * FROM classes WHERE name LIKE ? ORDER BY name", (f"%{name_pattern}%",)
            )
            return [self._row_to_class(row) for row in cursor.fetchall()]

    # CRUD operations for Methods
    def save_method(self, method_info: MethodInfo) -> None:
        """Save or update a method."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute(
                """
                INSERT OR REPLACE INTO methods
                (uri, name, class_uri, visibility, return_type, parameters,
                 modifiers, is_static, docstring, original_string, throws, is_constructor)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """,
                (
                    method_info.uri,
                    method_info.name,
                    method_info.class_uri,
                    method_info.visibility,
                    method_info.return_type,
                    json.dumps(method_info.parameters),
                    json.dumps(method_info.modifiers),
                    1 if method_info.is_static else 0,
                    method_info.docstring,
                    method_info.original_string,
                    json.dumps(method_info.throws),
                    1 if method_info.is_constructor else 0,
                ),
            )
            conn.commit()

    def get_method(self, uri: str) -> MethodInfo | None:
        """Retrieve a method by URI."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute("SELECT * FROM methods WHERE uri = ?", (uri,))
            row = cursor.fetchone()
            if row:
                return self._row_to_method(row)
            return None

    def get_methods_by_class(self, class_uri: str) -> list[MethodInfo]:
        """Get all methods in a class."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute("SELECT * FROM methods WHERE class_uri = ? ORDER BY name", (class_uri,))
            return [self._row_to_method(row) for row in cursor.fetchall()]

    # CRUD operations for Fields
    def save_field(self, field_info: FieldInfo) -> None:
        """Save or update a field."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute(
                """
                INSERT OR REPLACE INTO fields
                (uri, name, class_uri, type, modifiers, docstring, is_static, is_final)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """,
                (
                    field_info.uri,
                    field_info.name,
                    field_info.class_uri,
                    field_info.type,
                    json.dumps(field_info.modifiers),
                    field_info.docstring,
                    1 if field_info.is_static else 0,
                    1 if field_info.is_final else 0,
                ),
            )
            conn.commit()

    def get_field(self, uri: str) -> FieldInfo | None:
        """Retrieve a field by URI."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute("SELECT * FROM fields WHERE uri = ?", (uri,))
            row = cursor.fetchone()
            if row:
                return self._row_to_field(row)
            return None

    def get_fields_by_class(self, class_uri: str) -> list[FieldInfo]:
        """Get all fields in a class."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute("SELECT * FROM fields WHERE class_uri = ? ORDER BY name", (class_uri,))
            return [self._row_to_field(row) for row in cursor.fetchall()]

    # CRUD operations for Packages
    def save_package(self, package_info: PackageInfo) -> None:
        """Save or update a package."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute(
                """
                INSERT OR REPLACE INTO packages
                (uri, name, file_path, classes)
                VALUES (?, ?, ?, ?)
            """,
                (
                    package_info.uri,
                    package_info.name,
                    package_info.file_path,
                    json.dumps(package_info.classes),
                ),
            )
            conn.commit()

    def get_package(self, uri: str) -> PackageInfo | None:
        """Retrieve a package by URI."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute("SELECT * FROM packages WHERE uri = ?", (uri,))
            row = cursor.fetchone()
            if row:
                return self._row_to_package(row)
            return None

    # CRUD operations for Tests
    def save_test(self, test_info: TestInfo) -> None:
        """Save or update test information."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute(
                """
                INSERT OR REPLACE INTO tests
                (uri, name, file_path, target_class, test_cases, fixtures, imports, class_members)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """,
                (
                    test_info.uri,
                    test_info.name,
                    test_info.file_path,
                    test_info.target_class,
                    json.dumps(test_info.test_cases),
                    json.dumps(test_info.fixtures),
                    json.dumps(test_info.imports),
                    json.dumps(test_info.class_members),
                ),
            )
            conn.commit()

    def get_test(self, uri: str) -> TestInfo | None:
        """Retrieve test information by URI."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute("SELECT * FROM tests WHERE uri = ?", (uri,))
            row = cursor.fetchone()
            if row:
                return self._row_to_test(row)
            return None

    def get_tests_for_class(self, class_uri: str) -> list[TestInfo]:
        """Get all tests for a target class."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute("SELECT * FROM tests WHERE target_class = ? ORDER BY name", (class_uri,))
            return [self._row_to_test(row) for row in cursor.fetchall()]

    # CRUD operations for Test Bundles
    def save_test_bundle(self, bundle: TestBundle) -> None:
        """Save or update a test bundle."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute(
                """
                INSERT OR REPLACE INTO test_bundles
                (test_uri, test_name, test_class_uri, target_method, fixtures_used,
                 external_dependencies, project_specific_resources, assertions, given_phase, when_phase, then_phase)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """,
                (
                    bundle.test_uri,
                    bundle.test_name,
                    bundle.test_class_uri,
                    bundle.target_method,
                    json.dumps(bundle.fixtures_used),
                    json.dumps(bundle.external_dependencies),
                    json.dumps(bundle.project_specific_resources),
                    json.dumps(bundle.assertions),
                    json.dumps(bundle.given_phase) if bundle.given_phase else None,
                    json.dumps(bundle.when_phase) if bundle.when_phase else None,
                    json.dumps(bundle.then_phase) if bundle.then_phase else None,
                ),
            )
            conn.commit()

    def get_test_bundle(self, uri: str) -> TestBundle | None:
        """Retrieve a test bundle by URI."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute("SELECT * FROM test_bundles WHERE test_uri = ?", (uri,))
            row = cursor.fetchone()
            if row:
                return self._row_to_test_bundle(row)
            return None

    def get_test_bundles_for_method(self, method_uri: str) -> list[TestBundle]:
        """Get all test bundles for a target method."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute(
                "SELECT * FROM test_bundles WHERE target_method = ? ORDER BY test_name",
                (method_uri,),
            )
            return [self._row_to_test_bundle(row) for row in cursor.fetchall()]

    # CRUD operations for Reference Relationships
    def save_reference_relationship(self, ref: ReferenceRelationship) -> None:
        """Save or update a reference relationship."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            phases_json = None
            if ref.phases and len(ref.phases) > 1:
                phases_json = json.dumps([p.value for p in ref.phases])

            cursor.execute(
                """
                INSERT OR REPLACE INTO reference_relationships
                (source_method, target_method, phase, phases, description, confidence, is_external)
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """,
                (
                    ref.source_method,
                    ref.target_method,
                    ref.phase.value,
                    phases_json,
                    ref.description,
                    ref.confidence,
                    1 if ref.is_external else 0,
                ),
            )
            conn.commit()

    def get_reference_relationship(self, source: str, target: str) -> ReferenceRelationship | None:
        """Retrieve a reference relationship."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute(
                "SELECT * FROM reference_relationships WHERE source_method = ? AND target_method = ?",
                (source, target),
            )
            row = cursor.fetchone()
            if row:
                return self._row_to_reference(row)
            return None

    def get_reference_relationships_for_method(
        self, method_uri: str
    ) -> list[ReferenceRelationship]:
        """Get all reference relationships where this method is the source."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute(
                "SELECT * FROM reference_relationships WHERE source_method = ? ORDER BY target_method",
                (method_uri,),
            )
            return [self._row_to_reference(row) for row in cursor.fetchall()]

    def get_complete_relationships(self, method_uri: str) -> list[ReferenceRelationship]:
        """Get complete relationships (all three phases) for a method."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute(
                "SELECT * FROM reference_relationships WHERE source_method = ? AND phases IS NOT NULL",
                (method_uri,),
            )
            relationships = [self._row_to_reference(row) for row in cursor.fetchall()]
            return [rel for rel in relationships if rel.is_complete]

    # Utility methods
    def clear(self) -> None:
        """Clear all data from the database."""
        with self._get_connection() as conn:
            cursor = conn.cursor()
            cursor.execute("DELETE FROM reference_relationships")
            cursor.execute("DELETE FROM test_bundles")
            cursor.execute("DELETE FROM tests")
            cursor.execute("DELETE FROM methods")
            cursor.execute("DELETE FROM fields")
            cursor.execute("DELETE FROM classes")
            cursor.execute("DELETE FROM packages")
            conn.commit()

    def close(self) -> None:
        """Close the database connection."""
        # Connections are managed by _get_connection; keep API for compatibility.
        if self._conn:
            self._conn.close()
            self._conn = None

    # Row conversion helpers
    @staticmethod
    def _from_json(value: str | None, default: Any) -> Any:
        """Deserialize JSON values with sensible defaults."""
        return json.loads(value) if value else default

    @staticmethod
    def _row_to_class(row: sqlite3.Row) -> ClassInfo:
        """Convert database row to ClassInfo."""
        return ClassInfo(
            uri=row["uri"],
            name=row["name"],
            file_path=row["file_path"],
            package=row["package"],
            superclasses=MetainfoDatabase._from_json(row["superclasses"], []),
            super_interfaces=MetainfoDatabase._from_json(row["super_interfaces"], []),
            class_docstring=row["class_docstring"],
            original_string=row["original_string"],
            is_abstract=bool(row["is_abstract"]),
            is_interface=bool(row["is_interface"]),
            is_record=bool(row["is_record"]),
        )

    @staticmethod
    def _row_to_method(row: sqlite3.Row) -> MethodInfo:
        """Convert database row to MethodInfo."""
        return MethodInfo(
            uri=row["uri"],
            name=row["name"],
            class_uri=row["class_uri"],
            visibility=row["visibility"],
            return_type=row["return_type"],
            parameters=MetainfoDatabase._from_json(row["parameters"], []),
            modifiers=MetainfoDatabase._from_json(row["modifiers"], []),
            is_static=bool(row["is_static"]),
            docstring=row["docstring"],
            original_string=row["original_string"],
            throws=MetainfoDatabase._from_json(row["throws"], []),
            is_constructor=bool(row["is_constructor"]),
        )

    @staticmethod
    def _row_to_field(row: sqlite3.Row) -> FieldInfo:
        """Convert database row to FieldInfo."""
        return FieldInfo(
            uri=row["uri"],
            name=row["name"],
            class_uri=row["class_uri"],
            type=row["type"],
            modifiers=MetainfoDatabase._from_json(row["modifiers"], []),
            docstring=row["docstring"],
            is_static=bool(row["is_static"]),
            is_final=bool(row["is_final"]),
        )

    @staticmethod
    def _row_to_package(row: sqlite3.Row) -> PackageInfo:
        """Convert database row to PackageInfo."""
        return PackageInfo(
            uri=row["uri"],
            name=row["name"],
            file_path=row["file_path"],
            classes=MetainfoDatabase._from_json(row["classes"], []),
        )

    @staticmethod
    def _row_to_test(row: sqlite3.Row) -> TestInfo:
        """Convert database row to TestInfo."""
        return TestInfo(
            uri=row["uri"],
            name=row["name"],
            file_path=row["file_path"],
            target_class=row["target_class"],
            test_cases=MetainfoDatabase._from_json(row["test_cases"], []),
            fixtures=MetainfoDatabase._from_json(row["fixtures"], []),
            imports=MetainfoDatabase._from_json(row["imports"], []),
            class_members=MetainfoDatabase._from_json(row["class_members"], {}),
        )

    @staticmethod
    def _row_to_test_bundle(row: sqlite3.Row) -> TestBundle:
        """Convert database row to TestBundle."""
        return TestBundle(
            test_uri=row["test_uri"],
            test_name=row["test_name"],
            test_class_uri=row["test_class_uri"],
            target_method=row["target_method"],
            fixtures_used=MetainfoDatabase._from_json(row["fixtures_used"], []),
            external_dependencies=MetainfoDatabase._from_json(row["external_dependencies"], {}),
            project_specific_resources=MetainfoDatabase._from_json(
                row["project_specific_resources"], []
            ),
            assertions=MetainfoDatabase._from_json(row["assertions"], []),
            given_phase=MetainfoDatabase._from_json(row["given_phase"], None),
            when_phase=MetainfoDatabase._from_json(row["when_phase"], None),
            then_phase=MetainfoDatabase._from_json(row["then_phase"], None),
        )

    @staticmethod
    def _row_to_reference(row: sqlite3.Row) -> ReferenceRelationship:
        """Convert database row to ReferenceRelationship."""
        phases = None
        if row["phases"]:
            phases = {ReferencePhase(p) for p in json.loads(row["phases"])}
        else:
            phases = {ReferencePhase(row["phase"])}

        return ReferenceRelationship(
            source_method=row["source_method"],
            target_method=row["target_method"],
            phase=ReferencePhase(row["phase"]),
            phases=phases,
            description=row["description"],
            confidence=row["confidence"],
            is_external=bool(row["is_external"]),
        )
