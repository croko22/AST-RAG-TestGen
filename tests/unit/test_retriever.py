"""Unit tests for retriever module."""

from pathlib import Path

from core import DependencyResolver, JavaFileRetriever


class TestJavaFileRetriever:

    def test_initialization(self, mock_project_root):
        """Test retriever initializes with project root."""
        retriever = JavaFileRetriever(mock_project_root)
        assert retriever.project_root == Path(mock_project_root)

    def test_find_file_by_class_name_simple(self, retriever):
        """Test finding files by simple class name."""
        file = retriever.find_file_by_class_name("Service")
        assert file is not None
        assert "Service.java" in file

    def test_find_file_by_class_name_qualified(self, retriever):
        """Test finding files by fully qualified name."""
        file = retriever.find_file_by_class_name("com.example.Service")
        assert file is not None
        assert "Service.java" in file

    def test_find_nonexistent_class(self, retriever):
        """Test that non-existent classes return None."""
        file = retriever.find_file_by_class_name("NonExistent")
        assert file is None

    def test_get_all_java_files_excludes_tests(self, retriever):
        """Test that test files are excluded."""
        files = retriever.get_all_java_files()
        for f in files:
            assert "/test/" not in f or "/Test.java" not in f

    def test_caching(self, retriever):
        """Test that files are cached after first scan."""
        # First scan
        files1 = retriever.get_all_java_files()
        assert len(files1) >= 2

        # Second scan should use cache
        files2 = retriever.get_all_java_files()
        assert files1 == files2

    def test_parse_file_returns_parsed_class(self, retriever):
        """Test that parse_file returns ParsedJavaClass."""
        service_file = retriever.find_file_by_class_name("Service")
        result = retriever.parse_file(service_file)

        assert result is not None
        assert result.name == "Service"

    def test_parse_nonexistent_file_returns_none(self, retriever):
        """Test that parsing non-existent file returns None."""
        result = retriever.parse_file("/nonexistent/File.java")
        assert result is None


class TestDependencyResolver:

    def test_initialization(self, retriever):
        """Test resolver initialization."""
        resolver = DependencyResolver(retriever)
        assert resolver.retriever == retriever

    def test_resolve_dependencies_depth_0(self, retriever):
        """Test resolving with max_depth=0 (only target class)."""
        resolver = DependencyResolver(retriever)
        deps = resolver.resolve_dependencies("Service", max_depth=0)
        assert len(deps) >= 1
        dep_names = [dep.name for dep in deps]
        assert "Service" in dep_names

    def test_resolve_dependencies_depth_2(self, retriever):
        """Test resolving with default depth."""
        resolver = DependencyResolver(retriever)
        deps = resolver.resolve_dependencies("Service", max_depth=2)
        assert len(deps) > 0

    def test_resolve_nonexistent_class(self, resolver):
        """Test resolving non-existent class."""
        deps = resolver.resolve_dependencies("NonExistent")
        assert isinstance(deps, list)
        assert len(deps) == 0

    def test_cycle_detection(self, resolver, tmp_path):
        """Test that circular dependencies don't cause infinite loops."""
        # Create circular A -> B -> A
        a_file = tmp_path / "A.java"
        b_file = tmp_path / "B.java"
        a_file.write_text("class A { B b; }")
        b_file.write_text("class B { A a; }")

        retriever = JavaFileRetriever(str(tmp_path))
        resolver = DependencyResolver(retriever)

        # Should complete without hanging
        deps = resolver.resolve_dependencies("A")
        assert isinstance(deps, list)

    def test_get_method_signatures(self, retriever):
        """Test getting method signatures for a class."""
        resolver = DependencyResolver(retriever)
        signatures = resolver.get_method_signatures("Repository")

        assert "Repository" in signatures
        assert "findAll" in signatures

    def test_get_method_signatures_nonexistent(self, resolver):
        """Test getting signatures for non-existent class."""
        signatures = resolver.get_method_signatures("NonExistent")

        assert "NonExistent" in signatures
        assert "not found" in signatures.lower()

    def test_resolve_dependencies_filters_types(self, retriever):
        """Test that dependencies are resolved."""
        resolver = DependencyResolver(retriever)
        deps = resolver.resolve_dependencies("Service", max_depth=1)

        assert len(deps) > 0
