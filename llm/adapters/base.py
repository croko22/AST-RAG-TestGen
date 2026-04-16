"""
Base adapter interface for LLM providers.

This module defines the contract that all LLM provider adapters must implement.
"""

from __future__ import annotations

from abc import ABC, abstractmethod
from dataclasses import dataclass


@dataclass
class LLMAdapterConfig:
    """Configuration for an LLM adapter."""

    api_key: str
    model: str
    base_url: str | None = None
    max_tokens: int = 4096
    temperature: float = 0.3
    top_p: float = 1.0


class BaseLLMAdapter(ABC):
    """Base class for LLM provider adapters."""

    def __init__(self, config: LLMAdapterConfig):
        """Initialize the adapter.

        Args:
            config: Adapter configuration.
        """
        self.config = config

    @abstractmethod
    def generate_test(self, code_under_test: str, dependency_context: str) -> str:
        """Generate a unit test for the given code.

        Args:
            code_under_test: The Java code to generate tests for.
            dependency_context: Context about dependencies.

        Returns:
            Generated test code.
        """

    @abstractmethod
    def build_system_prompt(self) -> str:
        """Build the system prompt for the LLM.

        Returns:
            System prompt string.
        """

    @abstractmethod
    def build_user_prompt(self, code_under_test: str, dependency_context: str) -> str:
        """Build the user prompt for the LLM.

        Args:
            code_under_test: The Java code to generate tests for.
            dependency_context: Context about dependencies.

        Returns:
            User prompt string.
        """

    def _build_common_system_prompt(self) -> str:
        """Build common system prompt for all providers.

        Returns:
            Common system prompt string.
        """
        return """You are a Senior Java software engineer expert in unit testing (JUnit 5 + Mockito).
Your goal is to generate unit tests that COMPILE ON FIRST TRY, achieve high branch coverage, and validate business logic.

Follow these rules:
1. Use JUnit 5 framework (org.junit.jupiter.*)
2. Include necessary imports:
   - JUnit 5: import org.junit.jupiter.api.*; import static org.junit.jupiter.api.Assertions.*;
3. Create test methods for all public methods
4. Use @Test, @BeforeEach, @AfterEach annotations
5. Use Mockito for mocking dependencies: @ExtendWith(MockitoExtension.class), @Mock, @InjectMocks
6. Include assertions to verify expected behavior
7. Handle edge cases and error conditions
8. Add descriptive comments for complex logic

Generate only the test class code, no explanations or markdown formatting."""
