"""
Bundle Extractor module.

Extracts test cases from Java test files and maps them to their focal methods
using a deterministic heuristic. Emits TestBundle objects.
"""

import re
from pathlib import Path

from core.metainfo.schemas import TestBundle
from core.parsing.models import MethodSignature, ParsedJavaClass
from core.parsing.parser import JavaParser


class BundleExtractor:
    """Extracts TestBundles from Java test files."""

    def __init__(self, parser: JavaParser | None = None):
        """Initialize the extractor with a Java parser.

        Args:
            parser: Optional JavaParser instance. If not provided, a new one is created.
        """
        self.parser = parser or JavaParser()

    def extract_from_file(self, file_path: str | Path) -> list[TestBundle]:
        """
        Parse a test file and extract all test bundles.

        Args:
            file_path: Path to the Java test file.

        Returns:
            A list of TestBundle objects, sorted deterministically by test method name.
        """
        path_str = str(file_path)
        parsed_class = self.parser.parse_file(path_str)
        return self.extract_from_parsed_class(parsed_class)

    def extract_from_parsed_class(self, parsed_class: ParsedJavaClass) -> list[TestBundle]:
        """
        Extract test bundles from a parsed Java class.

        Args:
            parsed_class: The parsed test class.

        Returns:
            A list of TestBundle objects, sorted deterministically by test method name.
        """
        bundles = []

        class_uri = (
            f"{parsed_class.package}.{parsed_class.name}"
            if parsed_class.package
            else parsed_class.name
        )

        for method in parsed_class.methods:
            if self._is_test_method(method):
                focal_method = self.map_test_to_focal_method(method.name)

                # Create a minimal TestBundle with the required fields
                bundle = TestBundle(
                    test_uri=f"{class_uri}.{method.name}",
                    test_name=method.name,
                    test_class_uri=class_uri,
                    target_method=focal_method,
                    fixtures_used=[],
                    external_dependencies={},
                    project_specific_resources=[],
                    assertions=[],
                    given_phase=None,
                    when_phase=None,
                    then_phase=None,
                )
                bundles.append(bundle)

        # Sort bundles by test_name to ensure deterministic output
        bundles.sort(key=lambda b: b.test_name)
        return bundles

    def _is_test_method(self, method: MethodSignature) -> bool:
        """
        Determine if a method is a test method.

        MVP Heuristic: A test method's name starts with 'test' (case-insensitive).
        Annotations like @Test are not currently parsed by the baseline JavaParser,
        so we rely on naming conventions.

        Args:
            method: The method signature to check.

        Returns:
            True if it's a test method, False otherwise.
        """
        return method.name.lower().startswith("test")

    def map_test_to_focal_method(self, test_name: str) -> str:
        """
        Map a test method name to its focal method using a clear heuristic.

        Heuristic:
        1. If the name starts with "test_", remove the prefix.
        2. If the name starts with "test" followed by an uppercase letter,
           remove "test" and lowercase the first letter.
        3. Otherwise, return the original test name (fallback).

        Examples:
            - testCalculateTotal -> calculateTotal
            - test_calculate_total -> calculate_total
            - testRun -> run
            - test -> test

        Args:
            test_name: The name of the test method.

        Returns:
            The name of the focal method.
        """
        # test_something -> something
        if test_name.startswith("test_"):
            return test_name[5:]

        # testSomething -> something
        match = re.match(r"^test([A-Z])(.*)$", test_name)
        if match:
            first_letter, rest = match.groups()
            return first_letter.lower() + rest

        # testsomething -> something
        if test_name.startswith("test") and len(test_name) > 4:
            return test_name[4:]

        return test_name
