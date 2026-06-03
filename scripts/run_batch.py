#!/usr/bin/env python3
"""
Batch test generator for RefTest-12 projects.
Runs projects sequentially, generating tests for each source file.

Usage:
    export NVIDIA_API_KEY="nvapi-..."
    python scripts/run_batch.py [project_name] [--model MODEL]
"""

import argparse
import json
import subprocess
import sys
import time
from pathlib import Path

MODEL = "meta/llama-3.3-70b-instruct"
PROVIDER = "nvidia"
DATASET_DIR = Path("datasets/reftest-12")
OUTPUT_BASE = Path("results/nvidia")
TIMEOUT_PER_FILE = 180  # seconds

PROJECTS = [
    ("cucumber-expressions", 23),
    ("commons-dbutils", 19),
    ("commons-validator", 61),
    ("datafaker", 63),
    ("ice4j", 124),
    ("jsoup", 66),
    ("openapi-diff", 104),
    ("rtree", 49),
    ("morel", 101),
    ("commons-collections4", 225),
]


def find_java_files(project_name: str) -> list[Path]:
    """Find all Java source files (not test files) in the project."""
    project_dir = DATASET_DIR / project_name
    all_java = list(project_dir.rglob("*.java"))
    # Filter out test files and common non-main paths
    filtered = []
    for f in all_java:
        p = str(f).lower()
        if "/test/" in p or "test" in f.name or "/target/" in p:
            continue
        if "/src/main/java/" in p or "/main/java/" in p or f.parent.name == "java":
            filtered.append(f)
    # Fallback: any .java not in test dir
    if not filtered:
        filtered = [f for f in all_java if "/test/" not in str(f).lower()]
    return filtered


def find_project_root(java_file: Path, project_name: str) -> Path:
    """Find the Maven/Gradle project root for a Java file."""
    # Walk up from the file to find pom.xml or build.gradle
    for parent in [java_file] + list(java_file.parents):
        if (parent / "pom.xml").exists() or (parent / "build.gradle").exists():
            return parent
    # Fallback to the dataset project dir
    return DATASET_DIR / project_name


def generate_test(java_file: Path, project_name: str, run_id: str) -> dict:
    """Generate test for a single Java file."""
    project_path = find_project_root(java_file, project_name)
    output_dir = OUTPUT_BASE / project_name / run_id
    output_dir.mkdir(parents=True, exist_ok=True)

    t0 = time.time()
    try:
        result = subprocess.run(
            [
                "python",
                "main.py",
                "generate",
                str(java_file),
                str(project_path),
                "--provider",
                PROVIDER,
                "--model",
                MODEL,
                "--output",
                str(output_dir),
            ],
            capture_output=True,
            text=True,
            timeout=TIMEOUT_PER_FILE,
        )
        elapsed = int((time.time() - t0) * 1000)

        # Check generated test file
        gen_files = list(output_dir.glob("*.java"))
        test_code = gen_files[0].read_text() if gen_files else ""
        test_count = test_code.count("@Test") if test_code else 0
        assertion_count = test_code.count("assert") + test_code.count("Assert") if test_code else 0

        status = "success" if gen_files else "error"
        return {
            "run_id": run_id,
            "status": status,
            "latency_ms": elapsed,
            "generation_time_ms": elapsed,
            "test_count": test_count,
            "assertion_count": assertion_count,
            "file": str(java_file.relative_to(project_path)),
            "test_code_length": len(test_code),
            "success": bool(gen_files),
        }
    except subprocess.TimeoutExpired:
        return {
            "run_id": run_id,
            "status": "timeout",
            "latency_ms": TIMEOUT_PER_FILE * 1000,
            "test_count": 0,
            "assertion_count": 0,
            "file": str(java_file.relative_to(project_path)),
            "success": False,
        }
    except Exception as e:
        return {
            "run_id": run_id,
            "status": "error",
            "latency_ms": int((time.time() - t0) * 1000),
            "test_count": 0,
            "assertion_count": 0,
            "error": str(e),
            "file": str(java_file.relative_to(project_path)),
            "success": False,
        }


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("project", nargs="?", help="Project name to run")
    parser.add_argument("--model", default=MODEL)
    args = parser.parse_args()

    if args.project:
        # Validate project exists
        if not (DATASET_DIR / args.project).exists():
            print(f"Project not found: {args.project}")
            sys.exit(1)
        projects = [(args.project, 0)]
    else:
        projects = PROJECTS

    for project_name, _ in projects:
        java_files = find_java_files(project_name)
        print(f"\n{'=' * 60}")
        print(f"📁 {project_name} — {len(java_files)} source files")
        print(f"{'=' * 60}")

        if not java_files:
            print(f"  ⚠️  No Java source files found in {project_name}")
            continue

        results = []
        for i, jf in enumerate(java_files, 1):
            run_id = f"run_{i:04d}"
            rel_path = str(jf.relative_to(DATASET_DIR / project_name))
            print(f"  [{i}/{len(java_files)}] {rel_path}...", end=" ", flush=True)

            r = generate_test(jf, project_name, run_id)
            results.append(r)

            # Write individual result.json
            result_path = OUTPUT_BASE / project_name / run_id / "result.json"
            result_path.parent.mkdir(parents=True, exist_ok=True)
            result_path.write_text(json.dumps(r, indent=2))

            status_icon = "✅" if r["success"] else "❌"
            print(
                f"{status_icon} ({r['latency_ms'] / 1000:.1f}s, {r['test_count']} tests, {r['assertion_count']} assertions)"
            )

        # Generate summary
        success_count = sum(1 for r in results if r["success"])
        avg_latency = sum(r["latency_ms"] for r in results) / len(results) if results else 0
        total_tests = sum(r["test_count"] for r in results)
        total_assertions = sum(r["assertion_count"] for r in results)

        summary = {
            "project": project_name,
            "generated_at": time.strftime("%Y-%m-%dT%H:%M:%SZ", time.gmtime()),
            "config": {"provider": PROVIDER, "model": args.model},
            "statistics": {
                "total_files": len(java_files),
                "success_count": success_count,
                "success_rate": round(success_count / len(java_files), 4) if java_files else 0,
                "avg_latency_ms": int(avg_latency),
                "total_tests_generated": total_tests,
                "total_assertions": total_assertions,
                "avg_tests_per_file": round(total_tests / len(java_files), 1) if java_files else 0,
                "avg_assertions_per_file": round(total_assertions / len(java_files), 1)
                if java_files
                else 0,
            },
        }

        summary_path = OUTPUT_BASE / project_name / "summary.json"
        summary_path.write_text(json.dumps(summary, indent=2))

        print(f"\n  📊 {project_name}: {success_count}/{len(java_files)} success")
        print(f"     {total_tests} tests, {total_assertions} assertions")
        print(f"     Avg latency: {avg_latency / 1000:.1f}s")

        # Small delay between projects to avoid rate limits
        time.sleep(5)

    print("\n✅ Batch complete!")
    print(f"Results in: {OUTPUT_BASE}")


if __name__ == "__main__":
    main()
