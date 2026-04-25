# LLM Client - Multi-provider Interface

## Purpose

Provide a unified interface to multiple LLM providers for test generation.

## Supported Providers

| Provider | SDK | Default Model | API Style |
|----------|-----|---------------|-----------|
| Anthropic | `anthropic` | `claude-3-5-sonnet-20241022` | Native |
| OpenAI | `openai` | `gpt-4-turbo` | Native |
| GLM (Zhipu AI) | `zhipuai` | `glm-5-turbo` | OpenAI-compatible |
| Gemini | `google-generativeai` | `gemini-2.0-flash` | Native (different) |
| OpenRouter | `openai` (custom base) | `anthropic/claude-3.5-sonnet` | OpenAI-compatible |
| NVIDIA NIM | `openai` (custom base) | `meta/llama-3.3-70b-instruct` | OpenAI-compatible |

> **Note:** The previous NVIDIA default `meta/llama-3.1-405b-instruct` is deprecated and slow. The current default is `meta/llama-3.3-70b-instruct`.

## Configuration

```python
@dataclass
class LLMConfig:
    provider: str = "anthropic"
    model: str = "claude-3-5-sonnet-20241022"
    api_key: Optional[str] = None      # Auto-loaded from env
    base_url: Optional[str] = None     # For OpenRouter/NVIDIA
    max_tokens: int = 4096
    temperature: float = 0.3
    top_p: float = 1.0                 # Used by GLM, Gemini
```

**API Key Loading**: Automatically loads from environment variables:
- `ANTHROPIC_API_KEY`, `OPENAI_API_KEY`, `GLM_API_KEY`, `GEMINI_API_KEY`, `NVIDIA_API_KEY`, `OPENROUTER_API_KEY`

## Main Interface

```python
class LLMClient:
    def __init__(self, config: Optional[LLMConfig] = None)
    def generate_test(
        self,
        code_under_test: str,
        dependency_context: str,
        system_prompt: Optional[str] = None,
    ) -> str
```

## Provider-Specific Details

### Anthropic
```python
response = self._client.messages.create(
    model=model,
    max_tokens=max_tokens,
    temperature=temperature,
    system=system_prompt,  # Separate field
    messages=[{"role": "user", "content": user_prompt}],
)
return response.content[0].text
```

### OpenAI / OpenRouter / NVIDIA
```python
response = self._client.chat.completions.create(
    model=model,
    max_tokens=max_tokens,
    temperature=temperature,
    messages=[
        {"role": "system", "content": system_prompt},
        {"role": "user", "content": user_prompt},
    ],
)
return response.choices[0].message.content
```

**OpenRouter Specifics**:
- `base_url`: `https://openrouter.ai/api/v1`
- Custom headers: `HTTP-Referer`, `X-Title`

**NVIDIA Specifics**:
- `base_url`: `https://integrate.api.nvidia.com/v1`

### GLM (Zhipu AI)
```python
response = self._client.chat.completions.create(
    model=model,
    max_tokens=max_tokens,
    temperature=temperature,
    top_p=top_p,  # GLM supports this
    messages=[...],
)
```

### Gemini
```python
model = self._client.GenerativeModel(model)
combined_prompt = f"{system_prompt}\n\n{user_prompt}"  # No separate system field

response = model.generate_content(
    combined_prompt,
    generation_config={
        "max_output_tokens": max_tokens,
        "temperature": temperature,
        "top_p": top_p,
    },
)
return response.text
```

## Prompts

**System Prompt (Spanish)**:
```
Eres un ingeniero de software Senior experto en Java y pruebas unitarias (JUnit 5 + Mockito).
Tu objetivo es generar pruebas unitarias que COMPILEN A LA PRIMERA, logren alta cobertura de ramas y validen la lógica de negocio.
```

**User Prompt**:
```
A continuación, te proporciono la clase que debes probar (Code Under Test) y, de vital importancia, el CONTEXTO ESTÁTICO (dependencias) que nuestro motor AST ha recuperado del proyecto.

=========================================
1. CLASE A PROBAR (Code Under Test):
=========================================
{code_under_test}

=========================================
2. CONTEXTO RECUPERADO (RAG - Firmas de Dependencias):
=========================================
Para evitar alucinaciones, utiliza ÚNICAMENTE estos métodos y firmas cuando necesites hacer mocks o instanciar objetos. NO inventes métodos que no estén aquí:

{dependency_context}

=========================================
3. REGLAS ESTRICTAS DE GENERACIÓN:
=========================================
- Escribe ÚNICAMENTE el código Java de la clase de prueba. Nada de explicaciones, ni Markdown extra.
- Usa JUnit 5 (org.junit.jupiter.api) y Mockito (org.mockito).
- Haz mock de TODAS las dependencias inyectadas en la clase principal usando @Mock y @InjectMocks.
- NO uses aserciones genéricas como assertTrue(true). Verifica los valores de retorno exactos o las interacciones con los mocks (ej. verify(repo, times(1)).save(any())).
- Asegúrate de importar todas las clases necesarias.
```

## Usage

```python
from llm import LLMClient, LLMConfig

config = LLMConfig(
    provider="nvidia",
    model="meta/llama-3.3-70b-instruct",
    temperature=0.3,
)
client = LLMClient(config)

test = client.generate_test(code_under_test, dependency_context)
print(test)
```

## CLI Integration

```bash
# Default (Anthropic)
python main.py service.java project/

# OpenAI
python main.py service.java project/ --provider openai --model gpt-4-turbo

# GLM
python main.py service.java project/ --provider glm --model glm-5-turbo

# NVIDIA
python main.py service.java project/ --provider nvidia --model meta/llama-3.3-70b-instruct

# OpenRouter
python main.py service.java project/ --provider openrouter --model anthropic/claude-3.5-sonnet
```
