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


def _serialize_metrics(metrics: EvalMetrics) -> dict[str, Any]:
    return {
        "compile_pass": metrics.compile_pass,
        "test_pass": metrics.test_pass,
        "coverage_pct": metrics.coverage_pct,
        "branch_coverage_pct": metrics.branch_coverage_pct,
        "coverage_source": metrics.coverage_source,
        "coverage_reason": metrics.coverage_reason,
        "failure_type": metrics.failure_type,
        "failure_message": metrics.failure_message,
        "compile_errors": metrics.compile_errors,
        "attempt_count": metrics.attempt_count,
        "final_status": metrics.final_status,
        "generation_time_ms": metrics.generation_time_ms,
        "assertion_count": metrics.assertion_count,
        "trivial_flag": metrics.trivial_flag,
        "test_count": metrics.test_count,
        "quality_score": metrics.quality_score,
        "mutation_score_pct": metrics.mutation_score_pct,
        "killed_mutations": metrics.killed_mutations,
        "total_mutations": metrics.total_mutations,
        "timings": {
            "parse_ms": metrics.timings.parse_ms,
            "retrieval_ms": metrics.timings.retrieval_ms,
            "prompt_ms": metrics.timings.prompt_ms,
            "llm_ms": metrics.timings.llm_ms,
            "postproc_ms": metrics.timings.postproc_ms,
        },
    }


def _write_run_result(run_dir: Path, result: RunResult) -> None:
    result_path = run_dir / "result.json"
    result_data = {
        "run_id": result.run_id,
        "status": result.status,
        "latency_ms": result.latency_ms,
        "metrics": _serialize_metrics(result.metrics),
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
    feedback_context: str | None = None,
) -> tuple[str, int, Any]:
    from pathlib import Path

    from orchestration.generator import generate_test_for_file

    java_file = plan.java_file
    project_path = plan.project_root

    full_java_path = str(Path(project_path) / java_file)

    start_time = time.monotonic()

    result = generate_test_for_file(
        java_file_path=full_java_path,
        java_project_path=project_path,
        output_dir=str(output_dir),
        max_dependencies=plan.max_dependencies,
        llm_provider=plan.provider,
        llm_model=plan.model,
        feedback_context=feedback_context,
    )

    end_time = time.monotonic()
    latency_ms = int((end_time - start_time) * 1000)

    return str(result.output_path), latency_ms, result


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


def _attempt_rank(metrics: EvalMetrics) -> tuple[int, int, int, int]:
    """Rank attempts so the most useful one survives (higher is better)."""
    return (
        1 if metrics.compile_pass else 0,
        1 if metrics.test_pass else 0,
        -len(metrics.compile_errors),
        metrics.assertion_count,
    )


def _error_signature(metrics: EvalMetrics) -> tuple[str, ...] | None:
    """Stable signature of compile errors, used to detect stalled repair loops."""
    if not metrics.compile_errors:
        return None
    return tuple(sorted(metrics.compile_errors))


def _build_repair_feedback(metrics: EvalMetrics, limit: int = 10) -> str:
    """Build feedback text from structured compiler errors for the next attempt."""
    errors = metrics.compile_errors[:limit]
    if errors:
        return "\n".join(f"- {error}" for error in errors)
    return metrics.failure_message or "Compilation failed without captured diagnostics."


def _restore_best_attempt(attempt: tuple[int, int, Any, EvalMetrics]) -> None:
    """Rewrite the best attempt's code to disk so artifacts match the result."""
    _, _, generation_result, _ = attempt
    code = getattr(generation_result, "test_code", None)
    output_path = getattr(generation_result, "output_path", None)
    if not code or not output_path:
        return
    try:
        Path(output_path).write_text(code, encoding="utf-8")
    except OSError:
        pass


def _execute_with_repair(
    plan: RunPlan,
    run_dir: Path,
    eval_config: EvaluationConfig | None,
    max_attempts: int,
) -> tuple[EvalMetrics, str, int]:
    """Generate, compile, and repair up to ``max_attempts`` times.

    On a compile failure the structured diagnostics are fed back into the next
    generation attempt. The loop stops early when an attempt compiles or when
    the diagnostics stop changing (no progress).

    Returns:
        Tuple of (best metrics, best output path, total latency across attempts).
    """
    from benchmark.evaluator import evaluate_run

    attempts: list[tuple[int, int, Any, EvalMetrics]] = []
    feedback_context: str | None = None
    previous_signature: tuple[str, ...] | None = None

    for attempt in range(1, max_attempts + 1):
        try:
            _, latency_ms, generation_result = _run_with_timeout(
                _execute_generation,
                plan.timeout_seconds,
                plan,
                run_dir,
                feedback_context,
            )
        except TimeoutError:
            if attempts:
                break
            raise

        if eval_config is None:
            metrics = EvalMetrics(compile_pass=True, test_pass=True, failure_type=None)
        else:
            metrics = evaluate_run(
                run_dir,
                eval_config,
                plan.project_root,
                generation_result=generation_result,
            )

        metrics.attempt_count = attempt
        metrics.final_status = "success" if metrics.compile_pass else "failed"
        attempts.append((attempt, latency_ms, generation_result, metrics))

        if metrics.compile_pass:
            break

        signature = _error_signature(metrics)
        if signature is not None and signature == previous_signature:
            break
        previous_signature = signature
        feedback_context = _build_repair_feedback(metrics)

    best = max(attempts, key=lambda entry: _attempt_rank(entry[3]))
    if best is not attempts[-1]:
        _restore_best_attempt(best)

    total_latency_ms = sum(entry[1] for entry in attempts)
    _, _, best_generation, best_metrics = best
    return best_metrics, best_generation.output_path, total_latency_ms


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

    max_attempts = 1 + max(0, getattr(plan, "repair_attempts", 0))

    try:
        metrics, output_path, latency_ms = _execute_with_repair(
            plan,
            run_dir,
            eval_config,
            max_attempts,
        )
        status = "ok" if metrics.failure_type is None else "failed"

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
