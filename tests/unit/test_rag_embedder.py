"""Unit tests for rag/embedder.py (T06)."""

from __future__ import annotations

from unittest.mock import MagicMock, patch

import pytest

from rag.embedder import EmbeddingService


def _mock_vec(values=None, shape_1d=384):
    if values is not None:
        mock = MagicMock()
        mock.tolist.return_value = values
        return mock
    mock = MagicMock()
    mock.tolist.return_value = [0.1] * shape_1d
    return mock


class TestEmbeddingService:
    def test_import_error_without_lib(self):
        with patch.dict("sys.modules", {"sentence_transformers": None}):
            svc = EmbeddingService()
            with pytest.raises(ImportError, match="sentence-transformers"):
                svc.embed(["hello"])

    def test_embed_single_import_error(self):
        with patch.dict("sys.modules", {"sentence_transformers": None}):
            svc = EmbeddingService()
            with pytest.raises(ImportError, match="sentence-transformers"):
                svc.embed_single("hello")

    def test_embed_empty_list(self):
        mock_model = MagicMock()
        svc = EmbeddingService()
        svc._model = mock_model
        result = svc.embed([])
        assert result == []

    def test_embed_caches_results(self):
        mock_model = MagicMock()
        vec1 = MagicMock()
        vec1.tolist.return_value = [0.1] * 384
        mock_model.encode.return_value = [vec1]
        mock_model.get_sentence_embedding_dimension.return_value = 384

        svc = EmbeddingService()
        svc._model = mock_model

        r1 = svc.embed(["hello world"])
        r2 = svc.embed(["hello world"])
        assert r1 == r2
        mock_model.encode.assert_called_once()

    def test_embed_single_caches(self):
        mock_model = MagicMock()
        vec1 = MagicMock()
        vec1.tolist.return_value = [0.2] * 384
        mock_model.encode.return_value = [vec1]
        mock_model.get_sentence_embedding_dimension.return_value = 384

        svc = EmbeddingService()
        svc._model = mock_model

        r1 = svc.embed_single("test text")
        r2 = svc.embed_single("test text")
        assert r1 == r2
        assert mock_model.encode.call_count == 1

    def test_embed_truncates_long_text(self):
        mock_model = MagicMock()
        vec1 = MagicMock()
        vec1.tolist.return_value = [0.3] * 384
        mock_model.encode.return_value = [vec1]

        svc = EmbeddingService()
        svc._model = mock_model

        long_text = "x" * 10000
        svc.embed([long_text])
        call_args = mock_model.encode.call_args[0][0]
        assert len(call_args[0]) <= 8192

    def test_embed_empty_text_returns_zeros(self):
        mock_model = MagicMock()
        mock_model.get_sentence_embedding_dimension.return_value = 384

        svc = EmbeddingService()
        svc._model = mock_model

        result = svc.embed([""])
        assert result == [[0.0] * 384]

    def test_embed_single_empty_returns_zeros(self):
        mock_model = MagicMock()
        mock_model.get_sentence_embedding_dimension.return_value = 384

        svc = EmbeddingService()
        svc._model = mock_model

        result = svc.embed_single("")
        assert result == [0.0] * 384

    def test_embedding_dim(self):
        mock_model = MagicMock()
        mock_model.get_sentence_embedding_dimension.return_value = 384

        svc = EmbeddingService()
        svc._model = mock_model

        assert svc.embedding_dim == 384

    def test_batch_embed(self):
        mock_model = MagicMock()
        v1 = MagicMock()
        v1.tolist.return_value = [0.1] * 384
        v2 = MagicMock()
        v2.tolist.return_value = [0.2] * 384
        mock_model.encode.return_value = [v1, v2]

        svc = EmbeddingService()
        svc._model = mock_model

        result = svc.embed(["text1", "text2"])
        assert len(result) == 2
        assert len(result[0]) == 384
        assert len(result[1]) == 384

    def test_model_lazy_load(self):
        svc = EmbeddingService()
        assert svc._model is None

    def test_custom_model_name(self):
        svc = EmbeddingService(model_name="custom-model")
        assert svc._model_name == "custom-model"
