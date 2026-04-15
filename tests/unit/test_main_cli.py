"""Unit tests for CLI flag handling in main module."""

from argparse import Namespace
from pathlib import Path

import main
from cli.parser import build_arg_parser


class TestMainCli:
    def test_feature_flags_default_to_disabled(self):
        """Experimental flags are disabled unless explicitly provided."""
        parser = build_arg_parser()

        args = parser.parse_args(["service.java", "project/"])

        assert args.enable_metainfo_db is False
        assert args.enable_reftest_parity is False

    def test_feature_flags_can_be_enabled_explicitly(self):
        """Experimental flags can be enabled from CLI."""
        parser = build_arg_parser()

        args = parser.parse_args(
            [
                "service.java",
                "project/",
                "--enable-metainfo-db",
                "--enable-reftest-parity",
            ]
        )

        assert args.enable_metainfo_db is True
        assert args.enable_reftest_parity is True

    def test_main_exits_when_java_file_is_missing(self, monkeypatch, capsys):
        args = Namespace(
            java_file="missing.java",
            project_path="project/",
            output="./tests_generados",
            max_deps=10,
            provider="anthropic",
            model="claude-3-5-sonnet-20241022",
            enable_metainfo_db=False,
            enable_reftest_parity=False,
            print=False,
        )

        class DummyParser:
            def parse_args(self):
                return args

        monkeypatch.setattr("cli.legacy.build_arg_parser", lambda: DummyParser())
        monkeypatch.setattr(
            Path,
            "exists",
            lambda p: False if str(p) == "missing.java" else True,
        )

        exit_code = main.main()

        assert exit_code == 1
        out = capsys.readouterr().out
        assert "Java file not found: missing.java" in out

    def test_main_exits_when_project_path_is_missing(self, monkeypatch, capsys):
        args = Namespace(
            java_file="service.java",
            project_path="missing-project/",
            output="./tests_generados",
            max_deps=10,
            provider="anthropic",
            model="claude-3-5-sonnet-20241022",
            enable_metainfo_db=False,
            enable_reftest_parity=False,
            print=False,
        )

        class DummyParser:
            def parse_args(self):
                return args

        monkeypatch.setattr("cli.legacy.build_arg_parser", lambda: DummyParser())
        monkeypatch.setattr(
            Path,
            "exists",
            lambda p: False if str(p) == "missing-project" else True,
        )

        exit_code = main.main()

        assert exit_code == 1
        out = capsys.readouterr().out
        assert "Project path not found: missing-project/" in out

    def test_main_runs_generator_and_prints_output_when_requested(self, monkeypatch, capsys):
        args = Namespace(
            java_file="service.java",
            project_path="project/",
            output="./tests_generados",
            max_deps=3,
            provider="openai",
            model="gpt-4-turbo",
            enable_metainfo_db=True,
            enable_reftest_parity=True,
            print=True,
        )

        class DummyParser:
            def parse_args(self):
                return args

        monkeypatch.setattr("cli.legacy.build_arg_parser", lambda: DummyParser())
        monkeypatch.setattr(Path, "exists", lambda _: True)

        def fake_generate_test_for_file(**kwargs):
            assert kwargs["java_file_path"] == "service.java"
            assert kwargs["java_project_path"] == "project/"
            assert kwargs["output_dir"] == "./tests_generados"
            assert kwargs["max_dependencies"] == 3
            assert kwargs["llm_provider"] == "openai"
            assert kwargs["llm_model"] == "gpt-4-turbo"
            assert kwargs["enable_metainfo_db"] is True
            assert kwargs["enable_reftest_parity"] is True
            return "public class ServiceTest {}"

        monkeypatch.setattr(
            "orchestration.generator.generate_test_for_file", fake_generate_test_for_file
        )

        exit_code = main.main()

        assert exit_code == 0
        output = capsys.readouterr().out
        assert "public class ServiceTest {}" in output
        assert "Test saved to ./tests_generados/serviceTest.java" in output

    def test_main_exits_when_generation_raises_exception(self, monkeypatch, capsys):
        args = Namespace(
            java_file="service.java",
            project_path="project/",
            output="./tests_generados",
            max_deps=10,
            provider="anthropic",
            model="claude-3-5-sonnet-20241022",
            enable_metainfo_db=False,
            enable_reftest_parity=False,
            print=False,
        )

        class DummyParser:
            def parse_args(self):
                return args

        monkeypatch.setattr("cli.legacy.build_arg_parser", lambda: DummyParser())
        monkeypatch.setattr(Path, "exists", lambda _: True)

        def fake_generate_test_for_file(**_):
            raise RuntimeError("boom")

        monkeypatch.setattr(
            "orchestration.generator.generate_test_for_file", fake_generate_test_for_file
        )

        exit_code = main.main()

        assert exit_code == 1
        assert "Error: boom" in capsys.readouterr().err
