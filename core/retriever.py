"""
Retriever module for finding Java source files in a project.
Handles file discovery and class location.
"""

from pathlib import Path
from typing import List, Optional, Dict
import fnmatch

from .parser import ParsedJavaClass, extract_dependencies_from_file


class JavaFileRetriever:
    """
    Retrieves Java source files from a project directory.
    Can search by class name or package.
    """

    def __init__(self, project_root: str):
        """
        Initialize the retriever.

        Args:
            project_root: Root directory of the Java project
        """
        self.project_root = Path(project_root)
        if not self.project_root.exists():
            raise FileNotFoundError(f"Project root not found: {project_root}")

        self._java_files_cache: Optional[List[Path]] = None
        self._class_index: Optional[Dict[str, Path]] = None

    def _scan_java_files(self) -> List[Path]:
        """Scan and cache all Java files in the project."""
        if self._java_files_cache is None:
            self._java_files_cache = []
            for path in self.project_root.rglob("*.java"):
                # Skip test files by default
                if "test" not in path.parts and "Test.java" not in path.name:
                    self._java_files_cache.append(path)
        return self._java_files_cache

    def _build_class_index(self):
        """Build an index of class names to file paths."""
        if self._class_index is None:
            self._class_index = {}
            for java_file in self._scan_java_files():
                try:
                    parsed = extract_dependencies_from_file(str(java_file))
                    if parsed.name and parsed.name != "Unknown":
                        # Index by simple class name
                        self._class_index[parsed.name] = java_file

                        # Also index with package if available
                        if parsed.package:
                            full_name = f"{parsed.package}.{parsed.name}"
                            self._class_index[full_name] = java_file
                except Exception:
                    # Skip files that can't be parsed
                    continue

    def find_file_by_class_name(self, class_name: str) -> Optional[str]:
        """
        Find a Java file by its class name.

        Args:
            class_name: Simple class name or fully qualified name

        Returns:
            Path to the Java file, or None if not found
        """
        self._build_class_index()
        file_path = self._class_index.get(class_name)
        return str(file_path) if file_path else None

    def find_files_by_pattern(self, pattern: str) -> List[str]:
        """
        Find Java files matching a glob pattern.

        Args:
            pattern: Glob pattern (e.g., "**/Repository.java")

        Returns:
            List of matching file paths
        """
        return [str(f) for f in self.project_root.glob(pattern)]

    def get_all_java_files(self) -> List[str]:
        """
        Get all Java files in the project (excluding tests).

        Returns:
            List of file paths
        """
        return [str(f) for f in self._scan_java_files()]

    def parse_file(self, file_path: str) -> Optional[ParsedJavaClass]:
        """
        Parse a Java file and return its parsed representation.

        Args:
            file_path: Path to the Java file

        Returns:
            ParsedJavaClass or None if parsing fails
        """
        try:
            return extract_dependencies_from_file(file_path)
        except Exception:
            return None


class DependencyResolver:
    """
    Resolves dependencies by finding their source files
    and extracting relevant information.
    """

    def __init__(self, retriever: JavaFileRetriever):
        """
        Initialize the resolver.

        Args:
            retriever: JavaFileRetriever instance
        """
        self.retriever = retriever

    def resolve_dependencies(
        self, class_name: str, max_depth: int = 2
    ) -> List[ParsedJavaClass]:
        """
        Recursively resolve dependencies for a class.

        Args:
            class_name: Name of the class to resolve
            max_depth: Maximum recursion depth

        Returns:
            List of ParsedJavaClass for resolved dependencies
        """
        visited = set()
        result = []

        def _resolve(name: str, depth: int):
            if depth > max_depth or name in visited:
                return

            visited.add(name)
            file_path = self.retriever.find_file_by_class_name(name)

            if file_path:
                parsed = self.retriever.parse_file(file_path)
                if parsed:
                    result.append(parsed)

                    for dep in parsed.dependencies:
                        if dep.type in ("class", "interface", "import", "field"):
                            _resolve(dep.name, depth + 1)

        _resolve(class_name, 0)
        return result

    def get_method_signatures(self, class_name: str) -> str:
        """
        Get formatted method signatures for a class.

        Args:
            class_name: Name of the class

        Returns:
            Formatted string with method signatures
        """
        file_path = self.retriever.find_file_by_class_name(class_name)
        if not file_path:
            return f"// Class {class_name} not found"

        parsed = self.retriever.parse_file(file_path)
        if not parsed:
            return f"// Could not parse {class_name}"

        lines = [f"// {class_name}"]

        if parsed.package:
            lines.append(f"package {parsed.package};")
            lines.append("")

        class_type = "interface" if "interface" in class_name or any(
            "interface" in imp for imp in parsed.imports
        ) else "class"
        lines.append(f"public {class_type} {parsed.name} {{")

        for method in parsed.methods:
            params = ", ".join(method.parameters)
            visibility = method.visibility
            static = "static " if method.is_static else ""
            lines.append(f"    {visibility} {static}{method.return_type} {method.name}({params});")

        lines.append("}")
        lines.append("")
        return "\n".join(lines)


if __name__ == "__main__":
    # Example usage
    import sys

    if len(sys.argv) < 3:
        print("Usage: python retriever.py <project_path> <class_name>")
        sys.exit(1)

    project_path = sys.argv[1]
    class_name = sys.argv[2]

    retriever = JavaFileRetriever(project_path)
    file = retriever.find_file_by_class_name(class_name)

    if file:
        print(f"Found: {file}")

        parsed = retriever.parse_file(file)
        if parsed:
            print(f"\nClass: {parsed.name}")
            print(f"Package: {parsed.package}")
            print(f"Methods: {len(parsed.methods)}")
            for method in parsed.methods:
                params = ", ".join(method.parameters)
                print(f"  - {method.return_type} {method.name}({params})")
    else:
        print(f"Class '{class_name}' not found in project")
