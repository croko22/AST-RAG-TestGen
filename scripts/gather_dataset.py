#!/usr/bin/env python3
"""
Gather the RefTest 12-project dataset.

Clones the 12 benchmark repositories, scans for Java source files,
parses them with Tree-sitter, filters methods using RefTest criteria,
and writes a campaign manifest.

Usage:
    python scripts/gather_dataset.py
    python scripts/gather_dataset.py --skip-clone
    python scripts/gather_dataset.py --repos-dir /path/to/repos --output-dir /path/to/output
"""

from __future__ import annotations

import argparse
import json
import re
import subprocess
import sys
from pathlib import Path

# ---------------------------------------------------------------------------
# Make project root importable when running from scripts/ directory
# ---------------------------------------------------------------------------
_PROJECT_ROOT = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(_PROJECT_ROOT))

from core.parsing.models import _filter_reftest_methods as filter_reftest_methods
from core.parsing.parser import JavaParser
from rag.retriever import JavaFileRetriever

# ---------------------------------------------------------------------------
# Repository definitions
# ---------------------------------------------------------------------------
REPOS: list[dict[str, str | None]] = [
    {
        "name": "commons-cli",
        "url": "https://github.com/apache/commons-cli.git",
        "tag": "commons-cli-1.5.0",
    },
    {
        "name": "commons-collections4",
        "url": "https://github.com/apache/commons-collections.git",
        "tag": "rel/commons-collections-4.5.0",
    },
    {
        "name": "datafaker",
        "url": "https://github.com/datafaker-net/datafaker.git",
        "tag": "v1.9.0",
    },
    {
        "name": "binance-connector",
        "url": "https://github.com/binance/binance-connector-java.git",
        "tag": None,  # No git tags; clone master
    },
    {"name": "jsoup", "url": "https://github.com/jhy/jsoup.git", "tag": None},
    {
        "name": "openapi-diff",
        "url": "https://github.com/OpenAPITools/openapi-diff.git",
        "tag": None,
    },
    {
        "name": "cucumber-expressions",
        "url": "https://github.com/cucumber/cucumber-expressions.git",
        "tag": None,
    },
    {"name": "rtree", "url": "https://github.com/davidmoten/rtree.git", "tag": None},
    {"name": "ice4j", "url": "https://github.com/jitsi/ice4j.git", "tag": None},
    {"name": "morel", "url": "https://github.com/hydromatic/morel.git", "tag": None},
    {
        "name": "commons-dbutils",
        "url": "https://github.com/apache/commons-dbutils.git",
        "tag": None,
    },
    {
        "name": "commons-validator",
        "url": "https://github.com/apache/commons-validator.git",
        "tag": None,
    },
]


# ---------------------------------------------------------------------------
# Helpers
# ---------------------------------------------------------------------------


def sanitize_id(text: str) -> str:
    """Lowercase, replace non-alnum with hyphens, collapse duplicates."""
    text = text.lower()
    text = re.sub(r"[^a-z0-9]+", "-", text)
    text = re.sub(r"-+", "-", text)
    return text.strip("-")


def clone_repo(repo: dict, repos_dir: Path) -> Path:
    """Clone a single repository (shallow) into *repos_dir*."""
    dest = repos_dir / repo["name"]
    if dest.exists():
        print(f"  Already exists: {repo['name']} -- skipping clone")
        return dest

    cmd = ["git", "clone", "--depth", "1"]
    if repo["tag"]:
        cmd += ["--branch", repo["tag"]]
    cmd += [repo["url"], str(dest)]

    print(f"  Cloning {repo['name']} ...", end=" ", flush=True)
    result = subprocess.run(cmd, capture_output=True, text=True)
    if result.returncode != 0:
        print("FAILED")
        print(f"    {result.stderr.strip()}")
        # Remove partial clone so next run can retry
        if dest.exists():
            subprocess.run(["rm", "-rf", str(dest)], check=False)
        return dest
    print("OK")
    return dest


def build_expected_test_path(java_file: str, repo_root: Path) -> str:
    """
    Derive the conventional Maven test path from a main-source path.

    src/main/java/com/example/Foo.java  -->  src/test/java/com/example/FooTest.java
    """
    rel = Path(java_file)
    parts = rel.parts

    # Find "src/main/java" prefix and replace with "src/test/java"
    try:
        src_idx = list(parts).index("src")
        if (
            len(parts) > src_idx + 2
            and parts[src_idx + 1] == "main"
            and parts[src_idx + 2] == "java"
        ):
            test_parts = parts[:src_idx] + ("src", "test", "java") + parts[src_idx + 3 :]
            test_path = Path(*test_parts)
            # Replace .java suffix with Test.java
            test_path = test_path.with_name(test_path.stem + "Test.java")
            return str(test_path)
    except ValueError:
        pass

    # Fallback: if the file is directly in the repo, construct a reasonable path
    class_name = rel.stem
    parent = rel.parent
    test_rel = parent / f"{class_name}Test.java"
    return str(test_rel)


def scan_repo(repo: dict, repo_path: Path, parser: JavaParser) -> list[dict]:
    """
    Scan a cloned repository and return manifest entries for qualifying
    focal methods.
    """
    if not repo_path.exists():
        print(f"  WARNING: {repo_path} does not exist, skipping")
        return []

    retriever = JavaFileRetriever(str(repo_path))
    java_files = retriever.get_all_java_files()
    print(f"  Scanning {len(java_files)} files in {repo['name']} ...")

    entries: list[dict] = []

    for java_file in java_files:
        try:
            parsed = parser.parse_file(java_file)
        except Exception:
            continue

        if parsed.name == "Unknown" or not parsed.package:
            continue

        eligible = filter_reftest_methods(parsed.methods)
        if not eligible:
            continue

        rel_path = str(Path(java_file).relative_to(repo_path))
        expected_test = build_expected_test_path(rel_path, repo_path)

        for method in eligible:
            entry_id = sanitize_id(f"{repo['name']}-{parsed.name}-{method.name}")
            entries.append(
                {
                    "id": entry_id,
                    "java_file": rel_path,
                    "expected_test_path": expected_test,
                }
            )

    print(f"  Found {len(entries)} focal methods in {repo['name']}")
    return entries


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------


def main() -> None:
    ap = argparse.ArgumentParser(description="Gather the RefTest 12-project dataset manifest")
    ap.add_argument(
        "--repos-dir",
        default=str(_PROJECT_ROOT / "datasets" / "reftest-12"),
        help="Directory where repositories are cloned (default: datasets/reftest-12/)",
    )
    ap.add_argument(
        "--output-dir",
        default=str(_PROJECT_ROOT / "campaigns" / "reftest-12"),
        help="Directory to write manifest.json (default: campaigns/reftest-12/)",
    )
    ap.add_argument(
        "--skip-clone",
        action="store_true",
        help="Skip git clone; assume repos already exist in --repos-dir",
    )
    args = ap.parse_args()

    repos_dir = Path(args.repos_dir)
    output_dir = Path(args.output_dir)
    repos_dir.mkdir(parents=True, exist_ok=True)
    output_dir.mkdir(parents=True, exist_ok=True)

    # --- Clone ---------------------------------------------------------------
    if not args.skip_clone:
        print("Cloning repositories ...")
        for repo in REPOS:
            clone_repo(repo, repos_dir)
        print()

    # --- Scan & parse --------------------------------------------------------
    print("Scanning repositories ...")
    parser = JavaParser()
    all_entries: list[dict] = []

    for repo in REPOS:
        repo_path = repos_dir / repo["name"]
        entries = scan_repo(repo, repo_path, parser)
        all_entries.extend(entries)

    # --- Deduplicate ids (rare but possible with overloaded methods) ---------
    seen_ids: set[str] = set()
    unique_entries: list[dict] = []
    for entry in all_entries:
        if entry["id"] not in seen_ids:
            seen_ids.add(entry["id"])
            unique_entries.append(entry)
        else:
            # Disambiguate by appending a counter
            base = entry["id"]
            counter = 2
            while f"{base}-{counter}" in seen_ids:
                counter += 1
            entry["id"] = f"{base}-{counter}"
            seen_ids.add(entry["id"])
            unique_entries.append(entry)

    # --- Write manifest ------------------------------------------------------
    manifest = {
        "manifest_version": 1,
        "project_root": str(repos_dir),
        "dataset": unique_entries,
    }

    manifest_path = output_dir / "manifest.json"
    manifest_path.write_text(
        json.dumps(manifest, indent=2, ensure_ascii=False) + "\n",
        encoding="utf-8",
    )

    # --- Summary -------------------------------------------------------------
    print()
    print("=" * 60)
    print(f"  {len(REPOS)} projects")
    print(f"  {len(unique_entries)} focal methods")
    print(f"  Manifest written to {manifest_path}")
    print("=" * 60)


if __name__ == "__main__":
    main()
