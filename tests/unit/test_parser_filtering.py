"""Unit tests for method filtering capabilities in the parser.

Covers: is_private, effective_loc, is_in_inner_class fields,
the filter_reftest_methods function, and the reftest_eligible_methods property.
"""

from core.parsing.models import MethodSignature, ParsedJavaClass
from core.parsing.models import _filter_reftest_methods as filter_reftest_methods
from core.parsing.parser import JavaParser


def _parse(java_code: str, tmp_path) -> ParsedJavaClass:
    """Helper: write Java code to a temp file and parse it."""
    file = tmp_path / "Subject.java"
    file.write_text(java_code)
    return JavaParser().parse_file(str(file))


def _method_by_name(parsed: ParsedJavaClass, name: str) -> MethodSignature:
    """Helper: find a method by name in a parsed class."""
    matches = [m for m in parsed.methods if m.name == name]
    assert matches, f"No method named '{name}' found"
    return matches[0]


# ---------------------------------------------------------------------------
# is_private
# ---------------------------------------------------------------------------


class TestIsPrivate:
    def test_private_method_flagged(self, tmp_path):
        """Private methods must have is_private=True."""
        parsed = _parse(
            """
class Subject {
    private void secret() {
        int x = 1;
        int y = 2;
    }
}
""",
            tmp_path,
        )
        method = _method_by_name(parsed, "secret")
        assert method.is_private is True

    def test_public_method_not_flagged(self, tmp_path):
        """Public methods must have is_private=False."""
        parsed = _parse(
            """
class Subject {
    public void visible() {
        int x = 1;
        int y = 2;
    }
}
""",
            tmp_path,
        )
        method = _method_by_name(parsed, "visible")
        assert method.is_private is False


# ---------------------------------------------------------------------------
# effective_loc
# ---------------------------------------------------------------------------


class TestEffectiveLoc:
    def test_single_line_method_detected(self, tmp_path):
        """A one-liner method body should have effective_loc=1."""
        parsed = _parse(
            """
class Subject {
    public int getId() { return this.id; }
}
""",
            tmp_path,
        )
        method = _method_by_name(parsed, "getId")
        assert method.effective_loc == 1

    def test_multi_line_method_loc(self, tmp_path):
        """A method with several statements should count effective LOC correctly."""
        parsed = _parse(
            """
class Subject {
    public void compute() {
        int a = 1;
        int b = 2;
        int c = a + b;
    }
}
""",
            tmp_path,
        )
        method = _method_by_name(parsed, "compute")
        assert method.effective_loc == 3

    def test_blank_and_comment_lines_excluded(self, tmp_path):
        """Blank lines and comments must not count toward effective_loc."""
        parsed = _parse(
            """
class Subject {
    public void process() {

        // This is a comment
        int step1 = 1;

        /* block comment line */
        int step2 = 2;
    }
}
""",
            tmp_path,
        )
        method = _method_by_name(parsed, "process")
        assert method.effective_loc == 2


# ---------------------------------------------------------------------------
# is_in_inner_class
# ---------------------------------------------------------------------------


class TestIsInInnerClass:
    def test_inner_class_method_flagged(self, tmp_path):
        """Methods inside a nested inner class must have is_in_inner_class=True."""
        parsed = _parse(
            """
class Subject {
    public void outer() {
        int x = 1;
        int y = 2;
    }

    class InnerHelper {
        public void innerWork() {
            int a = 1;
            int b = 2;
        }
    }
}
""",
            tmp_path,
        )
        method = _method_by_name(parsed, "innerWork")
        assert method.is_in_inner_class is True

    def test_top_level_method_not_flagged(self, tmp_path):
        """Methods in the outer class must have is_in_inner_class=False."""
        parsed = _parse(
            """
class Subject {
    public void outer() {
        int x = 1;
        int y = 2;
    }

    class InnerHelper {
        public void innerWork() {
            int a = 1;
            int b = 2;
        }
    }
}
""",
            tmp_path,
        )
        method = _method_by_name(parsed, "outer")
        assert method.is_in_inner_class is False


# ---------------------------------------------------------------------------
# filter_reftest_methods
# ---------------------------------------------------------------------------


class TestFilterReftestMethods:
    def test_filter_excludes_private(self):
        """filter_reftest_methods must remove private methods."""
        methods = [
            MethodSignature(
                visibility="private",
                return_type="void",
                name="secret",
                parameters=[],
                is_private=True,
                effective_loc=5,
                is_in_inner_class=False,
            ),
            MethodSignature(
                visibility="public",
                return_type="void",
                name="visible",
                parameters=[],
                is_private=False,
                effective_loc=5,
                is_in_inner_class=False,
            ),
        ]
        result = filter_reftest_methods(methods)
        assert len(result) == 1
        assert result[0].name == "visible"

    def test_filter_excludes_single_line(self):
        """filter_reftest_methods must remove methods with effective_loc <= 1."""
        methods = [
            MethodSignature(
                visibility="public",
                return_type="int",
                name="getId",
                parameters=[],
                is_private=False,
                effective_loc=1,
                is_in_inner_class=False,
            ),
            MethodSignature(
                visibility="public",
                return_type="void",
                name="doWork",
                parameters=[],
                is_private=False,
                effective_loc=3,
                is_in_inner_class=False,
            ),
        ]
        result = filter_reftest_methods(methods)
        assert len(result) == 1
        assert result[0].name == "doWork"

    def test_filter_excludes_inner_class(self):
        """filter_reftest_methods must remove inner-class methods."""
        methods = [
            MethodSignature(
                visibility="public",
                return_type="void",
                name="innerMethod",
                parameters=[],
                is_private=False,
                effective_loc=5,
                is_in_inner_class=True,
            ),
            MethodSignature(
                visibility="public",
                return_type="void",
                name="outerMethod",
                parameters=[],
                is_private=False,
                effective_loc=5,
                is_in_inner_class=False,
            ),
        ]
        result = filter_reftest_methods(methods)
        assert len(result) == 1
        assert result[0].name == "outerMethod"

    def test_filter_keeps_eligible(self):
        """Public, multi-line, top-level methods must pass the filter."""
        methods = [
            MethodSignature(
                visibility="public",
                return_type="String",
                name="process",
                parameters=["String"],
                is_private=False,
                effective_loc=4,
                is_in_inner_class=False,
            ),
        ]
        result = filter_reftest_methods(methods)
        assert len(result) == 1
        assert result[0].name == "process"


# ---------------------------------------------------------------------------
# reftest_eligible_methods property
# ---------------------------------------------------------------------------


class TestReftestEligibleProperty:
    def test_reftest_eligible_methods_property(self, tmp_path):
        """ParsedJavaClass.reftest_eligible_methods must return only filtered methods."""
        parsed = _parse(
            """
class Subject {
    private void secret() {
        int x = 1;
        int y = 2;
    }

    public int getId() { return this.id; }

    class Inner {
        public void innerWork() {
            int a = 1;
            int b = 2;
        }
    }

    public void realWork() {
        int step1 = 1;
        int step2 = 2;
        int step3 = 3;
    }
}
""",
            tmp_path,
        )

        eligible = parsed.reftest_eligible_methods
        eligible_names = [m.name for m in eligible]

        # secret is private -> excluded
        assert "secret" not in eligible_names
        # getId is single-line -> excluded
        assert "getId" not in eligible_names
        # innerWork is in inner class -> excluded
        assert "innerWork" not in eligible_names
        # realWork is public, multi-line, top-level -> kept
        assert "realWork" in eligible_names
        assert len(eligible) == 1
