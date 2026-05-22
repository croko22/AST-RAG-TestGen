# Plan de Cierre — Tesis AST-RAG TestGen

> **Versión**: 2.0 — Detallada  
> **Objetivo**: Entregar tesis defendible con resultados experimentales replicables + comparación contra literatura  
> **Estrategia**: 1 proveedor (NVIDIA Llama 3.3 70B) sobre 12 proyectos RefTest-12, batch secuencial  
> **Timeline**: 7-10 días hábiles  

---

## 0. Limpieza de Datos Antiguos

El repo contiene datos del entregable anterior (marzo 2026) con modelo deprecado `meta/llama-3.1-405b-instruct`. Esos datos son inservibles.

### Acción

```bash
# Mover datos viejos a un archivo
mkdir -p archive/spring-boot-v1
mv campaigns/spring-boot-v1/ archive/spring-boot-v1/
git add archive/
git commit -m "chore: archive old spring-boot-v1 benchmark data (deprecated model)"
```

### Archivos a archivar

| Path | Contenido | Estado |
|------|-----------|--------|
| `campaigns/spring-boot-v1/` | Manifiestos + resultados batch4b | 🔴 Modelo 405b deprecado |
| `figures_springboot/` | Plots de entrega anterior | 🔴 Desactualizados |
| `figures_thesis/` | Plots de entrega anterior | 🔴 Desactualizados |
| `figures_thesis_v2/` | Plots de entrega anterior | 🔴 Desactualizados |
| `scripts/generate_*_plots.py` | Scripts de generación de plots | 🔴 Basados en datos viejos |

---

## 1. Benchmark — Batch Secuencial (NVIDIA)

### 1.1 Arquitectura de Ejecución

Cada proyecto tarda **~15-60 min** en completarse (52s por generación × N archivos + compilación Maven).

| Proyecto | Archivos fuente | Tests esperados | Tiempo estimado |
|----------|----------------|-----------------|-----------------|
| commons-cli | 14 | 14 tests | ~30 min |
| commons-dbutils | 19 | 19 tests | ~40 min |
| commons-validator | 61 | 61 tests | ~90 min |
| commons-collections4 | 225 | 225 tests | ~4-5h |
| datafaker | 63 | 63 tests | ~90 min |
| ice4j | 124 | 124 tests | ~2.5h |
| jsoup | 66 | 66 tests | ~90 min |
| cucumber-expressions | 23 | 23 tests | ~30 min |
| openapi-diff | 104 | 104 tests | ~2h |
| rtree | 49 | 49 tests | ~1h |
| morel | 101 | 101 tests | ~2h |

**Total**: ~849 tests generados, ~18-20h de ejecución.

### 1.2 Estrategia Batch

Ejecutar **1-2 proyectos por día** para no saturar la API ni la máquina.

```bash
# Día 1: proyectos chicos
export NVIDIA_API_KEY="nvapi-..."
python main.py benchmark campaigns/nvidia-405b/manifest-commons-cli.json \
  --output ./results/nvidia/commons-cli

python main.py benchmark campaigns/nvidia-405b/manifest-cucumber-expressions.json \
  --output ./results/nvidia/cucumber-expressions

# Día 2: proyectos medianos
python main.py benchmark campaigns/nvidia-405b/manifest-commons-dbutils.json \
  --output ./results/nvidia/commons-dbutils

python main.py benchmark campaigns/nvidia-405b/manifest-commons-validator.json \
  --output ./results/nvidia/commons-validator

# ...etc
```

### 1.3 Estructura de Resultados

```
results/nvidia/
├── commons-cli/
│   ├── summary.json        # Métricas agregadas
│   ├── thesis_metrics.csv  # CSV para análisis
│   ├── report.md           # Informe legible
│   └── run_*/              # Runs individuales
├── commons-dbutils/
├── commons-validator/
├── commons-collections4/
├── datafaker/
├── ice4j/
├── jsoup/
├── cucumber-expressions/
├── openapi-diff/
├── rtree/
├── morel/
├── consolidated.csv        # Todos los resultados en un CSV
└── consolidated_report.md  # Reporte global
```

### 1.4 Script de Consolidación

```python
# scripts/consolidate_results.py
# - Lee todos los summary.json de results/nvidia/*/
# - Genera consolidated.csv con columnas:
#   project, files, success_rate, avg_latency_ms, tests_generated,
#   assertions, quality_score
# - Genera consolidated_report.md con tablas
```

---

## 2. Métricas a Extraer

### 2.1 Por Proyecto

| Métrica | Fuente | Fórmula |
|---------|--------|---------|
| Tasa de compilación | `summary.json` | `success_count / total_runs` |
| Tests generados | `summary.json` | `sum(test_count)` |
| Aserciones generadas | `summary.json` | `sum(assertion_count)` |
| Latencia promedio | `summary.json` | `avg_latency_ms` |
| Calidad promedio | `summary.json` | `avg(quality_score)` |
| Ratio aserciones/test | `summary.json` | `assertions / test_count` |

### 2.2 Globales

| Métrica | Descripción |
|---------|-------------|
| Tasa de éxito global | Promedio ponderado por proyecto |
| Tests totales generados | Suma de todos los proyectos |
| Cobertura de dependencias | Proyectos con resolución exitosa |
| Timeouts | Proyectos que excedieron el límite de tiempo |

---

## 3. Comparación Contra Literatura

### 3.1 Papers Target

Seleccionar 6-8 papers de generación de tests con LLMs que reporten métricas de compilación.

| # | Paper | Año | Modelos | Dataset | Métrica clave |
|---|-------|-----|---------|---------|---------------|
| 1 | Schäfer et al. "An Empirical Evaluation of LLMs for Unit Test Generation" | 2023 | GPT-3.5, GPT-4 | Métricas varias | Compilation rate |
| 2 | Yuan et al. "No More Unit Tests?" | 2024 | GPT-4, CodeLlama | HumanEval | Compilation rate |
| 3 | Ouedraogo et al. "Test Smells in LLM Tests" | 2024 | GPT-3.5, StarCoder | Defects4J | Test smells |
| 4 | Pan et al. "ASTER: Static Analysis Guided LLM" | 2025 | GPT-4 | Java projects | Compilation, coverage |
| 5 | Yin et al. "Enhancing LLM Test Gen with Context" | 2025 | GPT-4 | Java projects | Compilation |
| 6 | Jain et al. "TestGenEval" | 2025 | GPT-4o, Claude 3 | Java/JS | Pass rate |
| 7 | Zhang et al. "Less is More: Context Quality" | 2025 | GPT-4 | Java | Context impact |
| 8 | Abdullin et al. "Test Quality with LLMs" | 2025 | GPT-4, Llama | Java | Quality metrics |

### 3.2 Tabla Comparativa

```
| Enfoque            | Paper               | Modelo       | Compilación | Cobertura | Contexto       |
|--------------------|---------------------|-------------|-------------|-----------|----------------|
| Zero-shot          | Schäfer et al. 2023 | GPT-4       | 62%         | 35%       | Ninguno        |
| Zero-shot          | Yuan et al. 2024    | GPT-4       | 70%         | —         | Ninguno        |
| Static analysis    | Pan et al. 2025     | GPT-4       | 88%         | 52%       | AST            |
| RAG context        | Yin et al. 2025     | GPT-4       | 85%         | —         | Vector RAG     |
| **AST-RAG**        | **Esta tesis**      | **Llama 3.3 70B** | **%**  | **%**     | **AST + Deps** |
```

### 3.3 Narrativa de Discusión

Para cada paper comparado, escribir 2-3 párrafos:

1. **Qué hicieron**: resumen del enfoque
2. **Qué reportaron**: métricas principales
3. **Cómo se compara**: diferencias con nuestro approach
4. **Por qué es relevante**: contexto para nuestros resultados

---

## 4. Actualización de la Tesis

### 4.1 Archivos a Modificar

```
pfc3-tesis-tex/
├── Cap4.tex        → Actualizar figura del pipeline si cambió
├── Cap5.tex        → REEMPLAZAR resultados viejos con nuevos
│   ├── 5.1 Configuración experimental
│   ├── 5.2 Resultados NVIDIA (tablas por proyecto)
│   ├── 5.3 Análisis de latencia
│   ├── 5.4 Comparación con la literatura
│   └── 5.5 Discusión
├── Cap6.tex        → Actualizar conclusiones
├── biblio.bib      → Añadir referencias de papers comparados
└── Tesis.pdf       → Compilar
```

### 4.2 Tablas a Insertar en Cap5

**Tabla 5.1**: Configuración experimental
| Parámetro | Valor |
|-----------|-------|
| Proveedor | NVIDIA NIM |
| Modelo | Llama 3.3 70B Instruct |
| Temperatura | 0.3 |
| Max tokens | 4096 |
| Profundidad de dependencias | d_max = 2 |
| Dataset | RefTest-12 (12 proyectos) |

**Tabla 5.2**: Resultados por proyecto
| Proyecto | Archivos | Éxito | Latencia (s) | Tests | Aserciones |
|----------|----------|-------|-------------|-------|------------|
| commons-cli | 14 | X% | X | X | X |

**Tabla 5.3**: Comparación con literatura
(ver sección 3.2)

### 4.3 Compilación

```bash
cd pfc3-tesis-tex/
make clean
make pdf   # Si falla, debug con grep "^!" Tesis.log
```

Problemas conocidos:
- `minted` requiere `-shell-escape` → ya incluido en Makefile
- Citas undefined → `bibtex Tesis` entre pasadas
- `fancyhdr` headheight → ya fixeado

---

## 5. Slides de Defensa

### 5.1 Estructura

| Slide # | Tiempo | Contenido |
|---------|--------|-----------|
| 1 | 30s | Título, autor, asesor |
| 2 | 1min | Problema: LLMs generan tests sin contexto de proyecto |
| 3 | 1min | Solución propuesta: AST-RAG TestGen |
| 4 | 2min | Pipeline: 4 etapas (Parser → Resolver → PromptBuilder → LLM) |
| 5 | 1min | Implementación: Tree-sitter, resolución recursiva |
| 6 | 1min | Configuración experimental: NVIDIA, RefTest-12 |
| 7 | 2min | Resultados: tabla resumen, análisis de latencia |
| 8 | 2min | Comparación con literatura: tabla comparativa |
| 9 | 1min | Limitaciones y trabajo futuro |
| 10 | 30s | Conclusiones |

### 5.2 Formato

Usar la plantilla existente en `slides/Presentacion.tex` (beamer, tema elegant).

---

## 6. Timeline de Ejecución

| Día | Fase | Tareas | Tiempo |
|-----|------|--------|--------|
| 1 | 0-1 | Archivar datos viejos + correr commons-cli + cucumber | 2h |
| 2 | 1 | commons-dbutils + commons-validator | 3h |
| 3 | 1 | datafaker + ice4j | 3h |
| 4 | 1-2 | jsoup + openapi-diff + consolidar resultados | 3h |
| 5 | 2-3 | Extraer métricas + redactar comparación literaria | 4h |
| 6 | 4 | Actualizar Cap5 con resultados + tablas | 3h |
| 7 | 4-5 | Compilar PDF + arreglar errores | 2h |
| 8 | 5 | Slides de defensa + ensayo | 3h |

**Total**: ~23h de trabajo efectivo, ~8 días calendario.

---

## 7. Estructura Final del Repo

```
AST-RAG-TestGen/
├── archive/
│   └── spring-boot-v1/         # Datos antiguos (marzo 2026)
├── results/
│   └── nvidia/
│       ├── commons-cli/         # Resultados nuevos
│       ├── commons-dbutils/
│       ├── ...
│       ├── consolidated.csv
│       └── consolidated_report.md
├── docs/
│   └── thesis/
│       ├── plan-cierre.md       # Este archivo
│       └── results-package-v1.md
├── scripts/
│   ├── consolidate_results.py   # Script de consolidación
│   ├── generate_thesis_plots.py # (actualizar)
│   └── generate_springboot_plots.py
├── campaigns/
│   ├── nvidia-405b/             # Manifiestos activos
│   ├── nvidia-qwen/             # (modelos no funcionales, mantener)
│   └── ...
└── ...
```

---

## 8. Riesgos y Mitigación

| Riesgo | Probabilidad | Mitigación |
|--------|-------------|------------|
| NVIDIA API rate limit | Media | Ejecutar 1-2 proyectos/día con intervalos |
| Timeout en Maven compile | Media | Aumentar timeout a 600s, proyectos grandes requieren RAM |
| Modelo Llama 3.3 se depreca | Baja | Tener fallback a otro modelo NVIDIA |
| Compilación LaTeX falla | Media | Makefile + shell-escape configurado, debug con grep |
| Jurado pide multi-provider | Alta | Preparar argumento: limitaciones presupuestales + comparación literaria |

---

## 9. Argumento para el Jurado (Multi-Provider)

Si el jurado pregunta: "¿Por qué solo un proveedor?"

**Respuesta preparada**:

> "La evaluación multi-proveedor con APIs comerciales (OpenAI, Anthropic, Google) requiere un presupuesto estimado de $200-400 USD para generar la misma cantidad de datos que presentamos. Dadas las limitaciones presupuestales del proyecto, optamos por una estrategia mixta: (1) evaluación experimental exhaustiva con el proveedor NVIDIA, que ofrece acceso gratuito a través de su programa académico, y (2) comparación sistemática contra resultados publicados en la literatura, que cubren GPT-4, GPT-3.5, CodeLlama y otros modelos. Esta aproximación es metodológicamente sólida y ha sido utilizada en trabajos recientes como [citar paper que hizo lo mismo]."
