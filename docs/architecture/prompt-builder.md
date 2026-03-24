# Prompt Builder Module - Context Assembly

## Purpose

Assemble the dynamic prompt by combining code under test with extracted dependency context.

## Implementation

`core/prompt_builder.py` - `PromptBuilder` class.

## Key Method

```python
def build_prompt(
    self,
    java_file_path: str,
    max_dependencies: int = 10,
) -> tuple[str, str]:
    """Returns (code_under_test, dependency_context)"""
```

## Context Assembly Process

### 1. Parse File Under Test

```python
parsed = self.retriever.parse_file(java_file_path)
code_under_test = parsed.content  # FULL file content
```

### 2. Extract Unique Dependency Names

From:
- `parsed.dependencies` (import and field types)
- `parsed.imports` (excluding `java.*`, `javax.*`, `org.*`)

### 3. Resolve and Format Each Dependency

```python
for dep_name in dependency_names:
    if count >= max_dependencies:
        # Truncate with message
        lines.append(f"\n// ... and {len(dependency_names) - max_dependencies} more dependencies")
        break
    signature = self.dependency_resolver.get_method_signatures(dep_name)
    lines.append(signature)
```

### 4. Format Method Signatures

Each dependency is formatted as a skeleton:

```java
// {class_name}
package {package};

public {class_type} {parsed.name} {
    {visibility} {return_type} {method_name}({params});
    ...
}
```

## Design Choice: Signatures vs Full Code

**We send method signatures, NOT full dependency code.**

| Approach | Tokens | Pros | Cons |
|----------|--------|------|------|
| Full code | High | Complete context | Expensive, LLM may copy implementations |
| Signatures | Low | Cheap, forces understanding | Missing implementation details |

**Rationale**: For test generation, we only need API contracts (method signatures, return types) to mock correctly. Full implementations add noise and increase cost.

## Output Format

```python
code_under_test = """
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    // ... full implementation
}
"""

dependency_context = """
// UsuarioRepository
package com.example.demo.repository;
public interface UsuarioRepository {
    Usuario save(Usuario usuario);
}

// EmailService
public class EmailService {
    public void sendWelcomeEmail(String email);
}
"""
```

## Usage

```python
from core import PromptBuilder, JavaFileRetriever, DependencyResolver

retriever = JavaFileRetriever("/path/to/project")
resolver = DependencyResolver(retriever)
builder = PromptBuilder(retriever, resolver)

code, context = builder.build_prompt("MyService.java", max_dependencies=5)
```

## Gotchas

- Dependencies are truncated when exceeding `max_dependencies`
- Field type dependencies are added separately from import dependencies
- If a dependency cannot be found, returns `// Class {name} not found`
