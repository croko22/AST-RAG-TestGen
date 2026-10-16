"""
Anthropic/Claude adapter for LLM client.

This module provides the Anthropic adapter implementation.
"""

from __future__ import annotations

import inspect

try:
    import anthropic

    ANTHROPIC_AVAILABLE = True
except ImportError:
    ANTHROPIC_AVAILABLE = False

from llm.adapters.base import BaseLLMAdapter, LLMAdapterConfig


class AnthropicAdapter(BaseLLMAdapter):
    """Anthropic/Claude adapter for LLM client."""

    def __init__(self, config: LLMAdapterConfig):
        """Initialize the Anthropic adapter.

        Args:
            config: Adapter configuration.
        """
        super().__init__(config)
        if not ANTHROPIC_AVAILABLE:
            raise ImportError("anthropic package is not installed")

        self.client = anthropic.Anthropic(api_key=config.api_key)

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

        # anthropic SDK v1 removed temperature/top_p from messages.create()
        # (deprecated by the API; see anthropic-sdk-python MIGRATION.md).
        # Only send sampling params the installed signature actually accepts.
        params = inspect.signature(self.client.messages.create).parameters
        kwargs: dict = {
            "model": self.config.model,
            "max_tokens": self.config.max_tokens,
            "system": system_prompt,
            "messages": [
                {
                    "role": "user",
                    "content": user_prompt,
                }
            ],
        }
        if "temperature" in params:
            kwargs["temperature"] = self.config.temperature
        if "top_p" in params:
            kwargs["top_p"] = self.config.top_p

        message = self.client.messages.create(**kwargs)

        content = message.content[0].text if message.content else ""
        return content
