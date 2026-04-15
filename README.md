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

Use `pip install -e .[dev]` as the canonical local setup before running lint and type checks.

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

### Optional Dependencies

**Rich Terminal Output** (Recommended for better UX):

The project includes `rich` for beautiful terminal output:
- Progress bars for benchmarks
- Formatted tables for results
- Syntax highlighting for generated code
- Color-coded success/error messages

Rich is automatically installed with the main package. If you prefer plain output, you can uninstall it:

```bash
pip uninstall rich
```

The application will automatically fall back to plain text output.

## Usage

### Modern CLI (Recommended)

AST-RAG TestGen now includes a modern CLI powered by Typer with auto-completion:

```bash
# Generate test for a Java file
python main.py generate <java_file> <java_project_path> [options]

# Run benchmarks
python main.py benchmark <manifest> [options]

# List available providers
python main.py providers

# Enable shell completion
python main.py --install-completion
```

#### Generate Command

```bash
# Basic usage
python main.py generate UsuarioService.java mock-java-project/

# Use different provider
python main.py generate service.java project/ --provider nvidia --model meta/llama-3.1-405b-instruct

# Custom output directory
python main.py generate service.java project/ --output ./my_tests

# Limit dependencies
python main.py generate service.java project/ --max-deps 5

# Print to stdout
python main.py generate service.java project/ --print
```

#### Benchmark Command

```bash
# Run benchmark with manifest
python main.py benchmark benchmark.yaml --output ./results

# Dry run (skip LLM calls)
python main.py benchmark benchmark.yaml --dry-run
```

#### Providers Command

```bash
# List all available providers and their default models
python main.py providers
```

### Legacy CLI (Backward Compatible)

The legacy argparse-based CLI is still available for compatibility:

```bash
python main.py <java_file> <java_project_path> [options]
```

### Arguments

| Argument | Description |
|----------|-------------|
| `java_file` | Path to the Java file to generate tests for |
| `project_path` | Root path of the Java project (for dependency resolution) |

### Options

| Option | Default | Description |
|--------|---------|-------------|
| `--provider`, `-p` | `anthropic` | LLM provider: `anthropic`, `openai`, `glm`, `gemini`, `nvidia`, `openrouter` |
| `--model`, `-m` | `claude-3-5-sonnet-20241022` | LLM model to use |
| `--output`, `-o` | `./tests_generados` | Output directory for generated tests |
| `--max-deps`, `-d` | `10` | Maximum number of dependencies to include |
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
├── cli/                      # Command-line interfaces
│   ├── modern.py            # Modern Typer-based CLI
│   ├── legacy.py            # Legacy argparse-based CLI
│   └── parser.py            # Argument parsing logic
├── core/                    # Core parsing and retrieval
│   ├── parsing/             # AST parsing layer
│   │   ├── models.py        # Data models (ParsedJavaClass, etc.)
│   │   └── parser.py        # Tree-sitter parser
│   ├── filtering/           # Method filtering
│   │   └── filters.py       # Filtering logic
│   ├── extraction/          # Dependency extraction
│   │   └── extractor.py     # Dependency resolver
│   ├── parser.py            # Legacy parser (deprecated)
│   ├── retriever.py         # Legacy retriever (deprecated)
│   └── prompt_builder.py    # Dynamic prompt assembly
├── llm/                     # LLM provider adapters
│   ├── adapters/            # Provider-specific adapters
│   │   ├── base.py          # Base adapter interface
│   │   ├── anthropic.py     # Anthropic adapter
│   │   ├── openai.py        # OpenAI adapter
│   │   ├── glm.py           # GLM (Zhipu AI) adapter
│   │   ├── gemini.py        # Gemini adapter
│   │   ├── nvidia.py        # NVIDIA adapter
│   │   └── openrouter.py    # OpenRouter adapter
│   ├── client.py            # Legacy client (deprecated)
│   └── client_new.py        # New client using adapters
├── orchestration/           # Pipeline orchestration
│   ├── generator.py         # Test generation orchestration
│   └── benchmark.py         # Benchmark orchestration
├── output/                  # Output handling
│   └── console.py           # Rich console wrapper
├── config/                  # Configuration management
│   └── settings.py          # Pydantic settings
├── main.py                  # Main entry point (46 lines)
├── pyproject.toml           # Project configuration
├── requirements.txt         # Pip dependencies
├── environment.yml          # Conda dependencies
├── .env.template            # API key template
└── tests_generados/         # Output directory
```

## Development

### Running Tests

```bash
# Run all tests
pytest

# Run only benchmark tests
pytest tests/benchmark/
```

### Linting

```bash
python -m ruff check core llm main.py
python -m ruff format --check core llm main.py
```

### Type Checking

```bash
python -m mypy core llm main.py
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

## Known Limitations

### Benchmark Mode

- **Coverage Parsing**: Coverage command output parsing supports Maven text output. JaCoCo XML support may require additional configuration.
- **Token/Cost Fields**: Provider token usage and cost estimates are not available from all SDKs and are recorded as nullable fields when unavailable.
- **Provider Variability**: LLM outputs may vary between runs even with fixed seeds due to provider-side non-determinism. Use multiple trials for statistically meaningful comparisons.
- **Sequential Default**: Default concurrency is 1 (sequential). Higher concurrency may cause rate limiting with some providers.

## License

MIT
