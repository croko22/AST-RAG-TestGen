#!/usr/bin/env python3
"""Benchmark all free models across NVIDIA and OpenRouter."""

import json
from pathlib import Path

# Models to test
MODELS = [
    # NVIDIA models
    {"provider": "nvidia", "model": "meta/llama-3.1-405b-instruct"},
    {"provider": "nvidia", "model": "meta/llama-3.1-70b-instruct"},
    {"provider": "nvidia", "model": "nvidia/llama-3.1-nemotron-70b-instruct"},
    {"provider": "nvidia", "model": "mistralai/mixtral-8x22b-instruct"},
    {"provider": "nvidia", "model": "google/gemma-2-27b-it"},

    # OpenRouter models
    {"provider": "openrouter", "model": "qwen/qwen-2.5-72b-instruct"},
    {"provider": "openrouter", "model": "deepseek/deepseek-chat"},
    {"provider": "openrouter", "model": "deepseek/deepseek-coder"},
    {"provider": "openrouter", "model": "microsoft/wizardlm-2-8x22b"},
    {"provider": "openrouter", "model": "nousresearch/nous-hermes-2-mixtral-8x7b"},
]

BASE_MANIFEST = {
    "manifest_version": 1,
    "project_root": "/home/croko/CODE/unsa/Tesis-RAG-vs-RL-Arena/datasets/reftest-12",
    "dataset": [
        {
            "id": "commons-cli-helpformatter-createpadding",
            "java_file": "commons-cli/src/main/java/org/apache/commons/cli/HelpFormatter.java",
            "expected_test_path": "commons-cli/src/test/java/org/apache/commons/cli/HelpFormatterTest.java"
        },
        {
            "id": "commons-cli-helpformatter-printhelp",
            "java_file": "commons-cli/src/main/java/org/apache/commons/cli/HelpFormatter.java",
            "expected_test_path": "commons-cli/src/test/java/org/apache/commons/cli/HelpFormatterTest.java"
        },
        {
            "id": "commons-cli-options-create",
            "java_file": "commons-cli/src/main/java/org/apache/commons/cli/Options.java",
            "expected_test_path": "commons-cli/src/test/java/org/apache/commons/cli/OptionsTest.java"
        }
    ],
    "run": {
        "trials": 3,
        "seed": 42,
        "max_dependencies": 5,
        "timeout_seconds": 300,
        "retry_count": 0,
        "concurrency": 1
    },
    "evaluation": {
        "compile_cmd": "cd commons-cli && mvn test-compile -q -Dmaven.compiler.source=1.8 -Dmaven.compiler.target=1.8",
        "test_cmd": "cd commons-cli && mvn test -q -Dmaven.compiler.source=1.8 -Dmaven.compiler.target=1.8",
        "coverage_cmd": "cd commons-cli && mvn test -Dcoverage=true -Dmaven.compiler.source=1.8 -Dmaven.compiler.target=1.8"
    },
    "scoring": {
        "weights": {
            "success": 0.5,
            "coverage": 0.3,
            "latency": 0.2
        }
    },
    "toolchain_versions": {
        "python_version": "3.12.0",
        "java_version": "openjdk 17.0.2",
        "maven_version": "Apache Maven 3.9.5",
        "ast_rag_version": "1.0.0"
    }
}


def create_manifest(provider: str, model: str, output_path: Path) -> None:
    """Create a manifest for a specific model."""
    manifest = BASE_MANIFEST.copy()
    manifest["matrix"] = {
        "providers": [
            {"name": provider, "model": model}
        ]
    }
    output_path.write_text(json.dumps(manifest, indent=2), encoding="utf-8")
    print(f"Created manifest: {output_path}")


def run_benchmark(manifest_path: Path, output_dir: Path) -> dict:
    """Run benchmark and return results."""
    print(f"\n{'='*60}")
    print(f"Running benchmark: {manifest_path.name}")
    print(f"Output: {output_dir}")
    print(f"{'='*60}\n")

    try:
        # Import benchmark modules
        from benchmark.manifest import load_manifest
        from benchmark.planner import plan_runs
        from benchmark.runner import execute_runs
        from benchmark.schemas import EvaluationConfig

        # Load manifest
        manifest = load_manifest(manifest_path)

        # Create run plans
        plans = plan_runs(manifest)

        print(f"Created {len(plans)} run plans")

        # Create evaluation config
        eval_config = EvaluationConfig(
            compile_cmd=manifest.evaluation.compile_cmd,
            test_cmd=manifest.evaluation.test_cmd,
            coverage_cmd=manifest.evaluation.coverage_cmd,
        )

        # Execute runs
        results = execute_runs(
            plans,
            output_dir,
            dry_run=False,
            eval_config=eval_config,
        )

        print("✓ Benchmark completed successfully")
        print(f"  Total runs: {len(results)}")
        passed = sum(1 for r in results if r.metrics.compile_pass)
        print(f"  Compile pass: {passed}/{len(results)} ({passed/len(results)*100:.1f}%)")

        return {
            "status": "success",
            "results": [
                {
                    "run_id": r.run_id,
                    "status": r.status,
                    "latency_ms": r.latency_ms,
                    "metrics": {
                        "compile_pass": r.metrics.compile_pass,
                        "test_pass": r.metrics.test_pass,
                        "coverage_pct": r.metrics.coverage_pct,
                        "failure_type": r.metrics.failure_type,
                        "failure_message": r.metrics.failure_message,
                    },
                }
                for r in results
            ],
        }

    except Exception as e:
        print(f"✗ Benchmark failed with exception: {e}")
        import traceback
        traceback.print_exc()
        return {"status": "error", "error": str(e)}


def main():
    """Run benchmarks for all models."""
    base_dir = Path(__file__).parent
    manifests_dir = base_dir / "campaigns" / "all-models" / "manifests"
    results_dir = base_dir / "campaigns" / "all-models" / "results"

    manifests_dir.mkdir(parents=True, exist_ok=True)
    results_dir.mkdir(parents=True, exist_ok=True)

    all_results = {}

    for i, model_config in enumerate(MODELS, 1):
        provider = model_config["provider"]
        model = model_config["model"]

        print(f"\n{'#'*60}")
        print(f"# Model {i}/{len(MODELS)}: {provider}/{model}")
        print(f"{'#'*60}")

        # Create manifest
        safe_model_name = model.replace("/", "_").replace(":", "_")
        manifest_path = manifests_dir / f"{provider}_{safe_model_name}.json"
        create_manifest(provider, model, manifest_path)

        # Run benchmark
        model_output_dir = results_dir / provider / safe_model_name
        result = run_benchmark(manifest_path, model_output_dir)

        all_results[f"{provider}/{model}"] = result

        # Save intermediate results
        summary_path = results_dir / "summary.json"
        summary_path.write_text(json.dumps(all_results, indent=2), encoding="utf-8")

    # Print final summary
    print(f"\n{'='*60}")
    print("FINAL SUMMARY")
    print(f"{'='*60}\n")

    for model_key, result in all_results.items():
        status = result.get("status", "unknown")
        print(f"{model_key}: {status}")

        if status == "success" and "results" in result:
            results_list = result["results"]
            total = len(results_list)
            passed = sum(1 for r in results_list if r.get("metrics", {}).get("compile_pass", False))
            print(f"  Compile pass: {passed}/{total} ({passed/total*100:.1f}%)")

    print(f"\nFull results saved to: {results_dir / 'summary.json'}")


if __name__ == "__main__":
    main()
