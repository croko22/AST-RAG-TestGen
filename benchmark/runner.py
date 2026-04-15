"""Benchmark runner: orchestrates execution of planned runs."""

from __future__ import annotations

import json
import signal
import time
from pathlib import Path
from typing import Any

from benchmark.schemas import EvaluationConfig
from benchmark.types import EvalMetrics, RunPlan, RunResult


class TimeoutError(Exception):
    """Raised when a run exceeds its timeout."""

    def __init__(self, seconds: int, run_id: str) -> None:
        self.seconds = seconds
        self.run_id = run_id
        super().__init__(f"Run {run_id} exceeded timeout of {seconds}s")


class RunExecutionError(Exception):
    """Raised when a run fails unexpectedly."""

    def __init__(self, run_id: str, message: str) -> None:
        self.run_id = run_id
        self.message = message
        super().__init__(f"Run {run_id} failed: {message}")


def _create_run_workspace(base_output_dir: Path, run_id: str) -> Path:
    """Create an isolated workspace directory for a run."""
    run_dir = base_output_dir / run_id
    run_dir.mkdir(parents=True, exist_ok=True)
    return run_dir


def _write_run_result(run_dir: Path, result: RunResult) -> None:
    """Write incremental run result to JSON for crash resilience."""
    result_path = run_dir / "result.json"
    result_data = {
        "run_id": result.run_id,
        "status": result.status,
        "latency_ms": result.latency_ms,
        "metrics": {
            "compile_pass": result.metrics.compile_pass,
            "test_pass": result.metrics.test_pass,
            "coverage_pct": result.metrics.coverage_pct,
            "coverage_source": result.metrics.coverage_source,
            "coverage_reason": result.metrics.coverage_reason,
            "failure_type": result.metrics.failure_type,
            "failure_message": result.metrics.failure_message,
        },
        "output_path": result.output_path,
        "provider": result.provider,
        "model": result.model,
        "dataset_id": result.dataset_id,
        "trial": result.trial,
        "config_snapshot": result.config_snapshot,
    }
    result_path.write_text(json.dumps(result_data, indent=2), encoding="utf-8")


def _execute_generation(
    plan: RunPlan,
    output_dir: Path,
) -> tuple[str, int]:
    """
    Execute the generation flow for a run.

    This is a scaffold implementation that creates the isolated workspace
    and writes placeholder results. Full integration with the generation
    pipeline will be added in subsequent phases.

    Returns:
        Tuple of (output_path, latency_ms)
    """
    from pathlib import Path

    from main import generate_test_for_file

    java_file = plan.java_file
    project_path = plan.project_root

    full_java_path = str(Path(project_path) / java_file)

    start_time = time.monotonic()

    generate_test_for_file(
        java_file_path=full_java_path,
        java_project_path=project_path,
        output_dir=str(output_dir),
        max_dependencies=plan.max_dependencies,
        llm_provider=plan.provider,
        llm_model=plan.model,
    )

    end_time = time.monotonic()
    latency_ms = int((end_time - start_time) * 1000)

    test_class_path = Path(output_dir) / f"{Path(java_file).stem}Test.java"

    return str(test_class_path), latency_ms


def _run_with_timeout(
    func: callable,
    timeout_seconds: int,
    *args: Any,
    **kwargs: Any,
) -> Any:
    """Execute a function with timeout handling."""

    class TimeoutHandler:
        def __init__(self, seconds: int) -> None:
            self.seconds = seconds
            self.old_handler: signal.Handler | None = None

        def __enter__(self) -> TimeoutHandler:
            def handler(signum, frame):
                raise TimeoutError(self.seconds, "unknown")

            self.old_handler = signal.signal(signal.SIGALRM, handler)
            signal.alarm(self.seconds)
            return self

        def __exit__(self, exc_type: Any, exc_val: Any, exc_tb: Any) -> None:
            signal.alarm(0)
            if self.old_handler is not None:
                signal.signal(signal.SIGALRM, self.old_handler)

    with TimeoutHandler(timeout_seconds):
        return func(*args, **kwargs)


def execute_run(
    plan: RunPlan,
    base_output_dir: Path | str,
    dry_run: bool = False,
    eval_config: EvaluationConfig | None = None,
) -> RunResult:
    """
    Execute a single benchmark run.

    This function:
    1. Creates an isolated run workspace
    2. Attempts generation with timeout handling
    3. Writes incremental result for crash resilience
    4. Returns a RunResult envelope

    Args:
        plan: The run plan to execute
        base_output_dir: Base directory for all run outputs
        dry_run: If True, skip actual generation (for scaffold testing)

    Returns:
        RunResult with execution outcomes
    """
    from benchmark.planner import get_run_config_snapshot

    base_dir = Path(base_output_dir)
    run_dir = _create_run_workspace(base_dir, plan.run_id)

    config_snapshot = get_run_config_snapshot(plan)

    if dry_run:
        return RunResult(
            run_id=plan.run_id,
            status="dry_run",
            latency_ms=0,
            metrics=EvalMetrics(compile_pass=False, test_pass=False),
            output_path=str(run_dir),
            provider=plan.provider,
            model=plan.model,
            dataset_id=plan.dataset_id,
            trial=plan.trial,
            config_snapshot=config_snapshot,
        )

    try:
        output_path, latency_ms = _run_with_timeout(
            _execute_generation,
            plan.timeout_seconds,
            plan,
            run_dir,
        )

        if eval_config is not None:
            from benchmark.evaluator import evaluate_run
            metrics = evaluate_run(run_dir, eval_config, plan.project_root)
            status = "ok" if metrics.failure_type is None else "failed"
        else:
            metrics = EvalMetrics(
                compile_pass=True,
                test_pass=True,
                coverage_pct=None,
                coverage_source=None,
                coverage_reason=None,
                failure_type=None,
                failure_message=None,
            )
            status = "ok"

    except TimeoutError:
        latency_ms = plan.timeout_seconds * 1000
        metrics = EvalMetrics(
            compile_pass=False,
            test_pass=False,
            failure_type="timeout",
            failure_message=f"Run exceeded {plan.timeout_seconds}s timeout",
        )
        status = "timeout"
        output_path = str(run_dir)

    except Exception as e:
        latency_ms = 0
        metrics = EvalMetrics(
            compile_pass=False,
            test_pass=False,
            failure_type="error",
            failure_message=str(e),
        )
        status = "error"
        output_path = str(run_dir)

    result = RunResult(
        run_id=plan.run_id,
        status=status,
        latency_ms=latency_ms,
        metrics=metrics,
        output_path=output_path,
        provider=plan.provider,
        model=plan.model,
        dataset_id=plan.dataset_id,
        trial=plan.trial,
        config_snapshot=config_snapshot,
    )

    _write_run_result(run_dir, result)

    return result


def execute_runs(
    plans: list[RunPlan],
    base_output_dir: Path | str,
    dry_run: bool = False,
    eval_config: EvaluationConfig | None = None,
) -> list[RunResult]:
    """
    Execute multiple runs sequentially.

    Failed runs do not abort the full benchmark - each failure is captured
    in the result with appropriate status.

    Args:
        plans: List of run plans to execute
        base_output_dir: Base directory for all run outputs
        dry_run: If True, skip actual generation

    Returns:
        List of RunResult in same order as input plans
    """
    results: list[RunResult] = []

    for plan in plans:
        try:
            result = execute_run(
                plan,
                base_output_dir,
                dry_run=dry_run,
                eval_config=eval_config,
            )
            results.append(result)
        except Exception as e:
            from benchmark.planner import get_run_config_snapshot

            config = get_run_config_snapshot(plan)
            results.append(
                RunResult(
                    run_id=plan.run_id,
                    status="error",
                    latency_ms=0,
                    metrics=EvalMetrics(
                        compile_pass=False,
                        test_pass=False,
                        failure_type="error",
                        failure_message=str(e),
                    ),
                    output_path="",
                    provider=plan.provider,
                    model=plan.model,
                    dataset_id=plan.dataset_id,
                    trial=plan.trial,
                    config_snapshot=config,
                )
            )

    return results
