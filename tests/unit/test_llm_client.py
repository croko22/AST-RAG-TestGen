"""Unit tests for LLM client module."""

import pytest
from unittest.mock import Mock, patch
from llm import LLMClient, LLMConfig, get_available_providers, get_default_model


class TestLLMConfig:

    def test_default_values(self):
        """Test default configuration values."""
        config = LLMConfig()
        assert config.provider == "anthropic"
        assert config.model == "claude-3-5-sonnet-20241022"
        assert config.temperature == 0.3
        assert config.max_tokens == 4096
        assert config.top_p == 1.0

    def test_custom_values(self):
        """Test custom configuration."""
        config = LLMConfig(
            provider="openai",
            model="gpt-4",
            temperature=0.7
        )
        assert config.provider == "openai"
        assert config.model == "gpt-4"
        assert config.temperature == 0.7

    def test_custom_max_tokens_and_top_p(self):
        """Test custom token limit and top_p values."""
        config = LLMConfig(max_tokens=2048, top_p=0.9)
        assert config.max_tokens == 2048
        assert config.top_p == 0.9


class TestLLMClient:

    def test_init_anthropic_provider(self):
        """Test initialization with Anthropic provider."""
        mock_client = Mock()
        with patch('llm.client.LLMClient._init_anthropic') as mock_init:
            mock_init.side_effect = lambda: setattr(LLMClient, '_client_mock', mock_client)
            config = LLMConfig(provider="anthropic", api_key="test-key")
            client = LLMClient(config)
            mock_init.assert_called_once()

    def test_init_openai_provider(self):
        """Test initialization with OpenAI provider."""
        with patch('llm.client.LLMClient._init_openai') as mock_init:
            config = LLMConfig(provider="openai", api_key="test-key")
            client = LLMClient(config)
            mock_init.assert_called_once()

    def test_init_glm_provider(self):
        """Test initialization with GLM provider."""
        with patch('llm.client.LLMClient._init_glm') as mock_init:
            config = LLMConfig(provider="glm", api_key="test-key")
            client = LLMClient(config)
            mock_init.assert_called_once()

    def test_init_gemini_provider(self):
        """Test initialization with Gemini provider."""
        with patch('llm.client.LLMClient._init_gemini') as mock_init:
            config = LLMConfig(provider="gemini", api_key="test-key")
            client = LLMClient(config)
            mock_init.assert_called_once()

    def test_init_openrouter_provider(self):
        """Test initialization with OpenRouter provider."""
        with patch('llm.client.LLMClient._init_openrouter') as mock_init:
            config = LLMConfig(provider="openrouter", api_key="test-key")
            client = LLMClient(config)
            mock_init.assert_called_once()

    def test_init_nvidia_provider(self):
        """Test initialization with NVIDIA provider."""
        with patch('llm.client.LLMClient._init_nvidia') as mock_init:
            config = LLMConfig(provider="nvidia", api_key="test-key")
            client = LLMClient(config)
            mock_init.assert_called_once()

    def test_init_unsupported_provider(self):
        """Test that unsupported provider raises ValueError."""
        config = LLMConfig(provider="unsupported")
        with pytest.raises(ValueError, match="Unsupported provider"):
            LLMClient(config)

    def test_build_system_prompt(self):
        """Test system prompt generation."""
        with patch('llm.client.LLMClient._init_anthropic'):
            client = LLMClient(LLMConfig(provider="anthropic", api_key="test-key"))
            prompt = client._build_system_prompt()

            assert "ingeniero de software Senior" in prompt
            assert "JUnit 5" in prompt
            assert "Mockito" in prompt

    def test_build_user_prompt_structure(self):
        """Test user prompt has correct structure."""
        with patch('llm.client.LLMClient._init_anthropic'):
            client = LLMClient(LLMConfig(provider="anthropic", api_key="test-key"))
            code = "public class Test {}"
            context = "// Context"
            prompt = client._build_user_prompt(code, context)

            assert "CLASE A PROBAR" in prompt
            assert "CONTEXTO RECUPERADO" in prompt
            assert code in prompt
            assert context in prompt

    def test_build_user_prompt_rules(self):
        """Test that generation rules are in prompt."""
        with patch('llm.client.LLMClient._init_anthropic'):
            client = LLMClient(LLMConfig(provider="anthropic", api_key="test-key"))
            prompt = client._build_user_prompt("code", "context")

            assert "JUnit 5" in prompt
            assert "Mockito" in prompt
            assert "@Mock" in prompt
            assert "NO uses aserciones genéricas" in prompt

    @patch('llm.client.LLMClient._init_anthropic')
    def test_generate_test_uses_config(self, mock_init):
        """Test that generate_test uses config values."""
        mock_client = Mock()
        mock_response = Mock()
        mock_response.content = [Mock(text="public class TestServiceTest {}")]
        mock_client.messages.create.return_value = mock_response
        
        config = LLMConfig(
            provider="anthropic",
            model="test-model",
            temperature=0.5,
            max_tokens=1000,
            api_key="test-key"
        )
        client = LLMClient(config)
        client._client = mock_client
        
        result = client.generate_test("code", "context")

        mock_client.messages.create.assert_called_once()
        call_kwargs = mock_client.messages.create.call_args[1]
        assert call_kwargs["model"] == "test-model"
        assert call_kwargs["temperature"] == 0.5
        assert call_kwargs["max_tokens"] == 1000


class TestUtilityFunctions:

    def test_get_available_providers(self):
        """Test that all expected providers are available."""
        providers = get_available_providers()
        assert "anthropic" in providers
        assert "openai" in providers
        assert "glm" in providers
        assert "gemini" in providers
        assert "openrouter" in providers
        assert "nvidia" in providers

    def test_get_default_model(self):
        """Test default model for each provider."""
        assert get_default_model("anthropic") == "claude-3-5-sonnet-20241022"
        assert get_default_model("openai") == "gpt-4-turbo"
        assert get_default_model("glm") == "glm-4-plus"
        assert get_default_model("gemini") == "gemini-2.0-flash"
        assert get_default_model("openrouter") == "anthropic/claude-3.5-sonnet"
        assert get_default_model("nvidia") == "meta/llama-3.1-405b-instruct"

    def test_get_default_model_unknown_provider(self):
        """Test default model for unknown provider."""
        result = get_default_model("unknown")
        assert result == "unknown"

    def test_generate_test_openai_provider(self):
        """Test generate_test with OpenAI provider."""
        mock_client = Mock()
        mock_response = Mock()
        mock_response.choices = [Mock(message=Mock(content="public class Test {}"))]
        mock_client.chat.completions.create.return_value = mock_response

        with patch('llm.client.LLMClient._init_openai'):
            config = LLMConfig(provider="openai", model="gpt-4", api_key="test-key")
            client = LLMClient(config)
            client._client = mock_client
            result = client.generate_test("code", "context")
            assert result == "public class Test {}"

    def test_generate_test_glm_provider(self):
        """Test generate_test with GLM provider."""
        mock_client = Mock()
        mock_response = Mock()
        mock_response.choices = [Mock(message=Mock(content="test output"))]
        mock_client.chat.completions.create.return_value = mock_response

        with patch('llm.client.LLMClient._init_glm'):
            config = LLMConfig(provider="glm", api_key="test-key")
            client = LLMClient(config)
            client._client = mock_client
            result = client.generate_test("code", "context")
            assert result == "test output"

    def test_generate_test_gemini_provider(self):
        """Test generate_test with Gemini provider."""
        mock_response = Mock()
        mock_response.text = "gemini output"

        mock_model = Mock()
        mock_model.generate_content.return_value = mock_response

        mock_client = Mock()
        mock_client.GenerativeModel.return_value = mock_model

        with patch('llm.client.LLMClient._init_gemini'):
            config = LLMConfig(provider="gemini", api_key="test-key")
            client = LLMClient(config)
            client._client = mock_client
            result = client.generate_test("code", "context")
            assert result == "gemini output"

    def test_generate_test_nvidia_provider(self):
        """Test generate_test with NVIDIA provider."""
        mock_client = Mock()
        mock_response = Mock()
        mock_response.choices = [Mock(message=Mock(content="nvidia output"))]
        mock_client.chat.completions.create.return_value = mock_response

        with patch('llm.client.LLMClient._init_nvidia'):
            config = LLMConfig(provider="nvidia", api_key="test-key")
            client = LLMClient(config)
            client._client = mock_client
            result = client.generate_test("code", "context")
            assert result == "nvidia output"

    def test_config_base_url(self):
        """Test LLMConfig with custom base_url."""
        config = LLMConfig(
            provider="openrouter",
            api_key="test-key",
            base_url="https://custom.api/v1"
        )
        assert config.base_url == "https://custom.api/v1"

    def test_config_openrouter_default_url(self):
        """Test OpenRouter gets default base_url."""
        config = LLMConfig(provider="openrouter", api_key="test-key")
        assert config.base_url == "https://openrouter.ai/api/v1"
