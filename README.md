# AST-RAG TestGen

Sistema de generación de pruebas unitarias para Java utilizando AST (Abstract Syntax Tree) y RAG (Retrieval-Augmented Generation).

## Arquitectura

El sistema es una tubería de 4 pasos:

1. **Extractor (Tree-sitter)**: Lee archivos Java y extrae dependencias reales
2. **RAG (Buscador)**: Busca en el proyecto Java los archivos de las dependencias
3. **Podador (Slicer)**: Extrae solo las firmas de los métodos (para no reventar el contexto)
4. **Ensamblador de Prompts**: Junta el código a testear + las firmas recuperadas y las envía al LLM

## Instalación

```bash
# Crear el entorno conda
conda env create -f environment.yml

# Activar el entorno
conda activate ast-rag-testgen

# Configurar las API Keys en .env
cp .env.template .env
# Editar .env con tus API keys
```

## Uso

```bash
# Generar test para un archivo Java específico
python main.py /path/to/MiClase.java /path/to/java/project

# Usar OpenAI
python main.py service.java project/ --provider openai --model gpt-4-turbo

# Usar GLM (Zhipu AI)
python main.py service.java project/ --provider glm --model glm-4-plus

# Usar Gemini
python main.py service.java project/ --provider gemini --model gemini-2.0-flash-exp

# Usar OpenRouter
python main.py service.java project/ --provider openrouter --model anthropic/claude-3.5-sonnet

# Especificar directorio de salida personalizado
python main.py service.java project/ --output ./mis_tests
```

## Proveedores LLM Disponibles

| Proveedor | SDK | Modelos por defecto |
|-----------|-----|-------------------|
| **Anthropic** | `anthropic` | `claude-3-5-sonnet-20241022` |
| **OpenAI** | `openai` | `gpt-4-turbo` |
| **GLM** (Zhipu AI) | `zhipuai` | `glm-4-plus` |
| **Gemini** (Google) | `google-generativeai` | `gemini-2.0-flash-exp` |
| **OpenRouter** | `openai` | `anthropic/claude-3.5-sonnet` |

## Estructura del Proyecto

```
AST-RAG-TestGen/
├── environment.yml          # Dependencias de conda
├── .env                     # API Keys (configurar)
├── core/
│   ├── parser.py            # Tree-sitter para extraer imports y clases
│   ├── retriever.py         # Buscador de archivos Java en el proyecto
│   └── prompt_builder.py    # Ensamblador del Master Prompt dinámico
├── llm/
│   └── client.py            # Cliente para OpenAI/Anthropic
├── main.py                  # Orquestador principal
└── tests_generados/         # Aquí se guardan los tests generados
```

## Dependencias Principales

- Python 3.11
- tree-sitter + tree-sitter-java (AST parsing)
- anthropic / openai (LLM API clients)
- pydantic (validación de datos)
- python-dotenv (configuración)
