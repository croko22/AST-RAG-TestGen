"""Unit tests for pydantic-settings configuration."""

import os
from pathlib import Path
from unittest.mock import patch

import pytest

from config import (
    AppConfig,
    BenchmarkConfig,
    FeatureFlagsConfig,
    JavaProjectConfig,
    LLMProviderConfig,
    get_config,
    reload_config,
)


class TestLLMProviderConfig:
    """Test LLM provider configuration."""

    def test_default_values(self):
        """Test default configuration values."""
        # Clear environment to avoid loading from .env
        with patch.dict(os.environ, {}, clear=True):
            config = LLMProviderConfig()
            assert config.provider == "anthropic"
            assert config.model == "claude-3-5-sonnet-20241022"
            assert config.max_tokens == 4096
            assert config.temperature == 0.3
            assert config.top_p == 1.0

    def test_get_api_key_anthropic(self):
        """Test getting API key for Anthropic."""
        config = LLMProviderConfig(
            anthropic_api_key="test-key",
            provider="anthropic",
        )
        assert config.get_api_key() == "test-key"

    def test_get_api_key_openai(self):
        """Test getting API key for OpenAI."""
        config = LLMProviderConfig(
            openai_api_key="test-key",
            provider="openai",
        )
        assert config.get_api_key() == "test-key"

    def test_get_api_key_glm(self):
        """Test getting API key for GLM."""
        config = LLMProviderConfig(
            glm_api_key="test-key",
            provider="glm",
        )
        assert config.get_api_key() == "test-key"

    def test_get_api_key_gemini(self):
        """Test getting API key for Gemini."""
        config = LLMProviderConfig(
            gemini_api_key="test-key",
            provider="gemini",
        )
        assert config.get_api_key() == "test-key"

    def test_get_api_key_openrouter(self):
        """Test getting API key for OpenRouter."""
        config = LLMProviderConfig(
            openrouter_api_key="test-key",
            provider="openrouter",
        )
        assert config.get_api_key() == "test-key"

    def test_get_api_key_nvidia(self):
        """Test getting API key for NVIDIA."""
        config = LLMProviderConfig(
            nvidia_api_key="test-key",
            provider="nvidia",
        )
        assert config.get_api_key() == "test-key"

    def test_get_default_model(self):
        """Test getting default model for provider."""
        config = LLMProviderConfig(provider="openai")
        assert config.get_default_model() == "gpt-4-turbo"

    def test_base_url_for_openrouter(self):
        """Test base URL is set for OpenRouter."""
        config = LLMProviderConfig(provider="openrouter")
        assert config.base_url == "https://openrouter.ai/api/v1"

    def test_provider_order_parsing(self):
        """Test parsing provider order from string."""
        config = LLMProviderConfig(provider_order="anthropic,openai,glm")
        assert config.provider_order == ["anthropic", "openai", "glm"]

    def test_provider_order_list(self):
        """Test provider order as list."""
        config = LLMProviderConfig(provider_order=["anthropic", "openai"])
        assert config.provider_order == ["anthropic", "openai"]

    def test_validation_max_tokens(self):
        """Test max_tokens validation."""
        with pytest.raises(ValueError):
            LLMProviderConfig(max_tokens=0)
        with pytest.raises(ValueError):
            LLMProviderConfig(max_tokens=200000)

    def test_validation_temperature(self):
        """Test temperature validation."""
        with pytest.raises(ValueError):
            LLMProviderConfig(temperature=-0.1)
        with pytest.raises(ValueError):
            LLMProviderConfig(temperature=2.1)

    def test_validation_top_p(self):
        """Test top_p validation."""
        with pytest.raises(ValueError):
            LLMProviderConfig(top_p=-0.1)
        with pytest.raises(ValueError):
            LLMProviderConfig(top_p=1.1)


class TestJavaProjectConfig:
    """Test Java project configuration."""

    def test_default_values(self):
        """Test default configuration values."""
        with patch.dict(os.environ, {}, clear=True):
            config = JavaProjectConfig()
            assert config.project_path == Path("./").resolve()
            assert config.test_output_dir == Path("./tests_generados").resolve()
            assert config.max_dependencies == 10

    def test_path_parsing(self):
        """Test path parsing from string."""
        with patch.dict(os.environ, {}, clear=True):
            config = JavaProjectConfig(project_path="/tmp/test")
            assert config.project_path == Path("/tmp/test").resolve()

    def test_validation_max_dependencies(self):
        """Test max_dependencies validation."""
        with pytest.raises(ValueError):
            JavaProjectConfig(max_dependencies=-1)
        with pytest.raises(ValueError):
            JavaProjectConfig(max_dependencies=101)


class TestBenchmarkConfig:
    """Test benchmark configuration."""

    def test_default_values(self):
        """Test default configuration values."""
        with patch.dict(os.environ, {}, clear=True):
            config = BenchmarkConfig()
            assert config.output_dir == Path("./benchmark_results").resolve()
            assert config.dry_run is False
            assert config.timeout_seconds == 600

    def test_path_parsing(self):
        """Test path parsing from string."""
        with patch.dict(os.environ, {}, clear=True):
            config = BenchmarkConfig(output_dir="/tmp/results")
            assert config.output_dir == Path("/tmp/results").resolve()

    def test_validation_timeout(self):
        """Test timeout validation."""
        with pytest.raises(ValueError):
            BenchmarkConfig(timeout_seconds=0)
        with pytest.raises(ValueError):
            BenchmarkConfig(timeout_seconds=4000)


class TestFeatureFlagsConfig:
    """Test feature flags configuration."""

    def test_default_values(self):
        """Test default configuration values."""
        with patch.dict(os.environ, {}, clear=True):
            config = FeatureFlagsConfig()
            assert config.enable_metainfo_db is False
            assert config.enable_reftest_parity is False
            assert config.enable_rich_output is True
            assert config.enable_typer_cli is True


class TestAppConfig:
    """Test main application configuration."""

    def test_default_values(self):
        """Test default configuration values."""
        with patch.dict(os.environ, {}, clear=True):
            config = AppConfig()
            assert isinstance(config.llm, LLMProviderConfig)
            assert isinstance(config.java, JavaProjectConfig)
            assert isinstance(config.benchmark, BenchmarkConfig)
            assert isinstance(config.features, FeatureFlagsConfig)

    def test_load_from_environment(self):
        """Test loading configuration from environment."""
        with patch.dict(
            os.environ,
            {
                "LLM_PROVIDER": "openai",
                "LLM_MODEL": "gpt-4-turbo",
                "MAX_DEPENDENCIES": "20",
            },
            clear=True,
        ):
            config = AppConfig.load()
            assert config.llm.provider == "openai"
            assert config.llm.model == "gpt-4-turbo"
            assert config.java.max_dependencies == 20


class TestConfigCaching:
    """Test configuration caching."""

    def test_get_config_caching(self):
        """Test that get_config returns cached instance."""
        config1 = get_config()
        config2 = get_config()
        assert config1 is config2

    def test_reload_config(self):
        """Test that reload_config clears cache."""
        config1 = get_config()
        config2 = reload_config()
        assert config1 is not config2

    def test_reload_config_after_env_change(self):
        """Test that reload_config picks up environment changes."""
        config1 = get_config()
        original_provider = config1.llm.provider

        with patch.dict(os.environ, {"LLM_PROVIDER": "openai"}):
            config2 = reload_config()
            assert config2.llm.provider == "openai"
            assert config1.llm.provider == original_provider
