#!/bin/bash
# Spring Boot Benchmark Campaign v1 - Execution Script
# ======================================================
# This script runs the benchmark campaign on selected Spring Boot repositories.
#
# Usage:
#   ./run.sh                    # Run full campaign
#   ./run.sh --clone-only       # Only clone repositories
#   ./run.sh --dry-run          # Dry run (no LLM calls)
#   ./run.sh --help             # Show help
#
# Requirements:
#   - Python 3.10+
#   - Java 11+
#   - Maven 3.6+
#   - LLM API key (ANTHROPIC_API_KEY or equivalent)
#
# Output:
#   - results/           # Per-run results (JSON)
#   - summary.json       # Aggregated metrics
#   - report.md          # Human-readable report
#   - thesis_metrics.csv # Thesis-ready metrics

set -euo pipefail

# Configuration
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
MANIFEST_PATH="$SCRIPT_DIR/manifest.json"
OUTPUT_DIR="$SCRIPT_DIR/results"
REPOS_DIR="$SCRIPT_DIR/repos"

# Repository definitions
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
NC='\033[0m' # No Color

# Helper functions
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

show_help() {
    cat << EOF
Spring Boot Benchmark Campaign v1 - Execution Script

Usage: $0 [OPTIONS]

Options:
    --clone-only    Only clone repositories, don't run benchmark
    --dry-run       Run benchmark in dry-run mode (no LLM calls)
    --help          Show this help message

Environment Variables:
    ANTHROPIC_API_KEY       Anthropic API key (required for LLM)
    LLM_PROVIDER           Override default LLM provider
    LLM_MODEL               Override default model
    BENCHMARK_OUTPUT       Override output directory

Examples:
    $0                      # Run full campaign
    $0 --clone-only         # Clone repos only
    ANTHROPIC_API_KEY=sk-xxx $0  # With API key

EOF
}

# Clone all repositories
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

# Check environment prerequisites
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

    # Check LLM API key
    if [ -z "${ANTHROPIC_API_KEY:-}" ] && [ -z "${OPENAI_API_KEY:-}" ] && [ -z "${NVIDIA_API_KEY:-}" ]; then
        log_warn "No LLM API key found in environment"
        log_warn "Set ANTHROPIC_API_KEY, OPENAI_API_KEY, or NVIDIA_API_KEY"
    fi

    log_success "Prerequisites check complete"
}

# Capture toolchain versions
capture_toolchain() {
    log_info "Capturing toolchain versions..."

    local toolchain_file="$OUTPUT_DIR/toolchain-versions.json"

    python3 -c "
import json
import sys
import subprocess

versions = {
    'python_version': sys.version.split()[0],
    'java_version': 'unknown',
    'maven_version': 'unknown',
    'ast_rag_version': '1.0.0'
}

try:
    result = subprocess.run(['java', '-version'], capture_output=True, text=True, timeout=5)
    if result.stderr:
        versions['java_version'] = result.stderr.split('\\n')[0].strip()
except:
    pass

try:
    result = subprocess.run(['mvn', '-version'], capture_output=True, text=True, timeout=5)
    if result.stdout:
        versions['maven_version'] = result.stdout.split('\\n')[0].strip()
except:
    pass

print(json.dumps(versions, indent=2))
" > "$toolchain_file"

    log_success "Toolchain versions captured: $toolchain_file"
}

# Run the benchmark
run_benchmark() {
    local dry_run_flag=""
    if [ "${1:-}" = "--dry-run" ]; then
        dry_run_flag="--benchmark-dry-run"
        log_warn "Running in DRY-RUN mode (no LLM calls)"
    fi

    log_info "Setting up output directory..."
    mkdir -p "$OUTPUT_DIR"

    log_info "Capturing toolchain versions..."
    capture_toolchain

    log_info "Running benchmark..."
    log_info "Manifest: $MANIFEST_PATH"
    log_info "Output: $OUTPUT_DIR"

    # Change to project root to run the benchmark
    cd "$PROJECT_ROOT"

    # Run the benchmark
    if [ -n "$dry_run_flag" ]; then
        python3 main.py \
            --benchmark-manifest "$MANIFEST_PATH" \
            --benchmark-output "$OUTPUT_DIR" \
            $dry_run_flag
    else
        python3 main.py \
            --benchmark-manifest "$MANIFEST_PATH" \
            --benchmark-output "$OUTPUT_DIR"
    fi

    local exit_code=$?

    if [ $exit_code -eq 0 ]; then
        log_success "Benchmark completed successfully"
    else
        log_error "Benchmark failed with exit code $exit_code"
    fi

    return $exit_code
}

# Export thesis metrics
export_thesis_metrics() {
    local summary_file="$OUTPUT_DIR/summary.json"
    local csv_file="$OUTPUT_DIR/thesis_metrics.csv"

    if [ ! -f "$summary_file" ]; then
        log_warn "Summary file not found, skipping CSV export"
        return 1
    fi

    log_info "Exporting thesis metrics to CSV..."

    python3 -c "
import json

with open('$summary_file', 'r') as f:
    summary = json.load(f)

# Write CSV
with open('$csv_file', 'w') as f:
    f.write('model,success_rate,avg_latency_sec,coverage_pct,score\n')

    for model_id, metrics in summary.get('rankings', {}).items():
        success_rate = metrics.get('success_rate', 0)
        avg_latency = metrics.get('avg_latency_ms', 0) / 1000.0
        coverage = metrics.get('coverage_pct', 0)
        score = metrics.get('score', 0)

        f.write(f'{model_id},{success_rate:.2f},{avg_latency:.2f},{coverage:.2f},{score:.2f}\n')

print(f'Exported thesis metrics to $csv_file')
"

    log_success "Thesis metrics exported: $csv_file"
}

# Main execution
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
    echo "=========================================="
    echo ""

    case "$mode" in
        clone-only)
            check_prerequisites
            clone_repos
            ;;
        dry-run)
            check_prerequisites
            clone_repos
            run_benchmark --dry-run
            ;;
        full)
            check_prerequisites
            clone_repos
            run_benchmark
            export_thesis_metrics || true
            ;;
    esac

    echo ""
    echo "=========================================="
    log_success "Campaign execution complete!"
    echo "Results: $OUTPUT_DIR"
    echo "=========================================="
}

# Run main with all arguments
main "$@"
