"""
LLM client for interfacing with multiple LLM APIs.
Supports: Anthropic, OpenAI, GLM (Zhipu AI), Gemini, OpenRouter, NVIDIA.
"""

from __future__ import annotations

import os
from dataclasses import dataclass

# Import adapters
from llm.adapters import (
    AnthropicAdapter,
    BaseLLMAdapter,
    GeminiAdapter,
    GLMAdapter,
    LLMAdapterConfig,
    NVIDIAAdapter,
    OpenAIAdapter,
    OpenRouterAdapter,
)


@dataclass
class LLMConfig:
    """Configuration for LLM API calls.

    This class provides backward compatibility while using the new adapter system.
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
        try:
            from config import get_config

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

            # Use provider_order from config if not set
            if not self.provider_order and llm_config.provider_order:
                self.provider_order = llm_config.provider_order

            # Set base_url for OpenRouter if not provided
            if not self.base_url and self.provider == "openrouter":
                self.base_url = llm_config.base_url or "https://openrouter.ai/api/v1"

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
    """Client for interacting with multiple LLM APIs using adapters."""

    # Default models for each provider
    DEFAULT_MODELS = {
        "anthropic": "claude-3-5-sonnet-20241022",
        "openai": "gpt-4-turbo",
        "glm": "glm-5-turbo",
        "zhipu": "glm-5-turbo",  # Alias
        "gemini": "gemini-2.0-flash",
        "google": "gemini-2.0-flash",  # Alias
        "openrouter": "anthropic/claude-3.5-sonnet",
        "nvidia": "meta/llama-3.3-70b-instruct",
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
        "glm-4.32b-0414-128k",
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

    # Provider to adapter mapping
    _ADAPTER_MAP = {
        "anthropic": AnthropicAdapter,
        "openai": OpenAIAdapter,
        "glm": GLMAdapter,
        "zhipu": GLMAdapter,  # Alias
        "gemini": GeminiAdapter,
        "google": GeminiAdapter,  # Alias
        "openrouter": OpenRouterAdapter,
        "nvidia": NVIDIAAdapter,
    }

    def __init__(self, config: LLMConfig | None = None):
        """
        Initialize LLM client.

        Args:
            config: LLMConfig object. If None, loads from environment.
        """
        if config is None:
            config = LLMConfig()

        self.config = config
        self.adapter = self._create_adapter(config)

    def _create_adapter(self, config: LLMConfig) -> BaseLLMAdapter:
        """Create the appropriate adapter for the provider.

        Args:
            config: LLM configuration.

        Returns:
            Configured adapter instance.
        """
        provider = config.provider.lower()
        adapter_class = self._ADAPTER_MAP.get(provider)

        if adapter_class is None:
            raise ValueError(f"Unsupported provider: {config.provider}")

        adapter_config = LLMAdapterConfig(
            api_key=config.api_key or "",
            model=config.model,
            base_url=config.base_url,
            max_tokens=config.max_tokens,
            temperature=config.temperature,
            top_p=config.top_p,
        )

        return adapter_class(adapter_config)

    def generate_test(self, code_under_test: str, dependency_context: str) -> str:
        """
        Generate a unit test for the given Java code.

        Args:
            code_under_test: The Java code to generate tests for
            dependency_context: Context about dependencies

        Returns:
            Generated test code
        """
        return self.adapter.generate_test(code_under_test, dependency_context)


def get_available_providers() -> list[str]:
    """Get list of available LLM providers.

    Returns:
        List of provider names.
    """
    return list(LLMClient._ADAPTER_MAP.keys())


def get_default_model(provider: str) -> str:
    """Get the default model for a provider.

    Args:
        provider: Provider name

    Returns:
        Default model name
    """
    return LLMClient.DEFAULT_MODELS.get(provider, "claude-3-5-sonnet-20241022")
