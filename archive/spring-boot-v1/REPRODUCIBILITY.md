# Spring Boot Benchmark Campaign v1 — Reproducibility Protocol

**Campaign**: spring-boot-v1  
**Date**: 2026-03-29  
**Status**: Complete (9/9 targets = 100% success)

---

## 1. Environment & Toolchain Requirements

### System Requirements
| Component | Version | Notes |
|-----------|---------|-------|
| Python | 3.12.0 | Required for AST-RAG TestGen |
| Java | OpenJDK 17.0.2 | Required for Maven compilation |
| Maven | 3.9.5 | Required for build/test execution |
| Git | Any recent | For cloning repositories |

### LLM Provider
| Provider | Model | API Key Env Var |
|----------|-------|-----------------|
| NVIDIA | meta/llama-3.1-405b-instruct | `NVIDIA_API_KEY` |

Alternative providers (not used in this run):
- `ANTHROPIC_API_KEY` (Anthropic Claude)
- `OPENAI_API_KEY` (OpenAI GPT)
- `GLM_API_KEY` (GLM)
- `GEMINI_API_KEY` (Google Gemini)

---

## 2. Repository Targets

| ID | Repository | Branch | Size | Services |
|----|------------|--------|------|----------|
| petclinic | spring-petclinic | main | small | 4 (Owner, Pet, Vet, Visit) |
| gs-spring-boot | gs-spring-boot | main | small | 2 (Application, HelloController) |
| jwt-security | spring-boot-3-jwt-security | main | small-medium | 3 (Auth, Book, User) |
| tutorials | tutorials | master | large | 0 (excluded - no valid services) |

**Note**: tutorials repository was excluded due to complex structure and no clear service-layer files matching our criteria.

---

## 3. Execution Commands

### Full Campaign Rerun

```bash
# Navigate to project root
cd /path/to/Tesis-RAG-vs-RL-Arena

# Ensure conda environment is activated
conda activate ast-rag-testgen

# Set API key
export NVIDIA_API_KEY="your-api-key-here"

# Run the campaign
./campaigns/spring-boot-v1/run.sh
```

### Alternative: Direct Python Execution

```bash
python main.py benchmark \
    --manifest campaigns/spring-boot-v1/manifest.yaml \
    --output campaigns/spring-boot-v1/results
```

### Batch-4b Petclinic Fix (if needed)

```bash
python main.py benchmark \
    --manifest campaigns/spring-boot-v1/manifest-batch4b.json \
    --output campaigns/spring-boot-v1/results-batch4b
```

---

## 4. Output Artifacts

| Artifact | Location | Description |
|----------|----------|-------------|
| Manifest | `campaigns/spring-boot-v1/manifest.yaml` | Campaign configuration |
| Results (Batch 1) | `campaigns/spring-boot-v1/results/results.json` | Per-run results (9 runs) |
| Summary (Batch 1) | `campaigns/spring-boot-v1/results/summary.json` | Aggregated metrics |
| Report | `campaigns/spring-boot-v1/results/report.md` | Human-readable summary |
| CSV | `campaigns/spring-boot-v1/results/thesis_metrics.csv` | Thesis-ready metrics |
| Results (Batch 4b) | `campaigns/spring-boot-v1/results-batch4b/results.json` | Petclinic fix results (4 runs) |
| Toolchain | `campaigns/spring-boot-v1/results/toolchain-versions.json` | Captured toolchain versions |

### Generated Tests Location
- Batch 1: `campaigns/spring-boot-v1/results/run_*/`
- Batch 4b: `campaigns/spring-boot-v1/results-batch4b/run_*/`

---

## 5. Rerun Semantics

### Determinism Factors
1. **Fixed seed**: `seed=42` in manifest.yaml ensures deterministic random operations
2. **Sequential execution**: `concurrency=1` avoids race conditions
3. **Single trial**: `trials=1` for v1 (no averaging across trials)

### Non-Deterministic Factors
1. **LLM responses**: Model outputs may vary slightly between runs (temperature=0.3 used)
2. **Network latency**: API call timing affected by network conditions
3. **External repositories**: Repository availability depends on GitHub

### Reproducing Exact Results
To reproduce **exact** results:
1. Use the same `run_id` values (can be pre-generated and stored)
2. Use the same API key
3. Ensure toolchain versions match (Python 3.12.0, Java 17.0.2, Maven 3.9.5)
4. Use the same repository commits (shallow clones may differ)

---

## 6. Key Configuration Parameters

```yaml
# From manifest.yaml
run:
  trials: 1
  seed: 42
  max_dependencies: 10
  timeout_seconds: 300
  retry_count: 1
  concurrency: 1

matrix:
  providers:
    - name: "nvidia"
      model: "meta/llama-3.1-405b-instruct"
      temperature: 0.3
      max_tokens: 4096

scoring:
  weights:
    success: 0.5
    coverage: 0.3
    latency: 0.2
```

---

## 7. Troubleshooting

### Common Issues

| Issue | Cause | Solution |
|-------|-------|----------|
| API rate limits | Too many concurrent requests | Set `concurrency: 1` in manifest |
| Repository not found | Shallow clone issues | Use `git clone --depth 1` or full clone |
| Java compilation fails | Wrong Java version | Ensure Java 11+ is installed |
| Tests fail to compile | Missing dependencies | Run `mvn dependency:resolve` first |

### Verification Commands

```bash
# Verify environment
python3 --version  # Should be 3.12.0
java -version     # Should be OpenJDK 17.x
mvn -version      # Should be Maven 3.9.x

# Verify API key
echo $NVIDIA_API_KEY  # Should not be empty

# Verify repositories
ls -la campaigns/spring-boot-v1/repos/
```

---

## 8. Citation Reference

For thesis use, reference campaign results:
- **Metrics source**: `campaigns/spring-boot-v1/results/thesis_metrics.csv`
- **Full results**: `campaigns/spring-boot-v1/results/results.json`
- **Report**: `campaigns/spring-boot-v1/results/report.md`
- **Toolchain**: `campaigns/spring-boot-v1/manifest-batch4b.json` (contains toolchain_versions field)
