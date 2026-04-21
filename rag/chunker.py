"""AST-aware chunker for Java source files."""

from __future__ import annotations

from typing import TYPE_CHECKING

from rag.models import CodeChunk

if TYPE_CHECKING:
    from core.parsing.models import ParsedJavaClass


def _make_chunk_id(file_path: str, chunk_type: str, name: str) -> str:
    return f"{file_path}:{chunk_type}:{name}"


def _extract_method_content(source: str, method_name: str) -> tuple[str, int, int]:
    lines = source.split("\n")
    start = 0
    end = 0
    brace_depth = 0
    found = False
    for i, line in enumerate(lines):
        if not found and method_name in line and ("(" in line):
            start = i + 1
            found = True
            brace_depth += line.count("{") - line.count("}")
            if brace_depth <= 0 and "{" in line:
                end = i + 1
                break
            continue
        if found:
            brace_depth += line.count("{") - line.count("}")
            if brace_depth <= 0:
                end = i + 1
                break
    if found and end == 0:
        end = len(lines)
    content = "\n".join(lines[start:end]) if found else ""
    return content, start + 1, end


class ASTChunker:
    def chunk(self, parsed: ParsedJavaClass) -> list[CodeChunk]:
        chunks: list[CodeChunk] = []
        file_path = parsed.file_path or ""

        for imp in parsed.imports:
            chunks.append(
                CodeChunk(
                    chunk_id=_make_chunk_id(file_path, "import", imp),
                    content=f"import {imp};",
                    chunk_type="import",
                    file_path=file_path,
                    package=parsed.package,
                    class_name=parsed.name,
                    metadata={"import": imp},
                )
            )

        if parsed.fields:
            field_lines = []
            for f in parsed.fields:
                modifiers = []
                if f.visibility != "package-private":
                    modifiers.append(f.visibility)
                if f.is_static:
                    modifiers.append("static")
                if f.is_final:
                    modifiers.append("final")
                prefix = " ".join(modifiers) + " " if modifiers else ""
                field_lines.append(f"    {prefix}{f.type} {f.name};")
            chunks.append(
                CodeChunk(
                    chunk_id=_make_chunk_id(file_path, "class_fields", parsed.name),
                    content="\n".join(field_lines),
                    chunk_type="class_fields",
                    file_path=file_path,
                    package=parsed.package,
                    class_name=parsed.name,
                )
            )

        for method in parsed.methods:
            content, start_line, end_line = _extract_method_content(parsed.content, method.name)
            chunks.append(
                CodeChunk(
                    chunk_id=_make_chunk_id(file_path, "method", method.name),
                    content=content or f"{method.return_type} {method.name}(...)",
                    chunk_type="method",
                    file_path=file_path,
                    package=parsed.package,
                    class_name=parsed.name,
                    method_name=method.name,
                    start_line=start_line,
                    end_line=end_line,
                    metadata={
                        "visibility": method.visibility,
                        "return_type": method.return_type,
                        "is_static": str(method.is_static),
                    },
                )
            )

        return chunks

    def chunk_file(self, file_path: str) -> list[CodeChunk]:
        from core.parsing.parser import JavaParser

        parser = JavaParser()
        parsed = parser.parse_file(file_path)
        return self.chunk(parsed)

    def chunk_content(self, content: str, file_path: str = "") -> list[CodeChunk]:
        from core.parsing.parser import JavaParser

        parser = JavaParser()
        parsed = parser.parse_content(content, file_path)
        return self.chunk(parsed)
