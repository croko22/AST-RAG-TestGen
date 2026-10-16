"""Unit tests for LLM provider adapters."""

from types import SimpleNamespace

from llm.adapters.anthropic import AnthropicAdapter
from llm.adapters.base import LLMAdapterConfig


class _FakeV1Messages:
    """Stub mirroring the anthropic SDK v1 signature: no temperature/top_p."""

    def __init__(self):
        self.calls = []

    def create(self, *, model, max_tokens, system, messages, extra_body=None):
        self.calls.append(
            {
                "model": model,
                "max_tokens": max_tokens,
                "system": system,
                "messages": messages,
                "extra_body": extra_body,
            }
        )
        return SimpleNamespace(content=[SimpleNamespace(text="class GeneratedTest {}")])


class _FakeV0Messages:
    """Stub mirroring older anthropic SDK signatures that accept sampling args."""

    def __init__(self):
        self.calls = []

    def create(self, *, model, max_tokens, temperature, top_p, system, messages):
        self.calls.append(
            {
                "model": model,
                "max_tokens": max_tokens,
                "temperature": temperature,
                "top_p": top_p,
                "system": system,
                "messages": messages,
            }
        )
        return SimpleNamespace(content=[SimpleNamespace(text="class GeneratedTest {}")])


def _adapter_with(messages_stub) -> AnthropicAdapter:
    adapter = AnthropicAdapter(
        LLMAdapterConfig(api_key="test-key", model="claude-3-5-sonnet-20241022")
    )
    adapter.client = SimpleNamespace(messages=messages_stub)
    return adapter


class TestAnthropicAdapter:
    def test_generate_test_with_sdk_v1_signature(self):
        """The adapter must not pass sampling kwargs the installed SDK rejects."""
        stub = _FakeV1Messages()
        adapter = _adapter_with(stub)

        result = adapter.generate_test("class A {}", "deps")

        assert result == "class GeneratedTest {}"
        assert len(stub.calls) == 1
        assert "temperature" not in stub.calls[0]
        assert "top_p" not in stub.calls[0]

    def test_generate_test_keeps_sampling_for_sdk_v0_signature(self):
        """Older SDKs still accept temperature/top_p, so they must still be sent."""
        stub = _FakeV0Messages()
        adapter = _adapter_with(stub)

        adapter.generate_test("class A {}", "deps")

        assert len(stub.calls) == 1
        assert stub.calls[0]["temperature"] == 0.3
        assert stub.calls[0]["top_p"] == 1.0
