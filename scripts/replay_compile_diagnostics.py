"""Replay compilation on already-generated tests to recover real diagnostics.

Because the generated ``*Test.java`` files are persisted under ``results/``,
compilation can be replayed without any LLM calls. This produces the failure
taxonomy needed to decide whether the repair loop can help.

Usage:
    python scripts/replay_compile_diagnostics.py \
        --manifest campaigns/nvidia-405b/manifest-commons-dbutils.json \
        --test results/nvidia/commons-dbutils/run_0001/BeanProcessorTest.java \
        --output results/diagnostics/commons-dbutils.json
"""

from __future__ import annotations

import argparse
import json
import shutil
import sys
import tempfile
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parent.parent
if str(REPO_ROOT) not in sys.path:
    sys.path.insert(0, str(REPO_ROOT))

from benchmark.manifest import load_manifest  # noqa: E402
from postproc._runner import run_command, stage_test_file  # noqa: E402
from postproc.diagnostics import parse_javac_diagnostics, summarize_diagnostics  # noqa: E402


def replay_one(test_path: Path, project_root: Path, compile_cmd: str, timeout: int) -> dict:
    """Compile a single generated test in an isolated copy of the project."""
    with tempfile.TemporaryDirectory(prefix="replay_compile_") as temp_name:
        temp_path = Path(temp_name)
        if (project_root / "pom.xml").exists():
            shutil.copytree(project_root, temp_path, dirs_exist_ok=True)

        stage_test_file(test_path, temp_path)
        result = run_command(compile_cmd, temp_path, timeout=timeout)

        combined = "\n".join(part for part in (result.stdout, result.stderr) if part)
        diagnostics = parse_javac_diagnostics(combined)
        relevant = [d for d in diagnostics if d.file and d.file.endswith(test_path.name)]
        selected = relevant or diagnostics

        return {
            "test": str(test_path),
            "success": result.success,
            "diagnostic_count": len(selected),
            "diagnostics": [diagnostic.format() for diagnostic in selected],
            "summary": summarize_diagnostics(selected),
        }


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--manifest", required=True, help="Benchmark manifest (JSON/TOML).")
    parser.add_argument(
        "--test",
        action="append",
        required=True,
        dest="tests",
        help="Generated test file to replay. Repeatable.",
    )
    parser.add_argument("--output", help="Write JSON results to this path instead of stdout.")
    parser.add_argument("--timeout", type=int, default=600, help="Compile timeout in seconds.")
    args = parser.parse_args()

    manifest = load_manifest(args.manifest)
    project_root = Path(manifest.project_root)
    compile_cmd = manifest.evaluation.compile_cmd

    records = []
    for test in args.tests:
        test_path = Path(test)
        print(f"[replay] compiling {test_path.name} ...", file=sys.stderr)
        record = replay_one(test_path, project_root, compile_cmd, args.timeout)
        records.append(record)
        status = "OK" if record["success"] else f"{record['diagnostic_count']} error(s)"
        print(f"[replay]   -> {status}", file=sys.stderr)

    payload = json.dumps(records, indent=2, ensure_ascii=False)
    if args.output:
        output_path = Path(args.output)
        output_path.parent.mkdir(parents=True, exist_ok=True)
        output_path.write_text(payload, encoding="utf-8")
        print(f"[replay] wrote {output_path}", file=sys.stderr)
    else:
        print(payload)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
