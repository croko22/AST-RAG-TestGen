# AST-RAG TestGen - Architecture Overview

## Purpose

AST-RAG TestGen is a 4-step pipeline for generating Java unit tests using AST-based context retrieval.

## Pipeline

```
┌─────────────────────────────────────────────────────────────────┐
│ 4-STEP PIPELINE                                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│ ┌──────────────┐ ┌──────────────────┐ ┌─────────────────────┐  │
│ │ 1. PARSING   │──▶│ 2. EXTRACTION    │──▶│ 3. PROMPT BUILDING │  │
│ │ Tree-sitter  │ │ DependencyResolv.│ │ Adapter-specific    │  │
│ └──────────────┘ └──────────────────┘ └─────────────────────┘  │
│                                                                 │
│ ▼                                                               │
│ ┌─────────────────────┐                                        │
│ │ 4. LLM GENERATION   │                                        │
│ │ Provider Adapter    │                                        │
│ └─────────────────────┘                                        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## Data Flow

```
Java File
↓
Tree-sitter Parser (core/parsing/parser.py)
↓
ParsedJavaClass {name, package, imports, methods, fields}
↓
DependencyResolver (core/extraction/extractor.py)
↓
dependency_context: method signatures from resolved dependencies
↓
LLM Adapter builds prompt (llm/adapters/*/build_user_prompt)
↓
LLM API call via provider-specific adapter
↓
Generated Test Code → tests_generados/{ClassName}Test.java
```
┌─────────────────────────────────────────────────────────────────┐
│                        4-STEP PIPELINE                      │
├─────────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌──────────┐    ┌──────────────┐    ┌────────────────┐     │
│  │ 1. PARSE │───▶│ 2. RETRIEVE │───▶│ 3. BUILD PROMPT│     │
│  │ Tree-sitter│    │ RAG + Depth  │    │ Slicer         │     │
│  └──────────┘    └──────────────┘    └────────────────┘     │
│                                                  │            │
│                                                  ▼            │
│                                        ┌──────────────────┐   │
│                                        │ 4. GENERATE     │   │
│                                        │ Multi-provider   │   │
│                                        │ LLM             │   │
│                                        └──────────────────┘   │
│                                                               │
└─────────────────────────────────────────────────────────────────┘
```

## Data Flow

```
Java File
    ↓
JavaParser.parse_file()
    ↓
ParsedJavaClass {name, package, imports, dependencies, methods, fields, content}
    ↓
DependencyResolver.resolve_dependencies(class_name, max_depth=2)
    ↓
List[ParsedJavaClass] (resolved dependencies)
    ↓
PromptBuilder.build_prompt(java_file_path, max_dependencies=10)
    ↓
(code_under_test, dependency_context) → method signatures (not full code)
    ↓
LLMClient.generate_test(code_under_test, dependency_context)
    ↓
Generated Test Code → tests_generados/{ClassName}Test.java
```

## Components

| Module | File | Responsibility |
|--------|------|----------------|
| **CLI Layer** | `cli/` | Command-line interfaces (modern Typer + legacy argparse) |
| **Parsing Layer** | `core/parsing/` | AST parsing with Tree-sitter: package, imports, class name, methods, fields |
| **Filtering Layer** | `core/filtering/` | Method filtering logic (public, testable, etc.) |
| **Extraction Layer** | `core/extraction/` | Dependency resolution and indexing |
| **Prompt Builder** | `core/prompt_builder.py` | Assemble prompt: full code under test + extracted method signatures |
| **LLM Adapters** | `llm/adapters/` | Provider-specific adapters (Anthropic, OpenAI, GLM, Gemini, NVIDIA, OpenRouter) |
| **LLM Client** | `llm/client_new.py` | Multi-provider interface using adapter pattern |
| **Orchestration** | `orchestration/` | Pipeline orchestration (generation + benchmark) |
| **Output** | `output/` | Rich console output with fallback |
| **Config** | `config/` | Pydantic-based configuration management |
| **Entry Point** | `main.py` | Minimal entry point (46 lines) that routes to CLI modules |

## Module Organization

The codebase is organized into focused modules with clear separation of concerns:

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
└── extraction/         # Dependency extraction
    └── extractor.py    # Dependency resolver

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

orchestration/          # Pipeline orchestration
├── generator.py        # Test generation orchestration
└── benchmark.py        # Benchmark orchestration

output/                 # Output handling
└── console.py          # Rich console wrapper

config/                 # Configuration management
└── settings.py         # Pydantic settings
```

## Key Characteristics

- **AST-based**: No Java compilation required, works with partial files
- **Depth-limited resolution**: `max_depth=2` prevents dependency explosion
- **Method signatures as context**: Reduces tokens vs full dependency code
- **Multi-provider LLM**: Switch between providers with `--provider` flag
- **Spanish prompts**: System and user prompts in Spanish for JUnit 5 + Mockito tests

## Entry Point

```bash
python main.py <java_file> <java_project_path> [options]
```

Example:
```bash
python main.py mock-java-project/src/main/java/com/example/demo/service/UsuarioService.java \
  mock-java-project/ --provider nvidia --model meta/llama-3.1-405b-instruct
```
