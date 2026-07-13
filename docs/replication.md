## Replication Guide

## Prerequisites

- **Python 3.11+** — verify with `python --version`
- **Java 11+ JDK** — required for compilation validation and coverage analysis of generated tests. Verify with `javac -version`
- **Git** — verify with `git --version`
- **At least one LLM API key** (see the table in Step 3)
- (Optional) **Conda** — for isolated environment management

## Step 1: Clone the Repository

```bash
git clone https://github.com/croko22/AST-RAG-TestGen.git
cd AST-RAG-TestGen
```

## Step 2: Environment Setup

### Option A: Conda (recommended)

```bash
conda env create -f environment.yml
conda activate ast-rag-testgen
```

### Option B: pip

```bash
# Install the package in editable mode with development tools
pip install -e .[dev]
```

Verify the installation:

```bash
python -c "import tree_sitter_java; print('tree-sitter OK')"
python main.py providers
```

## Step 3: Configure API Keys

Copy the template and edit it with your credentials:

```bash
cp .env.template .env
```

Open `.env` and set at least **one** API key. You only need one provider to generate tests.

| Provider | Environment Variable | Where to Get It |
|----------|---------------------|-----------------|
| Anthropic | `ANTHROPIC_API_KEY` | https://console.anthropic.com/ |
| OpenAI | `OPENAI_API_KEY` | https://platform.openai.com/api-keys |
| Gemini (Google) | `GEMINI_API_KEY` | https://aistudio.google.com/apikey |
| GLM (Zhipu AI) | `GLM_API_KEY` | https://open.bigmodel.cn/user/apikey |
| OpenRouter | `OPENROUTER_API_KEY` | https://openrouter.ai/keys |
| NVIDIA | `NVIDIA_API_KEY` | https://build.nvidia.com/ (click "Get API Key") |

Then set the provider and model you want to use:

```bash
# Example: using OpenAI
LLM_PROVIDER=openai
LLM_MODEL=gpt-4-turbo
```

For the full list of default models per provider, see `python main.py providers`.

## Step 4: Generate Your First Test (Quickstart)

The repository includes a small example Java project under `examples/`. No external setup needed — the files are committed to the repo so they exist on a fresh clone.

```bash
# Generate a test for CalculatorService (the service depends on Calculator + StringUtils)
python main.py generate examples/src/main/java/com/example/CalculatorService.java examples/
```

To print the generated test directly to stdout instead of writing a file:

```bash
python main.py generate examples/src/main/java/com/example/CalculatorService.java examples/ --print
```

### Expected Output

A JUnit 5 + Mockito test file is generated for `CalculatorService`, covering methods such as `addAndFormat`, `reverseSum`, `isConcatenatedPalindrome`, etc. The file is saved to `tests_generados/` (or printed to the terminal with `--print`).

### Choosing a Different Provider

```bash
# Using NVIDIA (free tier available)
python main.py generate examples/src/main/java/com/example/CalculatorService.java examples/ \
  --provider nvidia --model meta/llama-3.3-70b-instruct

# Using OpenAI
python main.py generate examples/src/main/java/com/example/CalculatorService.java examples/ \
  --provider openai --model gpt-4-turbo

# Using Gemini
python main.py generate examples/src/main/java/com/example/CalculatorService.java examples/ \
  --provider gemini --model gemini-2.0-flash-exp
```

## Step 5: RAG-Enhanced Generation (Optional)

For larger codebases, the RAG pipeline retrieves semantically relevant code chunks to enrich the prompt context:

```bash
python main.py generate examples/src/main/java/com/example/CalculatorService.java examples/ \
  --rag-enabled --alpha 0.7
```

The `--alpha` parameter controls the blend between vector similarity (semantic) and AST-based relevance:

- `alpha = 1.0`: pure vector similarity
- `alpha = 0.0`: pure structural (AST) relevance
- `alpha = 0.7` (default): weighted toward semantic similarity

## Step 6: Benchmark Replication

### Dataset

The full benchmark dataset (RefTest-12) will be published alongside the thesis document. Once available, download and extract it to `datasets/reftest-12/`. The directory structure should look like:

```
datasets/reftest-12/
├── manifest.yaml           # Benchmark configuration
├── projects/
│   ├── project-1/
│   ├── project-2/
│   └── ...
└── tests/
    └── reference/
```

### Running the Benchmark

```bash
# Run the full benchmark
python main.py benchmark datasets/reftest-12/manifest.yaml --output ./results

# Dry run (skip LLM calls, useful for validating the manifest)
python main.py benchmark datasets/reftest-12/manifest.yaml --dry-run
```

### Expected Results

The benchmark produces metrics for each project and model combination:

- **Compile Rate**: percentage of generated tests that compile successfully
- **Test Pass Rate**: percentage of tests that pass at runtime
- **Line Coverage**: percentage of source lines covered by the generated tests (via JaCoCo)
- **Branch Coverage**: percentage of branches covered
- **Quality Scores**: heuristic assessment covering assertion density, method diversity, and triviality avoidance

Results are saved to the output directory as JSON and summary tables. For the complete results table and statistical analysis, refer to the thesis document.

## Troubleshooting

| Problem | Solution |
|---------|----------|
| `ModuleNotFoundError: tree_sitter` | Run `pip install -e .` again, or `conda activate ast-rag-testgen` if using conda |
| `ModuleNotFoundError: tree_sitter_java` | Ensure `tree-sitter-java` is installed: `pip install tree-sitter-java` |
| API key errors (401/403) | Check `.env` has the correct key for your chosen provider; verify no trailing whitespace or quotes |
| `examples/ not found` | Make sure you are in the repo root (`AST-RAG-TestGen/`), not a subdirectory |
| Coverage not available | JaCoCo must be configured in the target Java project; see `docs/architecture/benchmark-mode-hardening.md` |
| `CUDA not available` (RAG mode) | The embedding model runs on CPU by default; no GPU is required |
| Generated test does not compile | Try a different model or provider; NVIDIA `llama-3.3-70b-instruct` is a good free fallback |

## Questions?

Open an issue: https://github.com/croko22/AST-RAG-TestGen/issues