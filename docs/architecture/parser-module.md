# Parser Module - Tree-sitter AST Extraction

## Purpose

Parse Java files using Tree-sitter to extract structured information without compilation.

## Implementation

`core/parser.py` uses `tree-sitter` and `tree-sitter-java` packages.

### Key Classes

```python
@dataclass
class ParsedJavaClass:
    name: str
    package: Optional[str]
    imports: List[str]
    dependencies: List[JavaDependency]
    methods: List[MethodSignature]
    fields: List[Dict]
    file_path: str
    content: str           # Full file content
```

### Extraction Process

1. **Package**: Extract from `package_declaration` node
2. **Imports**: Extract from `import_declaration` nodes (handles `.*` wildcards)
3. **Class Name**: Extract from first `class_declaration` or `interface_declaration`
4. **Methods**: Extract from `method_declaration` nodes with visibility, return type, parameters, static flag
5. **Fields**: Extract from `field_declaration` nodes with type and name
6. **Dependencies**: Build from imports + field types, excluding `java.*`, `javax.*`, `org.springframework`

### Method Signature Format

```python
@dataclass
class MethodSignature:
    visibility: str      # "public", "private", "protected", "package-private"
    return_type: str
    name: str
    parameters: List[str]
    is_static: bool = False
```

### Dependency Types

- `"import"`: From import statements
- `"field"`: Field type dependencies
- `"class"` / `"interface"`: Used in retriever resolution

## Why Tree-sitter?

| Aspect | Tree-sitter | JavaParser/javac |
|--------|-------------|-----------------|
| Compilation | Not required | Required |
| Partial files | Works | Fails |
| Pure Python | Yes | No JNI/shelling out |
| Accuracy | Grammar-based | Full compiler |
| Speed | Fast | Slower |

## Usage

```python
from core import extract_dependencies_from_file

parsed = extract_dependencies_from_file("MyService.java")
print(f"Class: {parsed.name}")
print(f"Methods: {len(parsed.methods)}")
```

## Gotchas

- Standard library imports (`java.*`, `javax.*`, `org.springframework`) are filtered out
- Wildcard imports (`import com.example.*`) cannot resolve specific classes
- Empty files return class name "Unknown"
