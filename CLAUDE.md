# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Common Commands

```bash
# Create/activate conda environment
conda env create -f environment.yml
conda activate ast-rag-testgen

# Modern CLI (recommended)
python main.py generate <java_file> <java_project_path> [options]
python main.py benchmark <manifest> [options]
python main.py providers

# Legacy CLI (backward compatible)
python main.py <java_file> <java_project_path> [options]

# Examples:
python main.py generate UsuarioService.java mock-java-project/
python main.py generate service.java project/ --provider nvidia --model meta/llama-3.1-405b-instruct
python main.py generate service.java project/ --output ./my_tests --max-deps 5
python main.py benchmark benchmark.yaml --output ./results
python main.py providers
```

## Architecture

AST-RAG TestGen is a 4-step pipeline for generating Java unit tests using AST-based context retrieval:

1. **Extractor** (`core/parser.py`): Uses Tree-sitter to parse Java files and extract:
   - Package declarations
   - Import statements
   - Class/interface names
   - Field declarations
   - Method signatures

2. **Retriever** (`core/retriever.py`): Finds and resolves dependency files in the Java project
   - `JavaFileRetriever`: Scans project for `.java` files
   - `DependencyResolver`: Recursively resolves dependencies with depth limit
   - Builds class name to file path index

3. **Prompt Builder** (`core/prompt_builder.py`): Assembles the dynamic prompt
   - Combines code under test with extracted method signatures
   - Formats context for LLM consumption

4. **LLM Client** (`llm/client.py`): Multi-provider LLM interface
   - Supports: anthropic, openai, glm, gemini, nvidia, openrouter
   - Each provider has default models and API-specific handling
   - NVIDIA and OpenRouter use OpenAI-compatible API

### Data Flow

```
Java File → JavaParser → ParsedJavaClass
    ↓
JavaProjectPath → JavaFileRetriever → DependencyResolver
    ↓
ParsedJavaClass + ResolvedDependencies → PromptBuilder
    ↓
Prompt → LLMClient → Generated Test
```

### Key Classes

- `ParsedJavaClass`: Complete parsed representation of a Java file
- `JavaDependency`: Represents a dependency with name, type, package
- `MethodSignature`: Extracted method metadata (visibility, return type, params)
- `LLMConfig`: Config for LLM API calls (provider, model, temperature)
- `LLMClient`: Multi-provider interface to various LLM APIs

## Configuration

- Environment variables loaded from `.env` via `python-dotenv`
- API keys: `ANTHROPIC_API_KEY`, `OPENAI_API_KEY`, `GLM_API_KEY`, `GEMINI_API_KEY`, `NVIDIA_API_KEY`, `OPENROUTER_API_KEY`
- Provider defaults: `LLM_PROVIDER`, `LLM_MODEL`

## Terminal Output

The project uses `rich` for beautiful terminal output:
- **Progress bars** for benchmark execution
- **Formatted tables** for results display
- **Syntax highlighting** for generated code
- **Color-coded messages** (green for success, red for errors, cyan for info)

If `rich` is not installed, the application falls back to plain text output.

## Documentation

Detailed architecture and design documentation:

- `docs/architecture/overview.md` - Complete pipeline overview and data flow
- `docs/architecture/parser-module.md` - Tree-sitter AST parsing details
- `docs/architecture/retriever-module.md` - Dependency resolution and indexing
- `docs/architecture/prompt-builder.md` - Context assembly strategy
- `docs/architecture/llm-client.md` - Multi-provider LLM interface
- `docs/decisions/tree-sitter-choice.md` - Why Tree-sitter over javac
- `docs/decisions/dependency-depth.md` - Why max_depth=2
- `docs/decisions/prompt-strategy.md` - Spanish prompt engineering choices
- `docs/api-contracts/retriever-api.md` - JavaFileRetriever/DependencyResolver contracts
- `docs/api-contracts/data-types.md` - ParsedJavaClass, JavaDependency, MethodSignature

## Project Structure

```
core/           # AST parsing, retrieval, prompt building
llm/            # LLM API clients (multi-provider)
main.py          # Orchestrator entry point
docs/            # Architecture, decisions, API contracts
environment.yml   # Conda dependencies
.env.template     # Environment variable template
tests_generados/ # Output directory (gitignored)
```

## Testing

Mock Java project (`mock-java-project/`) exists for testing but is gitignored.

Generate tests for any service:
```bash
python main.py mock-java-project/src/main/java/com/example/demo/service/UsuarioService.java mock-java-project/ --provider nvidia
```
