"""
NVIDIA adapter for LLM client.

This module provides the NVIDIA adapter implementation.
"""

from __future__ import annotations

try:
    import openai

    NVIDIA_AVAILABLE = True
except ImportError:
    NVIDIA_AVAILABLE = False

from llm.adapters.base import BaseLLMAdapter, LLMAdapterConfig


class NVIDIAAdapter(BaseLLMAdapter):
    """NVIDIA adapter for LLM client."""

    def __init__(self, config: LLMAdapterConfig):
        """Initialize the NVIDIA adapter.

        Args:
            config: Adapter configuration.
        """
        super().__init__(config)
        if not NVIDIA_AVAILABLE:
            raise ImportError("openai package is not installed")

        # NVIDIA uses OpenAI-compatible API
        base_url = config.base_url or "https://integrate.api.nvidia.com/v1"

        self.client = openai.OpenAI(
            api_key=config.api_key,
            base_url=base_url,
        )

    def generate_test(self, code_under_test: str, dependency_context: str) -> str:
        """Generate a unit test for the given code.

        Args:
            code_under_test: The Java code to generate tests for.
            dependency_context: Context about dependencies.

        Returns:
            Generated test code.
        """
        system_prompt = self.build_system_prompt()
        user_prompt = self.build_user_prompt(code_under_test, dependency_context)

        response = self.client.chat.completions.create(
            model=self.config.model,
            messages=[
                {
                    "role": "system",
                    "content": system_prompt,
                },
                {
                    "role": "user",
                    "content": user_prompt,
                },
            ],
            max_tokens=self.config.max_tokens,
            temperature=self.config.temperature,
            top_p=self.config.top_p,
        )

        content = response.choices[0].message.content
        if content is None:
            return ""
        return content
