# Benchmark Mode Hardening (v1)

## Scope

This document captures the hardening behavior added for `benchmark-fidelity-hardening-v1`.

The changes are additive and keep legacy single-file mode unchanged.

## Preflight Policy (Fail vs Warn)

Benchmark mode now runs a manifest preflight pass before planning/execution.

- Hard fail (`severity=error`):
  - `MANIFEST_PATH_MISSING`
  - `MANIFEST_DATASET_FILE_MISSING`
  - `MANIFEST_PROVIDER_UNKNOWN`
  - `MANIFEST_PROVIDER_CREDENTIAL_MISSING`
  - `MANIFEST_TOOLCHAIN_MISSING`
- Warn-only (`severity=warning`):
  - `MANIFEST_EXPECTED_TEST_PARENT_MISSING`

If any `error` finding exists, benchmark mode exits with non-zero status and prints remediation hints.
Warnings are surfaced but do not block execution.

## Coverage Fidelity Behavior

When `evaluation.coverage_cmd` is configured and compile/test succeed:

1. Prefer JaCoCo artifact parsing from `target/site/jacoco/jacoco.xml` (`LINE` counter).
2. If artifact is missing/unparsable, fallback to stdout regex extraction.
3. If no valid percentage in `[0, 100]` is found, set `coverage_pct = null` and persist reason.

Run-level coverage metadata persisted in outputs:

- `coverage_pct`
- `coverage_source` (`jacoco_xml`, `stdout_regex`, or `null`)
- `coverage_reason` (`coverage_unavailable` or `null`)

## Provenance Persistence

Reporter now emits a campaign-level `provenance.json` sidecar in the benchmark output root.

Each record includes:

- `repo_id`
- `repo_path`
- `resolved_commit`
- `branch`
- `dirty`
- `status` (`ok` or `unavailable`)
- `reason` (when unavailable)
- `captured_at`

`results.json` and `summary.json` also include:

- provenance artifact path
- provenance status counts

## Reporting Diagnostics

`summary.json` now includes campaign diagnostics:

- `preflight_warning_count`
- `coverage_fallback_count`
- `coverage_unavailable_count`

These diagnostics are also mirrored in `results.json` metadata.

## Acceptance Checklist (Spec Mapping)

- [x] Coverage extraction uses artifact-first strategy with fallback and null semantics.
- [x] Provenance artifact is emitted at campaign scope with non-blocking unavailable handling.
- [x] Manifest preflight enforces deterministic fail/warn policy before execution.
- [x] Evaluation config is threaded through batch execution path.
- [x] Campaign diagnostics surface preflight warnings and coverage fidelity counters.
