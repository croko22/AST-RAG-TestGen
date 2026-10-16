"""Unit tests for the modern Typer CLI defaults."""

import inspect
from types import SimpleNamespace

import pytest

from cli import modern


def _generate_params(app):
    command = next(c for c in app.registered_commands if c.callback.__name__ == "generate")
    return inspect.signature(command.callback).parameters


def _default(params, name):
    """Unwrap typer's OptionInfo wrapper to reach the raw default value."""
    default = params[name].default
    return getattr(default, "default", default)


class TestModernCliDefaults:
    def test_generate_defaults_come_from_config(self, monkeypatch):
        """Provider/model/output defaults must come from .env config, not literals."""
        config = SimpleNamespace(
            llm=SimpleNamespace(
                provider="openrouter",
                model="nvidia/nemotron-3-super-120b-a12b:free",
            ),
            java=SimpleNamespace(test_output_dir="./custom_out", max_dependencies=7),
        )
        monkeypatch.setattr("config.get_config", lambda: config)

        params = _generate_params(modern.create_modern_cli())

        assert _default(params, "provider") == "openrouter"
        assert _default(params, "model") == "nvidia/nemotron-3-super-120b-a12b:free"
        assert _default(params, "output") == "./custom_out"
        assert _default(params, "max_deps") == 7

    def test_generate_defaults_fall_back_when_config_unavailable(self, monkeypatch):
        """A broken config must not break the CLI: hardcoded defaults stay."""

        def _raise():
            raise RuntimeError("boom")

        monkeypatch.setattr("config.get_config", _raise)

        params = _generate_params(modern.create_modern_cli())

        assert _default(params, "provider") == "anthropic"
        assert _default(params, "model") == "claude-3-5-sonnet-20241022"

    @pytest.mark.skipif(not modern.TYPER_AVAILABLE, reason="typer not installed")
    def test_create_modern_cli_returns_app(self):
        assert modern.create_modern_cli() is not None
