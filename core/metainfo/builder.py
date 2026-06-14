"""Project-wide builder for metainfo entities.

Parses Java source files and persists normalized entities into MetainfoDatabase.
"""

from collections.abc import Callable, Iterable
from dataclasses import dataclass, field
from pathlib import Path
from typing import TYPE_CHECKING

from core.parsing.scanner import JavaFileScanner
from .database import MetainfoDatabase
from .schemas import ClassInfo, FieldInfo, MethodInfo, PackageInfo

if TYPE_CHECKING:
    from core.parsing.models import ParsedJavaClass


@dataclass
class BuildResult:
    """Result summary for metainfo build operations."""

    files_scanned: int = 0
    classes_saved: int = 0
    methods_saved: int = 0
    fields_saved: int = 0
    packages_saved: int = 0
    failed_files: list[str] = field(default_factory=list)


class MetainfoBuilder:
    """Build metainfo records for a Java project."""

    def __init__(
        self,
        project_root: str,
        database: MetainfoDatabase,
        parser: Callable[[str], "ParsedJavaClass"] | None = None,
    ):
        self.project_root = Path(project_root)
        self.database = database
        self.parser = parser or self._default_parser

    @staticmethod
    def _default_parser(file_path: str) -> "ParsedJavaClass":
        from core.parsing.parser import JavaParser

        return JavaParser().parse_file(file_path)

    def build(self, include_tests: bool = False) -> BuildResult:
        """Parse the project and persist all discovered entities."""
        result = BuildResult()

        for file_path in self._iter_java_files(include_tests=include_tests):
            result.files_scanned += 1
            try:
                parsed = self.parser(str(file_path))
                self._persist_parsed_file(parsed, result)
            except Exception:
                result.failed_files.append(str(file_path))

        return result

    def _iter_java_files(self, include_tests: bool) -> Iterable[Path]:
        scanner = JavaFileScanner(self.project_root)
        yield from sorted(scanner.scan(include_tests=include_tests))

    def _persist_parsed_file(self, parsed: "ParsedJavaClass", result: BuildResult) -> None:
        class_uri = self._class_uri(parsed)

        self.database.save_class(
            ClassInfo(
                uri=class_uri,
                name=parsed.name,
                file_path=parsed.file_path,
                package=parsed.package,
                class_docstring=None,
                original_string=parsed.content,
            )
        )
        result.classes_saved += 1

        if parsed.package:
            self._save_or_update_package(parsed.package, class_uri)
            result.packages_saved += 1

        for method in parsed.methods:
            parameters = [
                {"name": f"arg{i}", "type": param_type}
                for i, param_type in enumerate(method.parameters)
            ]
            signature = MetainfoDatabase.build_method_signature(parameters)
            self.database.save_method(
                MethodInfo(
                    uri=MetainfoDatabase.build_canonical_method_uri(
                        class_uri, method.name, signature
                    ),
                    name=method.name,
                    class_uri=class_uri,
                    visibility=method.visibility,
                    return_type=method.return_type,
                    parameters=parameters,
                    modifiers=self._build_modifiers(method.visibility, method.is_static),
                    is_static=method.is_static,
                    docstring=None,
                    original_string=None,
                    signature=signature,
                    legacy_uri=MetainfoDatabase.build_legacy_method_uri(class_uri, method.name),
                )
            )
            result.methods_saved += 1

        for parsed_field in parsed.fields:
            field_name = parsed_field.get("name")
            field_type = parsed_field.get("type")
            if not field_name or not field_type:
                continue
            self.database.save_field(
                FieldInfo(
                    uri=f"{class_uri}.{field_name}",
                    name=field_name,
                    class_uri=class_uri,
                    type=field_type,
                    docstring=None,
                )
            )
            result.fields_saved += 1

    def _save_or_update_package(self, package_name: str, class_uri: str) -> None:
        existing = self.database.get_package(package_name)
        if existing:
            classes = set(existing.classes)
            classes.add(class_uri)
            existing.classes = sorted(classes)
            self.database.save_package(existing)
            return

        self.database.save_package(
            PackageInfo(uri=package_name, name=package_name, file_path=None, classes=[class_uri])
        )

    @staticmethod
    def _class_uri(parsed: "ParsedJavaClass") -> str:
        return f"{parsed.package}.{parsed.name}" if parsed.package else parsed.name

    @staticmethod
    def _build_modifiers(visibility: str, is_static: bool) -> list[str]:
        modifiers = [visibility]
        if is_static:
            modifiers.append("static")
        return modifiers
