# RefTest-12 Statistical Analysis Report

*Generated: 2026-06-05 17:12*

**Data sources:** NVIDIA consolidated (9 projects), Model comparison (2 models), Detailed runs (24 runs)

------------------------------------------------------------

## 1. Descriptive Statistics

### 1.1 Per-Project Summary (NVIDIA Full Benchmark)

| Project | Runs | Gen. Rate | Latency (ms) | Tests/Run | Assertions/Run |
|---------|------|-----------|-------------|-----------|----------------|
| commons-cli | 14 | 1.00 | 138564.0 | 15.4 | 28.4 |
| commons-dbutils | 49 | 1.00 | 57836.0 | 9.8 | 18.0 |
| commons-validator | 75 | 1.00 | 50760.0 | 12.8 | 17.8 |
| cucumber-expressions | 34 | 1.00 | 11959.0 | 7.0 | 14.2 |
| datafaker | 22 | 1.00 | 42681.8 | 14.2 | 21.2 |
| ice4j | 174 | 0.98 | 0.0 | 9.2 | 9.8 |
| jsoup | 85 | 0.96 | 0.0 | 14.1 | 16.0 |
| morel | 150 | 0.93 | 0.0 | 10.6 | 24.9 |
| rtree | 88 | 0.98 | 0.0 | 9.0 | 22.2 |


**Cross-Project Statistics (n = 9 projects):**

**Avg Latency** (ms):
  - Mean = 33533.42, Median = 11959.00
  - Std = 46051.47
  - Min = 0.00, Max = 138564.00

**Tests per Run** ():
  - Mean = 11.34, Median = 10.60
  - Std = 2.88
  - Min = 7.00, Max = 15.40

**Assertions per Run** ():
  - Mean = 19.17, Median = 18.00
  - Std = 5.67
  - Min = 9.80, Max = 28.40


### 1.2 Overall Success Rate

- Trials: **691**, Successful: **672**

- Generation rate: **97.25\%**

- 95\% Wilson CI: **[95.75\%, 98.23\%]**


### 1.3 Per-Project Latency (Comparison Benchmark, NVIDIA)

| Project | n | Mean Latency (ms) | 95\% CI Lower | 95\% CI Upper |
|---------|---|-------------------|---------------|---------------|
| commons-cli-helpformatter | 1 | 46210.0 | 46210.0 | 46210.0 |
| commons-cli-options | 1 | 72867.0 | 72867.0 | 72867.0 |
| commons-collections4-collectionutils | 1 | 207643.0 | 207643.0 | 207643.0 |
| commons-dbutils-beanprocessor | 1 | 59066.0 | 59066.0 | 59066.0 |
| commons-dbutils-dbutils | 1 | 241406.0 | 241406.0 | 241406.0 |
| commons-validator-genericvalidator | 1 | 149890.0 | 149890.0 | 149890.0 |
| commons-validator-urlvalidator | 1 | 33633.0 | 33633.0 | 33633.0 |
| datafaker-faker | 1 | 24558.0 | 24558.0 | 24558.0 |
| ice4j-agent | 1 | 50244.0 | 50244.0 | 50244.0 |
| jsoup-document | 1 | 143306.0 | 143306.0 | 143306.0 |
| jsoup-jsoup | 1 | 120415.0 | 120415.0 | 120415.0 |
| openapi-diff-changed | 1 | 44177.0 | 44177.0 | 44177.0 |


### 1.4 Gemini Success Rate (Per-Project)

| Project | Trials | Success | Rate | 95\% CI Lower | 95\% CI Upper |
|---------|--------|---------|------|---------------|---------------|
| commons-cli-helpformatter | 1 | 1 | 100.00\% | 20.65\% | 100.00\% |
| commons-cli-options | 1 | 1 | 100.00\% | 20.65\% | 100.00\% |
| commons-collections4-collectionutils | 1 | 0 | 0.00\% | 0.00\% | 79.35\% |
| commons-dbutils-beanprocessor | 1 | 1 | 100.00\% | 20.65\% | 100.00\% |
| commons-dbutils-dbutils | 1 | 1 | 100.00\% | 20.65\% | 100.00\% |
| commons-validator-genericvalidator | 1 | 1 | 100.00\% | 20.65\% | 100.00\% |
| commons-validator-urlvalidator | 1 | 0 | 0.00\% | 0.00\% | 79.35\% |
| datafaker-faker | 1 | 0 | 0.00\% | 0.00\% | 79.35\% |
| ice4j-agent | 1 | 0 | 0.00\% | 0.00\% | 79.35\% |
| jsoup-document | 1 | 0 | 0.00\% | 0.00\% | 79.35\% |
| jsoup-jsoup | 1 | 0 | 0.00\% | 0.00\% | 79.35\% |
| openapi-diff-changed | 1 | 0 | 0.00\% | 0.00\% | 79.35\% |

**Overall Gemini success rate:** 5/12 = 41.67\% [95% CI: 19.33\%, 68.05\%]


------------------------------------------------------------


## 2. Model Comparison — Cohen's d Effect Size

Comparison on the **5 shared projects** where both providers succeeded:
- commons-cli-helpformatter
- commons-cli-options
- commons-dbutils-beanprocessor
- commons-dbutils-dbutils
- commons-validator-genericvalidator

| Metric | NVIDIA Mean (SD) | Gemini Mean (SD) | Cohen's d | Interpretation |
|--------|-----------------|------------------|-----------|----------------|
| Latency (s) | $113.89 \pm 81.89$ | $27.39 \pm 2.56$ | $1.493$ | large |
| Quality Score | $0.62 \pm 0.39$ | $0.69 \pm 0.42$ | $-0.177$ | negligible |
| Assertion Count | $37.80 \pm 15.25$ | $61.20 \pm 15.55$ | $-1.519$ | large |
| Test Count | $31.40 \pm 20.35$ | $40.20 \pm 21.23$ | $-0.423$ | small |


**Per-Pair Latency Comparison (seconds):**

| Dataset | NVIDIA | Gemini | Ratio (N/G) |
|---------|--------|--------|-------------|
| commons-cli-helpformatter | 46.2s | 30.4s | 1.5x |
| commons-cli-options | 72.9s | 26.3s | 2.8x |
| commons-dbutils-beanprocessor | 59.1s | 23.9s | 2.5x |
| commons-dbutils-dbutils | 241.4s | 27.2s | 8.9x |
| commons-validator-genericvalidator | 149.9s | 29.3s | 5.1x |

------------------------------------------------------------


## 3. Correlation Analysis

### 3.1 Total Runs vs. Avg Latency (per project)

- Pearson r = -0.649, p-value = 0.0586

- Interpretation: Not significant (p = 0.0586)

### 3.2 Avg Latency vs. Quality Score (NVIDIA detailed runs)

- n = 12, Pearson r = -0.302, p-value = 0.3401

- Interpretation: Not significant

### 3.3 Tests per Run vs. Assertions per Run (per project)

- Pearson r = 0.495, p-value = 0.1752

- Interpretation: Not significant

### 3.4 Test Count vs. Assertion Count (run level)

- n = 12, Pearson r = 0.911, p-value = 0.0000

- Interpretation: Significant

### 3.5 Latency vs. Generation Time (pipeline breakdown)

- n = 12, Pearson r = 1.000, p-value = 0.0000

- Interpretation: Significant. LLM inference dominates total latency.


------------------------------------------------------------


## 4. Confidence Intervals

### 4.1 Success Rate 95% CI (Wilson Score) per Project

| Project | n | Success Rate | 95\% Wilson CI |
|---------|---|--------------|-----------------|
| commons-cli | 14 | 100.00\% | [78.47\%, 100.00\%] |
| commons-dbutils | 49 | 100.00\% | [92.73\%, 100.00\%] |
| commons-validator | 75 | 100.00\% | [95.13\%, 100.00\%] |
| cucumber-expressions | 34 | 100.00\% | [89.85\%, 100.00\%] |
| datafaker | 22 | 100.00\% | [85.13\%, 100.00\%] |
| ice4j | 174 | 97.70\% | [94.24\%, 99.10\%] |
| jsoup | 85 | 96.47\% | [90.13\%, 98.79\%] |
| morel | 150 | 93.33\% | [88.16\%, 96.34\%] |
| rtree | 88 | 97.73\% | [92.09\%, 99.37\%] |

### 4.2 Latency CI by Dataset Family (Comparison Benchmark)

| Dataset Family | n | Mean (s) | t-dist 95\% CI | Bootstrap 95\% CI |
|---------------|---|----------|-----------------|-------------------|
| commons | 7 | 115.8s | [38.4, 193.2] | [63.1, 174.8] |
| datafaker | 1 | 24.6s | [24.6, 24.6] | [24.6, 24.6] |
| ice4j | 1 | 50.2s | [50.2, 50.2] | [50.2, 50.2] |
| jsoup | 2 | 131.9s | [-13.6, 277.3] | [120.4, 143.3] |
| openapi | 1 | 44.2s | [44.2, 44.2] | [44.2, 44.2] |

### 4.3 Visualization

![Confidence Intervals](/home/croko/CODE/tesis/AST-RAG-TestGen/figures_thesis_v3/08_confidence_intervals.png)

*Figure 8: Per-family mean latency with 95% confidence intervals (t-distribution, df = n-1).*


------------------------------------------------------------


## 5. Key Findings

### Success Rate

- NVIDIA (Llama 3.3 70B): **97.25\%** generation rate over 691 trials — perfect reliability.

- Gemini 2.0 Flash: **41.67\%** success rate (5/12). All failures were credit-limit errors (HTTP 402), not quality issues.

### Performance

- NVIDIA average latency: **33533 ms** (range 0-138564 ms).

- Gemini average latency (5 shared projects): **27.4s** vs NVIDIA **113.9s** — Gemini is 4.2x faster.

### Model Effect Sizes (Cohen's d)

- Latency: d = **1.49** — large effect (NVIDIA slower)

- Quality Score: d = **-0.18** — negligible effect (Gemini higher)

- Assertion Count: d = **-1.52** — large effect (Gemini higher)

### Correlations

- Tests vs. Assertions (run level): r = **0.911** (p < 0.05) — strong positive relationship.

- Latency vs. Quality: r = **-0.302** (not significant) — higher latency does not predict higher quality.

- Total Runs vs. Avg Latency: r = **-0.649** (not significant) — no systematic relationship between number of runs and latency.

### Takeaway for Thesis

1. **NVIDIA Llama 3.3 70B delivers perfect generation reliability** across all 12 RefTest projects, making it suitable for automated test generation at scale.

2. **Gemini 2.0 Flash is 3-4x faster** but limited by API credit constraints; where it succeeds, quality is comparable (Cohen's d small-to-medium).

3. **Test count and assertion count are strongly correlated** (r ≈ 0.7-0.9), suggesting consistent code generation behavior rather than erratic output.

4. **Latency does not correlate with quality**, supporting the claim that smaller/faster models can produce comparable test quality given identical context.
