"""Unit tests for postproc/validator.py (T11)."""

from __future__ import annotations

import xml.etree.ElementTree as ET
from pathlib import Path

from postproc.validator import (
    CompileResult,
    CoverageResult,
    TestRunResult,
    _classify_output,
    _parse_test_totals,
    parse_coverage,
    run_tests,
    validate_compilation,
)


def _write_jacoco_xml(
    path: Path,
    line_missed: int = 2,
    line_covered: int = 8,
    branch_missed: int = 1,
    branch_covered: int = 3,
) -> Path:
    report = ET.Element("report", name="Test")
    pkg = ET.SubElement(report, "package", name="com/example")
    cls = ET.SubElement(pkg, "class", name="com/example/Service")
    method = ET.SubElement(cls, "method", name="getData")
    ET.SubElement(
        method, "counter", type="LINE", missed=str(line_missed), covered=str(line_covered)
    )
    ET.SubElement(
        method, "counter", type="BRANCH", missed=str(branch_missed), covered=str(branch_covered)
    )
    ET.SubElement(cls, "counter", type="LINE", missed=str(line_missed), covered=str(line_covered))
    ET.SubElement(
        cls, "counter", type="BRANCH", missed=str(branch_missed), covered=str(branch_covered)
    )
    ET.SubElement(pkg, "counter", type="LINE", missed=str(line_missed), covered=str(line_covered))
    ET.SubElement(
        pkg, "counter", type="BRANCH", missed=str(branch_missed), covered=str(branch_covered)
    )
    ET.SubElement(
        report, "counter", type="LINE", missed=str(line_missed), covered=str(line_covered)
    )
    ET.SubElement(
        report, "counter", type="BRANCH", missed=str(branch_missed), covered=str(branch_covered)
    )
    tree = ET.ElementTree(report)
    ET.indent(tree, space="  ")
    tree.write(str(path), encoding="unicode", xml_declaration=True)
    return path


class TestCompileResult:
    def test_defaults(self):
        r = CompileResult(success=True)
        assert r.success is True
        assert r.errors == []
        assert r.warnings == []
        assert r.returncode == -1

    def test_with_errors(self):
        r = CompileResult(success=False, errors=["compilation error"], returncode=1)
        assert len(r.errors) == 1
        assert r.returncode == 1


class TestTestRunResult:
    def test_defaults(self):
        r = TestRunResult(success=True)
        assert r.passed == 0
        assert r.failed == 0
        assert r.test_count == 0

    def test_with_counts(self):
        r = TestRunResult(success=True, passed=3, failed=1, errors=0, test_count=4)
        assert r.test_count == 4


class TestCoverageResult:
    def test_defaults(self):
        r = CoverageResult()
        assert r.line_pct is None
        assert r.branch_pct is None
        assert r.missed_lines == 0

    def test_with_values(self):
        r = CoverageResult(line_pct=80.0, branch_pct=75.0, missed_lines=2, covered_lines=8)
        assert r.line_pct == 80.0
        assert r.branch_pct == 75.0


class TestValidateCompilation:
    def test_missing_file(self, tmp_path):
        result = validate_compilation(
            tmp_path / "nofile.java",
            tmp_path,
            compile_cmd="javac {file}",
        )
        assert result.success is False
        assert any("not found" in e for e in result.errors)

    def test_no_compile_cmd(self, tmp_path):
        test_file = tmp_path / "Test.java"
        test_file.write_text("class Test {}")
        result = validate_compilation(test_file, tmp_path)
        assert result.success is False
        assert any("No compile command" in e for e in result.errors)

    def test_compile_success(self, tmp_path):
        test_file = tmp_path / "dummy.txt"
        test_file.write_text("ok")
        result = validate_compilation(
            test_file,
            tmp_path,
            compile_cmd="echo OK",
        )
        assert result.success is True


class TestRunTests:
    def test_missing_file(self, tmp_path):
        result = run_tests(
            tmp_path / "nofile.java",
            tmp_path,
            test_cmd="mvn test",
        )
        assert result.success is False

    def test_no_test_cmd(self, tmp_path):
        test_file = tmp_path / "Test.java"
        test_file.write_text("class Test {}")
        result = run_tests(test_file, tmp_path)
        assert result.success is False

    def test_run_success(self, tmp_path):
        test_file = tmp_path / "dummy.txt"
        test_file.write_text("ok")
        result = run_tests(
            test_file,
            tmp_path,
            test_cmd="echo 'Tests run: 3, Failures: 0, Errors: 0'",
        )
        assert result.success is True
        assert result.test_count == 3
        assert result.passed == 3
        assert result.failed == 0


class TestParseCoverage:
    def test_missing_file(self, tmp_path):
        result = parse_coverage(tmp_path / "no_jacoco.xml")
        assert result.line_pct is None
        assert result.branch_pct is None

    def test_valid_xml(self, tmp_path):
        xml_path = _write_jacoco_xml(tmp_path / "jacoco.xml")
        result = parse_coverage(xml_path)
        assert result.line_pct == 80.0
        assert result.branch_pct == 75.0
        assert result.missed_lines == 2
        assert result.covered_lines == 8
        assert result.missed_branches == 1
        assert result.covered_branches == 3

    def test_malformed_xml(self, tmp_path):
        xml_path = tmp_path / "bad.xml"
        xml_path.write_text("<not valid xml<<")
        result = parse_coverage(xml_path)
        assert result.line_pct is None

    def test_no_counters(self, tmp_path):
        xml_path = tmp_path / "empty_report.xml"
        report = ET.Element("report", name="Test")
        tree = ET.ElementTree(report)
        tree.write(str(xml_path), encoding="unicode", xml_declaration=True)
        result = parse_coverage(xml_path)
        assert result.line_pct is None
        assert result.branch_pct is None

    def test_zero_coverage(self, tmp_path):
        xml_path = _write_jacoco_xml(tmp_path / "jacoco.xml", line_missed=10, line_covered=0)
        result = parse_coverage(xml_path)
        assert result.line_pct == 0.0
        assert result.missed_lines == 10
        assert result.covered_lines == 0

    def test_full_coverage(self, tmp_path):
        xml_path = _write_jacoco_xml(tmp_path / "jacoco.xml", line_missed=0, line_covered=10)
        result = parse_coverage(xml_path)
        assert result.line_pct == 100.0

    def test_branch_only(self, tmp_path):
        xml_path = tmp_path / "branch_only.xml"
        report = ET.Element("report", name="Test")
        ET.SubElement(report, "counter", type="BRANCH", missed="2", covered="8")
        tree = ET.ElementTree(report)
        tree.write(str(xml_path), encoding="unicode", xml_declaration=True)
        result = parse_coverage(xml_path)
        assert result.line_pct is None
        assert result.branch_pct == 80.0


class TestClassifyOutput:
    def test_empty(self):
        errors, warnings = _classify_output("")
        assert errors == []
        assert warnings == []

    def test_errors_only(self):
        errors, warnings = _classify_output("error: cannot find symbol\nerror: class not found")
        assert len(errors) == 2
        assert len(warnings) == 0

    def test_warnings_only(self):
        errors, warnings = _classify_output("warning: unchecked cast\nwarning: deprecated API")
        assert len(errors) == 0
        assert len(warnings) == 2

    def test_mixed(self):
        output = "error: bad code\nwarning: minor issue\n[INFO] Building project"
        errors, warnings = _classify_output(output)
        assert len(errors) == 1
        assert len(warnings) == 1


class TestParseTestTotals:
    def test_maven_surefire_format(self):
        passed, failed, errors = _parse_test_totals("Tests run: 10, Failures: 2, Errors: 1")
        assert passed == 7
        assert failed == 2
        assert errors == 1

    def test_all_passing(self):
        passed, failed, errors = _parse_test_totals("Tests run: 5, Failures: 0, Errors: 0")
        assert passed == 5
        assert failed == 0

    def test_no_match(self):
        passed, failed, errors = _parse_test_totals("Build success")
        assert passed == 0
        assert failed == 0
        assert errors == 0
