# Retriever Module - Dependency Resolution

## Purpose

Find and resolve dependency files in a Java project to provide context for test generation.

## Components

### JavaFileRetriever

Scans project for `.java` files and builds class name → file path index.

```python
class JavaFileRetriever:
    def __init__(self, project_root: str)
    def find_file_by_class_name(self, class_name: str) -> Optional[str]
    def find_files_by_pattern(self, pattern: str) -> List[str]
    def get_all_java_files(self) -> List[str]
    def parse_file(self, file_path: str) -> Optional[ParsedJavaClass]
```

**Caching**: Files are scanned once and cached (`_java_files_cache`, `_class_index`).

**Exclusions**: Test files are excluded (paths containing "test" or files ending in "Test.java").

**Indexing**: Two-level index:
- Simple class name: `Usuario` → `/path/Usuario.java`
- Fully qualified: `com.example.Usuario` → `/path/Usuario.java`

### DependencyResolver

Recursively resolves dependencies with depth limit.

```python
class DependencyResolver:
    def __init__(self, retriever: JavaFileRetriever)
    def resolve_dependencies(self, class_name: str, max_depth: int = 2) -> List[ParsedJavaClass]
    def get_method_signatures(self, class_name: str) -> str
```

**Depth Limit**: Default `max_depth=2` prevents exponential dependency explosion.

**Cycle Detection**: Uses `visited` set to prevent infinite recursion.

**Resolution Logic**:
```
Depth 0: Direct dependencies (imports, fields)
Depth 1: Dependencies of dependencies
Depth 2: Dependencies of dependencies of dependencies
```

**Method Signatures**: `get_method_signatures()` returns formatted string (not full code):
```java
// UsuarioRepository
package com.example.demo.repository;
public interface UsuarioRepository {
    Usuario save(Usuario usuario);
    Optional<Usuario> findById(Long id);
}
```

## Usage Example

```python
retriever = JavaFileRetriever("/path/to/java/project")
resolver = DependencyResolver(retriever)

# Find a class
file_path = retriever.find_file_by_class_name("UsuarioService")
print(file_path)  # /path/to/UsuarioService.java

# Resolve dependencies
deps = resolver.resolve_dependencies("UsuarioService", max_depth=2)
print(f"Found {len(deps)} dependencies")

# Get method signatures
signatures = resolver.get_method_signatures("UsuarioRepository")
print(signatures)
```

## Gotchas

- Files that cannot be parsed are silently skipped
- Class name lookup is case-sensitive
- Unresolvable dependencies don't block the process (return None)
- Wildcard imports cannot be resolved to specific files
