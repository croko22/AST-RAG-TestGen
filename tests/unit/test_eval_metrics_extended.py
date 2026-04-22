"""Unit tests for enhanced EvalMetrics (T03)."""

from benchmark.types import EvalMetrics, PipelineTimings


class TestPipelineTimings:
    def test_defaults(self):
        t = PipelineTimings()
        assert t.parse_ms == 0
        assert t.retrieval_ms == 0
        assert t.prompt_ms == 0
        assert t.llm_ms == 0
        assert t.postproc_ms == 0

    def test_construction(self):
        t = PipelineTimings(
            parse_ms=100, retrieval_ms=50, prompt_ms=10, llm_ms=2000, postproc_ms=500
        )
        assert t.parse_ms == 100
        total = t.parse_ms + t.retrieval_ms + t.prompt_ms + t.llm_ms + t.postproc_ms
        assert total == 2660


class TestEvalMetricsExtended:
    def test_existing_fields_unchanged(self):
        m = EvalMetrics(compile_pass=True, test_pass=True)
        assert m.compile_pass is True
        assert m.test_pass is True
        assert m.coverage_pct is None
        assert m.coverage_source is None
        assert m.failure_type is None

    def test_new_fields_defaults(self):
        m = EvalMetrics(compile_pass=True, test_pass=True)
        assert m.branch_coverage_pct is None
        assert m.generation_time_ms == 0
        assert m.assertion_count == 0
        assert m.trivial_flag is False
        assert m.test_count == 0
        assert isinstance(m.timings, PipelineTimings)

    def test_new_fields_set(self):
        m = EvalMetrics(
            compile_pass=True,
            test_pass=True,
            branch_coverage_pct=75.0,
            generation_time_ms=3500,
            assertion_count=5,
            trivial_flag=False,
            test_count=3,
        )
        assert m.branch_coverage_pct == 75.0
        assert m.generation_time_ms == 3500
        assert m.assertion_count == 5
        assert m.test_count == 3

    def test_backward_compat_no_new_args(self):
        m = EvalMetrics(compile_pass=False, test_pass=False, failure_message="compile error")
        assert m.failure_message == "compile error"
        assert m.branch_coverage_pct is None
        assert m.timings.parse_ms == 0


class TestQualityScore:
    def test_trivial_flag_zero(self):
        m = EvalMetrics(compile_pass=True, test_pass=True, trivial_flag=True)
        assert m.quality_score == 0.0

    def test_zero_test_count(self):
        m = EvalMetrics(compile_pass=True, test_pass=True, test_count=0)
        assert m.quality_score == 0.0

    def test_good_metrics(self):
        m = EvalMetrics(
            compile_pass=True,
            test_pass=True,
            test_count=4,
            assertion_count=8,
            coverage_pct=80.0,
        )
        score = m.quality_score
        assert 0.0 < score <= 1.0
        expected_base = 8 / 4
        expected_coverage = 80.0 / 100.0
        expected = min(expected_base * 0.6 + expected_coverage * 0.4, 1.0)
        assert abs(score - expected) < 0.001

    def test_no_coverage(self):
        m = EvalMetrics(
            compile_pass=True,
            test_pass=True,
            test_count=2,
            assertion_count=4,
            coverage_pct=None,
        )
        score = m.quality_score
        expected = min(4 / 2 * 0.6 + 0.0 * 0.4, 1.0)
        assert abs(score - expected) < 0.001
