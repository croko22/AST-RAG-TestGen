Subject: Avance de Tesis - AST-RAG TestGen

Estimado profesor Sarmiento,

Les comparto el progreso de la tesis "RAG vs RL para Generación de Tests Unitarios en Java".

## Estado Actual

He reescrito completamente el pipeline (el código inicial tenía muchos problemas de arquitectura). El nuevo sistema incluye:

1. **Pipeline de 4 pasos**: Parsing (Tree-sitter) → Dependency Extraction → RAG retrieval → LLM generation
2. **MCP Server**: Integración con editor/IDE vía Model Context Protocol
3. **Post-processing**: Validación de compilación, ejecución de tests, análisis de coverage, quality scoring
4. **Feedback loop**: Retry automático en errores de compilación
5. **Benchmark mode**: Evaluación en proyectos Java reales

## Resultados de Benchmarks

| Proyecto | Tests | Assertions | Quality | Status |
|----------|-------|------------|---------|--------|
| mock-java-project | 13 | 13+ | 1.0 | ✅ 100% pass |
| ice4j | 4 | 2 | 1.0 | ✅ compila |
| jsoup | 27 | 45 | 1.0 | ⚠️ necesita imports |
| commons-dbutils | - | - | - | ⚠️ 33% compile |

El compile rate de 33% es comparable al 45.7% de RefTest (el estado del arte).

## Próximos Pasos
1. Ejecutar más benchmarks en proyectos RefTest-12
2. Analizar failure patterns para mejorar el prompt
3. Comparar retrieval RAG vs AST-only
4. Integrar mutation testing con PIT

El código está en: https://github.com/croko22/AST-RAG-TestGen

Adjunto el documento de avance (PDF).

¿Hay alguna recomendación o ajuste que sugieren?

Saludos,
Kevin Chambi