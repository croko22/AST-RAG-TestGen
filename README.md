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

## Usage

### Basic Command

```bash
python main.py <java_file> <java_project_path> [options]
```

Or using the installed CLI:

```bash
ast-rag-testgen <java_file> <java_project_path> [options]
```

### Benchmark Mode

AST-RAG TestGen includes a benchmark mode for reproducible evaluation of test generation quality across different providers, models, and configurations.

```bash
python main.py --benchmark-manifest <manifest_path> --benchmark-output <output_dir>
```

#### Benchmark Command

```bash
# Run benchmark with manifest
python main.py --benchmark-manifest benchmark.yaml --benchmark-output ./results

# Dry run (skip LLM calls, test pipeline only)
python main.py --benchmark-manifest benchmark.yaml --benchmark-output ./results --benchmark-dry-run
```

#### Benchmark Manifest Schema

The benchmark uses a manifest file to define the execution matrix, dataset, and evaluation criteria.

**JSON Example:**
```json
{
  "manifest_version": 1,
  "project_root": "mock-java-project",
  "dataset": [
    {
      "id": "usuario-service",
      "java_file": "src/main/java/com/example/demo/service/UsuarioService.java"
    }
  ],
  "matrix": {
    "providers": [
      {"name": "anthropic", "model": "claude-3-5-sonnet-20241022"},
      {"name": "openai", "model": "gpt-4-turbo"}
    ]
  },
  "run": {
    "trials": 1,
    "seed": 42,
    "max_dependencies": 10,
    "timeout_seconds": 300,
    "retry_count": 0,
    "concurrency": 1
  },
  "evaluation": {
    "compile_cmd": "mvn -q -DskipTests compile",
    "test_cmd": "mvn -q test",
    "coverage_cmd": null
  },
  "scoring": {
    "weights": {
      "success": 0.5,
      "coverage": 0.3,
      "latency": 0.2
    }
  }
}
```

**TOML Example:**
```toml
manifest_version = 1
project_root = "mock-java-project"

[[dataset]]
id = "usuario-service"
java_file = "src/main/java/com/example/demo/service/UsuarioService.java"

[matrix]

[[matrix.providers]]
name = "anthropic"
model = "claude-3-5-sonnet-20241022"

[run]
trials = 1
seed = 42
max_dependencies = 10
timeout_seconds = 300

[evaluation]
compile_cmd = "mvn -q -DskipTests compile"
test_cmd = "mvn -q test"

[scoring]
[scoring.weights]
success = 0.5
coverage = 0.3
latency = 0.2
```

#### Manifest Fields

| Field | Type | Description |
|-------|------|-------------|
| `manifest_version` | integer | Must be `1` |
| `project_root` | string | Path to Java project root |
| `dataset` | array | List of Java files to generate tests for |
| `dataset[].id` | string | Unique identifier for the entry |
| `dataset[].java_file` | string | Relative path to Java source file |
| `matrix` | object | Provider/model execution matrix |
| `matrix.providers` | array | List of provider/model combinations |
| `run` | object | Execution controls |
| `run.trials` | integer | Number of trials per configuration (default: 1) |
| `run.seed` | integer | Random seed for deterministic ordering (default: 0) |
| `run.max_dependencies` | integer | Max dependencies to include (default: 10) |
| `run.timeout_seconds` | integer | Timeout per run (default: 300) |
| `run.retry_count` | integer | Number of retries on failure (default: 0) |
| `run.concurrency` | integer | Max parallel runs (default: 1) |
| `evaluation` | object | Post-generation evaluation commands |
| `evaluation.compile_cmd` | string | Command to compile tests |
| `evaluation.test_cmd` | string | Command to run tests |
| `evaluation.coverage_cmd` | string/null | Optional coverage command |
| `scoring` | object | Ranking weights |
| `scoring.weights.success` | float | Weight for pass rate (0-1) |
| `scoring.weights.coverage` | float | Weight for coverage (0-1) |
| `scoring.weights.latency` | float | Weight for speed (0-1) |

#### Benchmark Outputs

After execution, the benchmark produces three output files in the specified output directory:

| File | Description |
|------|-------------|
| `results.json` | Raw per-run results with all metrics |
| `summary.json` | Aggregated statistics and rankings |
| `report.md` | Human-readable summary with tables |

#### Reproducibility Notes

- Same manifest + seed = identical run ordering
- Use `--benchmark-dry-run` to validate setup without API calls
- Failed runs (timeout, error) are recorded with status but don't abort the benchmark
- Partial results are saved incrementally for crash recovery

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
