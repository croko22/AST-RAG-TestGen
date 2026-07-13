# Contributing to AST-RAG TestGen

Thank you for your interest in contributing. This project is part of a thesis, so the
contribution process is lightweight but still follows professional standards.

## Reporting Issues

Use the [GitHub issue tracker](https://github.com/croko22/AST-RAG-TestGen/issues) to
report bugs, request features, or ask questions. When reporting a bug, include:

- Steps to reproduce
- Expected vs. actual behavior
- Provider and model used (if applicable)
- Java version (`javac -version`)

## Development Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/croko22/AST-RAG-TestGen.git
   cd AST-RAG-TestGen
   ```

2. Set up the environment (see [README.md](README.md) for full details):

   ```bash
   # Option A: conda (recommended)
   conda env create -f environment.yml
   conda activate ast-rag-testgen

   # Option B: pip with dev dependencies
   pip install -e .[dev]
   ```

3. Configure API keys:

   ```bash
   cp .env.template .env
   # Edit .env with at least one provider key
   ```

4. Verify the installation:

   ```bash
   python main.py providers
   pytest
   ```

## Running Tests

```bash
# Full test suite
pytest

# Only unit tests
pytest -m unit

# Only integration tests
pytest -m integration

# With coverage report
pytest --cov=core --cov=llm --cov-report=html
```

## Pre-commit Hooks

The project uses [pre-commit](https://pre-commit.com/) with ruff for linting and
formatting. Install the hooks after cloning:

```bash
pip install pre-commit
pre-commit install
```

Hooks run automatically on every commit. To run them manually:

```bash
pre-commit run --all-files
```

## Code Style

- **Linter**: [ruff](https://docs.astral.sh/ruff/) — configuration in `pyproject.toml`
- **Type checker**: [mypy](https://mypy.readthedocs.io/) — configuration in `pyproject.toml`
- **Line length**: 100 characters
- **Target**: Python 3.11+

Run checks before pushing:

```bash
python -m ruff check core llm main.py
python -m ruff format --check core llm main.py
python -m mypy core llm main.py
```

## Branch and PR Conventions

- Create feature branches from `main`: `feat/description`, `fix/description`,
  `docs/description`, `refactor/description`
- Use [conventional commits](https://www.conventionalcommits.org/) for commit messages
- Keep PRs focused and reviewable — split large changes into multiple PRs
- Ensure all CI checks pass (ruff, mypy, pytest) before requesting review
- Reference issues in PR descriptions when applicable

## Project Structure

See [README.md](README.md) for the full directory layout and architecture overview.
Key directories:

- `core/` — AST parsing and dependency extraction
- `llm/` — Multi-provider LLM adapter pattern
- `rag/` — RAG pipeline (chunker, embedder, indexer, retriever)
- `postproc/` — Post-processing (validation, coverage, quality)
- `mcp_server/` — MCP server for IDE integration
- `orchestration/` — Pipeline orchestration
- `cli/` — Command-line interfaces