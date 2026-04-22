"""Unit tests for rag/chunker.py (T05)."""

from __future__ import annotations

from unittest.mock import patch

from core.parsing.models import (
    FieldDeclaration,
    MethodSignature,
    ParsedJavaClass,
)
from rag.chunker import ASTChunker


def _make_parsed(
    name="Service",
    package="com.example",
    methods=None,
    fields=None,
    imports=None,
    content="",
    file_path="Service.java",
) -> ParsedJavaClass:
    return ParsedJavaClass(
        name=name,
        package=package,
        imports=imports or [],
        fields=fields or [],
        methods=methods or [],
        content=content,
        file_path=file_path,
    )


JAVA_SERVICE = """\
package com.example.demo;

import java.util.List;
import com.example.Repository;

public class UsuarioService {
    private Repository repo;
    private String name;

    public String getData() {
        return repo.findAll();
    }

    public void save(String data) {
        repo.save(data);
    }

    private void internalHelper() {
        // internal
    }
}
"""


class TestASTChunker:
    def test_empty_class_no_chunks(self):
        parsed = _make_parsed()
        chunker = ASTChunker()
        chunks = chunker.chunk(parsed)
        assert chunks == []

    def test_fields_only(self):
        parsed = _make_parsed(
            fields=[
                FieldDeclaration(name="repo", type="Repository", visibility="private"),
                FieldDeclaration(
                    name="count", type="int", visibility="package-private", is_static=True
                ),
            ]
        )
        chunker = ASTChunker()
        chunks = chunker.chunk(parsed)
        assert len(chunks) == 1
        assert chunks[0].chunk_type == "class_fields"
        assert "repo" in chunks[0].content
        assert "count" in chunks[0].content

    def test_methods_only(self):
        parsed = _make_parsed(
            methods=[
                MethodSignature(name="getData", visibility="public", return_type="String"),
                MethodSignature(name="save", visibility="public", return_type="void"),
            ],
            content="public String getData() { return null; }\npublic void save(String x) { }",
        )
        chunker = ASTChunker()
        chunks = chunker.chunk(parsed)
        method_chunks = [c for c in chunks if c.chunk_type == "method"]
        assert len(method_chunks) == 2
        assert method_chunks[0].method_name == "getData"
        assert method_chunks[1].method_name == "save"

    def test_imports_produce_chunks(self):
        parsed = _make_parsed(imports=["java.util.List", "com.example.Repo"])
        chunker = ASTChunker()
        chunks = chunker.chunk(parsed)
        import_chunks = [c for c in chunks if c.chunk_type == "import"]
        assert len(import_chunks) == 2

    def test_full_class(self):
        parsed = _make_parsed(
            imports=["java.util.List"],
            fields=[FieldDeclaration(name="repo", type="Repo", visibility="private")],
            methods=[
                MethodSignature(name="getData", visibility="public", return_type="String"),
                MethodSignature(name="save", visibility="public", return_type="void"),
                MethodSignature(name="helper", visibility="private", return_type="void"),
            ],
            content=JAVA_SERVICE,
            file_path="UsuarioService.java",
        )
        chunker = ASTChunker()
        chunks = chunker.chunk(parsed)
        assert len(chunks) >= 5

        types = [c.chunk_type for c in chunks]
        assert "import" in types
        assert "class_fields" in types
        assert types.count("method") == 3

    def test_chunk_metadata(self):
        parsed = _make_parsed(
            package="com.example",
            methods=[
                MethodSignature(name="getData", visibility="public", return_type="String"),
            ],
            content="public String getData() { return null; }",
            file_path="Svc.java",
        )
        chunker = ASTChunker()
        chunks = chunker.chunk(parsed)
        method_chunks = [c for c in chunks if c.chunk_type == "method"]
        assert len(method_chunks) == 1
        c = method_chunks[0]
        assert c.file_path == "Svc.java"
        assert c.package == "com.example"
        assert c.class_name == "Service"
        assert c.method_name == "getData"
        assert c.metadata["visibility"] == "public"
        assert c.metadata["return_type"] == "String"

    def test_chunk_id_format(self):
        parsed = _make_parsed(
            methods=[MethodSignature(name="doStuff", visibility="public", return_type="void")],
            content="void doStuff() {}",
            file_path="Test.java",
        )
        chunker = ASTChunker()
        chunks = chunker.chunk(parsed)
        mc = [c for c in chunks if c.chunk_type == "method"][0]
        assert mc.chunk_id == "Test.java:method:doStuff"

    def test_interface_with_methods(self):
        parsed = _make_parsed(
            name="IRepository",
            methods=[
                MethodSignature(name="findAll", visibility="public", return_type="List"),
                MethodSignature(name="save", visibility="public", return_type="void"),
            ],
            content="List findAll(); void save(Object o);",
        )
        chunker = ASTChunker()
        chunks = chunker.chunk(parsed)
        method_chunks = [c for c in chunks if c.chunk_type == "method"]
        assert len(method_chunks) == 2

    def test_chunk_file_with_mock(self):
        mock_parsed = _make_parsed(
            methods=[MethodSignature(name="test", visibility="public", return_type="void")],
            content="void test() {}",
        )
        with patch("core.parsing.parser.JavaParser") as MockParser:
            MockParser.return_value.parse_file.return_value = mock_parsed
            chunker = ASTChunker()
            chunks = chunker.chunk_file("fake.java")
            assert len(chunks) >= 1

    def test_chunk_content_with_mock(self):
        mock_parsed = _make_parsed(
            fields=[FieldDeclaration(name="x", type="int", visibility="private")],
        )
        with patch("core.parsing.parser.JavaParser") as MockParser:
            MockParser.return_value.parse_content.return_value = mock_parsed
            chunker = ASTChunker()
            chunks = chunker.chunk_content("class Foo { private int x; }")
            assert any(c.chunk_type == "class_fields" for c in chunks)

    def test_static_field_in_metadata(self):
        parsed = _make_parsed(
            fields=[
                FieldDeclaration(
                    name="INST", type="Service", visibility="public", is_static=True, is_final=True
                ),
            ],
        )
        chunker = ASTChunker()
        chunks = chunker.chunk(parsed)
        assert len(chunks) == 1
        assert "static" in chunks[0].content
        assert "final" in chunks[0].content
