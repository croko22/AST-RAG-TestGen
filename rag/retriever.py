"""Hybrid retriever combining vector search with AST-based relevance."""

from __future__ import annotations

import logging
from pathlib import Path
from rag.models import RetrievalResult

try:
    from core.parsing.parser import JavaParser

    _parser = JavaParser()
except ImportError:
    _parser = None

logger = logging.getLogger(__name__)


def _compute_ast_scores(
    query_class: str,
    results: list[RetrievalResult],
    ast_dependencies: set[str] | None = None,
) -> list[float]:
    if ast_dependencies is None:
        return [0.0] * len(results)

    scores = []
    for r in results:
        class_name = r.chunk.class_name
        method_name = r.chunk.method_name
        package = r.chunk.package or ""

        full_ref = f"{package}.{class_name}" if package else class_name

        if full_ref in ast_dependencies or class_name in ast_dependencies:
            scores.append(1.0)
        elif method_name:
            matched = any(
                dep.endswith(f".{method_name}") or dep.endswith(f".{class_name}.{method_name}")
                for dep in ast_dependencies
            )
            scores.append(0.5 if matched else 0.0)
        else:
            scores.append(0.0)

    return scores


class HybridRetriever:
    def __init__(
        self,
        indexer=None,
        embedder=None,
        alpha: float = 0.5,
        ast_dependencies: set[str] | None = None,
        collection_name: str | None = None,
    ):
        self._indexer = indexer
        self._embedder = embedder
        self._alpha = alpha
        self._ast_dependencies = ast_dependencies or set()
        self._collection_name = collection_name

    def set_ast_dependencies(self, deps: set[str]) -> None:
        self._ast_dependencies = deps

    def retrieve(
        self,
        query: str,
        k: int = 10,
        alpha: float | None = None,
    ) -> list[RetrievalResult]:
        if alpha is None:
            alpha = self._alpha

        if not query.strip():
            return []

        if self._embedder is None or self._indexer is None:
            return []

        embedding = self._embedder.embed_single(query)
        vector_results = self._indexer.query(
            embedding, collection_name=self._collection_name, k=k * 2
        )

        if not vector_results:
            return []

        ast_scores = _compute_ast_scores(query, vector_results, self._ast_dependencies)

        for i, r in enumerate(vector_results):
            r.ast_score = ast_scores[i]
            r.hybrid_score = alpha * r.vector_score + (1 - alpha) * r.ast_score
            if alpha == 1.0:
                r.source = "vector"
            elif alpha == 0.0:
                r.source = "ast"
            else:
                r.source = "hybrid"

        vector_results.sort(key=lambda r: r.hybrid_score, reverse=True)
        return vector_results[:k]

    def build_context(
        self,
        results: list[RetrievalResult],
        max_tokens: int = 4000,
    ) -> str:
        if not results:
            return ""

        char_budget = max_tokens * 4
        parts: list[str] = []
        used = 0

        for r in results:
            chunk_text = r.chunk.content
            header = f"// {r.chunk.chunk_type}"
            if r.chunk.class_name:
                header += f" from {r.chunk.class_name}"
            if r.chunk.method_name:
                header += f".{r.chunk.method_name}"
            section = f"{header}\n{chunk_text}\n\n"
            if used + len(section) > char_budget:
                break
            parts.append(section)
            used += len(section)

        return "".join(parts)


class JavaFileRetriever:
    """
    Retrieves Java source files from a project directory.
    Can search by class name or package.
    """

    def __init__(self, project_root: str):
        self.project_root = Path(project_root)
        if not self.project_root.exists():
            raise FileNotFoundError(f"Project root not found: {project_root}")

        self._java_files_cache: list[Path] | None = None
        self._class_index: dict[str, Path] | None = None
        self._parse_errors: int = 0

    def _scan_java_files(self) -> list[Path]:
        if self._java_files_cache is None:
            self._java_files_cache = []
            for path in self.project_root.rglob("*.java"):
                if "test" not in path.parts and "Test.java" not in path.name:
                    self._java_files_cache.append(path)
        return self._java_files_cache

    def _build_class_index(self):
        if self._class_index is None:
            self._class_index = {}
            self._parse_errors = 0
            for java_file in self._scan_java_files():
                try:
                    parsed = _parser.parse_file(str(java_file)) if _parser else None
                    if parsed and parsed.name and parsed.name != "Unknown":
                        if parsed.package:
                            fqcn = f"{parsed.package}.{parsed.name}"
                            self._class_index[fqcn] = java_file
                        simple_key = parsed.name
                        if simple_key not in self._class_index:
                            self._class_index[simple_key] = java_file
                except Exception as e:
                    self._parse_errors += 1
                    logger.warning(f"Failed to parse {java_file}: {e}")

            if self._parse_errors > 0:
                logger.info(f"Skipped {self._parse_errors} files due to parse errors")

    def find_file_by_class_name(self, class_name: str) -> str | None:
        self._build_class_index()
        if self._class_index is None:
            return None
        file_path = self._class_index.get(class_name)
        return str(file_path) if file_path else None

    def find_files_by_pattern(self, pattern: str) -> list[str]:
        return [str(f) for f in self.project_root.glob(pattern)]

    def get_all_java_files(self) -> list[str]:
        return [str(f) for f in self._scan_java_files()]

    def parse_file(self, file_path: str) -> ParsedJavaClass | None:
        try:
            return _parser.parse_file(file_path) if _parser else None
        except Exception as e:
            logger.warning(f"Failed to parse {file_path}: {e}")
            return None


class DependencyResolver:
    """
    Resolves dependencies by finding their source files
    and extracting relevant information.
    """

    def __init__(self, retriever: JavaFileRetriever):
        self.retriever = retriever

    def resolve_dependencies(self, class_name: str, max_depth: int = 2) -> list[ParsedJavaClass]:
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

    def _detect_is_interface(self, parsed: ParsedJavaClass) -> bool:
        if parsed.name.startswith("I") and any(c.isupper() for c in parsed.name[1:]):
            return True
        interface_packages = ("java.util", "java.io", "java.sql", "javax.sql")
        for imp in parsed.imports:
            if any(imp.startswith(p) for p in interface_packages):
                return True
        return False

    def get_method_signatures(self, class_name: str) -> str:
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

        class_type = "interface" if self._detect_is_interface(parsed) else "class"
        lines.append(f"public {class_type} {parsed.name} {{")

        for method in parsed.methods:
            params = ", ".join(f"{t} {n}" for t, n in method.parameters)
            visibility = method.visibility
            static = "static " if method.is_static else ""
            lines.append(f" {visibility} {static}{method.return_type} {method.name}({params});")

        lines.append("}")
        lines.append("")
        return "\n".join(lines)
