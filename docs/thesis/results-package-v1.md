# Thesis Results Package

**Generated:** 2026-05-12 23:37:55 UTC
**Provider/Model:** nvidia/meta/llama-3.3-70b-instruct
**Dataset:** 8 projects, 8 total runs

## 1. Executive Summary

| Metric | Value |
|--------|-------|
| Projects evaluated | 8 |
| Total benchmark runs | 8 |
| Compilation success | 1 / 8 (12.5%) |
| Test execution success | 0 / 8 (0.0%) |
| Timeouts | 0 |
| Average quality score | 0.708 |
| Average generation time | 144.7s |
| Total generated tests | 102 |
| Total assertions | 195 |
| Average assertions per test | 1.9 |
| Trivial tests | 1 (12.5%) |

## 2. Per-Project Results

| Project | Class | Status | Tests | Assertions | Quality | Time(s) |
|---------|-------|--------|-------|------------|---------|---------|
| commons-dbutils | beanprocessor | ✅comp ❌test | 6 | 33 | 1.000 | 194.5 |
| commons-dbutils | dbutils | ❌ compile | 24 | 31 | 0.775 | 505.0 |
| commons-dbutils | rowprocessor | ❌ compile | 12 | 67 | 1.000 | 175.5 |
| commons-dbutils | queryloader | ❌ compile | 6 | 13 | 1.000 | 69.9 |
| datafaker | wordutils | ❌ compile | 6 | 6 | 0.600 | 20.7 |
| ice4j | request | ❌ compile | 4 | 2 | 0.000 | 40.1 |
| jsoup | cleaner | ❌ compile | 5 | 6 | 0.720 | 30.6 |
| datafaker | faker | ❌ compile | 39 | 37 | 0.569 | 121.1 |

## 3. Failure Analysis

**8 failed run(s)** categorized into 1 pattern(s):

### 3.1 Other (8/8, 100%)

- **Count:** 8
- **% of failures:** 100%
- **Recommendation:** Inspect failure message and adjust test logic

```
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended

```

## 4. Quality Metrics

| Range | Label | Count |
|-------|-------|-------|
| >= 0.8 | Excellent | 3 |
| 0.5 – 0.79 | Good | 4 |
| 0.3 – 0.49 | Fair | 0 |
| > 0 – 0.29 | Poor | 0 |
| 0 (trivial) | Trivial | 1 |

- **Average quality score:** 0.708
- **Average (non-zero):** 0.809

**Highest quality:**
- commons-dbutils-beanprocessor: 1.000 (33 assertions, 6 tests)
- commons-dbutils-rowprocessor: 1.000 (67 assertions, 12 tests)
- commons-dbutils-queryloader: 1.000 (13 assertions, 6 tests)

**Lowest quality:**
- ice4j-request: 0.000 (2 assertions, 4 tests)
- datafaker-faker: 0.569 (37 assertions, 39 tests)
- datafaker-wordutils: 0.600 (6 assertions, 6 tests)

## 5. Timing Breakdown

Average across 8 runs:

| Phase | Avg Time (ms) | % of Total |
|-------|---------------|------------|
| Parse (AST) | 2 | 0.0% |
| Retrieval (deps) | 2087 | 2.8% |
| Prompt building | 1213 | 1.6% |
| LLM generation | 70684 | 95.5% |
| Post-processing | 0 | 0.0% |

- **Average total:** 73986ms (74.0s)
- **Average LLM time:** 70684ms (95.5% of total)

### Per-Project Timing

| Project | Class | LLM (s) | Total (s) |
|---------|-------|---------|-----------|
| commons-dbutils | beanprocessor | 97.2 | 194.5 |
| commons-dbutils | dbutils | 252.5 | 505.0 |
| commons-dbutils | rowprocessor | 87.7 | 175.5 |
| commons-dbutils | queryloader | 29.3 | 69.9 |
| datafaker | wordutils | 8.0 | 20.7 |
| ice4j | request | 20.1 | 40.1 |
| jsoup | cleaner | 10.3 | 30.6 |
| datafaker | faker | 60.4 | 121.1 |
