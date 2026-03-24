# Retriever Module API Contracts

These are the primary interfaces used by `PromptBuilder` and the orchestrator.

## JavaFileRetriever

### Constructor

```python
def __init__(self, project_root: str)
    """
    Initialize the retriever.

    Args:
        project_root: Root directory of the Java project

    Raises:
        FileNotFoundError: If project_root doesn't exist
    """
```

### find_file_by_class_name

```python
def find_file_by_class_name(self, class_name: str) -> Optional[str]
    """
    Find a Java file by its class name.

    Args:
        class_name: Simple class name (e.g., "UsuarioService")
                    or fully qualified name (e.g., "com.example.UsuarioService")

    Returns:
        Path to the Java file as string, or None if not found

    Notes:
        - Uses cached class index
        - Index is built on first call
        - Case-sensitive
    """
```

### get_all_java_files

```python
def get_all_java_files(self) -> List[str]
    """
    Get all Java files in the project (excluding tests).

    Returns:
        List of file paths as strings

    Notes:
        - Excludes paths containing "test"
        - Excludes files ending in "Test.java"
        - Results are cached
    """
```

### parse_file

```python
def parse_file(self, file_path: str) -> Optional[ParsedJavaClass]
    """
    Parse a Java file and return its parsed representation.

    Args:
        file_path: Path to the Java file

    Returns:
        ParsedJavaClass with extracted information, or None if parsing fails

    Notes:
        - Silently returns None on parsing errors
        - Uses Tree-sitter internally
    """
```

## DependencyResolver

### Constructor

```python
def __init__(self, retriever: JavaFileRetriever)
    """
    Initialize the resolver.

    Args:
        retriever: JavaFileRetriever instance for file discovery
    """
```

### resolve_dependencies

```python
def resolve_dependencies(
    self,
    class_name: str,
    max_depth: int = 2
) -> List[ParsedJavaClass]
    """
    Recursively resolve dependencies for a class.

    Args:
        class_name: Name of the class to resolve
        max_depth: Maximum recursion depth (default: 2)

    Returns:
        List of ParsedJavaClass for resolved dependencies

    Notes:
        - Uses visited set to prevent cycles
        - Only resolves dependencies of type "class", "interface", "import"
        - Order: depth-first traversal
        - Includes the target class itself in results
    """
```

**Depth Example**:
```
max_depth=0: [TargetClass] only
max_depth=1: [TargetClass, DirectDep1, DirectDep2]
max_depth=2: [TargetClass, DirectDep1, DirectDep2, IndirectDep1, IndirectDep2]
```

### get_method_signatures

```python
def get_method_signatures(self, class_name: str) -> str
    """
    Get formatted method signatures for a class.

    Args:
        class_name: Name of the class

    Returns:
        Formatted string with method signatures in Java skeleton format

    Return format when not found:
        "// Class {class_name} not found"

    Example output:
        "// UsuarioRepository
        package com.example.demo.repository;

        public interface UsuarioRepository {
            Usuario save(Usuario usuario);
            Optional<Usuario> findById(Long id);
        }
        "
    """
```

## Usage Example

```python
from core import JavaFileRetriever, DependencyResolver

# Initialize
retriever = JavaFileRetriever("/path/to/project")
resolver = DependencyResolver(retriever)

# Find a file
file_path = retriever.find_file_by_class_name("UsuarioService")
if not file_path:
    raise ValueError("Service not found")

# Parse it
parsed = retriever.parse_file(file_path)
print(f"Class: {parsed.name}")
print(f"Methods: {len(parsed.methods)}")

# Resolve dependencies
deps = resolver.resolve_dependencies("UsuarioService", max_depth=2)
print(f"Dependencies: {len(deps)}")

# Get method signatures for a dependency
repo_signatures = resolver.get_method_signatures("UsuarioRepository")
print(repo_signatures)
```

## Error Handling

| Method | Failure Behavior |
|--------|-----------------|
| `find_file_by_class_name` | Returns `None` |
| `parse_file` | Returns `None` (silent) |
| `get_method_signatures` | Returns `"// Class X not found"` string |
| `resolve_dependencies` | Returns empty list `[]` |
| `__init__` (JavaFileRetriever) | Raises `FileNotFoundError` |
