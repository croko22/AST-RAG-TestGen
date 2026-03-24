# AST-RAG TestGen - Architecture Overview

## Purpose

AST-RAG TestGen is a 4-step pipeline for generating Java unit tests using AST-based context retrieval.

## Pipeline

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
| Parser | `core/parser.py` | Extract AST using Tree-sitter: package, imports, class name, methods, fields, dependencies |
| Retriever | `core/retriever.py` | Find Java files, build class→path index, resolve dependencies recursively with depth limit |
| Prompt Builder | `core/prompt_builder.py` | Assemble prompt: full code under test + extracted method signatures |
| LLM Client | `llm/client.py` | Multi-provider interface: Anthropic, OpenAI, GLM, Gemini, OpenRouter, NVIDIA |
| Orchestrator | `main.py` | CLI entry point, coordinates pipeline, saves generated tests |

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
