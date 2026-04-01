# Spring Boot Benchmark Campaign v1 — Learnings & Gaps

**Campaign**: spring-boot-v1  
**Date**: 2026-03-29  
**Status**: Complete

---

## 1. Key Learnings

### 1.1 Repository Selection Criteria
- **Good**: Hand-picked Spring Boot repos with clear service layers work best
- **Learning**: Automated repo discovery is risky — manual curation ensures stable builds
- **Recommendation**: Always validate repo structure before campaign execution

### 1.2 Path Resolution is Critical
- **Issue**: Initial manifest used wrong paths for petclinic (`service/OwnerService.java` instead of `owner/Owner.java`)
- **Impact**: First batch of petclinic targets failed to resolve
- **Fix**: Created `manifest-batch4b.json` with corrected paths
- **Learning**: Always verify Java file paths exist in repository before running

### 1.3 Dependency Depth Trade-offs
- **Setting**: `max_dependencies: 10` used for all runs
- **Observation**: Some complex services (JWT security) took longer (157s) than simple ones (6s)
- **Learning**: Consider varying depth based on service complexity in future runs

### 1.4 LLM Provider Stability
- **Provider**: NVIDIA (meta/llama-3.1-405b-instruct)
- **Result**: 100% success rate (9/9 runs completed successfully)
- **Observation**: No API errors or timeouts encountered in this campaign
- **Learning**: NVIDIA API proved reliable for this workload

### 1.5 Coverage Measurement Gap
- **Issue**: `coverage_pct` is `null` in all results
- **Root cause**: Coverage extraction not fully implemented in evaluator
- **Impact**: Cannot calculate weighted scores properly (coverage weight = 0.3)
- **Recommendation**: Implement coverage measurement for future campaigns

---

## 2. Issues Encountered

### 2.1 Petclinic Path Mismatch (Critical — Fixed)

| Aspect | Detail |
|--------|--------|
| **Issue** | Manifest referenced non-existent paths |
| **Paths in manifest.yaml** | `src/main/java/org/springframework/samples/petclinic/service/OwnerService.java` |
| **Actual paths** | `src/main/java/org/springframework/samples/petclinic/owner/Owner.java` |
| **Error type** | File not found during dependency resolution |
| **Resolution** | Created new manifest `manifest-batch4b.json` with correct paths |
| **Run IDs affected** | petclinic-owner, petclinic-pet, petclinic-vet, petclinic-visit |
| **Status** | Fixed in Batch-4b — all 4 targets succeeded |

### 2.2 Coverage Extraction Not Implemented (Known Gap)

| Aspect | Detail |
|--------|--------|
| **Issue** | `coverage_pct` always returns null |
| **Root cause** | `benchmark/evaluator.py` doesn't extract JaCoCo or similar coverage data |
| **Impact** | Scoring formula cannot fully evaluate (coverage weight effectively 0) |
| **Recommendation** | Implement coverage extraction for v2 |

### 2.3 Tutorial Repository Excluded (By Design)

| Aspect | Detail |
|--------|--------|
| **Repository** | https://github.com/eugenp/tutorials.git |
| **Issue** | No clear service-layer files matching criteria |
| **Size** | Very large (multiple modules) |
| **Decision** | Excluded from campaign (0 targets run) |
| **Status** | Documented but not attempted |

---

## 3. Gaps Identified

### 3.1 Coverage Measurement
- **Gap**: No coverage data extracted from Maven test runs
- **Severity**: Medium — affects scoring accuracy
- **Fix needed**: Parse Maven output or integrate JaCoCo report parsing

### 3.2 Multi-Trial Statistics
- **Gap**: Only single trial per service (`trials: 1`)
- **Severity**: Low — limits statistical significance
- **Fix needed**: Run multiple trials and average for v2

### 3.3 Multi-Provider Comparison
- **Gap**: Only NVIDIA provider used
- **Severity**: Low — limits thesis comparison scope
- **Fix needed**: Add Anthropic/OpenAI for RAG vs RL comparison in v2

### 3.4 Tutorial Repository Not Tested
- **Gap**: tutorials repo excluded
- **Severity**: Low — only 1 of 4 repos tested
- **Fix needed**: Find valid service paths or exclude from manifest

---

## 4. Mitigations Applied

| Issue | Mitigation | Effectiveness |
|-------|------------|---------------|
| Petclinic path mismatch | Created manifest-batch4b.json with correct paths | ✅ Fixed |
| Coverage null | Used success rate as primary metric | ✅ Workaround |
| No API errors | Sequential execution (concurrency=1) | ✅ Preventative |

---

## 5. Recommendations for Future Campaigns

### Priority 1 (Critical)
1. Implement coverage extraction in evaluator
2. Add path validation before running benchmark

### Priority 2 (High)
3. Add multi-provider comparison (Anthropic + OpenAI)
4. Run multiple trials per service (trials: 3)

### Priority 3 (Medium)
5. Test on larger repositories (add more services per repo)
6. Implement result caching to avoid redundant LLM calls

---

## 6. Metrics Summary

| Metric | Value |
|--------|-------|
| Total runs | 13 (9 + 4) |
| Successful | 13 |
| Success rate | 100% |
| Avg latency | 85.5s (across all) |
| Models tested | 1 (meta/llama-3.1-405b-instruct) |
| Providers used | 1 (NVIDIA) |
| Repositories | 3 of 4 (petclinic, gs-spring-boot, jwt-security) |

---

## 7. Thesis Relevance

- **RAG vs RL**: This campaign establishes baseline metrics for RAG-based test generation
- **Comparison point**: Success rate (100%) and latency (avg 85.5s) are key comparison points
- **Gap to address**: Coverage measurement needed for complete comparison
- **Future work**: Add RL-based approach for apples-to-apples comparison
