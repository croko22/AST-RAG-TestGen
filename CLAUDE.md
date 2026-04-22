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

# MCP Server (editor/IDE integration)
python main.py serve --transport stdio
python main.py serve --transport http --port 8000

# RAG-enabled generation
python main.py generate service.java project/ --rag-enabled --alpha 0.7

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

1. **Parsing Layer** (`core/parsing/`): Uses Tree-sitter to parse Java files and extract:
   - Package declarations
   - Import statements
   - Class/interface names
   - Field declarations
   - Method signatures

2. **Extraction Layer** (`core/extraction/`): Finds and resolves dependency files in the Java project
   - `DependencyResolver`: Recursively resolves dependencies with depth limit
   - Builds class name to file path index

3. **Filtering Layer** (`core/filtering/`): Filters methods for test generation
   - Public methods
   - Testable methods
   - Custom filtering rules

4. **LLM Generation** (`llm/client_new.py`): Multi-provider LLM interface using adapter pattern
   - Supports: anthropic, openai, glm, gemini, nvidia, openrouter
   - Each provider has dedicated adapter in `llm/adapters/`
   - Prompt is built in the adapter's `build_user_prompt()` method
   - Provider-specific API handling

### Data Flow

```
Java File → Tree-sitter Parser → ParsedJavaClass
↓
JavaProjectPath → DependencyResolver → dependency_context (method signatures)
↓
[IF RAG ENABLED] JavaProjectPath → Chunker → Embedder → ChromaDB → HybridRetriever → rag_context
↓
ParsedJavaClass + dependency_context + [rag_context] → Prompt Builder → LLM Adapter
↓
Prompt → LLM API → Generated Test → [Post-processing: compile, run, coverage, quality]
```

### Actual Pipeline in orchestration/generator.py

The real pipeline is:
1. Parse Java file (Tree-sitter) → `core/parsing/parser.py`
2. Resolve dependencies → `core/extraction/extractor.py` (DependencyResolver)
3. Build prompt → `llm/adapters/base.py` (_build_common_system_prompt) + each adapter's build_user_prompt
4. Generate with LLM → `llm/adapters/*` (provider-specific)

### Key Classes

- `ParsedJavaClass`: Complete parsed representation of a Java file
- `JavaDependency`: Represents a dependency with name, type, package
- `MethodSignature`: Extracted method metadata (visibility, return type, params)
- `LLMConfig`: Config for LLM API calls (provider, model, temperature)
- `LLMClient`: Multi-provider interface to various LLM APIs
- `CodeChunk`: RAG chunk (method/class/import-level) with metadata
- `RetrievalResult`: Hybrid retrieval result (vector + AST scores)
- `GenerationResult`: Pipeline output with test_code, timings, retrieval_strategy

### RAG Pipeline (`rag/`)

5. **RAG Layer** (`rag/`): Semantic code retrieval augmenting AST-based context
- `ASTChunker`: Chunks parsed Java into method/class/import-level pieces
- `EmbeddingService`: sentence-transformers wrapper (all-MiniLM-L6-v2), lazy-loaded
- `ChromaIndexer`: ChromaDB vector storage with per-project collections
- `HybridRetriever`: Alpha-weighted blending of vector similarity + AST relevance
- Hybrid formula: `alpha * vector_score + (1-alpha) * ast_score`

### MCP Server (`mcp_server/`)

6. **MCP Server** (`mcp_server/`): FastMCP-based server for editor/IDE integration
- `generate_tests`: Full pipeline via MCP tool
- `analyze_code`: AST analysis + dependency graph via MCP tool
- `coverage_suggestions`: Quality assessment + coverage suggestions via MCP tool
- Supports STDIO (editor) and HTTP/SSE (remote) transports

### Post-Processing (`postproc/`)

7. **Post-Processing** (`postproc/`): Validation and quality assessment
- `validate_compilation`: Compile generated tests
- `run_tests`: Execute tests and capture results
- `parse_coverage`: JaCoCo XML parsing (LINE + BRANCH coverage)
- `assess_test_quality`: Heuristic quality scoring (assertions, triviality, diversity)

## Configuration

- Environment variables loaded from `.env` via `python-dotenv`
- API keys: `ANTHROPIC_API_KEY`, `OPENAI_API_KEY`, `GLM_API_KEY`, `GEMINI_API_KEY`, `NVIDIA_API_KEY`, `OPENROUTER_API_KEY`
- Provider defaults: `LLM_PROVIDER`, `LLM_MODEL`
- RAG config: `RAG_ENABLED`, `RAG_ALPHA`, `RAG_EMBEDDING_MODEL`, `RAG_CHROMA_PERSIST_DIR`
- MCP Server: `MCP_HOST`, `MCP_PORT`, `MCP_TRANSPORT`
- Post-processing: `POSTPROC_AUTO_COMPILE`, `POSTPROC_AUTO_RUN`, `POSTPROC_QUALITY_THRESHOLD`

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
- `docs/architecture/modular-refactoring.md` - Modular architecture refactoring details
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
cli/                    # Command-line interfaces
├── modern.py           # Modern Typer-based CLI
├── legacy.py           # Legacy argparse-based CLI
└── parser.py           # Argument parsing logic

core/                   # Core parsing and retrieval
├── parsing/            # AST parsing layer
│   ├── models.py       # Data models
│   └── parser.py       # Tree-sitter parser
├── filtering/          # Method filtering
│   └── filters.py      # Filtering logic
├── extraction/         # Dependency extraction
│   └── extractor.py    # Dependency resolver
└── prompt_builder.py   # Dynamic prompt assembly

llm/                    # LLM provider adapters
├── adapters/           # Provider-specific adapters
│   ├── base.py         # Base adapter interface
│   ├── anthropic.py    # Anthropic adapter
│   ├── openai.py       # OpenAI adapter
│   ├── glm.py          # GLM adapter
│   ├── gemini.py       # Gemini adapter
│   ├── nvidia.py       # NVIDIA adapter
│   └── openrouter.py   # OpenRouter adapter
└── client_new.py       # Multi-provider client

orchestration/ # Pipeline orchestration
├── generator.py # Test generation orchestration
└── benchmark.py # Benchmark orchestration

rag/ # RAG pipeline
├── models.py # CodeChunk, RetrievalResult, IndexStats
├── protocols.py # Chunker, Embedder, Indexer, Retriever protocols
├── chunker.py # AST-aware chunking (method/class/import)
├── embedder.py # sentence-transformers wrapper
├── indexer.py # ChromaDB vector storage
└── retriever.py # Hybrid retrieval (vector + AST)

postproc/ # Post-processing
├── validator.py # Compile, run tests, JaCoCo coverage
└── quality.py # Quality heuristics (assertions, triviality, diversity)

mcp_server/ # MCP server for editor/IDE
├── server.py # FastMCP tools (generate, analyze, coverage)
└── cli.py # CLI entry point (serve command)

output/ # Output handling
└── console.py # Rich console wrapper

config/                 # Configuration management
└── settings.py         # Pydantic settings

main.py                 # Main entry point (46 lines)
docs/                   # Architecture, decisions, API contracts
environment.yml         # Conda dependencies
.env.template           # Environment variable template
tests_generados/        # Output directory (gitignored)
```

## Testing

Mock Java project (`mock-java-project/`) exists for testing but is gitignored.

Generate tests for any service:
```bash
python main.py mock-java-project/src/main/java/com/example/demo/service/UsuarioService.java mock-java-project/ --provider nvidia
```
