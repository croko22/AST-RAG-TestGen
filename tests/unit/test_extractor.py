"""Unit tests for the BundleExtractor."""

import pytest

from core.extraction.extractor import BundleExtractor
from core.parsing.models import MethodSignature, ParsedJavaClass


class TestBundleExtractor:
    @pytest.fixture
    def extractor(self):
        return BundleExtractor()

    @pytest.mark.parametrize(
        "test_name, expected_focal",
        [
            ("testCalculateTotal", "calculateTotal"),
            ("test_calculate_total", "calculate_total"),
            ("testRun", "run"),
            ("test_run", "run"),
            ("test", "test"),
            ("testsomething", "something"),
            (
                "TestCalculate",
                "TestCalculate",
            ),  # Does not start with "test" exactly (case-sensitive for the heuristic)
            ("test_123", "123"),
        ],
    )
    def test_map_test_to_focal_method(self, extractor, test_name, expected_focal):
        """Test the heuristic for mapping test method names to focal methods."""
        assert extractor.map_test_to_focal_method(test_name) == expected_focal

    def test_is_test_method(self, extractor):
        """Test the heuristic for identifying test methods."""
        # Methods starting with test (case-insensitive) are tests
        assert extractor._is_test_method(
            MethodSignature(
                name="testCalculate", visibility="public", return_type="void", parameters=[]
            )
        )
        assert extractor._is_test_method(
            MethodSignature(
                name="test_calculate", visibility="public", return_type="void", parameters=[]
            )
        )
        assert extractor._is_test_method(
            MethodSignature(
                name="TEST_CALCULATE", visibility="public", return_type="void", parameters=[]
            )
        )

        assert not extractor._is_test_method(
            MethodSignature(
                name="calculate", visibility="public", return_type="void", parameters=[]
            )
        )
        assert not extractor._is_test_method(
            MethodSignature(name="setUp", visibility="public", return_type="void", parameters=[])
        )
        assert not extractor._is_test_method(
            MethodSignature(name="tearDown", visibility="public", return_type="void", parameters=[])
        )

    def test_extract_from_parsed_class(self, extractor):
        """Test extraction of TestBundles from a ParsedJavaClass."""
        parsed_class = ParsedJavaClass(
            name="CalculatorTest",
            package="com.example",
            imports=[],
            dependencies=[],
            fields=[],
            file_path="CalculatorTest.java",
            content="...",
            methods=[
                MethodSignature(
                    name="testAdd", visibility="public", return_type="void", parameters=[]
                ),
                MethodSignature(
                    name="setUp", visibility="public", return_type="void", parameters=[]
                ),
                MethodSignature(
                    name="test_subtract", visibility="public", return_type="void", parameters=[]
                ),
                MethodSignature(
                    name="testMultiply", visibility="public", return_type="void", parameters=[]
                ),
            ],
        )

        bundles = extractor.extract_from_parsed_class(parsed_class)

        assert len(bundles) == 3

        # The output should be deterministically sorted by test_name
        assert bundles[0].test_name == "testAdd"
        assert bundles[0].test_uri == "com.example.CalculatorTest.testAdd"
        assert bundles[0].test_class_uri == "com.example.CalculatorTest"
        assert bundles[0].target_method == "add"

        assert bundles[1].test_name == "testMultiply"
        assert bundles[1].target_method == "multiply"

        assert bundles[2].test_name == "test_subtract"
        assert bundles[2].target_method == "subtract"

    def test_extract_from_parsed_class_no_package(self, extractor):
        """Test extraction when class has no package."""
        parsed_class = ParsedJavaClass(
            name="CalculatorTest",
            package=None,
            imports=[],
            dependencies=[],
            fields=[],
            file_path="CalculatorTest.java",
            content="...",
            methods=[
                MethodSignature(
                    name="testAdd", visibility="public", return_type="void", parameters=[]
                )
            ],
        )

        bundles = extractor.extract_from_parsed_class(parsed_class)

        assert len(bundles) == 1
        assert bundles[0].test_class_uri == "CalculatorTest"
        assert bundles[0].test_uri == "CalculatorTest.testAdd"
