# Decision: Tree-sitter for AST Parsing

## Context

AST-RAG TestGen needs to parse Java source files to extract:
- Package declarations
- Import statements
- Class/interface names
- Method signatures
- Field declarations
- Dependency relationships

## Alternatives Considered

### 1. JavaParser / Java Compiler
- **Pros**: Full compiler accuracy, complete type information
- **Cons**: Requires Java compilation, slow for large projects, requires Java runtime, fails on incomplete files

### 2. Regex-based Parsing
- **Pros**: Fast, no dependencies
- **Cons**: Fragile, breaks on edge cases (nested generics, annotations, comments), no AST structure

### 3. Tree-sitter
- **Pros**: Grammar-based parsing, fast, works with partial/syntactically incomplete files, pure Python integration
- **Cons**: Less type info than full compiler, requires grammar file

## Decision

**Use Tree-sitter with `tree-sitter-java` grammar.**

## Rationale

1. **No compilation required**: Can parse any `.java` file without requiring a full Java project setup
2. **Works with partial files**: Test generation often works on files that don't compile standalone
3. **Pure Python**: No JNI or subprocess calls to javac, easier deployment
4. **Sufficient for our use case**: We need structure (signatures, names), not full type resolution
5. **Fast**: Parses large projects quickly for dependency scanning

## Implementation

```python
from tree_sitter import Language, Parser

# Load Java grammar
import tree_sitter_java as tsjava
java_lang = Language(tsjava.language())

# Parse file
parser = Parser()
parser.language = java_lang
tree = parser.parse(bytes(content, "utf8"))
```

## Trade-offs

| Aspect | Impact |
|--------|--------|
| Type inference | Lost - we can't infer generic type parameters from AST alone |
| Semantic errors | Not detected - Tree-sitter is syntactic, not semantic |
| Our mitigation | For test generation, we only need method signatures and names, not full type semantics |

## Dependencies

- `tree-sitter`: Parser engine
- `tree-sitter-java`: Java grammar

Installed via:
```bash
pip install tree-sitter tree-sitter-java
```
