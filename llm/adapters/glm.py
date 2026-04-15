"""
GLM (Zhipu AI) adapter for LLM client.

This module provides the GLM adapter implementation.
"""

from __future__ import annotations

try:
    import zhipuai

    GLM_AVAILABLE = True
except ImportError:
    GLM_AVAILABLE = False

from llm.adapters.base import BaseLLMAdapter, LLMAdapterConfig


class GLMAdapter(BaseLLMAdapter):
    """GLM (Zhipu AI) adapter for LLM client."""

    def __init__(self, config: LLMAdapterConfig):
        """Initialize the GLM adapter.

        Args:
            config: Adapter configuration.
        """
        super().__init__(config)
        if not GLM_AVAILABLE:
            raise ImportError("zhipuai package is not installed")

        self.client = zhipuai.ZhipuAI(api_key=config.api_key)

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

        return response.choices[0].message.content

    def build_system_prompt(self) -> str:
        """Build the system prompt for the LLM.

        Returns:
            System prompt string.
        """
        return self._build_common_system_prompt()

    def build_user_prompt(self, code_under_test: str, dependency_context: str) -> str:
        """Build the user prompt for the LLM.

        Args:
            code_under_test: The Java code to generate tests for.
            dependency_context: Context about dependencies.

        Returns:
            User prompt string.
        """
        return f"""Generate comprehensive unit tests for the following Java class:

## Code Under Test
```java
{code_under_test}
```

## Dependency Context
{dependency_context}

Generate a complete test class with proper imports, setup, and test methods."""
