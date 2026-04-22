"""Post-processing pipeline: validation and quality assessment for generated tests."""

from postproc.quality import QualityReport, assess_test_quality
from postproc.validator import (
    CompileResult,
    CoverageResult,
    TestRunResult,
    parse_coverage,
    run_tests,
    validate_compilation,
)

__all__ = [
    "CompileResult",
    "CoverageResult",
    "TestRunResult",
    "QualityReport",
    "validate_compilation",
    "run_tests",
    "parse_coverage",
    "assess_test_quality",
]
