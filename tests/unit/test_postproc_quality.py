"""Unit tests for postproc/quality.py (T12)."""

from __future__ import annotations

from postproc.quality import (
    QualityReport,
    _count_assertions,
    _count_tests,
    _normalize_for_comparison,
    assess_test_quality,
)

TRIVIAL_TEST = """\
import org.junit.Test;
import static org.junit.Assert.*;

public class ServiceTest {
    @Test
    public void testGetName() {
        // no assertions
    }

    @Test
    public void testSetName() {
        // no assertions
    }
}
"""

MEANINGFUL_TEST = """\
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {
    @Test
    public void shouldReturnAllUsers() {
        UsuarioService service = new UsuarioService();
        List<User> result = service.getAllUsers();
        assertEquals(3, result.size());
        assertNotNull(result.get(0));
    }

    @Test
    public void shouldSaveNewUser() {
        UsuarioService service = new UsuarioService();
        service.saveUser(new User("Alice"));
        verify(mockRepo).save(any());
    }

    @Test
    public void shouldThrowOnNullInput() {
        UsuarioService service = new UsuarioService();
        assertThrows(IllegalArgumentException.class, () -> {
            service.saveUser(null);
        });
    }
}
"""

MIXED_TEST = """\
import org.junit.Test;
import static org.junit.Assert.*;

public class MixedTest {
    @Test
    public void testSomething() {
        // empty - trivial
    }

    @Test
    public void testWithAssertion() {
        assertEquals(2, 1 + 1);
    }

    @Test
    public void testWithMockito() {
        verify(mockRepo).findAll();
    }
}
"""

DUPLICATE_TEST = """\
import org.junit.Test;
import static org.junit.Assert.*;

public class DupTest {
    @Test
    public void testAddition1() {
        assertEquals(4, 2 + 2);
        assertTrue(true);
    }

    @Test
    public void testAddition2() {
        assertEquals(4, 2 + 2);
        assertTrue(true);
    }
}
"""

HARDCODED_TEST = """\
import org.junit.Test;
import static org.junit.Assert.*;

public class HardcodedTest {
    @Test
    public void testWithDummyValues() {
        assertEquals("foo", service.getName());
        assertEquals("bar", service.getValue());
    }

    @Test
    public void testWithRealisticValues() {
        assertEquals("John Doe", user.getFullName());
        assertEquals("john@example.com", user.getEmail());
    }
}
"""


class TestCountTests:
    def test_no_tests(self):
        assert _count_tests("class Foo {}") == 0

    def test_single_test(self):
        assert _count_tests("@Test\npublic void test() {}") == 1

    def test_multiple_tests(self):
        code = "@Test void a() {}\n@Test void b() {}\n@Test void c() {}"
        assert _count_tests(code) == 3

    def test_test_with_annotation_params(self):
        assert _count_tests("@Test(timeout=1000)\npublic void test() {}") == 1


class TestCountAssertions:
    def test_no_assertions(self):
        assert _count_assertions("void test() {}") == 0

    def test_assertions(self):
        code = "assertEquals(1, 1); assertTrue(ok); assertThrows(Exception.class, () -> {});"
        assert _count_assertions(code) == 3

    def test_mockito_verify(self):
        code = "verify(mock).doSomething(); verifyNoMoreInteractions(mock);"
        assert _count_assertions(code) == 2

    def test_mixed_assertions(self):
        code = 'assertNull(x); assertNotNull(y); assertSame(a, b); fail("oops");'
        assert _count_assertions(code) == 4


class TestAssessTestQuality:
    def test_empty_code(self):
        report = assess_test_quality("")
        assert report.score == 0.0
        assert report.trivial_flag is True
        assert report.test_count == 0

    def test_trivial_tests(self):
        report = assess_test_quality(TRIVIAL_TEST)
        assert report.test_count == 2
        assert report.assertion_count == 0
        assert report.trivial_flag is True
        assert report.score < 50.0
        assert len(report.issues) > 0

    def test_meaningful_tests(self):
        report = assess_test_quality(MEANINGFUL_TEST)
        assert report.test_count == 3
        assert report.assertion_count >= 4
        assert report.trivial_flag is False
        assert report.score > 50.0

    def test_mixed_tests(self):
        report = assess_test_quality(MIXED_TEST)
        assert report.test_count == 3
        assert report.assertion_count >= 2
        assert len(report.issues) > 0

    def test_duplicate_tests(self):
        report = assess_test_quality(DUPLICATE_TEST)
        assert report.test_count == 2
        assert report.assertion_count >= 2
        assert any("duplicate" in i.lower() for i in report.issues)

    def test_custom_threshold(self):
        report = assess_test_quality(TRIVIAL_TEST, quality_threshold=0.2)
        assert report.trivial_flag is True

    def test_high_threshold_flags_meaningful(self):
        report = assess_test_quality(MEANINGFUL_TEST, quality_threshold=0.99)
        assert report.trivial_flag is True


class TestQualityReport:
    def test_fields(self):
        report = QualityReport(
            score=75.0,
            trivial_flag=False,
            assertion_count=5,
            test_count=3,
            issues=[],
        )
        assert report.score == 75.0
        assert report.assertion_density == 0.0
        assert report.meaningfulness == 0.0
        assert report.diversity == 0.0


class TestNormalizeForComparison:
    def test_strips_annotation(self):
        result = _normalize_for_comparison("@Test\npublic void foo() { x; }")
        assert "@Test" not in result
        assert "METHOD" in result

    def test_strips_annotation_with_params(self):
        result = _normalize_for_comparison("@Test(timeout=100) public void foo() { x; }")
        assert "@Test" not in result

    def test_normalizes_whitespace(self):
        result = _normalize_for_comparison("  a  \n  b  \t  c  ")
        assert "  " not in result
