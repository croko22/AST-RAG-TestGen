# Core Data Types

These are the primary data structures used throughout the AST-RAG pipeline.

## ParsedJavaClass

The complete parsed representation of a Java source file.

```python
@dataclass
class ParsedJavaClass:
    """Represents a parsed Java class/interface with all its details."""

    name: str                    # Simple class name, e.g., "UsuarioService"
    package: Optional[str]        # Package, e.g., "com.example.demo.service"
    imports: List[str]            # Import statements
    dependencies: List[JavaDependency]  # Extracted dependencies
    methods: List[MethodSignature]      # Method signatures
    fields: List[Dict]           # Field declarations with type and name
    file_path: str               # Full path to source file
    content: str                 # Full file content as string
```

**Field format** (List[Dict]):
```python
[
    {"type": "UsuarioRepository", "name": "usuarioRepository"},
    {"type": "EmailService", "name": "emailService"},
]
```

## JavaDependency

Represents a dependency extracted from Java code.

```python
@dataclass
class JavaDependency:
    """Represents a dependency extracted from Java code."""

    name: str                    # Class/interface name, e.g., "Usuario"
    type: str                    # "class", "interface", "import", "field", "method"
    package: Optional[str]        # Package if known
    file_path: Optional[str]      # Resolved file path (populated by retriever)
```

**Type meanings**:
| Type | Source | Example |
|------|--------|---------|
| `import` | From import statement | `import com.example.Usuario` |
| `field` | From field type | `private UsuarioRepository repo;` |
| `class` | Class usage in code | `new Usuario()` |
| `interface` | Interface reference | `implements Serializable` |

## MethodSignature

Metadata for a method declaration.

```python
@dataclass
class MethodSignature:
    """Represents a method signature extracted from a Java class/interface."""

    visibility: str              # "public", "private", "protected", "package-private"
    return_type: str             # Return type, e.g., "Usuario", "void", "List<String>"
    name: str                   # Method name
    parameters: List[str]        # Parameter types, e.g., ["String", "Long"]
    is_static: bool = False     # Whether method is static
```

**Example**:
```python
MethodSignature(
    visibility="public",
    return_type="Usuario",
    name="crearUsuario",
    parameters=["Usuario"],
    is_static=False,
)
```

Represents:
```java
public Usuario crearUsuario(Usuario usuario) { ... }
```

## LLMConfig

Configuration for LLM API calls.

```python
@dataclass
class LLMConfig:
    """Configuration for LLM API calls."""

    provider: str = "anthropic"
    model: str = "claude-3-5-sonnet-20241022"
    api_key: Optional[str] = None       # Auto-loaded from env
    base_url: Optional[str] = None      # For custom endpoints
    max_tokens: int = 4096
    temperature: float = 0.3
    top_p: float = 1.0                 # Used by some providers
```

**Available providers**: `anthropic`, `openai`, `glm`, `gemini`, `openrouter`, `nvidia`

## Data Flow Example

```python
# 1. Parse file
parsed = ParsedJavaClass(
    name="UsuarioService",
    package="com.example.demo.service",
    imports=["com.example.demo.repository.UsuarioRepository", ...],
    dependencies=[
        JavaDependency(name="UsuarioRepository", type="import", ...),
        ...
    ],
    methods=[
        MethodSignature(
            visibility="public",
            return_type="Usuario",
            name="crearUsuario",
            parameters=["Usuario"],
        ),
        ...
    ],
    fields=[
        {"type": "UsuarioRepository", "name": "usuarioRepository"},
        ...
    ],
    file_path="/path/to/UsuarioService.java",
    content="@Service\npublic class UsuarioService {...}",
)

# 2. Resolve dependencies
deps = resolver.resolve_dependencies("UsuarioService", max_depth=2)
# Returns List[ParsedJavaClass] for dependencies

# 3. Get method signatures for context
signatures = resolver.get_method_signatures("UsuarioRepository")
# Returns formatted string with skeleton methods

# 4. Build prompt
code, context = builder.build_prompt("UsuarioService.java", max_dependencies=10)

# 5. Generate test
config = LLMConfig(provider="anthropic", model="claude-3-5-sonnet-20241022")
client = LLMClient(config)
test = client.generate_test(code, context)
```

## Validation Rules

| Type | Validation |
|------|------------|
| `ParsedJavaClass.name` | Must not be empty (returns "Unknown" if parsing fails) |
| `MethodSignature.visibility` | One of: "public", "private", "protected", "package-private" |
| `JavaDependency.type` | One of: "class", "interface", "import", "field", "method" |
| `LLMConfig.provider` | Must be in available providers list |
| `LLMConfig.temperature` | Range: 0.0 to 2.0 |
