"""Unit tests for config schema extension (T01)."""

from config import AppConfig, MCPServerConfig, PostProcConfig, RAGConfig


class TestRAGConfig:
    def test_defaults(self):
        cfg = RAGConfig()
        assert cfg.enabled is False
        assert cfg.alpha == 0.5
        assert cfg.embedding_provider == "local"
        assert cfg.embedding_model == "all-MiniLM-L6-v2"
        assert cfg.chroma_persist_dir == ".chroma_db"
        assert cfg.chunk_strategy == "method"
        assert cfg.chunk_max_tokens == 512
        assert cfg.retrieval_top_k == 10
        assert cfg.max_context_tokens == 4096
        assert cfg.retrieval_strategy == "hybrid"

    def test_alpha_bounds(self):
        RAGConfig(alpha=0.0)
        RAGConfig(alpha=1.0)

    def test_env_var_loading(self, monkeypatch):
        monkeypatch.setenv("RAG_ENABLED", "true")
        monkeypatch.setenv("RAG_ALPHA", "0.8")
        cfg = RAGConfig()
        assert cfg.enabled is True
        assert cfg.alpha == 0.8


class TestMCPServerConfig:
    def test_defaults(self):
        cfg = MCPServerConfig()
        assert cfg.transport == "stdio"
        assert cfg.host == "127.0.0.1"
        assert cfg.port == 8000
        assert cfg.debug is False

    def test_env_var_loading(self, monkeypatch):
        monkeypatch.setenv("MCP_TRANSPORT", "http")
        monkeypatch.setenv("MCP_PORT", "9000")
        cfg = MCPServerConfig()
        assert cfg.transport == "http"
        assert cfg.port == 9000


class TestPostProcConfig:
    def test_defaults(self):
        cfg = PostProcConfig()
        assert cfg.enabled is False
        assert cfg.auto_compile is False
        assert cfg.auto_run is False
        assert cfg.coverage_tool == "jacoco"
        assert cfg.quality_threshold == 0.5

    def test_env_var_loading(self, monkeypatch):
        monkeypatch.setenv("POSTPROC_ENABLED", "true")
        monkeypatch.setenv("POSTPROC_QUALITY_THRESHOLD", "0.8")
        cfg = PostProcConfig()
        assert cfg.enabled is True
        assert cfg.quality_threshold == 0.8


class TestAppConfigBackwardCompat:
    def test_new_sections_present(self):
        cfg = AppConfig()
        assert hasattr(cfg, "rag")
        assert hasattr(cfg, "mcp_server")
        assert hasattr(cfg, "postproc")
        assert isinstance(cfg.rag, RAGConfig)
        assert isinstance(cfg.mcp_server, MCPServerConfig)
        assert isinstance(cfg.postproc, PostProcConfig)

    def test_existing_sections_unchanged(self):
        cfg = AppConfig()
        assert hasattr(cfg, "llm")
        assert hasattr(cfg, "java")
        assert hasattr(cfg, "benchmark")
        assert hasattr(cfg, "features")
