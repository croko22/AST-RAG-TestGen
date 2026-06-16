"""Unified Java file scanner for project-wide file discovery.

Single source of truth for finding Java files and excluding test files.
"""

from __future__ import annotations

from pathlib import Path


class JavaFileScanner:
    """Scan a Java project for source files, excluding tests."""

    _TEST_DIRS = {"test", "tests", "testing"}
    _TEST_SUFFIXES = ("Test.java", "Tests.java", "IT.java", "E2ETest.java")

    def __init__(self, project_root: str | Path):
        self.project_root = Path(project_root)
        if not self.project_root.exists():
            raise FileNotFoundError(f"Project root not found: {project_root}")
        self._files_cache: list[Path] | None = None

    def scan(self, include_tests: bool = False) -> list[Path]:
        """Find all Java files, optionally including test files.

        Args:
            include_tests: If True, include test files. Default False.

        Returns:
            List of Path objects for Java source files.
        """
        if self._files_cache is None:
            self._files_cache = [
                p for p in self.project_root.rglob("*.java") if not p.name.startswith(".")
            ]

        if include_tests:
            return list(self._files_cache)

        return [p for p in self._files_cache if not self._is_test_file(p)]

    def _is_test_file(self, path: Path) -> bool:
        """Check if a path is a test file."""
        if any(part in self._TEST_DIRS for part in path.parts):
            return True
        return any(path.name.endswith(suffix) for suffix in self._TEST_SUFFIXES)

    def find_by_class_name(self, class_name: str) -> Path | None:
        """Find a Java file by its class name.

        Args:
            class_name: Simple or fully-qualified class name.

        Returns:
            Path to the file, or None if not found.
        """
        for p in self.scan():
            if p.stem == class_name or p.name == f"{class_name}.java":
                return p
        return None
