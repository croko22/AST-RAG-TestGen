"""Unit tests for Metainfo Database module."""

import sqlite3
from pathlib import Path

import pytest

from core.metainfo.database import MetainfoDatabase
from core.metainfo.schemas import (
    ClassInfo,
    FieldInfo,
    MethodInfo,
    PackageInfo,
    ReferenceMethodSet,
    ReferencePhase,
    ReferenceRelationship,
    ScopeGraph,
)
from core.metainfo.schemas import (
    TestBundle as MetainfoTestBundle,
)
from core.metainfo.schemas import (
    TestInfo as MetainfoTestInfo,
)


@pytest.fixture
def db_path(tmp_path):
    """Create a temporary database file."""
    return str(tmp_path / "test_metainfo.db")


@pytest.fixture
def db(db_path):
    """MetainfoDatabase instance for testing."""
    db = MetainfoDatabase(db_path)
    yield db
    # Cleanup after test - close connection first
    db.close()
    Path(db_path).unlink(missing_ok=True)


class TestMetainfoDB:
    @staticmethod
    def _seed_class(db, class_uri: str) -> None:
        db.save_class(
            ClassInfo(
                uri=class_uri,
                name=class_uri.split(".")[-1],
                file_path=f"src/main/java/{class_uri.replace('.', '/')}.java",
                package=".".join(class_uri.split(".")[:-1]),
                class_docstring=None,
                original_string=None,
            )
        )

    @classmethod
    def _seed_method(cls, db, method_uri: str) -> None:
        class_uri, method_name = method_uri.rsplit(".", 1)
        cls._seed_class(db, class_uri)
        db.save_method(
            MethodInfo(
                uri=method_uri,
                name=method_name,
                class_uri=class_uri,
                visibility="public",
                return_type="void",
                parameters=[],
                docstring=None,
                original_string=None,
            )
        )

    def test_database_creation_creates_parent_dir(self, tmp_path):
        """Database initialization creates missing parent directories."""
        db_path = tmp_path / "nested" / "db" / "metainfo.db"
        db = MetainfoDatabase(str(db_path))
        assert db_path.exists()
        db.close()

    def test_database_creation(self, db_path):
        """Test that database is created on initialization."""
        db = MetainfoDatabase(db_path)
        assert Path(db_path).exists()
        db.close()

    def test_schema_migrations_are_idempotent(self, tmp_path):
        """Running initialization multiple times does not duplicate migrations."""
        db_path = str(tmp_path / "idempotent.db")

        first = MetainfoDatabase(db_path)
        first.close()
        second = MetainfoDatabase(db_path)
        second.close()

        with sqlite3.connect(db_path) as conn:
            migration_rows = conn.execute(
                "SELECT version FROM schema_migrations ORDER BY version"
            ).fetchall()
            migration_versions = [row[0] for row in migration_rows]
            method_columns = conn.execute("PRAGMA table_info(methods)").fetchall()

        assert migration_versions == ["20260328_001_add_method_signature"]
        assert [col[1] for col in method_columns].count("signature") == 1
        assert [col[1] for col in method_columns].count("legacy_uri") == 1

    def test_forward_migration_upgrades_old_schema(self, tmp_path):
        """Old DB schema is upgraded in-place with migration ledger entry."""
        db_path = tmp_path / "forward.db"
        with sqlite3.connect(db_path) as conn:
            conn.execute(
                """
                CREATE TABLE classes (
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
            """
            )
            conn.execute(
                """
                CREATE TABLE methods (
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
            """
            )
            conn.execute(
                """
                INSERT INTO classes (uri, name, file_path, package)
                VALUES (?, ?, ?, ?)
            """,
                (
                    "com.example.LegacyService",
                    "LegacyService",
                    "src/main/java/com/example/LegacyService.java",
                    "com.example",
                ),
            )
            conn.execute(
                """
                INSERT INTO methods
                (uri, name, class_uri, visibility, return_type, parameters, modifiers, is_static, throws, is_constructor)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """,
                (
                    "com.example.LegacyService.compute",
                    "compute",
                    "com.example.LegacyService",
                    "public",
                    "int",
                    "[]",
                    "[]",
                    0,
                    "[]",
                    0,
                ),
            )
            conn.commit()

        db = MetainfoDatabase(str(db_path))
        method = db.get_method("com.example.LegacyService.compute")
        db.close()

        with sqlite3.connect(db_path) as conn:
            migration_rows = conn.execute(
                "SELECT version FROM schema_migrations ORDER BY version"
            ).fetchall()
            method_columns = conn.execute("PRAGMA table_info(methods)").fetchall()

        assert method is not None
        assert method.uri == "com.example.LegacyService.compute"
        assert [row[0] for row in migration_rows] == ["20260328_001_add_method_signature"]
        assert "signature" in [col[1] for col in method_columns]
        assert "legacy_uri" in [col[1] for col in method_columns]

    def test_connection_pragmas_are_applied(self, db):
        """Connection-level PRAGMAs should be enabled for safety/performance."""
        with db._get_connection() as conn:
            assert conn.execute("PRAGMA foreign_keys").fetchone()[0] == 1
            assert str(conn.execute("PRAGMA journal_mode").fetchone()[0]).lower() == "wal"
            assert conn.execute("PRAGMA synchronous").fetchone()[0] == 1
            assert conn.execute("PRAGMA busy_timeout").fetchone()[0] == 5000

    def test_transaction_commit_persists_changes(self, db):
        """transaction() commits changes when block exits successfully."""
        class_info = ClassInfo(
            uri="com.example.TxCommitService",
            name="TxCommitService",
            file_path="src/main/java/com/example/TxCommitService.java",
            package="com.example",
            class_docstring=None,
            original_string=None,
        )

        with db.transaction() as conn:
            db.save_class(class_info, conn=conn)

        assert db.get_class("com.example.TxCommitService") is not None

    def test_transaction_rollback_discards_changes(self, db):
        """transaction() rolls back if an exception is raised."""
        class_info = ClassInfo(
            uri="com.example.TxRollbackService",
            name="TxRollbackService",
            file_path="src/main/java/com/example/TxRollbackService.java",
            package="com.example",
            class_docstring=None,
            original_string=None,
        )

        with pytest.raises(RuntimeError, match="force rollback"):
            with db.transaction() as conn:
                db.save_class(class_info, conn=conn)
                raise RuntimeError("force rollback")

        assert db.get_class("com.example.TxRollbackService") is None

    def test_save_and_retrieve_class(self, db):
        """Test saving and retrieving a class."""
        class_info = ClassInfo(
            uri="com.example.TestService",
            name="TestService",
            file_path="src/main/java/com/example/TestService.java",
            package="com.example",
            superclasses=["AbstractService"],
            super_interfaces=["IService"],
            class_docstring="Test service class",
            original_string="class TestService extends AbstractService implements IService {}",
        )

        db.save_class(class_info)
        retrieved = db.get_class("com.example.TestService")

        assert retrieved is not None
        assert retrieved.name == "TestService"
        assert retrieved.package == "com.example"
        assert "AbstractService" in retrieved.superclasses

    def test_save_and_retrieve_method(self, db):
        """Test saving and retrieving a method."""
        self._seed_class(db, "com.example.TestService")
        method_info = MethodInfo(
            uri="com.example.TestService.getData",
            name="getData",
            class_uri="com.example.TestService",
            visibility="public",
            return_type="String",
            parameters=[],
            modifiers=["public"],
            is_static=False,
            docstring="Get data from repository",
            original_string="public String getData() { return repo.findAll(); }",
        )

        db.save_method(method_info)
        retrieved = db.get_method("com.example.TestService.getData")

        assert retrieved is not None
        assert retrieved.name == "getData"
        assert retrieved.return_type == "String"
        assert not retrieved.is_static

    def test_save_and_retrieve_field(self, db):
        """Test saving and retrieving a field."""
        self._seed_class(db, "com.example.TestService")
        field_info = FieldInfo(
            uri="com.example.TestService.repo",
            name="repo",
            class_uri="com.example.TestService",
            type="Repository",
            modifiers=["private", "final"],
            docstring="",
        )

        db.save_field(field_info)
        retrieved = db.get_field("com.example.TestService.repo")

        assert retrieved is not None
        assert retrieved.name == "repo"
        assert retrieved.type == "Repository"
        assert "private" in retrieved.modifiers

    def test_save_and_retrieve_package(self, db):
        """Test saving and retrieving a package."""
        package_info = PackageInfo(
            uri="com.example",
            name="com.example",
            file_path="src/main/java/com/example/package-info.java",
            classes=["TestService", "Repository"],
        )

        db.save_package(package_info)
        retrieved = db.get_package("com.example")

        assert retrieved is not None
        assert retrieved.name == "com.example"
        assert "TestService" in retrieved.classes

    def test_save_and_retrieve_test(self, db):
        """Test saving and retrieving test information."""
        self._seed_class(db, "com.example.TestService")
        test_info = MetainfoTestInfo(
            uri="com.example.TestServiceTest",
            name="TestServiceTest",
            file_path="src/test/java/com/example/TestServiceTest.java",
            target_class="com.example.TestService",
            test_cases=["testGetData", "testSaveData"],
            fixtures=["beforeEach", "setUp"],
        )

        db.save_test(test_info)
        retrieved = db.get_test("com.example.TestServiceTest")

        assert retrieved is not None
        assert retrieved.name == "TestServiceTest"
        assert retrieved.target_class == "com.example.TestService"
        assert "testGetData" in retrieved.test_cases

    def test_get_methods_by_class(self, db):
        """Test retrieving all methods for a class."""
        class_uri = "com.example.TestService"
        self._seed_class(db, class_uri)

        method1 = MethodInfo(
            uri=f"{class_uri}.getData",
            name="getData",
            class_uri=class_uri,
            visibility="public",
            return_type="String",
            parameters=[],
            docstring=None,
            original_string=None,
        )

        method2 = MethodInfo(
            uri=f"{class_uri}.setData",
            name="setData",
            class_uri=class_uri,
            visibility="public",
            return_type="void",
            parameters=[{"name": "data", "type": "String"}],
            docstring=None,
            original_string=None,
        )

        db.save_method(method1)
        db.save_method(method2)

        methods = db.get_methods_by_class(class_uri)

        assert len(methods) == 2
        method_names = [m.name for m in methods]
        assert "getData" in method_names
        assert "setData" in method_names

    def test_method_uri_strategy_prevents_overload_collisions(self, db):
        """Overloaded methods should have unique canonical URIs."""
        class_uri = "com.example.OverloadService"
        self._seed_class(db, class_uri)

        db.save_method(
            MethodInfo(
                uri=f"{class_uri}.process",
                name="process",
                class_uri=class_uri,
                visibility="public",
                return_type="void",
                parameters=[{"name": "arg0", "type": "String"}],
                docstring=None,
                original_string=None,
            )
        )
        db.save_method(
            MethodInfo(
                uri=f"{class_uri}.process",
                name="process",
                class_uri=class_uri,
                visibility="public",
                return_type="void",
                parameters=[{"name": "arg0", "type": "int"}],
                docstring=None,
                original_string=None,
            )
        )

        methods = db.get_methods_by_class(class_uri)
        uris = {method.uri for method in methods}
        signatures = {method.signature for method in methods}
        legacy_uris = {method.legacy_uri for method in methods}

        assert len(methods) == 2
        assert f"{class_uri}.process(String)" in uris
        assert f"{class_uri}.process(int)" in uris
        assert signatures == {"(String)", "(int)"}
        assert legacy_uris == {f"{class_uri}.process"}

    def test_get_fields_by_class(self, db):
        """Test retrieving all fields for a class."""
        class_uri = "com.example.TestService"
        self._seed_class(db, class_uri)

        field1 = FieldInfo(
            uri=f"{class_uri}.repo",
            name="repo",
            class_uri=class_uri,
            type="Repository",
            docstring=None,
        )

        field2 = FieldInfo(
            uri=f"{class_uri}.config",
            name="config",
            class_uri=class_uri,
            type="Config",
            docstring=None,
        )

        db.save_field(field1)
        db.save_field(field2)

        fields = db.get_fields_by_class(class_uri)

        assert len(fields) == 2
        field_names = [f.name for f in fields]
        assert "repo" in field_names
        assert "config" in field_names

    def test_get_classes_by_package(self, db):
        """Test retrieving all classes in a package."""
        package_name = "com.example"

        class1 = ClassInfo(
            uri=f"{package_name}.TestService",
            name="TestService",
            file_path="src/TestService.java",
            package=package_name,
            class_docstring=None,
            original_string=None,
        )

        class2 = ClassInfo(
            uri=f"{package_name}.Repository",
            name="Repository",
            file_path="src/Repository.java",
            package=package_name,
            class_docstring=None,
            original_string=None,
        )

        db.save_class(class1)
        db.save_class(class2)

        classes = db.get_classes_by_package(package_name)

        assert len(classes) == 2
        class_names = [c.name for c in classes]
        assert "TestService" in class_names
        assert "Repository" in class_names

    def test_save_and_retrieve_test_bundle(self, db):
        """Test saving and retrieving a test bundle."""
        self._seed_class(db, "com.example.TestService")
        self._seed_class(db, "com.example.TestServiceTest")
        self._seed_method(db, "com.example.TestService.getData")
        db.save_test(
            MetainfoTestInfo(
                uri="com.example.TestServiceTest",
                name="TestServiceTest",
                file_path="src/test/java/com/example/TestServiceTest.java",
                target_class="com.example.TestService",
                test_cases=[],
            )
        )

        test_bundle = MetainfoTestBundle(
            test_uri="com.example.TestServiceTest.testGetData",
            test_name="testGetData",
            test_class_uri="com.example.TestServiceTest",
            target_method="com.example.TestService.getData",
            fixtures_used=["beforeEach"],
            external_dependencies={"modules": ["Config"], "class_members": ["redis"]},
            project_specific_resources=["TestUtil.logTestResult"],
            given_phase=None,
            when_phase=None,
            then_phase=None,
        )

        db.save_test_bundle(test_bundle)
        retrieved = db.get_test_bundle("com.example.TestServiceTest.testGetData")

        assert retrieved is not None
        assert retrieved.test_name == "testGetData"
        assert retrieved.target_method == "com.example.TestService.getData"
        assert "beforeEach" in retrieved.fixtures_used

    def test_save_and_retrieve_reference_relationship(self, db):
        """Test saving and retrieving reference relationships."""
        self._seed_method(db, "com.example.Service.getData")
        self._seed_method(db, "com.example.Service.validateData")
        ref_rel = ReferenceRelationship(
            source_method="com.example.Service.getData",
            target_method="com.example.Service.validateData",
            phase=ReferencePhase.WHEN,
            phases={ReferencePhase.WHEN},
            description="Both methods validate data before return",
            confidence=0.85,
            is_external=False,
        )

        db.save_reference_relationship(ref_rel)
        retrieved = db.get_reference_relationship(
            "com.example.Service.getData", "com.example.Service.validateData"
        )

        assert retrieved is not None
        assert retrieved.phase == ReferencePhase.WHEN
        assert retrieved.confidence == 0.85
        assert not retrieved.is_external

    def test_get_reference_relationships_for_method(self, db):
        """Test getting all reference relationships for a method."""
        source_method = "com.example.Service.getData"
        self._seed_method(db, source_method)
        self._seed_method(db, "com.example.Service.validateData")
        self._seed_method(db, "com.example.Service.loadData")

        ref1 = ReferenceRelationship(
            source_method=source_method,
            target_method="com.example.Service.validateData",
            phase=ReferencePhase.WHEN,
            phases={ReferencePhase.WHEN},
            description="Validates data before returning",
            confidence=0.85,
        )

        ref2 = ReferenceRelationship(
            source_method=source_method,
            target_method="com.example.Service.loadData",
            phase=ReferencePhase.GIVEN,
            phases={ReferencePhase.GIVEN},
            description="Loads setup data before action",
            confidence=0.75,
        )

        db.save_reference_relationship(ref1)
        db.save_reference_relationship(ref2)

        refs = db.get_reference_relationships_for_method(source_method)

        assert len(refs) == 2
        phases = [r.phase for r in refs]
        assert ReferencePhase.WHEN in phases
        assert ReferencePhase.GIVEN in phases

    def test_get_complete_relationships_filters_partial(self, db):
        """Only complete GWT relationships are returned as complete."""
        source_method = "com.example.Service.getData"
        self._seed_method(db, source_method)
        self._seed_method(db, "com.example.Service.validateAndReturn")
        self._seed_method(db, "com.example.Service.loadData")

        complete_ref = ReferenceRelationship(
            source_method=source_method,
            target_method="com.example.Service.validateAndReturn",
            phase=ReferencePhase.GIVEN,
            phases={ReferencePhase.GIVEN, ReferencePhase.WHEN, ReferencePhase.THEN},
            description="Covers full setup-action-assertion flow",
            confidence=0.9,
        )
        partial_ref = ReferenceRelationship(
            source_method=source_method,
            target_method="com.example.Service.loadData",
            phase=ReferencePhase.GIVEN,
            phases={ReferencePhase.GIVEN, ReferencePhase.WHEN},
            description="Only setup and action overlap",
            confidence=0.7,
        )

        db.save_reference_relationship(complete_ref)
        db.save_reference_relationship(partial_ref)

        complete_relationships = db.get_complete_relationships(source_method)

        assert len(complete_relationships) == 1
        assert complete_relationships[0].target_method == "com.example.Service.validateAndReturn"

    def test_query_by_class_name(self, db):
        """Test searching classes by name (partial match)."""
        class1 = ClassInfo(
            uri="com.example.TestService",
            name="TestService",
            file_path="src/TestService.java",
            package="com.example",
            class_docstring=None,
            original_string=None,
        )

        class2 = ClassInfo(
            uri="org.other.AnotherService",
            name="AnotherService",
            file_path="src/AnotherService.java",
            package="org.other",
            class_docstring=None,
            original_string=None,
        )

        db.save_class(class1)
        db.save_class(class2)

        results = db.query_classes_by_name("Service")

        assert len(results) >= 1
        assert any(c.name == "TestService" for c in results)

    def test_clear_database(self, db):
        """Test clearing the database."""
        class_info = ClassInfo(
            uri="com.example.TestService",
            name="TestService",
            file_path="src/TestService.java",
            package="com.example",
            class_docstring=None,
            original_string=None,
        )

        db.save_class(class_info)
        db.clear()

        assert db.get_class("com.example.TestService") is None


class TestClassModel:
    def test_class_info_validation(self):
        """Test ClassInfo validation."""
        # Valid class info
        class_info = ClassInfo(
            uri="com.example.TestService",
            name="TestService",
            file_path="src/TestService.java",
            package="com.example",
            class_docstring=None,
            original_string=None,
        )
        assert class_info.uri == "com.example.TestService"

        # Missing required field
        with pytest.raises(ValueError):
            ClassInfo(
                uri="",  # Empty URI
                name="TestService",
                file_path="src/TestService.java",
                package="com.example",
                class_docstring=None,
                original_string=None,
            )


class TestMethodModel:
    def test_method_info_validation(self):
        """Test MethodInfo validation."""
        # Valid method info
        method_info = MethodInfo(
            uri="com.example.TestService.getData",
            name="getData",
            class_uri="com.example.TestService",
            visibility="public",
            return_type="String",
            parameters=[],
            docstring=None,
            original_string=None,
        )
        assert method_info.name == "getData"

        # Invalid visibility
        with pytest.raises(ValueError):
            MethodInfo(
                uri="com.example.TestService.getData",
                name="getData",
                class_uri="com.example.TestService",
                visibility="invalid",
                return_type="String",
                parameters=[],
                docstring=None,
                original_string=None,
            )

    def test_method_visibility_normalization(self):
        """Visibility is normalized before validation."""
        method_info = MethodInfo(
            uri="com.example.TestService.getData",
            name="getData",
            class_uri="com.example.TestService",
            visibility=" Public ",
            return_type="String",
            parameters=[],
            docstring=None,
            original_string=None,
        )

        assert method_info.visibility == "public"


class TestReferenceRelationshipModel:
    def test_reference_relationship_normalizes_phases(self):
        """Primary phase is always included in phases set."""
        rel = ReferenceRelationship(
            source_method="com.example.Source.method",
            target_method="com.example.Target.method",
            phase=ReferencePhase.THEN,
            phases={ReferencePhase.GIVEN},
            description="Assertion path",
            confidence=0.8,
        )

        assert rel.phases == {ReferencePhase.GIVEN, ReferencePhase.THEN}

    def test_reference_relationship_is_complete(self):
        """is_complete requires all Given/When/Then phases."""
        complete = ReferenceRelationship(
            source_method="com.example.Source.method",
            target_method="com.example.Target.complete",
            phase=ReferencePhase.WHEN,
            phases={ReferencePhase.GIVEN, ReferencePhase.WHEN, ReferencePhase.THEN},
            description="Complete relationship",
            confidence=0.95,
        )
        partial = ReferenceRelationship(
            source_method="com.example.Source.method",
            target_method="com.example.Target.partial",
            phase=ReferencePhase.WHEN,
            phases={ReferencePhase.WHEN},
            description="Partial relationship",
            confidence=0.55,
        )

        assert complete.is_complete is True
        assert partial.is_complete is False


class TestReferenceMethodSetModel:
    def test_all_methods(self):
        """Test getting all referenced method URIs."""
        ref_set = ReferenceMethodSet(
            focal_method_uri="com.example.Service.getData",
            complete=["com.example.Service.validate"],
            given=[("com.example.Service.setup", "Preconditions")],
            when=[("com.example.Service.loadData", "Load test data")],
            then=[("com.example.Service.assertResult", "Verify result")],
        )

        all_methods = ref_set.all_methods()

        assert len(all_methods) == 4
        assert "com.example.Service.validate" in all_methods
        assert "com.example.Service.setup" in all_methods
        assert "com.example.Service.loadData" in all_methods
        assert "com.example.Service.assertResult" in all_methods

    def test_rank(self):
        """Test ranking methods by priority."""
        ref_set = ReferenceMethodSet(
            focal_method_uri="com.example.Service.getData",
            complete=["com.example.Service.validate"],
            given=[("com.example.Service.setup", "Preconditions")],
            when=[("com.example.Service.loadData", "Load test data")],
            then=[("com.example.Service.assertResult", "Verify result")],
        )

        ranked = ref_set.rank(max_count=3)

        assert len(ranked) == 3
        # Complete method should be first
        assert ranked[0][0] == "com.example.Service.validate"
        assert ranked[0][2] == 1.0

        # Given should be second
        assert ranked[1][0] == "com.example.Service.setup"
        assert ranked[1][2] == 0.8

        # When should be third
        assert ranked[2][0] == "com.example.Service.loadData"
        assert ranked[2][2] == 0.6


class TestScopeGraphModel:
    def test_add_and_resolve_scope(self):
        """Test adding scopes and resolving references."""
        graph = ScopeGraph()

        # Add scopes
        graph.add_scope("class_scope", parent_id=None)
        graph.add_scope("method_scope", parent_id="class_scope")

        # Add definitions
        graph.add_definition("repo_var_def", scope_id="class_scope", symbol="repo_var")
        graph.add_definition("data_method_def", scope_id="method_scope", symbol="data_method")

        # Add reference
        graph.add_reference("repo_var_ref", scope_id="method_scope", symbol="repo_var")

        # Resolve reference - returns the definition node ID
        resolved = graph.resolve_local("repo_var_ref")

        assert resolved == "repo_var_def"

    def test_resolve_not_found(self):
        """Test resolving non-existent reference."""
        graph = ScopeGraph()

        graph.add_scope("class_scope")
        graph.add_definition("data_method_def", scope_id="class_scope", symbol="data_method")
        graph.add_reference("unknown_var_ref", scope_id="class_scope", symbol="unknown_var")

        resolved = graph.resolve_local("unknown_var_ref")

        assert resolved is None

    def test_parent_scope_traversal(self):
        """Test scope traversal to parent."""
        graph = ScopeGraph()

        graph.add_scope("global", parent_id=None)
        graph.add_scope("class_scope", parent_id="global")
        graph.add_scope("method_scope", parent_id="class_scope")

        graph.add_definition("repo_var_def", scope_id="class_scope", symbol="repo_var")
        graph.add_reference("repo_var_ref", scope_id="method_scope", symbol="repo_var")

        resolved = graph.resolve_local("repo_var_ref")

        assert resolved == "repo_var_def"

    def test_node_types(self):
        """Test that node type constants are set correctly."""
        assert ScopeGraph.NODE_SCOPE == "LocalScope"
        assert ScopeGraph.NODE_DEF == "LocalDef"
        assert ScopeGraph.NODE_IMPORT == "LocalImport"
        assert ScopeGraph.NODE_REF == "Reference"

    def test_edge_types(self):
        """Test that edge type constants are set correctly."""
        assert ScopeGraph.EDGE_SCOPE_TO_SCOPE == "ScopeToScope"
        assert ScopeGraph.EDGE_DEF_TO_SCOPE == "DefToScope"
        assert ScopeGraph.EDGE_IMPORT_TO_SCOPE == "ImportToScope"
        assert ScopeGraph.EDGE_REF_TO_SCOPE == "RefToScope"
