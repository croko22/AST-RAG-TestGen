"""Unit tests for parser module."""

from pathlib import Path

import pytest

from core.parsing.parser import JavaParser


class TestJavaParser:
    def test_parse_simple_class(self, sample_java_file):
        """Test parsing a simple Java class."""
        parser = JavaParser()
        result = parser.parse_file(sample_java_file)

        assert result.name == "TestService"
        assert result.package == "com.example.demo"
        assert len(result.imports) >= 1
        assert "java.util.List" in result.imports

    def test_extract_methods(self, sample_java_file):
        """Test method signature extraction."""
        result = JavaParser().parse_file(sample_java_file)

        assert len(result.methods) >= 1
        method = result.methods[0]
        assert method.name == "getData"
        assert method.return_type == "String"
        assert method.visibility == "public"

    def test_extract_fields(self, sample_java_file):
        """Test field extraction."""
        result = JavaParser().parse_file(sample_java_file)

        assert len(result.fields) >= 1
        field = result.fields[0]
        assert field.type == "Repository"
        assert field.name == "repo"

    def test_parse_nonexistent_file(self):
        """Test that non-existent files raise FileNotFoundError."""
        parser = JavaParser()
        with pytest.raises(FileNotFoundError):
            parser.parse_file("/nonexistent/Path.java")

    @pytest.mark.parametrize(
        "code,expected",
        [
            ("public void test() {}", "public"),
            ("private void test() {}", "private"),
            ("protected void test() {}", "protected"),
            ("void test() {}", "package-private"),
        ],
    )
    def test_visibility_extraction(self, code, expected, tmp_path):
        """Test visibility modifier extraction with different modifiers."""
        file = tmp_path / "Test.java"
        file.write_text(f"class Test {{{code}}}")
        result = JavaParser().parse_file(str(file))
        assert result.methods[0].visibility == expected

    def test_filter_standard_library_imports(self, sample_java_file):
        """Test that standard library imports are filtered from dependencies."""
        result = JavaParser().parse_file(sample_java_file)

        dep_names = [dep.name for dep in result.dependencies]
        assert "List" not in dep_names

    def test_content_is_preserved(self, sample_java_file):
        """Test that full file content is preserved."""
        result = JavaParser().parse_file(sample_java_file)

        original_content = Path(sample_java_file).read_text()
        assert result.content == original_content

    def test_static_method_detection(self, tmp_path):
        """Test detection of static methods."""
        file = tmp_path / "Test.java"
        file.write_text("class Test { public static void main(String[] args) {} }")
        result = JavaParser().parse_file(str(file))

        static_method = [m for m in result.methods if m.name == "main"][0]
        assert static_method.is_static is True

    def test_parse_interface(self, tmp_path):
        """Test parsing Java interface."""
        file = tmp_path / "TestInterface.java"
        file.write_text("""
package com.test;
public interface TestInterface {
    String getData();
    void setData(String data);
}
""")
        result = JavaParser().parse_file(str(file))
        assert result.name == "TestInterface"
        assert len(result.methods) == 2
        method_names = [m.name for m in result.methods]
        assert "getData" in method_names
        assert "setData" in method_names

    def test_parse_with_static_imports(self, tmp_path):
        """Test parsing with static imports."""
        file = tmp_path / "Test.java"
        file.write_text("""
package com.test;
import static java.util.Collections.emptyList;
import java.util.List;
public class Test {}
""")
        result = JavaParser().parse_file(str(file))
        assert "java.util.List" in result.imports
        assert any("Collections" in imp for imp in result.imports)

    def test_parse_with_wildcard_import(self, tmp_path):
        """Test parsing with wildcard imports."""
        file = tmp_path / "Test.java"
        file.write_text("""
package com.test;
import java.util.*;
public class Test {}
""")
        result = JavaParser().parse_file(str(file))
        assert "java.util" in result.imports

    def test_parse_with_spring_imports(self, tmp_path):
        """Test that Spring imports are filtered."""
        file = tmp_path / "Test.java"
        file.write_text("""
package com.test;
import org.springframework.stereotype.Service;
import com.example.Custom;
public class Test {}
""")
        result = JavaParser().parse_file(str(file))
        dep_names = [d.name for d in result.dependencies]
        assert "Service" not in dep_names
        assert "Custom" in dep_names

    def test_parse_with_parameters(self, tmp_path):
        """Test method with parameters."""
        file = tmp_path / "Test.java"
        file.write_text("""
class Test {
    public String process(String input, int count) {
        return input;
    }
}
""")
        result = JavaParser().parse_file(str(file))
        assert len(result.methods) == 1
        method = result.methods[0]
        assert method.name == "process"
        assert method.return_type == "String"

    def test_parse_no_package(self, tmp_path):
        """Test parsing file without package declaration."""
        file = tmp_path / "Test.java"
        file.write_text("public class Test {}")
        result = JavaParser().parse_file(str(file))
        assert result.name == "Test"
        assert result.package is None or result.package == ""

    def test_parse_custom_import(self, tmp_path):
        """Test custom import creates dependency."""
        file = tmp_path / "Test.java"
        file.write_text("""
package com.test;
import com.example.MyClass;
public class Test {}
""")
        result = JavaParser().parse_file(str(file))
        dep_names = [d.name for d in result.dependencies]
        assert "MyClass" in dep_names
