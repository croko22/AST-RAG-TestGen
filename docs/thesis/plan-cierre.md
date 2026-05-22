# Plan de Cierre — Tesis AST-RAG TestGen

> **Objetivo**: Entregar tesis funcional con resultados reales (NVIDIA) + comparación literaria.
> **Timeline**: 5-7 días hábiles.
> **Estrategia**: Un solo proveedor evaluado experimentalmente. Comparación contra literatura para multi-proveedor.

---

## Fase 1 — Benchmark Real (NVIDIA) → 1 día

**Qué**: Correr campaña con el modelo que funciona (`meta/llama-3.3-70b-instruct`).

**Target**: `commons-cli` (14 archivos fuente, dataset RefTest-12).

**Pipeline**:
```
1. python main.py benchmark campaigns/nvidia-405b/manifest-commons-cli.json --output ./results/nvidia-commons-cli
2. Extraer: success_rate, avg_latency, test_count, assertion_count
3. Generar tabla resumen
```

**Output**: `results/nvidia-commons-cli/summary.json` → métricas base.

---

## Fase 2 — Métricas Base → 1 día

Extraer del benchmark:

| Métrica | Cómo se obtiene |
|---------|----------------|
| Tasa de compilación | `compile_pass / total_runs` |
| Tasa de ejecución | `test_pass / total_runs` |
| Latencia promedio | `avg(generation_time_ms)` |
| Tests generados | `sum(test_count)` |
| Aserciones generadas | `sum(assertion_count)` |
| Calidad | `avg(quality_score)` |

---

## Fase 3 — Comparación Literaria → 2 días

Buscar papers de generación de tests con LLMs que reporten métricas comparables.

### Papers target

| Paper | Modelo | Métricas reportadas |
|-------|--------|-------------------|
| Schäfer et al. (2023) "An Empirical Evaluation of Using LLMs for Unit Test Generation" | GPT-3.5, GPT-4 | Compilation rate, coverage |
| Yuan et al. (2024) "No More Unit Tests? On the Evaluation of LLMs in Code Generation" | GPT-4, CodeLlama | Compilation rate |
| Ouedraogo et al. (2024) "Test Smells in LLM-Generated Unit Tests" | GPT-3.5, StarCoder | Test smells density |
| Jain et al. (2025) "TestGenEval: A Real World Unit Test Generation" | GPT-4o, Claude 3 | Pass rate, coverage |
| Pan et al. (2025) "ASTER: Static Analysis Guided LLM for Test Generation" | GPT-4 | Compilation rate |
| Yin et al. (2025) "Enhancing Unit Test Generation with LLMs via Project Context" | GPT-4 | Compilation, coverage |

### Tabla comparativa

```
| Paper | Enfoque | Modelo | Compilación | Cobertura | Dataset |
|-------|---------|--------|-------------|-----------|---------|
| Schäfer et al. | Zero-shot | GPT-4 | 62% | 35% | Java |
| Yuan et al. | Zero-shot | GPT-4 | 70% | — | Java |
| Pan et al. (ASTER) | Static analysis | GPT-4 | 88% | 52% | Java |
| Yin et al. | RAG context | GPT-4 | 85% | — | Java |
| **AST-RAG TestGen** | **AST context** | **Llama 3.3 70B** | **X%** | **Y%** | **Spring Boot** |
```

---

## Fase 4 — Actualizar Capítulo 5 (Resultados) → 1 día

Insertar en `pfc3-tesis-tex/Cap5.tex`:

1. **5.3 Resultados Experimentales** (nuevo)
   - Campaña NVIDIA sobre commons-cli
   - Tabla de resultados por archivo
   - Análisis de latencia

2. **5.4 Comparación con la Literatura** (nuevo)
   - Tabla comparativa contra papers
   - Discusión de diferencias

3. **5.5 Limitaciones** (actualizar)
   - Evaluación con un solo proveedor
   - Justificación: limitaciones presupuestales

---

## Fase 5 — Compilar PDF → 1 día

```bash
make pdf   # pdflatex + bibtex + pdflatex + pdflatex
```

Problema conocido: `minted` necesita `-shell-escape`.
Fix: `make pdf` ya incluye `-shell-escape` en el Makefile.

Si hay errores de compilación:
```
grep "^!" Tesis.log | head -5   # buscar errores fatales
grep "undefined" Tesis.log      # citas sin definir
```

---

## Fase 6 — Slides de Defensa → 1 día

Estructura de la presentación (15-20 min):

| Slide | Contenido |
|-------|-----------|
| 1 | Título + autor |
| 2 | Problema: falta de contexto en LLMs para test generation |
| 3 | Propuesta: AST-RAG TestGen pipeline |
| 4 | Arquitectura: 4 etapas |
| 5 | Implementación: Tree-sitter, resolución de dependencias |
| 6 | Resultados: tabla de métricas |
| 7 | Comparación literaria: tabla vs otros papers |
| 8 | Conclusiones y trabajo futuro |

---

## Checklist Final

- [ ] Benchmark NVIDIA corrido con resultados
- [ ] Tablas de métricas extraídas
- [ ] Comparación literaria redactada
- [ ] Capítulo 5 actualizado
- [ ] PDF compila sin errores
- [ ] Slides listos
- [ ] Ensayo de presentación

---

## Archivos Involucrados

```
AST-RAG-TestGen/
├── results/nvidia-commons-cli/          # Resultados benchmark
├── docs/thesis/plan-cierre.md           # Este plan
└── ...

pfc3-tesis-tex/
├── Cap5.tex                              # Actualizar resultados
├── Cap6.tex                              # Añadir trabajo futuro
├── biblio.bib                            # Añadir referencias (si faltan)
├── Tesis.pdf                             # Compilar
└── slides/Presentacion.tex              # Slides de defensa

tesis-paper/
└── elsarticle-template-harv.tex          # Paper (si aplica)
```
