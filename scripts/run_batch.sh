#!/bin/bash
# Batch test generator — runs projects sequentially
# Usage: export NVIDIA_API_KEY="..." && bash scripts/run_batch.sh [project]

set -e
PROJECT="${1:-commons-cli}"
MODEL="meta/llama-3.3-70b-instruct"
OUTPUT_BASE="results/nvidia"

echo "=========================================="
echo "Batch Generator — $PROJECT"
echo "Model: $MODEL"
echo "Output: $OUTPUT_BASE/$PROJECT"
echo "=========================================="

# Generate tests using the modern CLI
python main.py generate \
  datasets/reftest-12/$PROJECT/src/main/java/... \
  datasets/reftest-12/$PROJECT/ \
  --provider nvidia \
  --model $MODEL \
  --output $OUTPUT_BASE/$PROJECT

echo "Done: $PROJECT"
