# Modular Architecture Refactoring

## Overview

AST-RAG TestGen has been refactored from a monolithic codebase to a clean, modular architecture with focused modules and clear separation of concerns.

## Before Refactoring

The original codebase had 1,690 lines of monolithic code across 3 files:

- `main.py`: 671 lines doing CLI + orchestration + output + benchmark + compatibility
- `llm/client.py`: 498 lines with all providers in one file
- `core/parser.py`: 521 lines mixing parsing + filtering + extraction

## After Refactoring

The new architecture consists of 26 focused modules:

### Module Breakdown

#### CLI Layer (`cli/`)
- **`modern.py`**: Modern Typer-based CLI with subcommands and auto-completion
- **`legacy.py`**: Legacy argparse-based CLI for backward compatibility
- **`parser.py`**: Argument parsing logic shared between CLI implementations

#### Core Parsing Layer (`core/parsing/`)
- **`models.py`**: Data models (ParsedJavaClass, JavaDependency, MethodSignature)
- **`parser.py`**: Tree-sitter AST parser

#### Core Filtering Layer (`core/filtering/`)
- **`filters.py`**: Method filtering logic (public, testable, etc.)

#### Core Extraction Layer (`core/extraction/`)
- **`extractor.py`**: Dependency resolution and indexing

#### LLM Adapters (`llm/adapters/`)
- **`base.py`**: Base adapter interface
- **`anthropic.py`**: Anthropic adapter
- **`openai.py`**: OpenAI adapter
- **`glm.py`**: GLM (Zhipu AI) adapter
- **`gemini.py`**: Gemini adapter
- **`nvidia.py`**: NVIDIA adapter
- **`openrouter.py`**: OpenRouter adapter

#### LLM Client (`llm/`)
- **`client_new.py`**: Multi-provider client using adapter pattern
- **`client.py`**: Legacy client (deprecated, kept for backward compatibility)

#### Orchestration Layer (`orchestration/`)
- **`generator.py`**: Test generation orchestration
- **`benchmark.py`**: Benchmark orchestration

#### Output Layer (`output/`)
- **`console.py`**: Rich console wrapper with fallback

#### Configuration Layer (`config/`)
- **`settings.py`**: Pydantic-based configuration management

#### Entry Point
- **`main.py`**: Minimal entry point (46 lines) that routes to CLI modules

## Architecture Benefits

### 1. Separation of Concerns
Each module has a single, well-defined responsibility:
- CLI modules handle user interaction
- Core modules handle parsing and retrieval
- LLM modules handle provider-specific logic
- Orchestration modules coordinate the pipeline

### 2. Testability
Focused modules are easier to test:
- Each adapter can be tested independently
- CLI logic can be tested without LLM calls
- Parsing logic can be tested with mock data

### 3. Maintainability
Changes are localized to specific modules:
- Adding a new LLM provider only requires a new adapter
- CLI changes don't affect core parsing logic
- Output formatting is isolated

### 4. Extensibility
New features can be added without modifying existing code:
- New LLM providers: Add adapter + update client
- New CLI commands: Add to modern.py
- New filtering rules: Add to filters.py

### 5. Backward Compatibility
The refactoring maintains full backward compatibility:
- Legacy CLI still works
- Old imports still work (re-exported from main.py)
- Existing tests pass without modification

## Design Patterns

### Adapter Pattern
LLM providers use the adapter pattern:
```python
class BaseLLMAdapter(ABC):
    @abstractmethod
    def generate_test(self, code_under_test: str, dependency_context: str) -> str:
        pass

class AnthropicAdapter(BaseLLMAdapter):
    def generate_test(self, code_under_test: str, dependency_context: str) -> str:
        # Anthropic-specific implementation
```

### Strategy Pattern
CLI implementations use the strategy pattern:
```python
def main() -> int:
    if MODERN_CLI_AVAILABLE and is_modern_command():
        return run_modern_cli()
    return run_legacy_cli()
```

### Factory Pattern
LLM client uses factory pattern for adapter creation:
```python
def _create_adapter(self, config: LLMConfig) -> BaseLLMAdapter:
    adapter_class = self._ADAPTER_MAP.get(provider)
    return adapter_class(config)
```

## Migration Guide

### For Users
No changes required - the CLI interface remains the same:
```bash
# Old command (still works)
python main.py service.java project/

# New command (recommended)
python main.py generate service.java project/
```

### For Developers
When adding new features:

1. **New LLM Provider**: Create adapter in `llm/adapters/`
2. **New CLI Command**: Add to `cli/modern.py`
3. **New Filtering Rule**: Add to `core/filtering/filters.py`
4. **New Orchestration**: Add to `orchestration/`

### For Tests
Tests should import from the appropriate module:
```python
# Old way (still works)
from main import generate_test_for_file

# New way (recommended)
from orchestration.generator import generate_test_for_file
```

## Performance Impact

The refactoring has minimal performance impact:
- No additional overhead from module imports
- Adapter pattern adds negligible overhead
- Better code organization enables future optimizations

## Future Improvements

Potential areas for future enhancement:

1. **Async Support**: Add async adapters for concurrent LLM calls
2. **Caching**: Add caching layer for parsed Java files
3. **Plugin System**: Allow external adapters and filters
4. **Configuration**: Add more configuration options
5. **Monitoring**: Add metrics and observability

## Conclusion

The modular architecture provides a solid foundation for future development while maintaining backward compatibility and improving code quality. The focused modules make the codebase easier to understand, test, and extend.
