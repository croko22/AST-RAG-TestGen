# Test Suite Design for AST-RAG TestGen

**Date:** 2026-03-23
**Status:** Proposed

## Overview

Add comprehensive unit and integration tests to the AST-RAG TestGen project using pytest framework with 85% global coverage target.

## Background

The project currently has no test infrastructure. A test suite is needed to:
- Validate core functionality (parsing, retrieval, prompt building, LLM interaction)
- Enable confident refactoring
- Prevent regressions
- Support CI/CD integration

## Requirements

### Functional Requirements

- [FR1] Unit tests for all modules: `core/parser.py`, `core/retriever.py`, `core/prompt_builder.py`, `llm/client.py`
- [FR2] Integration tests for the full pipeline (Java file → generated test)
- [FR3] Mock fixtures for Java files and project structures
- [FR4] Coverage minimum 85% globally, 90% for parser and prompt_builder

### Non-Functional Requirements

- [NFR1] Tests should be fast (< 5 seconds for unit tests)
- [NFR2] Tests should be independent (no shared state)
- [NFR3] CI/CD ready (can run in automated pipelines)

## Design

### Test Structure

```
tests/
├── __init__.py
├── conftest.py                 # Global pytest fixtures
├── fixtures/
│   ├── __init__.py
│   ├── java_samples/           # Sample Java files for testing
│   │   ├── simple_service.java
│   │   ├── complex_service.java
│   │   └── interface_example.java
│   └── mock_project/          # Mock Java project structure
├── unit/
│   ├── __init__.py
│   ├── test_parser.py
│   ├── test_retriever.py
│   ├── test_prompt_builder.py
│   └── test_llm_client.py
└── integration/
    ├── __init__.py
    └── test_full_pipeline.py
```

### Test Framework

- **pytest**: Modern Python test runner with clean syntax
- **pytest-cov**: Coverage reporting (HTML, terminal, XML)
- **pytest-mock**: Mocking utilities

### Unit Tests

#### test_parser.py (~8 tests)

- Parse simple Java class (name, package, imports)
- Extract method signatures
- Extract field declarations
- Handle non-existent files
- Parametrize visibility extraction (public, private, protected, package-private)
- Filter standard library imports

#### test_retriever.py (~10 tests)

- JavaFileRetriever initialization
- Find by class name (simple and qualified)
- Find non-existent class (returns None)
- Exclude test files from results
- Caching behavior
- Dependency resolution with depth limits (0, 1, 2)
- Cycle detection (A → B → A)
- Method signature formatting

#### test_prompt_builder.py (~6 tests)

- Build prompt returns (code, context) tuple
- Code under test is full file content
- Dependency context contains method signatures
- Max dependencies truncates results

#### test_llm_client.py (~10 tests)

- LLMConfig default values
- LLMConfig custom values
- Initialize each provider (anthropic, openai, glm, gemini, openrouter, nvidia)
- Raise error for unsupported provider
- System prompt generation
- User prompt structure
- Generate test uses config values
- Get available providers
- Get default model per provider

### Integration Tests

#### test_full_pipeline.py (~5 tests)

- End-to-end pipeline up to prompt generation (no LLM)
- End-to-end with mocked LLM response
- Dependency resolution depth=2 includes repository
- Prompt builder limits dependencies correctly

### Fixtures (conftest.py)

- `sample_java_file`: Create temp Java file for testing
- `mock_project_root`: Create mock Java project structure
- `retriever`: JavaFileRetriever initialized with mock project
- `resolver`: DependencyResolver initialized with retriever
- `llm_config`: LLMConfig for testing (no real API calls)

### Coverage Targets

| Component | Target | Rationale |
|------------|---------|-----------|
| core/parser.py | 90% | Well-bounded extraction logic |
| core/retriever.py | 85% | File handling and caching |
| core/prompt_builder.py | 90% | Structured formatting logic |
| llm/client.py | 80% | Multiple providers, external API mocking |
| Global | 85% | Balance effort and value |

### Configuration

**pytest.ini**:
```ini
[pytest]
testpaths = tests
python_files = test_*.py
python_classes = Test*
python_functions = test_*
addopts =
    -v
    --strict-markers
    --tb=short
    --cov=core
    --cov=llm
    --cov-report=html
    --cov-report=term-missing
    --cov-report=xml
    --cov-fail-under=85

markers =
    unit: Unit tests
    integration: Integration tests
    slow: Slow running tests
```

**environment.yml** additions:
```yaml
- pytest>=7.4.0
- pytest-cov>=4.1.0
- pytest-mock>=3.11.0
```

**.gitignore** additions:
```
htmlcov/
.coverage
coverage.xml
.pytest_cache/
```

## Files to Create

1. `tests/__init__.py`
2. `tests/conftest.py`
3. `tests/fixtures/__init__.py`
4. `tests/fixtures/java_samples/simple_service.java`
5. `tests/fixtures/java_samples/complex_service.java`
6. `tests/fixtures/java_samples/interface_example.java`
7. `tests/unit/__init__.py`
8. `tests/unit/test_parser.py`
9. `tests/unit/test_retriever.py`
10. `tests/unit/test_prompt_builder.py`
11. `tests/unit/test_llm_client.py`
12. `tests/integration/__init__.py`
13. `tests/integration/test_full_pipeline.py`
14. `pytest.ini`

## Files to Modify

1. `environment.yml` - Add pytest dependencies
2. `.gitignore` - Add coverage artifacts

## Success Criteria

- All tests pass: `pytest` returns exit code 0
- Coverage meets target: 85%+ global, 90%+ for parser/prompt_builder
- CI-ready: Tests run without external dependencies (API keys not required)
- Documentation: README or CLAUDE.md updated with test commands

## Open Questions

None.

## References

- Project structure: See CLAUDE.md
- Architecture docs: See `docs/architecture/*.md`
- pytest documentation: https://docs.pytest.org/
