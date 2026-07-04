# Example Java Project

This is a minimal, self-contained Java project used for testing AST-RAG-TestGen on a clean clone — no external dependencies, no build tools, just pure JDK code.

## Structure

```
examples/
└── src/main/java/com/example/
    ├── Calculator.java          # Basic arithmetic (add, subtract, multiply, divide)
    ├── StringUtils.java         # String utilities (isPalindrome, reverse, countVowels)
    └── CalculatorService.java   # Service combining Calculator + StringUtils
```

## Quick Start

```bash
# From the repo root — generate a test for CalculatorService:
python main.py generate examples/src/main/java/com/example/CalculatorService.java examples/

# Print the generated test to stdout instead of writing a file:
python main.py generate examples/src/main/java/com/example/CalculatorService.java examples/ --print

# With RAG-enhanced context (optional):
python main.py generate examples/src/main/java/com/example/CalculatorService.java examples/ --rag-enabled --alpha 0.7
```

## Why This Example?

- **CalculatorService** depends on both **Calculator** and **StringUtils**, demonstrating the dependency resolver's ability to find related classes in the project.
- Each class has methods with different return types (`int`, `double`, `String`, `boolean`, `void`) and varying parameter lists — exercising the AST parser thoroughly.
- All code is pure JDK (no Spring, no Maven, no external jars) so it runs anywhere.