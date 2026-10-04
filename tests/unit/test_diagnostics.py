"""Unit tests for structured javac diagnostic parsing."""

from __future__ import annotations

from postproc.diagnostics import (
    JavacDiagnostic,
    parse_javac_diagnostics,
    summarize_diagnostics,
)

_MAVEN_OUTPUT = """[INFO] Scanning for projects...
[ERROR] /home/u/src/Foo.java:[12,34] cannot find symbol
[ERROR]   symbol:   class Bar
[ERROR]   location: class Foo
[ERROR] /home/u/src/Foo.java:[15,9] incompatible types: java.lang.String cannot be converted to int
[INFO] BUILD FAILURE
"""

_JAVAC_OUTPUT = """/home/u/src/Foo.java:12: error: cannot find symbol
    Bar b = new Bar();
    ^
  symbol:   class Bar
  location: class Foo
"""


def test_parse_maven_diagnostics_with_symbol_and_location():
    diagnostics = parse_javac_diagnostics(_MAVEN_OUTPUT)

    assert len(diagnostics) == 2
    first = diagnostics[0]
    assert first.file == "/home/u/src/Foo.java"
    assert first.line == 12
    assert first.column == 34
    assert first.symbol == "class Bar"
    assert first.location == "class Foo"
    assert "cannot find symbol" in first.format()
    assert "symbol: class Bar" in first.format()


def test_parse_javac_direct_diagnostics():
    diagnostics = parse_javac_diagnostics(_JAVAC_OUTPUT)

    assert len(diagnostics) == 1
    diagnostic = diagnostics[0]
    assert diagnostic.file == "/home/u/src/Foo.java"
    assert diagnostic.line == 12
    assert diagnostic.column is None
    assert diagnostic.symbol == "class Bar"


def test_parse_ignores_non_compiler_lines():
    diagnostics = parse_javac_diagnostics("[INFO] BUILD SUCCESS\n")
    assert diagnostics == []


def test_deduplicates_identical_diagnostics():
    repeated = "[ERROR] /a/Foo.java:[1,1] cannot find symbol\n" * 3
    diagnostics = parse_javac_diagnostics(repeated)
    assert len(diagnostics) == 1


def test_summarize_caps_and_reports_remaining():
    diagnostics = [
        JavacDiagnostic(file=f"/a/Foo{i}.java", line=i, column=1, message="cannot find symbol")
        for i in range(3)
    ]

    summary = summarize_diagnostics(diagnostics, limit=2)

    assert "3 error(s)" in summary
    assert "showing 2" in summary
    assert "... and 1 more error(s)." in summary


def test_summarize_empty_returns_empty_string():
    assert summarize_diagnostics([]) == ""
