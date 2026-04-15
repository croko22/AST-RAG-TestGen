"""
LLM client for interfacing with multiple LLM APIs.
Supports: Anthropic, OpenAI, GLM (Zhipu AI), Gemini, OpenRouter, NVIDIA.
"""

from __future__ import annotations

import os
from dataclasses import dataclass
from typing import Any

from dotenv import load_dotenv

# Load environment variables
load_dotenv()

# Import configuration
try:
    from config import get_config

    CONFIG_AVAILABLE = True
except ImportError:
    CONFIG_AVAILABLE = False


@dataclass
class LLMConfig:
    """Configuration for LLM API calls.

    This class provides backward compatibility while using the new
    pydantic-settings configuration system when available.
    """

    provider: str = "anthropic"
    model: str = "claude-3-5-sonnet-20241022"
    api_key: str | None = None
    base_url: str | None = None  # For OpenRouter and other custom endpoints
    max_tokens: int = 4096
    temperature: float = 0.3
    top_p: float = 1.0
    # For OpenRouter specific settings
    provider_order: list[str] | None = None  # Fallback providers for OpenRouter

    def __post_init__(self):
        """Load API key from environment if not provided."""
        # Try to use pydantic-settings configuration if available
        if CONFIG_AVAILABLE and not self.api_key:
            try:
                config = get_config()
                llm_config = config.llm

                # Override with values from pydantic-settings if not explicitly set
                if self.provider == "anthropic" and not self.api_key:
                    self.api_key = llm_config.anthropic_api_key
                elif self.provider == "openai" and not self.api_key:
                    self.api_key = llm_config.openai_api_key
                elif self.provider in ("glm", "zhipu") and not self.api_key:
                    self.api_key = llm_config.glm_api_key
                elif self.provider in ("gemini", "google") and not self.api_key:
                    self.api_key = llm_config.gemini_api_key
                elif self.provider == "openrouter" and not self.api_key:
                    self.api_key = llm_config.openrouter_api_key
                elif self.provider == "nvidia" and not self.api_key:
                    self.api_key = llm_config.nvidia_api_key

                # Set base_url for OpenRouter if not provided
                if not self.base_url and self.provider == "openrouter":
                    self.base_url = llm_config.base_url or "https://openrouter.ai/api/v1"

                # Use provider_order from config if not set
                if not self.provider_order and llm_config.provider_order:
                    self.provider_order = llm_config.provider_order

                return
            except Exception:
                # Fall back to manual environment variable loading
                pass

        # Manual environment variable loading (fallback)
        if not self.api_key:
            key_map = {
                "anthropic": "ANTHROPIC_API_KEY",
                "openai": "OPENAI_API_KEY",
                "glm": "GLM_API_KEY",
                "zhipu": "GLM_API_KEY",  # Alias
                "gemini": "GEMINI_API_KEY",
                "google": "GEMINI_API_KEY",  # Alias
                "openrouter": "OPENROUTER_API_KEY",
                "nvidia": "NVIDIA_API_KEY",
            }
            self.api_key = os.getenv(key_map.get(self.provider.lower(), ""))

        if not self.base_url and self.provider == "openrouter":
            self.base_url = "https://openrouter.ai/api/v1"


class LLMClient:
    """Client for interacting with multiple LLM APIs."""

    # Default models for each provider
    DEFAULT_MODELS = {
        "anthropic": "claude-3-5-sonnet-20241022",
        "openai": "gpt-4-turbo",
        "glm": "glm-4-plus",
        "zhipu": "glm-4-plus",  # Alias
        "gemini": "gemini-2.0-flash",
        "google": "gemini-2.0-flash",  # Alias
        "openrouter": "anthropic/claude-3.5-sonnet",
        "nvidia": "meta/llama-3.1-405b-instruct",
    }

    # Available GLM models from Zhipu AI
    GLM_MODELS = [
        "glm-4-plus",
        "glm-4.6",
        "glm-4.6v-flashx",
        "glm-4.7",
        "glm-5-turbo",
        "glm-4.5",
        "glm-4.6v",
        "glm-4.7-flash",
        "glm-4.7-flashx",
        "glm-4.5v",
        "glm-4.6v-flash",
        "glm-4.5-air",
        "glm-4.5-airx",
        "glm-4.5-flash",
        "glm-4-32b-0414-128k",
    ]

    # Available Gemini models
    GEMINI_MODELS = [
        "gemini-2.5-pro",
        "gemini-2.5-flash",
        "gemini-2.0-pro",
        "gemini-2.0-flash",
        "gemini-1.5-pro",
        "gemini-1.5-flash",
        "gemini-1.0-pro",
    ]

    def __init__(self, config: LLMConfig | None = None):
        """
        Initialize LLM client.

        Args:
            config: LLMConfig object. If None, loads from environment.
        """
        self.config = config or LLMConfig()
        self._client: Any = None
        self._init_client()

    def _init_client(self):
        """Initialize the appropriate API client."""
        provider = self.config.provider.lower()

        if provider in ("anthropic", "claude"):
            self._init_anthropic()
        elif provider in ("openai", "gpt"):
            self._init_openai()
        elif provider in ("glm", "zhipu"):
            self._init_glm()
        elif provider in ("gemini", "google"):
            self._init_gemini()
        elif provider == "openrouter":
            self._init_openrouter()
        elif provider == "nvidia":
            self._init_nvidia()
        else:
            raise ValueError(f"Unsupported provider: {self.config.provider}")

    def _init_anthropic(self):
        """Initialize Anthropic client."""
        try:
            from anthropic import Anthropic

            self._client = Anthropic(api_key=self.config.api_key)
        except ImportError as exc:
            raise ImportError(
                "Anthropic SDK not installed. Install with: pip install anthropic"
            ) from exc

    def _init_openai(self):
        """Initialize OpenAI client."""
        try:
            from openai import OpenAI

            self._client = OpenAI(api_key=self.config.api_key)
        except ImportError as exc:
            raise ImportError("OpenAI SDK not installed. Install with: pip install openai") from exc

    def _init_glm(self):
        """Initialize GLM (Zhipu AI) client."""
        try:
            from zhipuai import ZhipuAI

            self._client = ZhipuAI(api_key=self.config.api_key)
        except ImportError as exc:
            raise ImportError(
                "ZhipuAI SDK not installed. Install with: pip install zhipuai"
            ) from exc

    def _init_gemini(self):
        """Initialize Gemini client."""
        try:
            import google.generativeai as genai

            genai.configure(api_key=self.config.api_key)
            # Gemini doesn't use a client instance the same way
            self._client = genai
        except ImportError as exc:
            raise ImportError(
                "Google Generative AI SDK not installed. Install with: pip install google-generativeai"
            ) from exc

    def _init_openrouter(self):
        """Initialize OpenRouter client (uses OpenAI SDK with custom base)."""
        try:
            from openai import OpenAI

            self._client = OpenAI(
                api_key=self.config.api_key,
                base_url=self.config.base_url or "https://openrouter.ai/api/v1",
                default_headers={
                    "HTTP-Referer": "https://github.com/ast-rag-testgen",
                    "X-Title": "AST-RAG TestGen",
                },
            )
        except ImportError as exc:
            raise ImportError(
                "OpenAI SDK not installed (required for OpenRouter). Install with: pip install openai"
            ) from exc

    def _init_nvidia(self):
        """Initialize NVIDIA NIM client (uses OpenAI-compatible API)."""
        try:
            from openai import OpenAI

            self._client = OpenAI(
                api_key=self.config.api_key,
                base_url="https://integrate.api.nvidia.com/v1",
            )
        except ImportError as exc:
            raise ImportError(
                "OpenAI SDK not installed (required for NVIDIA). Install with: pip install openai"
            ) from exc

    def _require_client(self) -> Any:
        if self._client is None:
            raise RuntimeError("LLM client is not initialized")
        return self._client

    def generate_test(
        self,
        code_under_test: str,
        dependency_context: str,
        system_prompt: str | None = None,
    ) -> str:
        """
        Generate unit test code for the given Java class.

        Args:
            code_under_test: The Java class code to test
            dependency_context: The extracted method signatures/context
            system_prompt: Optional system prompt override

        Returns:
            Generated test code as string
        """
        if system_prompt is None:
            system_prompt = self._build_system_prompt()

        user_prompt = self._build_user_prompt(code_under_test, dependency_context)

        provider = self.config.provider.lower()

        if provider in ("anthropic", "claude"):
            return self._call_anthropic(system_prompt, user_prompt)
        elif provider in ("openai", "gpt"):
            return self._call_openai(system_prompt, user_prompt)
        elif provider in ("glm", "zhipu"):
            return self._call_glm(system_prompt, user_prompt)
        elif provider in ("gemini", "google"):
            return self._call_gemini(system_prompt, user_prompt)
        elif provider == "openrouter":
            return self._call_openrouter(system_prompt, user_prompt)
        elif provider == "nvidia":
            return self._call_nvidia(system_prompt, user_prompt)
        else:
            raise ValueError(f"Unsupported provider: {self.config.provider}")

    def _build_system_prompt(self) -> str:
        """Build the default system prompt for test generation."""
        return """Eres un ingeniero de software Senior experto en Java y pruebas unitarias (JUnit 5 + Mockito).
Tu objetivo es generar pruebas unitarias que COMPILEN A LA PRIMERA, logren alta cobertura de ramas y validen la lógica de negocio."""

    def _build_user_prompt(self, code_under_test: str, dependency_context: str) -> str:
        """Build the user prompt with code and context."""
        return f"""A continuación, te proporciono la clase que debes probar (Code Under Test) y, de vital importancia, el CONTEXTO ESTÁTICO (dependencias) que nuestro motor AST ha recuperado del proyecto.

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
- Usa JUnit 4 (org.junit) para aserciones y ciclo de vida de tests.
- IMPORTACIONES CORRECTAS:
  * JUnit 4: import org.junit.Test; import org.junit.Before;
  * Aserciones: import static org.junit.Assert.*; (assertEquals, assertNotNull, assertTrue, assertFalse, assertNull)
- NO uses Mockito (@Mock, @InjectMocks, mock(), when(), verify()) a menos que el proyecto explícitamente lo tenga como dependencia.
- Para clases simples (Option, OptionGroup, etc.), crea instancias reales con "new Option(...)" en lugar de mockearlas.
- Usa objetos reales en lugar de mocks para clases de datos simples.
- NO uses aserciones genéricas como assertTrue(true). Verifica los valores de retorno exactos.
- Asegúrate de importar todas las clases necesarias.
- CRÍTICO: Reglas para instanciación:
  * Si la clase bajo prueba tiene dependencias complejas (servicios, repositorios), considera usar mocks si están disponibles.
  * Si la clase NO tiene dependencias o usa clases simples (POJOs), crea instancias reales con "new ClassName()".
  * Instánciala directamente en @Before: "options = new Options();"
  * Usa objetos reales en los tests: 'Option option = new Option("a", "alpha");'
"""

    def _call_anthropic(self, system_prompt: str, user_prompt: str) -> str:
        """Call the Anthropic API."""
        client = self._require_client()
        response = client.messages.create(
            model=self.config.model,
            max_tokens=self.config.max_tokens,
            temperature=self.config.temperature,
            system=system_prompt,
            messages=[
                {"role": "user", "content": user_prompt},
            ],
        )
        return str(response.content[0].text)

    def _call_openai(self, system_prompt: str, user_prompt: str) -> str:
        """Call the OpenAI API."""
        client = self._require_client()
        response = client.chat.completions.create(
            model=self.config.model,
            max_tokens=self.config.max_tokens,
            temperature=self.config.temperature,
            messages=[
                {"role": "system", "content": system_prompt},
                {"role": "user", "content": user_prompt},
            ],
        )
        return str(response.choices[0].message.content or "")

    def _call_glm(self, system_prompt: str, user_prompt: str) -> str:
        """Call the GLM (Zhipu AI) API."""
        client = self._require_client()
        try:
            response = client.chat.completions.create(
                model=self.config.model,
                max_tokens=self.config.max_tokens,
                temperature=self.config.temperature,
                top_p=self.config.top_p,
                messages=[
                    {"role": "system", "content": system_prompt},
                    {"role": "user", "content": user_prompt},
                ],
            )
            return str(response.choices[0].message.content or "")
        except Exception:
            # Try legacy API format
            return self._call_glm_legacy(system_prompt, user_prompt)

    def _call_glm_legacy(self, system_prompt: str, user_prompt: str) -> str:
        """Fallback to legacy GLM API format."""
        # Zhipu AI SDK uses the same chat.completions.create API
        # If we got here, the API call failed - re-raise
        raise

    def _call_gemini(self, system_prompt: str, user_prompt: str) -> str:
        """Call the Gemini API."""
        client = self._require_client()
        model = client.GenerativeModel(self.config.model)

        # Gemini uses a different prompt structure - combine system and user
        combined_prompt = f"{system_prompt}\n\n{user_prompt}"

        response = model.generate_content(
            combined_prompt,
            generation_config={
                "max_output_tokens": self.config.max_tokens,
                "temperature": self.config.temperature,
                "top_p": self.config.top_p,
            },
        )
        return str(response.text)

    def _call_openrouter(self, system_prompt: str, user_prompt: str) -> str:
        """Call the OpenRouter API (OpenAI-compatible)."""
        client = self._require_client()
        response = client.chat.completions.create(
            model=self.config.model,
            max_tokens=self.config.max_tokens,
            temperature=self.config.temperature,
            messages=[
                {"role": "system", "content": system_prompt},
                {"role": "user", "content": user_prompt},
            ],
            extra_headers={
                "HTTP-Referer": "https://github.com/ast-rag-testgen",
                "X-Title": "AST-RAG TestGen",
            }
            if not client.default_headers
            else None,
        )
        return str(response.choices[0].message.content or "")

    def _call_nvidia(self, system_prompt: str, user_prompt: str) -> str:
        """Call the NVIDIA NIM API (OpenAI-compatible)."""
        client = self._require_client()
        response = client.chat.completions.create(
            model=self.config.model,
            max_tokens=self.config.max_tokens,
            temperature=self.config.temperature,
            messages=[
                {"role": "system", "content": system_prompt},
                {"role": "user", "content": user_prompt},
            ],
        )
        return str(response.choices[0].message.content or "")


def get_available_providers() -> list[str]:
    """Return list of available LLM providers."""
    return list(LLMClient.DEFAULT_MODELS.keys())


def get_default_model(provider: str) -> str:
    """Return the default model for a given provider."""
    return LLMClient.DEFAULT_MODELS.get(provider.lower(), "unknown")


if __name__ == "__main__":
    # Example usage

    code = """
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;

    public UsuarioService(UsuarioRepository usuarioRepository, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
    }

    public Usuario crearUsuario(Usuario usuario) {
        Usuario saved = usuarioRepository.save(usuario);
        emailService.sendWelcomeEmail(usuario.getEmail());
        return saved;
    }
}
    """

    dependency_context = """
// UsuarioRepository
public interface UsuarioRepository {
    Usuario save(Usuario usuario);
    Optional<Usuario> findById(Long id);
}

// EmailService
public class EmailService {
    public void sendWelcomeEmail(String email);
}
    """

    # Test different providers
    providers = ["anthropic", "openai", "glm", "gemini", "openrouter"]

    print("Available providers:", get_available_providers())

    for provider in providers:
        print(f"\n{'=' * 60}")
        print(f"Testing provider: {provider}")
        print(f"Default model: {get_default_model(provider)}")
        print(f"{'=' * 60}")

        try:
            client = LLMClient(LLMConfig(provider=provider))
            test = client.generate_test(code, dependency_context)
            print(f"✅ Success with {provider}")
            print(f"\nGenerated test:\n{test[:500]}...")
        except Exception as e:
            print(f"❌ Error with {provider}: {e}")
