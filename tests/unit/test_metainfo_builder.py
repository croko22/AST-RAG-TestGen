"""Unit tests for metainfo builder module."""

from pathlib import Path
from types import SimpleNamespace

from core.metainfo.builder import MetainfoBuilder
from core.metainfo.database import MetainfoDatabase


def _parsed(
    *,
    name: str,
    package: str,
    file_path: str,
    methods: list[object] | None = None,
    fields: list[dict] | None = None,
) -> SimpleNamespace:
    return SimpleNamespace(
        name=name,
        package=package,
        file_path=file_path,
        content=f"class {name} {{}}",
        methods=methods or [],
        fields=fields or [],
    )


def test_builder_excludes_tests_by_default(tmp_path):
    project_root = tmp_path / "project"
    src_main = project_root / "src" / "main" / "java" / "com" / "example"
    src_test = project_root / "src" / "test" / "java" / "com" / "example"
    src_main.mkdir(parents=True)
    src_test.mkdir(parents=True)

    main_file = src_main / "Service.java"
    test_file = src_test / "ServiceTest.java"
    main_file.write_text("public class Service {}", encoding="utf-8")
    test_file.write_text("public class ServiceTest {}", encoding="utf-8")

    method = SimpleNamespace(
        name="getData",
        visibility="public",
        return_type="String",
        parameters=["String"],
        is_static=False,
    )
    parser_map = {
        str(main_file): _parsed(
            name="Service",
            package="com.example",
            file_path=str(main_file),
            methods=[method],
            fields=[{"name": "repo", "type": "Repository"}],
        ),
        str(test_file): _parsed(
            name="ServiceTest",
            package="com.example",
            file_path=str(test_file),
        ),
    }

    db = MetainfoDatabase(str(tmp_path / "metainfo.db"))
    builder = MetainfoBuilder(
        str(project_root),
        db,
        parser=lambda path: parser_map[path],
    )

    result = builder.build()

    assert result.files_scanned == 1
    assert result.classes_saved == 1
    assert result.methods_saved == 1
    assert result.fields_saved == 1
    assert result.failed_files == []
    assert db.get_class("com.example.Service") is not None
    assert db.get_class("com.example.ServiceTest") is None

    pkg = db.get_package("com.example")
    assert pkg is not None
    assert pkg.classes == ["com.example.Service"]


def test_builder_includes_tests_when_enabled_and_updates_package(tmp_path):
    project_root = tmp_path / "project"
    src_main = project_root / "src" / "main" / "java" / "com" / "example"
    src_test = project_root / "src" / "test" / "java" / "com" / "example"
    src_main.mkdir(parents=True)
    src_test.mkdir(parents=True)

    main_file = src_main / "AService.java"
    test_file = src_test / "BServiceTest.java"
    main_file.write_text("public class AService {}", encoding="utf-8")
    test_file.write_text("public class BServiceTest {}", encoding="utf-8")

    parser_map = {
        str(main_file): _parsed(name="AService", package="com.example", file_path=str(main_file)),
        str(test_file): _parsed(name="BServiceTest", package="com.example", file_path=str(test_file)),
    }

    db = MetainfoDatabase(str(tmp_path / "metainfo.db"))
    builder = MetainfoBuilder(
        str(project_root),
        db,
        parser=lambda path: parser_map[path],
    )

    result = builder.build(include_tests=True)

    assert result.files_scanned == 2
    assert result.classes_saved == 2
    pkg = db.get_package("com.example")
    assert pkg is not None
    assert pkg.classes == ["com.example.AService", "com.example.BServiceTest"]


def test_builder_collects_failed_files_and_continues(tmp_path):
    project_root = tmp_path / "project"
    src_main = project_root / "src" / "main" / "java" / "com" / "example"
    src_main.mkdir(parents=True)

    ok_file = src_main / "OkService.java"
    bad_file = src_main / "BadService.java"
    ok_file.write_text("public class OkService {}", encoding="utf-8")
    bad_file.write_text("public class BadService {}", encoding="utf-8")

    def parser(path: str):
        if path == str(bad_file):
            raise ValueError("parse error")
        return _parsed(name="OkService", package="com.example", file_path=path)

    db = MetainfoDatabase(str(tmp_path / "metainfo.db"))
    builder = MetainfoBuilder(str(project_root), db, parser=parser)

    result = builder.build()

    assert result.files_scanned == 2
    assert result.classes_saved == 1
    assert str(bad_file) in result.failed_files
    assert len(result.failed_files) == 1
    assert db.get_class("com.example.OkService") is not None
