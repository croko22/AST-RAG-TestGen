"""Run planner: deterministic expansion of manifest matrix into run plans."""

from __future__ import annotations

import hashlib
import itertools
from typing import Any

from benchmark.schemas import BenchmarkManifest
from benchmark.types import RunPlan


def _stable_sort_key(item: Any) -> tuple[Any, ...]:
    """Generate a stable sort key for any comparable item."""
    if hasattr(item, "__iter__") and not isinstance(item, str):
        return tuple(_stable_sort_key(i) for i in item)
    return (type(item).__name__, item)


def _generate_run_id(dataset_id: str, provider: str, model: str, trial: int) -> str:
    """Generate a deterministic run ID from components."""
    components = f"{dataset_id}:{provider}:{model}:{trial}"
    hash_digest = hashlib.sha256(components.encode("utf-8")).hexdigest()[:12]
    return f"run_{hash_digest}"


def plan_runs(manifest: BenchmarkManifest) -> list[RunPlan]:
    """
    Expand manifest matrix into deterministic ordered run plans.

    The expansion follows a stable ordering:
    1. Dataset entries sorted by id
    2. Provider entries sorted by name, then model
    3. Trials from 1 to run.trials

    This ensures reproducible run IDs and ordering across repeated executions
    with the same manifest and seed.

    Args:
        manifest: Validated benchmark manifest

    Returns:
        List of RunPlan entries in deterministic order
    """
    sorted_datasets = sorted(manifest.dataset, key=lambda d: d.id)
    sorted_providers = sorted(
        manifest.matrix.providers, key=lambda p: (p.name, p.model)
    )

    run_config = manifest.run
    project_root = manifest.project_root

    plans: list[RunPlan] = []

    for dataset, provider, trial in itertools.product(
        sorted_datasets, sorted_providers, range(1, run_config.trials + 1)
    ):
        run_id = _generate_run_id(
            dataset_id=dataset.id,
            provider=provider.name,
            model=provider.model,
            trial=trial,
        )

        plan = RunPlan(
            run_id=run_id,
            dataset_id=dataset.id,
            java_file=dataset.java_file,
            provider=provider.name,
            model=provider.model,
            trial=trial,
            seed=run_config.seed,
            max_dependencies=run_config.max_dependencies,
            timeout_seconds=run_config.timeout_seconds,
            retry_count=run_config.retry_count,
            project_root=project_root,
        )
        plans.append(plan)

    return plans


def get_run_config_snapshot(plan: RunPlan) -> dict[str, Any]:
    """
    Get a stable configuration snapshot for a run plan.

    This snapshot is included in RunResult for reproducibility verification.
    """
    return {
        "run_id": plan.run_id,
        "dataset_id": plan.dataset_id,
        "java_file": plan.java_file,
        "provider": plan.provider,
        "model": plan.model,
        "trial": plan.trial,
        "seed": plan.seed,
        "max_dependencies": plan.max_dependencies,
        "timeout_seconds": plan.timeout_seconds,
        "retry_count": plan.retry_count,
        "project_root": plan.project_root,
    }
