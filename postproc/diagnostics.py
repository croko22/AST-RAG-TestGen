"""Structured parsing of Java compiler diagnostics from Maven/javac output.

Maven and javac emit compilation diagnostics as free-form text, usually on
stdout. This module turns that text into structured records so the repair loop
can feed concrete errors back to the language model instead of a raw log tail.
"""

from __future__ import annotations

import re
from dataclasses import dataclass

_MAVEN_LOCATION = re.compile(
    r"^\[ERROR\]\s+(?P<file>.+?\.java):\[(?P<line>\d+),(?P<col>\d+)\]\s*(?P<message>.*)$"
)
_MAVEN_LINE = re.compile(r"^\[ERROR\]\s+(?P<file>.+?\.java):(?P<line>\d+):\s*(?P<message>.*)$")
_JAVAC_ERROR = re.compile(
    r"^(?P<file>[^\[\s][^:]*\.java):(?P<line>\d+):\s*(?:error|warning):\s*(?P<message>.*)$",
    re.IGNORECASE,
)
_SYMBOL = re.compile(r"^\s*(?:\[ERROR\]\s+)?symbol:\s*(?P<symbol>.+?)\s*$", re.IGNORECASE)
_LOCATION = re.compile(r"^\s*(?:\[ERROR\]\s+)?location:\s*(?P<location>.+?)\s*$", re.IGNORECASE)


@dataclass(slots=True)
class JavacDiagnostic:
    """One structured Java compiler diagnostic."""

    file: str | None
    line: int | None
    column: int | None
    message: str
    symbol: str | None = None
    location: str | None = None

    def format(self) -> str:
        """Render a compact single-line representation."""
        position = self.file or "<unknown>"
        if self.line is not None:
            position += f":{self.line}"
            if self.column is not None:
                position += f":{self.column}"

        detail = self.message.strip()
        if self.symbol:
            detail = f"{detail} (symbol: {self.symbol})"
        if self.location:
            detail = f"{detail} (location: {self.location})"
        return f"{position}: {detail}"


def parse_javac_diagnostics(output: str) -> list[JavacDiagnostic]:
    """Parse Maven/javac output into unique structured diagnostics.

    Args:
        output: Combined stdout/stderr text from a compilation command.

    Returns:
        Deduplicated diagnostics in encounter order.
    """
    diagnostics: list[JavacDiagnostic] = []

    for raw_line in output.splitlines():
        line = raw_line.rstrip()
        if not line.strip():
            continue

        symbol_match = _SYMBOL.match(line)
        if symbol_match and diagnostics:
            diagnostics[-1].symbol = symbol_match.group("symbol").strip()
            continue

        location_match = _LOCATION.match(line)
        if location_match and diagnostics:
            diagnostics[-1].location = location_match.group("location").strip()
            continue

        match = _MAVEN_LOCATION.match(line) or _MAVEN_LINE.match(line) or _JAVAC_ERROR.match(line)
        if match is None:
            continue

        groups = match.groupdict()
        column = groups.get("col")
        diagnostics.append(
            JavacDiagnostic(
                file=groups.get("file"),
                line=_to_int(groups.get("line")),
                column=_to_int(column) if column else None,
                message=(groups.get("message") or "").strip() or "compilation error",
            )
        )

    return _deduplicate(diagnostics)


def summarize_diagnostics(diagnostics: list[JavacDiagnostic], limit: int = 10) -> str:
    """Render a bounded, readable list of diagnostics.

    Args:
        diagnostics: Parsed diagnostics.
        limit: Maximum number of diagnostics to include.

    Returns:
        Multi-line summary, or an empty string when there are no diagnostics.
    """
    if not diagnostics:
        return ""

    shown = diagnostics[:limit]
    lines = [f"javac diagnostics ({len(diagnostics)} error(s), showing {len(shown)}):"]
    for index, diagnostic in enumerate(shown, start=1):
        lines.append(f"{index}. {diagnostic.format()}")

    remaining = len(diagnostics) - len(shown)
    if remaining > 0:
        lines.append(f"... and {remaining} more error(s).")
    return "\n".join(lines)


def _to_int(value: str | None) -> int | None:
    if value is None:
        return None
    try:
        return int(value)
    except ValueError:
        return None


def _deduplicate(diagnostics: list[JavacDiagnostic]) -> list[JavacDiagnostic]:
    seen: set[tuple[str | None, int | None, int | None, str, str | None, str | None]] = set()
    unique: list[JavacDiagnostic] = []
    for diagnostic in diagnostics:
        key = (
            diagnostic.file,
            diagnostic.line,
            diagnostic.column,
            diagnostic.message,
            diagnostic.symbol,
            diagnostic.location,
        )
        if key in seen:
            continue
        seen.add(key)
        unique.append(diagnostic)
    return unique
