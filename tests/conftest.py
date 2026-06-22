"""Pytest configuration and shared fixtures."""

import pytest

# Try to import core modules, but don't fail if tree-sitter is not installed
try:
    from llm import LLMClient, LLMConfig
    from rag.retriever import DependencyResolver, JavaFileRetriever
except ImportError:
    # tree-sitter not available - set to None for tests that don't need it
    JavaFileRetriever = None
    DependencyResolver = None
    LLMClient = None
    LLMConfig = None


@pytest.fixture
def sample_java_file(tmp_path):
    """Create a temporary Java file for testing."""
    content = """
    package com.example.demo;
    import java.util.List;

    public class TestService {
        private Repository repo;

        public String getData() {
            return repo.findAll();
        }
    }
    """
    file = tmp_path / "TestService.java"
    file.write_text(content)
    return str(file)


@pytest.fixture
def mock_project_root(tmp_path):
    """Create a mock Java project structure."""
    src = tmp_path / "src" / "main" / "java" / "com" / "example"
    src.mkdir(parents=True)

    (src / "Service.java").write_text("""
package com.example;

public class Service {
    private Repository repository;

    public String getData() {
        return repository.findAll();
    }
}
""")
    (src / "Repository.java").write_text(
        "package com.example; public interface Repository { String findAll(); }"
    )

    return str(tmp_path)


@pytest.fixture
def retriever(mock_project_root):
    """JavaFileRetriever initialized with mock project."""
    if JavaFileRetriever is None:
        pytest.skip("Tree-sitter not installed")
    return JavaFileRetriever(mock_project_root)


@pytest.fixture
def resolver(retriever):
    """DependencyResolver initialized with retriever."""
    if DependencyResolver is None:
        pytest.skip("Tree-sitter not installed")
    return DependencyResolver(retriever)


@pytest.fixture
def llm_config():
    """LLMConfig for testing (no real API calls by default)."""
    if LLMConfig is None:
        pytest.skip("LLM client not available")
    return LLMConfig(
        provider="anthropic",  # Use mock in actual tests
        model="test-model",
        api_key="test-key",
    )


# tmp_path_factory is provided by pytest itself - do not redefine it
