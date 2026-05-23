#!/bin/bash
# Spring Boot Benchmark Campaign v1 - Multi-Provider Execution Script
# ======================================================
# Runs benchmarks across multiple LLM providers and generates a
# consolidated comparison report.
#
# Usage:
#   ./run-multi.sh                    # Run full multi-provider campaign
#   ./run-multi.sh --clone-only       # Only clone repositories
#   ./run-multi.sh --dry-run          # Dry run (no LLM calls)
#   ./run-multi.sh --help             # Show help
#
# Requirements:
#   - Python 3.10+
#   - Java 11+
#   - Maven 3.6+
#   - At least one LLM API key (ANTHROPIC_API_KEY, OPENAI_API_KEY, etc.)
#
# Output:
#   - results-{provider}/    # Per-provider results (JSON)
#   - results-comparison/    # Consolidated comparison report

set -euo pipefail

# ---------------------------------------------------------------------------
# Configuration
# ---------------------------------------------------------------------------
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
MANIFEST_PATH="$SCRIPT_DIR/manifest-multi.json"
REPOS_DIR="$SCRIPT_DIR/repos"
COMPARISON_DIR="$SCRIPT_DIR/results-comparison"

# Provider definitions: "provider|model|env_key"
# Edit this array to add/remove providers or change models.
declare -a PROVIDERS=(
    "nvidia|meta/llama-3.3-70b-instruct|NVIDIA_API_KEY"
    "openai|gpt-4o|OPENAI_API_KEY"
    "anthropic|claude-3-5-sonnet-20241022|ANTHROPIC_API_KEY"
    "gemini|gemini-2.0-flash|GEMINI_API_KEY"
    "openrouter|anthropic/claude-3.5-sonnet|OPENROUTER_API_KEY"
)

# Repository definitions (shared with run.sh)
declare -A REPOS=(
    ["spring-petclinic"]="https://github.com/spring-projects/spring-petclinic.git"
    ["spring-boot-3-jwt-security"]="https://github.com/ali-bouali/spring-boot-3-jwt-security.git"
    ["gs-spring-boot"]="https://github.com/spring-guides/gs-spring-boot.git"
)

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# ---------------------------------------------------------------------------
# Helper functions
# ---------------------------------------------------------------------------
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

log_provider() {
    echo -e "${CYAN}[PROVIDER]${NC} $1"
}

show_help() {
    cat << EOF
Spring Boot Benchmark Campaign v1 - Multi-Provider Execution Script

Usage: $0 [OPTIONS]

Options:
    --clone-only    Only clone repositories, don't run benchmarks
    --dry-run       Run benchmarks in dry-run mode (no LLM calls)
    --help          Show this help message

Environment Variables (at least one required):
    NVIDIA_API_KEY          NVIDIA / Llama API key
    OPENAI_API_KEY          OpenAI API key
    ANTHROPIC_API_KEY       Anthropic API key
    GEMINI_API_KEY          Google Gemini API key
    OPENROUTER_API_KEY      OpenRouter API key

Provider/Model Pairs (editable in script):
EOF
    for entry in "${PROVIDERS[@]}"; do
        IFS='|' read -r provider model env_key <<< "$entry"
        printf "    %-12s %-40s (requires %s)\n" "$provider" "$model" "$env_key"
    done

    cat << EOF

Examples:
    $0                          # Run full multi-provider campaign
    $0 --clone-only             # Clone repos only
    $0 --dry-run                # Dry run across all providers

EOF
}

# ---------------------------------------------------------------------------
# Clone all repositories
# ---------------------------------------------------------------------------
clone_repos() {
    log_info "Cloning Spring Boot repositories..."

    mkdir -p "$REPOS_DIR"

    for repo_name in "${!REPOS[@]}"; do
        repo_url="${REPOS[$repo_name]}"
        repo_path="$REPOS_DIR/$repo_name"

        if [ -d "$repo_path" ]; then
            log_warn "Repository $repo_name already exists, skipping clone"
            continue
        fi

        log_info "Cloning $repo_name from $repo_url..."
        git clone --depth 1 "$repo_url" "$repo_path"
        log_success "Cloned $repo_name"
    done

    log_success "All repositories cloned"
}

# ---------------------------------------------------------------------------
# Check environment prerequisites
# ---------------------------------------------------------------------------
check_prerequisites() {
    log_info "Checking prerequisites..."

    # Check Python
    if ! command -v python3 &> /dev/null; then
        log_error "Python 3 not found"
        exit 1
    fi
    PYTHON_VERSION=$(python3 --version | cut -d' ' -f2)
    log_info "Python version: $PYTHON_VERSION"

    # Check Java
    if ! command -v java &> /dev/null; then
        log_error "Java not found"
        exit 1
    fi
    JAVA_VERSION=$(java -version 2>&1 | head -1)
    log_info "Java: $JAVA_VERSION"

    # Check Maven
    if ! command -v mvn &> /dev/null; then
        log_error "Maven not found"
        exit 1
    fi
    MVN_VERSION=$(mvn -version 2>&1 | head -1)
    log_info "Maven: $MVN_VERSION"

    # Check that at least one provider has its API key set
    local found_key=0
    for entry in "${PROVIDERS[@]}"; do
        IFS='|' read -r _ _ env_key <<< "$entry"
        if [ -n "${!env_key:-}" ]; then
            found_key=1
            break
        fi
    done

    if [ "$found_key" -eq 0 ]; then
        log_warn "No LLM API keys found in environment"
        log_warn "Set at least one of: NVIDIA_API_KEY, OPENAI_API_KEY, ANTHROPIC_API_KEY, GEMINI_API_KEY, OPENROUTER_API_KEY"
    fi

    log_success "Prerequisites check complete"
}

# ---------------------------------------------------------------------------
# Run benchmark for a single provider
# ---------------------------------------------------------------------------
run_single_provider() {
    local provider="$1"
    local model="$2"
    local env_key="$3"
    local dry_run_flag="${4:-}"
    local output_dir="$SCRIPT_DIR/results-${provider}"

    log_provider "Running benchmark for $provider ($model)"

    # Check if the required API key is available
    if [ -z "${!env_key:-}" ]; then
        log_warn "Skipping $provider: $env_key is not set"
        return 0
    fi

    # Set up output directory
    mkdir -p "$output_dir"

    log_info "  Manifest : $MANIFEST_PATH"
    log_info "  Output   : $output_dir"
    log_info "  Provider : $provider"
    log_info "  Model    : $model"

    # Change to project root to run the benchmark
    cd "$PROJECT_ROOT"

    # Build the command
    local cmd="python3 main.py \
        --benchmark-manifest \"$MANIFEST_PATH\" \
        --benchmark-output \"$output_dir\" \
        --provider $provider \
        --model \"$model\""

    if [ -n "$dry_run_flag" ]; then
        cmd="$cmd --benchmark-dry-run"
    fi

    # Execute
    eval "$cmd"
    local exit_code=$?

    if [ $exit_code -eq 0 ]; then
        log_success "Benchmark completed for $provider"
    else
        log_error "Benchmark failed for $provider (exit code $exit_code)"
    fi

    return $exit_code
}

# ---------------------------------------------------------------------------
# Generate consolidated comparison report
# ---------------------------------------------------------------------------
generate_comparison() {
    log_info "Generating consolidated comparison report..."

    mkdir -p "$COMPARISON_DIR"

    local report_file="$COMPARISON_DIR/consolidated-report.md"

    python3 -c "
import json
import os
from datetime import datetime, timezone

script_dir = '$SCRIPT_DIR'
providers_config = [
    $(for entry in "${PROVIDERS[@]}"; do
        IFS='|' read -r provider model env_key <<< "$entry"
        echo "    {'provider': '$provider', 'model': '$model'},"
    done)
]

# Collect results from each provider
all_results = []
for cfg in providers_config:
    provider = cfg['provider']
    model = cfg['model']
    results_path = os.path.join(script_dir, f'results-{provider}', 'results.json')

    if not os.path.isfile(results_path):
        print(f'  [WARN] No results found for {provider}: {results_path}')
        continue

    with open(results_path, 'r') as f:
        data = json.load(f)

    runs = data.get('runs', [])
    if not runs:
        print(f'  [WARN] No runs found for {provider}')
        continue

    total = len(runs)
    success_count = sum(1 for r in runs if r.get('status') == 'ok')
    success_rate = (success_count / total * 100) if total > 0 else 0

    latencies = [r.get('latency_ms', 0) for r in runs if r.get('status') == 'ok']
    avg_latency = sum(latencies) / len(latencies) if latencies else 0

    compile_pass = sum(1 for r in runs if r.get('metrics', {}).get('compile_pass', False))
    test_pass = sum(1 for r in runs if r.get('metrics', {}).get('test_pass', False))

    # Compute a composite score:
    #   success_rate (weight 0.5) + compile_rate (0.25) + test_rate (0.25)
    #   All normalized to 0-100.
    compile_rate = (compile_pass / total * 100) if total > 0 else 0
    test_rate = (test_pass / total * 100) if total > 0 else 0
    score = (success_rate * 0.5) + (compile_rate * 0.25) + (test_rate * 0.25)

    all_results.append({
        'provider': provider,
        'model': model,
        'total_runs': total,
        'success_count': success_count,
        'success_rate': success_rate,
        'avg_latency_ms': avg_latency,
        'compile_pass': compile_pass,
        'compile_rate': compile_rate,
        'test_pass': test_pass,
        'test_rate': test_rate,
        'score': score,
    })

# Sort by score descending
all_results.sort(key=lambda x: x['score'], reverse=True)

# Generate Markdown report
now = datetime.now(timezone.utc).strftime('%Y-%m-%d %H:%M:%S UTC')
lines = []
lines.append('# Multi-Provider Benchmark Comparison Report')
lines.append('')
lines.append(f'Generated: {now}')
lines.append(f'Manifest: \`manifest-multi.json\`')
lines.append(f'Providers evaluated: {len(all_results)}')
lines.append('')
lines.append('## Summary Table')
lines.append('')
lines.append('| Rank | Provider | Model | Runs | Success Rate | Compile Rate | Test Rate | Avg Latency (ms) | Score |')
lines.append('|------|----------|-------|------|-------------|-------------|-----------|------------------|-------|')

for idx, r in enumerate(all_results, 1):
    lines.append(
        f'| {idx} '
        f'| {r[\"provider\"]} '
        f'| \`{r[\"model\"]}\` '
        f'| {r[\"success_count\"]}/{r[\"total_runs\"]} '
        f'| {r[\"success_rate\"]:.1f}% '
        f'| {r[\"compile_rate\"]:.1f}% '
        f'| {r[\"test_rate\"]:.1f}% '
        f'| {r[\"avg_latency_ms\"]:.0f} '
        f'| {r[\"score\"]:.1f} |'
    )

lines.append('')
lines.append('## Scoring Formula')
lines.append('')
lines.append('\`\`\`')
lines.append('score = (success_rate * 0.50) + (compile_rate * 0.25) + (test_rate * 0.25)')
lines.append('\`\`\`')
lines.append('')
lines.append('All component rates are normalized to 0-100 before weighting.')
lines.append('')

# Per-provider details
lines.append('## Per-Provider Details')
lines.append('')

for r in all_results:
    lines.append(f'### {r[\"provider\"]} (\`{r[\"model\"]}\`)')
    lines.append('')
    lines.append(f'- **Total runs**: {r[\"total_runs\"]}')
    lines.append(f'- **Successful**: {r[\"success_count\"]} ({r[\"success_rate\"]:.1f}%)')
    lines.append(f'- **Compile pass**: {r[\"compile_pass\"]} ({r[\"compile_rate\"]:.1f}%)')
    lines.append(f'- **Test pass**: {r[\"test_pass\"]} ({r[\"test_rate\"]:.1f}%)')
    lines.append(f'- **Avg latency**: {r[\"avg_latency_ms\"]:.0f} ms ({r[\"avg_latency_ms\"]/1000:.2f} s)')
    lines.append(f'- **Score**: {r[\"score\"]:.1f}')
    lines.append('')

report_content = '\n'.join(lines)

with open('$report_file', 'w') as f:
    f.write(report_content)

print(report_content)
"

    if [ $? -eq 0 ]; then
        log_success "Consolidated report saved: $report_file"
    else
        log_error "Failed to generate comparison report"
        return 1
    fi
}

# ---------------------------------------------------------------------------
# Main execution
# ---------------------------------------------------------------------------
main() {
    local mode="full"

    # Parse arguments
    while [ $# -gt 0 ]; do
        case "$1" in
            --clone-only)
                mode="clone-only"
                shift
                ;;
            --dry-run)
                mode="dry-run"
                shift
                ;;
            --help|-h)
                show_help
                exit 0
                ;;
            *)
                log_error "Unknown option: $1"
                show_help
                exit 1
                ;;
        esac
    done

    echo "=========================================="
    echo "Spring Boot Benchmark Campaign v1"
    echo "Multi-Provider Run"
    echo "=========================================="
    echo ""

    check_prerequisites
    clone_repos

    # clone-only mode stops here
    if [ "$mode" = "clone-only" ]; then
        echo ""
        echo "=========================================="
        log_success "Clone-only complete!"
        echo "=========================================="
        exit 0
    fi

    # Determine dry-run flag
    local dry_run_flag=""
    if [ "$mode" = "dry-run" ]; then
        dry_run_flag="--dry-run"
        log_warn "Running in DRY-RUN mode (no LLM calls)"
    fi

    # Run benchmark for each provider
    local total_providers=${#PROVIDERS[@]}
    local current=0
    local failed=0
    local skipped=0

    for entry in "${PROVIDERS[@]}"; do
        current=$((current + 1))
        IFS='|' read -r provider model env_key <<< "$entry"

        echo ""
        echo "------------------------------------------"
        log_info "Provider [$current/$total_providers]: $provider"
        echo "------------------------------------------"

        if [ -z "${!env_key:-}" ]; then
            log_warn "Skipping $provider: $env_key is not set"
            skipped=$((skipped + 1))
            continue
        fi

        if ! run_single_provider "$provider" "$model" "$env_key" "$dry_run_flag"; then
            failed=$((failed + 1))
            log_warn "Provider $provider failed, continuing with next..."
        fi
    done

    # Generate comparison report (even if some providers failed)
    echo ""
    echo "=========================================="
    log_info "Generating comparison report..."
    echo "=========================================="
    generate_comparison || true

    # Print summary
    echo ""
    echo "=========================================="
    log_success "Multi-provider campaign complete!"
    echo "  Providers attempted: $total_providers"
    echo "  Skipped (no API key): $skipped"
    echo "  Failed: $failed"
    echo "  Succeeded: $((total_providers - skipped - failed))"
    echo ""
    echo "  Results per provider: $SCRIPT_DIR/results-{provider}/"
    echo "  Comparison report   : $COMPARISON_DIR/consolidated-report.md"
    echo "=========================================="
}

# Run main with all arguments
main "$@"
