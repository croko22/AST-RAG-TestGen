"""Tests for CLI benchmark mode wiring and backward compatibility."""

from argparse import Namespace
from pathlib import Path
from unittest.mock import MagicMock, patch

import pytest

import main
from main import build_arg_parser


class TestBenchmarkModeDetection:
    """Test that benchmark mode is correctly detected from CLI args."""

    def test_benchmark_manifest_flag_enables_benchmark_mode(self):
        """--benchmark-manifest flag should enable benchmark mode."""
        parser = build_arg_parser()
        args = parser.parse_args(["--benchmark-manifest", "bench.yaml"])

        assert args.benchmark_manifest == "bench.yaml"
        # Path is resolved to absolute path by config system
        assert "benchmark_results" in args.benchmark_output
        assert args.benchmark_dry_run is False

    def test_benchmark_manifest_with_custom_output(self):
        """Custom output directory should be accepted."""
        parser = build_arg_parser()
        args = parser.parse_args([
            "--benchmark-manifest", "bench.yaml",
            "--benchmark-output", "./my_results",
        ])

        assert args.benchmark_manifest == "bench.yaml"
        assert args.benchmark_output == "./my_results"

    def test_benchmark_dry_run_flag(self):
        """--benchmark-dry-run should be accepted."""
        parser = build_arg_parser()
        args = parser.parse_args([
            "--benchmark-manifest", "bench.yaml",
            "--benchmark-dry-run",
        ])

        assert args.benchmark_dry_run is True

    def test_no_args_enables_legacy_mode(self):
        """Without --benchmark-manifest, should be legacy mode."""
        parser = build_arg_parser()
        args = parser.parse_args(["service.java", "project/"])

        assert args.benchmark_manifest is None
        assert args.java_file == "service.java"
        assert args.project_path == "project/"

    def test_java_file_and_project_path_optional_when_benchmark_enabled(self):
        """With benchmark mode, positional args should be optional."""
        parser = build_arg_parser()
        args = parser.parse_args(["--benchmark-manifest", "bench.yaml"])

        assert args.benchmark_manifest == "bench.yaml"
        assert args.java_file is None
        assert args.project_path is None


class TestBenchmarkModeExecution:
    """Test that benchmark mode executes correctly."""

    @patch("main.run_benchmark_mode")
    def test_main_routes_to_benchmark_mode(self, mock_benchmark):
        """main() should call run_benchmark_mode when benchmark flag is provided."""
        mock_benchmark.return_value = 0

        args = Namespace(
            benchmark_manifest="bench.yaml",
            benchmark_output="./results",
            benchmark_dry_run=False,
            java_file=None,
            project_path=None,
            provider="anthropic",
            model="claude-3-5-sonnet-20241022",
            output="./tests_generados",
            max_deps=10,
            enable_metainfo_db=False,
            enable_reftest_parity=False,
            print=False,
        )

        with patch.object(main, "build_arg_parser") as mock_parser:
            mock_parser.return_value.parse_args.return_value = args
            result = main.main()

        mock_benchmark.assert_called_once_with(
            manifest_path="bench.yaml",
            output_dir="./results",
            dry_run=False,
        )
        assert result == 0

    @patch("main.run_benchmark_mode")
    def test_main_routes_to_benchmark_mode_with_dry_run(self, mock_benchmark):
        """main() should pass dry_run flag to benchmark mode."""
        mock_benchmark.return_value = 0

        args = Namespace(
            benchmark_manifest="bench.yaml",
            benchmark_output="./results",
            benchmark_dry_run=True,
            java_file=None,
            project_path=None,
        )

        with patch.object(main, "build_arg_parser") as mock_parser:
            mock_parser.return_value.parse_args.return_value = args
            result = main.main()

        mock_benchmark.assert_called_once_with(
            manifest_path="bench.yaml",
            output_dir="./results",
            dry_run=True,
        )

    @patch("main.run_benchmark_mode")
    def test_main_returns_benchmark_exit_code(self, mock_benchmark):
        """main() should return benchmark mode exit code."""
        mock_benchmark.return_value = 1

        args = Namespace(
            benchmark_manifest="bench.yaml",
            benchmark_output="./results",
            benchmark_dry_run=False,
            java_file=None,
            project_path=None,
        )

        with patch.object(main, "build_arg_parser") as mock_parser:
            mock_parser.return_value.parse_args.return_value = args
            result = main.main()

        assert result == 1


class TestLegacyModeBackwardCompatibility:
    """Test that legacy mode is unchanged."""

    @patch("main.legacy_main")
    def test_main_routes_to_legacy_mode_without_benchmark_flag(self, mock_legacy):
        """main() should call legacy_main() when no benchmark flag."""
        mock_legacy.return_value = None

        args = Namespace(
            benchmark_manifest=None,
            java_file="service.java",
            project_path="project/",
            provider="anthropic",
            model="claude-3-5-sonnet-20241022",
            output="./tests_generados",
            max_deps=10,
            enable_metainfo_db=False,
            enable_reftest_parity=False,
            print=False,
        )

        with patch.object(main, "build_arg_parser") as mock_parser:
            mock_parser.return_value.parse_args.return_value = args
            main.main()

        mock_legacy.assert_called_once()

    @patch("main.legacy_main")
    def test_legacy_mode_preserves_all_legacy_args(self, mock_legacy):
        """Legacy mode should receive all original arguments."""
        captured_args = None

        def capture_args(args):
            nonlocal captured_args
            captured_args = args

        mock_legacy.side_effect = capture_args

        args = Namespace(
            benchmark_manifest=None,
            java_file="MyService.java",
            project_path="./my-project",
            provider="openai",
            model="gpt-4",
            output="./custom_output",
            max_deps=5,
            enable_metainfo_db=True,
            enable_reftest_parity=True,
            print=True,
        )

        with patch.object(main, "build_arg_parser") as mock_parser:
            mock_parser.return_value.parse_args.return_value = args
            main.main()

        assert captured_args is not None
        assert captured_args.java_file == "MyService.java"
        assert captured_args.project_path == "./my-project"
        assert captured_args.provider == "openai"
        assert captured_args.model == "gpt-4"
        assert captured_args.output == "./custom_output"
        assert captured_args.max_deps == 5
        assert captured_args.enable_metainfo_db is True
        assert captured_args.enable_reftest_parity is True
        assert captured_args.print is True

    @patch("main.legacy_main")
    def test_legacy_mode_default_values(self, mock_legacy):
        """Legacy mode should use default values."""
        captured_args = None

        def capture_args(args):
            nonlocal captured_args
            captured_args = args

        mock_legacy.side_effect = capture_args

        args = Namespace(
            benchmark_manifest=None,
            java_file="service.java",
            project_path="project/",
            provider="anthropic",
            model="claude-3-5-sonnet-20241022",
            output="./tests_generados",
            max_deps=10,
            enable_metainfo_db=False,
            enable_reftest_parity=False,
            print=False,
        )

        with patch.object(main, "build_arg_parser") as mock_parser:
            mock_parser.return_value.parse_args.return_value = args
            main.main()

        assert captured_args is not None
        assert captured_args.provider == "anthropic"
        assert captured_args.model == "claude-3-5-sonnet-20241022"
        assert captured_args.output == "./tests_generados"
        assert captured_args.max_deps == 10


class TestRunBenchmarkMode:
    """Test the run_benchmark_mode function directly."""

    @patch("benchmark.manifest.load_manifest")
    @patch("benchmark.manifest.validate_manifest_preflight")
    @patch("benchmark.planner.plan_runs")
    @patch("benchmark.runner.execute_runs")
    @patch("benchmark.reporter.build_report")
    @patch("benchmark.reporter.export_thesis_metrics_csv")
    def test_run_benchmark_mode_success(
        self, mock_csv, mock_report, mock_execute, mock_plan, mock_preflight, mock_load
    ):
        """run_benchmark_mode should complete all phases."""
        from benchmark.schemas import BenchmarkManifest

        mock_manifest = MagicMock(spec=BenchmarkManifest)
        mock_manifest.manifest_version = 1
        mock_manifest.evaluation = MagicMock()
        mock_load.return_value = mock_manifest
        mock_preflight.return_value = []
        mock_plan.return_value = [MagicMock(), MagicMock()]

        # execute_runs is called once per plan when rich is available (for progress bar)
        mock_result1 = MagicMock(status="ok", latency_ms=1000, provider="anthropic", model="claude-3-5-sonnet-20241022", metrics=MagicMock(coverage_pct=None))
        mock_result2 = MagicMock(status="ok", latency_ms=2000, provider="anthropic", model="claude-3-5-sonnet-20241022", metrics=MagicMock(coverage_pct=None))
        mock_execute.side_effect = [[mock_result1], [mock_result2]]

        mock_report.return_value = MagicMock(
            results_path=Path("results.json"),
            summary_path=Path("summary.json"),
            report_path=Path("report.md"),
            provenance_path=Path("provenance.json"),
        )
        mock_csv.return_value = Path("thesis_metrics.csv")

        result = main.run_benchmark_mode(
            manifest_path="bench.yaml",
            output_dir="./results",
            dry_run=False,
        )

        assert result == 0
        mock_load.assert_called_once_with("bench.yaml")
        mock_plan.assert_called_once()
        # execute_runs is called once per plan (2 plans = 2 calls)
        assert mock_execute.call_count == 2
        mock_report.assert_called_once()
        mock_csv.assert_called_once()

    @patch("benchmark.manifest.load_manifest")
    def test_run_benchmark_mode_invalid_manifest(self, mock_load):
        """run_benchmark_mode should return error code on invalid manifest."""
        from benchmark.manifest import ManifestValidationError

        mock_load.side_effect = ManifestValidationError("Invalid manifest")

        result = main.run_benchmark_mode(
            manifest_path="invalid.yaml",
            output_dir="./results",
            dry_run=False,
        )

        assert result == 1

    @patch("main.Path")
    def test_run_benchmark_mode_manifest_not_found(self, mock_path):
        """run_benchmark_mode should return error code when manifest not found."""
        mock_path_instance = MagicMock()
        mock_path_instance.exists.return_value = False
        mock_path.return_value = mock_path_instance

        result = main.run_benchmark_mode(
            manifest_path="missing.yaml",
            output_dir="./results",
            dry_run=False,
        )

        assert result == 1


class TestArgParserExamples:
    """Test documented examples work correctly."""

    def test_example_benchmark_command_parses(self):
        """The documented benchmark example should parse."""
        parser = build_arg_parser()
        args = parser.parse_args([
            "--benchmark-manifest", "benchmark.yaml",
            "--benchmark-output", "./benchmark_results",
        ])

        assert args.benchmark_manifest == "benchmark.yaml"
        assert args.benchmark_output == "./benchmark_results"

    def test_example_legacy_command_parses(self):
        """The documented legacy examples should parse."""
        parser = build_arg_parser()

        args = parser.parse_args(["service.java", "project/"])
        assert args.java_file == "service.java"
        assert args.project_path == "project/"

        args = parser.parse_args([
            "service.java", "project/",
            "--provider", "openai",
            "--model", "gpt-4-turbo",
        ])
        assert args.provider == "openai"
        assert args.model == "gpt-4-turbo"
