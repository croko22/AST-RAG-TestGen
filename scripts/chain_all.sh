#!/bin/bash
# Master chain — runs all remaining RefTest-12 projects sequentially
# Usage: export NVIDIA_API_KEY="nvapi-..." && bash scripts/chain_all.sh [start_from_project]
#
# Already done: commons-cli (14/14)
# Partial:      cucumber-expressions (6/34)
#
# Chain order: cucumber-expressions → commons-dbutils → commons-validator → datafaker → ice4j → jsoup → openapi-diff → rtree → morel → commons-collections4

START_FROM="${1:-1}"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$PROJECT_DIR"

# Load API key
if [ -z "$NVIDIA_API_KEY" ] && [ -f ".env" ]; then
  NVIDIA_API_KEY=$(grep "^NVIDIA_API_KEY=" .env | cut -d= -f2-)
fi
if [ -z "$NVIDIA_API_KEY" ]; then
  echo "FATAL: NVIDIA_API_KEY not set"
  exit 1
fi
export NVIDIA_API_KEY

PROJECTS=(
  "cucumber-expressions:7"    # resume from file 7 (first 6 done)
  "commons-dbutils:1"
  "commons-validator:1"
  "datafaker:1"
  "ice4j:1"
  "jsoup:1"
  "openapi-diff:1"
  "rtree:1"
  "morel:1"
  "commons-collections4:1"
)

START_SKIP=$((START_FROM - 1))
SKIPPED=0

for entry in "${PROJECTS[@]}"; do
  PROJ="${entry%%:*}"
  START="${entry##*:}"

  if [ $SKIPPED -lt $START_SKIP ]; then
    echo "=== ⏭️ SKIP $PROJ (start_from=$START_FROM) ==="
    SKIPPED=$((SKIPPED + 1))
    continue
  fi

  echo ""
  echo "=============================================="
  echo "🏁 Starting: $PROJ (from file #$START)"
  echo "Started at: $(date)"
  echo "=============================================="

  bash scripts/resume_batch.sh "$PROJ" "$START"

  EXIT=$?
  echo "[$PROJ] Exit code: $EXIT at $(date)"

  # Brief pause between projects
  sleep 5
done

echo ""
echo "=============================================="
echo "✅ ALL PROJECTS COMPLETE"
echo "Finished at: $(date)"
echo "=============================================="
