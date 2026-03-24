# Decision: Depth-Limited Dependency Resolution

## Context

`DependencyResolver.resolve_dependencies()` recursively resolves class dependencies to provide context for test generation.

## The Problem

Java projects often have deep dependency graphs:

```
Service → Repository → Entity → DTO → Util → ...
           ↓
        TransactionManager → EntityManager → ...
```

Without limits, recursive resolution can:
- Explode exponentially (O(n^depth))
- Include irrelevant dependencies (3rd party libraries, utilities)
- Exceed token limits in the prompt

## Alternatives

### 1. Unlimited Depth
- **Pros**: Complete dependency graph
- **Cons**: Exponential growth, often includes irrelevant classes

### 2. Fixed Max Depth
- **Pros**: Predictable size, stops explosion
- **Cons**: May miss some context

### 3. Breadth-First with Count Limit
- **Pros**: Controlled number of classes
- **Cons**: May prioritize wrong dependencies

## Decision

**Use fixed max depth with default value of 2.**

## Implementation

```python
def resolve_dependencies(
    self, class_name: str, max_depth: int = 2
) -> List[ParsedJavaClass]:
    visited = set()
    result = []

    def _resolve(name: str, depth: int):
        if depth > max_depth or name in visited:
            return

        visited.add(name)
        # ... resolve and recurse

    _resolve(class_name, 0)
    return result
```

## Rationale for max_depth=2

| Depth | What's Included | Use Case |
|-------|-----------------|----------|
| 0 | Only the class under test | No context |
| 1 | Direct dependencies (imports, field types) | Basic mocking |
| **2** | Direct + one level of indirect dependencies | **Good balance** |
| 3+ | More indirect dependencies, noise increases | Diminishing returns |

**Why 2 is optimal**:
- Captures Repository interfaces (direct)
- Captures Entity classes (via Repository, depth 2)
- Captures common utilities used by dependencies
- Typically < 20 classes per service (fits in prompt)

## Cycle Detection

```python
visited = set()  # Prevent infinite recursion on circular deps
```

## Configuration

CLI option allows adjustment:
```bash
python main.py service.java project/ --max-deps 5  # Default: 10
```

Note: `max_dependencies` in PromptBuilder controls how many resolved deps to include, while `max_depth` controls how deep to resolve.

## Trade-offs

| Aspect | Impact |
|--------|--------|
| Missing deep dependencies | Rare - if a service needs a deeply nested class, it's usually via direct dependency |
| Circular dependencies | Handled by visited set |
| User control | `max_dependencies` provides additional knob |

## Future Considerations

Could enhance with:
- Relevance scoring (prioritize classes in same package)
- Type-based filtering (exclude DTOs if not needed)
- Usage frequency in class under test
