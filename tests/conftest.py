"""Pytest configuration and shared fixtures."""

import pytest
from pathlib import Path
from core import JavaFileRetriever, DependencyResolver
from llm import LLMClient, LLMConfig


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
    (src / "Repository.java").write_text("package com.example; public interface Repository { String findAll(); }")

    return str(tmp_path)


@pytest.fixture
def retriever(mock_project_root):
    """JavaFileRetriever initialized with mock project."""
    return JavaFileRetriever(mock_project_root)


@pytest.fixture
def resolver(retriever):
    """DependencyResolver initialized with retriever."""
    return DependencyResolver(retriever)


@pytest.fixture
def llm_config():
    """LLMConfig for testing (no real API calls by default)."""
    return LLMConfig(
        provider="anthropic",  # Use mock in actual tests
        model="test-model",
        api_key="test-key"
    )


# tmp_path_factory is provided by pytest itself - do not redefine it
