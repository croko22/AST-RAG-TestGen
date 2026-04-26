# Model Comparison Benchmark

**Date**: 2026-04-26
**Dataset**: mock-java-project (UsuarioService.java)
**Provider**: NVIDIA NIM

## Results

| Model | Gen Time | Tests | Assertions | Compile | Tests Pass | Notes |
|-------|----------|-------|------------|---------|------------|-------|
| meta/llama-3.3-70b-instruct | 99s | 13 | 31 | ❌ | N/A | Missing JUnit deps |
| **qwen/qwen2.5-coder-32b-instruct** | **57s** | 13 | **62** | ✅ | **13/13** | **Best balance** |
| deepseek-ai/deepseek-v4-pro | 696s | 22 | 108 | ❌ | N/A | Too slow, 502 error |

## Conclusion

**Qwen 2.5 Coder 32B** is the recommended model for Java test generation:
- **42% faster** than Llama 3.3 70B
- **2x more assertions** per test
- **100% compilation and test pass rate** (after fixing pom.xml)
- Optimized for code generation tasks

## Fixes Applied

- Added `mock-java-project/pom.xml` with Spring Boot + JUnit Jupiter + Mockito
- Added missing `import java.math.BigDecimal` to ProductoRepository

## Next Steps

- Run full benchmark on RefTest 12 dataset with Qwen Coder
- Evaluate RAG vs RAG+feedback-loop with PIT mutation testing
