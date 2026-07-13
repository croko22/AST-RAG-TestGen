# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

- Documentation overhaul: fix repository URLs, add CHANGELOG, CONTRIBUTING, and LICENSE files
- Add missing `NVIDIA_API_KEY` to `.env.template`
- Add Java 11+ JDK prerequisite to README and replication guide

## [0.1.0] - 2026-07-04

### Overview

Initial public release of AST-RAG TestGen, a Java unit test generator that combines
AST-based context retrieval, optional RAG semantic retrieval, and multi-provider LLM
generation. Developed as part of a thesis project benchmarking LLM-driven test generation
on the RefTest-12 dataset.

### Added

- **AST parsing pipeline** using Tree-sitter to extract package, imports, class/interface
  names, fields, and method signatures from Java source files
- **Dependency resolution** with depth-limited recursive resolution and class-to-file
  indexing (2026-03-23)
- **Multi-provider LLM client** with adapter pattern supporting Anthropic, OpenAI, GLM
  (Zhipu AI), Google Gemini, NVIDIA, and OpenRouter (2026-03-23)
- **Metainfo database** for preserving overloaded methods with canonical URIs (2026-03-24)
- **BundleExtractor** for test-to-focal-method mapping (2026-03-28)
- **Manifest-driven benchmark pipeline** for evaluating generated tests across projects
  and models (2026-03-31)
- **RefTest-12 dataset** gatherer and parser filtering (2026-04-03)
- **Rich terminal output**: progress bars, formatted tables, syntax highlighting for
  generated code (2026-04-14)
- **Modern CLI** powered by Typer with shell completion support (2026-04-14)
- **Modular architecture**: split monolithic codebase into focused modules — `core/`,
  `llm/`, `rag/`, `postproc/`, `mcp_server/`, `cli/`, `orchestration/` (2026-04-15)
- **Pre-commit hooks** with ruff linting and formatting (2026-04-19)
- **RAG pipeline**: AST-aware chunker, sentence-transformers embedder, ChromaDB vector
  indexer, and hybrid retriever blending vector similarity with AST relevance via
  alpha-weighted scoring (2026-04-21)
- **MCP server** for editor/IDE integration with `generate_tests`, `analyze_code`, and
  `coverage_suggestions` tools (2026-04-21)
- **Post-processing layer**: compilation validation, test execution, JaCoCo coverage
  parsing (line + branch), and heuristic quality scoring (2026-04-21)
- **PIT mutation testing** integration for benchmark evaluation (2026-04-24)
- **Few-shot examples** in system prompt to improve test generation quality (2026-04-26)
- **Feedback loop**: compile-error retry with corrected prompt (disabled by default)
  (2026-05-10)
- **Thesis report CLI command** for generating results packages (2026-05-12)
- **Multi-provider campaign manifests** for all six LLM providers (2026-05-14)
- **Seaborn visualization** with thesis-quality figures (7 plots) (2026-06-02)
- **Statistical analysis** with effect sizes and confidence intervals (2026-06-03)
- **RefTest-12 expansion**: added commons-cli, commons-collections4, ice4j, jsoup,
  morel, rtree, and additional projects — 12 projects total (2026-06-04 to 2026-06-10)
- **Replication guide** and self-contained `examples/` project for thesis review
  (2026-07-04)

### Changed

- Updated default NVIDIA model from deprecated `meta/llama-3.1-405b-instruct` to
  `meta/llama-3.3-70b-instruct`
- Consolidated subprocess execution behind a shared `CommandRunner` seam (2026-06-14)
- Deepened LLM adapters — moved shared prompt methods to `BaseLLMAdapter` (2026-06-14)
- Unified Java file scanning behind `JavaFileScanner` (2026-06-14)
- Extracted RAG pipeline into a dedicated deep module (2026-06-14)

### Fixed

- Handle `None` content from LLM adapters with generator safety check
- Use effective collection name in ChromaDB indexer queries to avoid collisions
- Remove phantom openapi-diff dependency and fix zero-latency entries in consolidated
  benchmark reports
- Fix import rule in prompt for fully qualified class names
- Stabilize benchmark evaluator semantics regardless of rich availability

### Infrastructure

- GitHub Actions CI with lint (ruff), type check (mypy), and test (pytest) stages
- Conda environment (`environment.yml`) and pip `pyproject.toml` with optional
  dependencies for RAG and MCP features
- Comprehensive test suite with unit, integration, and benchmark test markers