"""
Configuration management using pydantic-settings.

This module provides type-safe configuration management with validation,
environment variable loading, and sensible defaults.
"""

from functools import lru_cache
from pathlib import Path
from typing import Literal

from pydantic import Field, field_validator
from pydantic_settings import BaseSettings, SettingsConfigDict


class LLMProviderConfig(BaseSettings):
    """Configuration for LLM providers."""

    # API Keys
    anthropic_api_key: str | None = Field(default=None, alias="ANTHROPIC_API_KEY")
    openai_api_key: str | None = Field(default=None, alias="OPENAI_API_KEY")
    glm_api_key: str | None = Field(default=None, alias="GLM_API_KEY")
    gemini_api_key: str | None = Field(default=None, alias="GEMINI_API_KEY")
    openrouter_api_key: str | None = Field(default=None, alias="OPENROUTER_API_KEY")
    nvidia_api_key: str | None = Field(default=None, alias="NVIDIA_API_KEY")

    # Provider selection
    provider: Literal[
        "anthropic",
        "openai",
        "glm",
        "zhipu",
        "gemini",
        "google",
        "openrouter",
        "nvidia",
    ] = Field(default="anthropic", alias="LLM_PROVIDER")

    # Model configuration
    model: str = Field(default="claude-3-5-sonnet-20241022", alias="LLM_MODEL")

    # Generation parameters
    max_tokens: int = Field(default=4096, ge=1, le=128000)
    temperature: float = Field(default=0.3, ge=0.0, le=2.0)
    top_p: float = Field(default=1.0, ge=0.0, le=1.0)

    # OpenRouter specific
    base_url: str | None = Field(default=None, alias="OPENROUTER_BASE_URL")
    provider_order: list[str] | None = Field(default=None, alias="OPENROUTER_PROVIDER_ORDER")

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
        env_ignore_empty=True,
    )

    @field_validator("provider_order", mode="before")
    @classmethod
    def parse_provider_order(cls, v: str | list[str] | None) -> list[str] | None:
        """Parse provider order from comma-separated string or list."""
        if v is None:
            return None
        if isinstance(v, str):
            return [p.strip() for p in v.split(",") if p.strip()]
        return v

    @field_validator("base_url")
    @classmethod
    def set_default_base_url(cls, v: str | None, info) -> str | None:
        """Set default base URL for OpenRouter if not provided."""
        if v is None and info.data.get("provider") == "openrouter":
            return "https://openrouter.ai/api/v1"
        return v

    def get_api_key(self) -> str | None:
        """Get the API key for the current provider."""
        key_map = {
            "anthropic": self.anthropic_api_key,
            "openai": self.openai_api_key,
            "glm": self.glm_api_key,
            "zhipu": self.glm_api_key,
            "gemini": self.gemini_api_key,
            "google": self.gemini_api_key,
            "openrouter": self.openrouter_api_key,
            "nvidia": self.nvidia_api_key,
        }
        return key_map.get(self.provider)

    def get_default_model(self) -> str:
        """Get the default model for the current provider."""
        default_models = {
            "anthropic": "claude-3-5-sonnet-20241022",
            "openai": "gpt-4-turbo",
            "glm": "glm-4-plus",
            "zhipu": "glm-4-plus",
            "gemini": "gemini-2.0-flash",
            "google": "gemini-2.0-flash",
            "openrouter": "anthropic/claude-3.5-sonnet",
            "nvidia": "meta/llama-3.1-405b-instruct",
        }
        return default_models.get(self.provider, self.model)


class JavaProjectConfig(BaseSettings):
    """Configuration for Java project settings."""

    project_path: Path = Field(default=Path("./"), alias="JAVA_PROJECT_PATH")
    test_output_dir: Path = Field(default=Path("./tests_generados"), alias="TEST_OUTPUT_DIR")
    max_dependencies: int = Field(default=10, ge=0, le=100, alias="MAX_DEPENDENCIES")

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
        env_ignore_empty=True,
    )

    @field_validator("project_path", "test_output_dir", mode="before")
    @classmethod
    def parse_path(cls, v: str | Path) -> Path:
        """Parse string path to Path object."""
        if isinstance(v, str):
            return Path(v).expanduser().resolve()
        return v.expanduser().resolve()


class BenchmarkConfig(BaseSettings):
    """Configuration for benchmark mode."""

    output_dir: Path = Field(default=Path("./benchmark_results"), alias="BENCHMARK_OUTPUT_DIR")
    dry_run: bool = Field(default=False, alias="BENCHMARK_DRY_RUN")
    timeout_seconds: int = Field(default=600, ge=1, le=3600, alias="BENCHMARK_TIMEOUT")

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
        env_ignore_empty=True,
    )

    @field_validator("output_dir", mode="before")
    @classmethod
    def parse_path(cls, v: str | Path) -> Path:
        """Parse string path to Path object."""
        if isinstance(v, str):
            return Path(v).expanduser().resolve()
        return v.expanduser().resolve()


class FeatureFlagsConfig(BaseSettings):
    """Configuration for feature flags."""

    enable_metainfo_db: bool = Field(default=False, alias="ENABLE_METAINFO_DB")
    enable_reftest_parity: bool = Field(default=False, alias="ENABLE_REFTEST_PARITY")
    enable_rich_output: bool = Field(default=True, alias="ENABLE_RICH_OUTPUT")
    enable_typer_cli: bool = Field(default=True, alias="ENABLE_TYPER_CLI")

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
        env_ignore_empty=True,
    )


class RAGConfig(BaseSettings):
    """Configuration for RAG retrieval pipeline."""

    enabled: bool = Field(default=False, alias="RAG_ENABLED")
    alpha: float = Field(default=0.5, ge=0.0, le=1.0, alias="RAG_ALPHA")
    embedding_provider: Literal["local", "api"] = Field(
        default="local", alias="RAG_EMBEDDING_PROVIDER"
    )
    embedding_model: str = Field(default="all-MiniLM-L6-v2", alias="RAG_EMBEDDING_MODEL")
    chroma_persist_dir: str = Field(default=".chroma_db", alias="RAG_CHROMA_PERSIST_DIR")
    chunk_strategy: Literal["method", "class", "file"] = Field(
        default="method", alias="RAG_CHUNK_STRATEGY"
    )
    chunk_max_tokens: int = Field(default=512, ge=64, le=8192, alias="RAG_CHUNK_MAX_TOKENS")
    retrieval_top_k: int = Field(default=10, ge=1, le=100, alias="RAG_RETRIEVAL_TOP_K")
    max_context_tokens: int = Field(default=4096, ge=256, le=32768, alias="RAG_MAX_CONTEXT_TOKENS")
    retrieval_strategy: Literal["ast", "rag", "hybrid"] = Field(
        default="hybrid", alias="RAG_RETRIEVAL_STRATEGY"
    )

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
        env_ignore_empty=True,
    )


class MCPServerConfig(BaseSettings):
    """Configuration for MCP server."""

    transport: Literal["stdio", "http"] = Field(default="stdio", alias="MCP_TRANSPORT")
    host: str = Field(default="127.0.0.1", alias="MCP_HOST")
    port: int = Field(default=8000, ge=1, le=65535, alias="MCP_PORT")
    debug: bool = Field(default=False, alias="MCP_DEBUG")

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
        env_ignore_empty=True,
    )


class PostProcConfig(BaseSettings):
    """Configuration for post-processing pipeline."""

    enabled: bool = Field(default=False, alias="POSTPROC_ENABLED")
    auto_compile: bool = Field(default=False, alias="POSTPROC_AUTO_COMPILE")
    auto_run: bool = Field(default=False, alias="POSTPROC_AUTO_RUN")
    coverage_tool: str = Field(default="jacoco", alias="POSTPROC_COVERAGE_TOOL")
    quality_threshold: float = Field(
        default=0.5, ge=0.0, le=1.0, alias="POSTPROC_QUALITY_THRESHOLD"
    )
    compile_cmd: str | None = Field(default=None, alias="POSTPROC_COMPILE_CMD")
    test_cmd: str | None = Field(default=None, alias="POSTPROC_TEST_CMD")
    coverage_cmd: str | None = Field(default=None, alias="POSTPROC_COVERAGE_CMD")

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
        env_ignore_empty=True,
    )


class AppConfig(BaseSettings):
    """Main application configuration combining all sub-configurations."""

    llm: LLMProviderConfig = Field(default_factory=LLMProviderConfig)
    java: JavaProjectConfig = Field(default_factory=JavaProjectConfig)
    benchmark: BenchmarkConfig = Field(default_factory=BenchmarkConfig)
    features: FeatureFlagsConfig = Field(default_factory=FeatureFlagsConfig)
    rag: RAGConfig = Field(default_factory=RAGConfig)
    mcp_server: MCPServerConfig = Field(default_factory=MCPServerConfig)
    postproc: PostProcConfig = Field(default_factory=PostProcConfig)

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
        env_ignore_empty=True,
    )

    @classmethod
    def load(cls) -> "AppConfig":
        """Load configuration from environment and .env file."""
        return cls()


@lru_cache
def get_config() -> AppConfig:
    """
    Get the application configuration (cached).

    This function is cached to avoid reloading configuration multiple times.
    Use this function to access configuration throughout the application.

    Returns:
        AppConfig: The application configuration.
    """
    return AppConfig.load()


def reload_config() -> AppConfig:
    """
    Reload the configuration (clears cache).

    Use this function when you need to reload configuration after
    changing environment variables or .env file.

    Returns:
        AppConfig: The reloaded application configuration.
    """
    get_config.cache_clear()
    return get_config()
