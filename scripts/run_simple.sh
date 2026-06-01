#!/bin/bash
# Simple batch runner — no Python dependencies
# Usage: export NVIDIA_API_KEY="..." && bash scripts/run_simple.sh <project>
PROJECT="$1"
# Try to load from .env if available
if [ -z "$NVIDIA_API_KEY" ] && [ -f ".env" ]; then
  NVIDIA_API_KEY=$(grep "^NVIDIA_API_KEY=" .env | cut -d= -f2-)
fi
if [ -z "$NVIDIA_API_KEY" ]; then
  echo "ERROR: NVIDIA_API_KEY not set. Do: export NVIDIA_API_KEY=\"nvapi-...\""
  exit 1
fi
export NVIDIA_API_KEY

echo "=== Generating tests for $PROJECT ==="

RESULTS="results/nvidia/$PROJECT"
mkdir -p "$RESULTS"

find datasets/reftest-12/$PROJECT/java -name "*.java" -not -path "*/test/*" -not -name "module-info.java" -not -name "package-info.java" 2>/dev/null > /tmp/files.txt
find datasets/reftest-12/$PROJECT/src -name "*.java" -not -path "*/test/*" -not -name "module-info.java" -not -name "package-info.java" 2>/dev/null >> /tmp/files.txt
TOTAL=$(wc -l < /tmp/files.txt)
COUNT=0

while IFS= read -r f; do
  [ -z "$f" ] && continue
  COUNT=$((COUNT+1))
  RUN_ID=$(printf "run_%04d" "$COUNT")
  REL=$(echo "$f" | sed 's|datasets/reftest-12/'"$PROJECT"'/||')
  
  echo "[$COUNT/$TOTAL] $REL"
  
  # Find project root (dir with pom.xml)
  PROJ_ROOT=$(dirname "$f")
  while [ "$PROJ_ROOT" != "/" ] && [ ! -f "$PROJ_ROOT/pom.xml" ] && [ ! -f "$PROJ_ROOT/build.gradle" ]; do
    PROJ_ROOT=$(dirname "$PROJ_ROOT")
  done
  
  timeout 120 python main.py generate "$f" "$PROJ_ROOT" \
    --provider nvidia --model meta/llama-3.3-70b-instruct \
    --output "$RESULTS/$RUN_ID" 2>/dev/null | tail -1
done < /tmp/files.txt

echo "=== $PROJECT done: $COUNT files ==="
