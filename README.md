# AST-RAG TestGen

Unit test generator for Java using AST-based Retrieval-Augmented Generation.

## Overview

AST-RAG TestGen generates Java unit tests through a 4-step pipeline:

1. **Extractor** (Tree-sitter): Parses Java files and extracts dependencies
2. **Retriever**: Locates dependency files in the Java project
3. **Slicer**: Extracts method signatures from dependencies
4. **Prompt Builder**: Assembles context and sends to LLM

## Installation

### Prerequisites

- Python 3.11 or later
- pip or conda

### Installation Methods

#### Using pip (recommended)

```bash
# Install from source
pip install -e .

# For development (includes linting, testing tools)
pip install -e .[dev]
```

#### Using conda

```bash
conda env create -f environment.yml
conda activate ast-rag-testgen
```

### Configuration

Configure API keys in `.env`:

```bash
cp .env.template .env
# Edit .env with your API keys
```

### Required API Keys

At least one of the following LLM provider keys must be configured in `.env`:

| Provider | Environment Variable |
|----------|---------------------|
| Anthropic | `ANTHROPIC_API_KEY` |
| OpenAI | `OPENAI_API_KEY` |
| GLM (Zhipu AI) | `GLM_API_KEY` |
| Gemini (Google) | `GEMINI_API_KEY` |
| OpenRouter | `OPENROUTER_API_KEY` |
| NVIDIA | `NVIDIA_API_KEY` |

## Usage

### Basic Command

```bash
python main.py <java_file> <java_project_path> [options]
```

Or using the installed CLI:

```bash
ast-rag-testgen <java_file> <java_project_path> [options]
```

### Arguments

| Argument | Description |
|----------|-------------|
| `java_file` | Path to the Java file to generate tests for |
| `project_path` | Root path of the Java project (for dependency resolution) |

### Options

| Option | Default | Description |
|--------|---------|-------------|
| `--provider` | `anthropic` | LLM provider: `anthropic`, `openai`, `glm`, `gemini`, `nvidia`, `openrouter` |
| `--model` | `claude-3-5-sonnet-20241022` | LLM model to use |
| `--output` | `./tests_generados` | Output directory for generated tests |
| `--max-deps` | `10` | Maximum number of dependencies to include |
| `--print` | `false` | Print generated test to stdout |

### Examples

```bash
# Generate test for a specific file
python main.py mock-java-project/src/main/java/com/example/demo/service/UsuarioService.java mock-java-project/

# Use OpenAI
python main.py service.java project/ --provider openai --model gpt-4-turbo

# Use GLM (Zhipu AI)
python main.py service.java project/ --provider glm --model glm-4-plus

# Use Gemini
python main.py service.java project/ --provider gemini --model gemini-2.0-flash-exp

# Use OpenRouter
python main.py service.java project/ --provider openrouter --model anthropic/claude-3.5-sonnet

# Use NVIDIA
python main.py service.java project/ --provider nvidia --model meta/llama-3.1-405b-instruct

# Custom output directory
python main.py service.java project/ --output ./my_tests

# Limit dependencies
python main.py service.java project/ --max-deps 5

# Print to stdout
python main.py service.java project/ --print
```

## LLM Providers

| Provider | Default Model |
|----------|---------------|
| Anthropic | `claude-3-5-sonnet-20241022` |
| OpenAI | `gpt-4-turbo` |
| GLM (Zhipu AI) | `glm-4-plus` |
| Gemini | `gemini-2.0-flash-exp` |
| NVIDIA | `meta/llama-3.1-405b-instruct` |
| OpenRouter | `anthropic/claude-3.5-sonnet` |

## Project Structure

```
AST-RAG-TestGen/
├── core/
│   ├── parser.py            # Tree-sitter AST parsing
│   ├── retriever.py         # Dependency resolution and indexing
│   └── prompt_builder.py    # Dynamic prompt assembly
├── llm/
│   └── client.py            # Multi-provider LLM interface
├── main.py                  # Main orchestrator
├── pyproject.toml            # Project configuration and packaging
├── requirements.txt          # Pip dependencies
├── environment.yml           # Conda dependencies
├── .env.template             # API key template
└── tests_generados/          # Output directory
```

## Development

### Running Tests

```bash
pytest
```

### Linting

```bash
ruff check .
ruff format --check .
```

### Type Checking

```bash
mypy core llm main.py
```

### Coverage

```bash
pytest --cov=core --cov=llm --cov-report=html
```

## Documentation

Detailed architecture and design documentation is available in `docs/`:

- `docs/architecture/overview.md` - Complete pipeline overview
- `docs/architecture/parser-module.md` - Tree-sitter AST parsing
- `docs/architecture/retriever-module.md` - Dependency resolution
- `docs/architecture/prompt-builder.md` - Context assembly
- `docs/architecture/llm-client.md` - Multi-provider LLM interface
- `docs/decisions/` - Architecture decision records
- `docs/api-contracts/` - API contracts and data types

## Dependencies

### Runtime

- Python >= 3.11
- tree-sitter >= 0.21.0
- tree-sitter-languages
- tree-sitter-java
- openai >= 1.0.0
- anthropic >= 0.18.0
- zhipuai >= 2.0.0
- google-generativeai >= 0.8.0
- pydantic >= 2.0.0
- python-dotenv >= 1.0.0
- pathspec >= 0.11.0

### Development

- mypy >= 1.0.0
- ruff >= 0.1.0
- pytest >= 7.4.0
- pytest-cov >= 4.1.0
- pytest-mock >= 3.11.0

## License

MIT
