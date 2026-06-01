#!/bin/bash
# Resume batch runner — skips completed files and resumes from where it left off
# Usage: export NVIDIA_API_KEY="nvapi-..." && bash scripts/resume_batch.sh <project> [start_from]
#
# - start_from: skip first (start_from-1) files and begin generating
# - The script uses the EXACT find order from the original run_simple.sh

PROJECT="$1"
START_FROM="${2:-1}"
MODEL="meta/llama-3.3-70b-instruct"

if [ -z "$PROJECT" ]; then
  echo "Usage: bash scripts/resume_batch.sh <project> [start_from]"
  exit 1
fi

if [ -z "$NVIDIA_API_KEY" ] && [ -f ".env" ]; then
  NVIDIA_API_KEY=$(grep "^NVIDIA_API_KEY=" .env | cut -d= -f2-)
fi
if [ -z "$NVIDIA_API_KEY" ]; then
  echo "ERROR: NVIDIA_API_KEY not set"
  exit 1
fi
export NVIDIA_API_KEY

RESULTS="results/nvidia/$PROJECT"
mkdir -p "$RESULTS"

# Collect ALL source files (no exclusions) — matches original run order
find datasets/reftest-12/$PROJECT/java -name "*.java" -not -path "*/test/*" 2>/dev/null > /tmp/files_${PROJECT}.txt
find datasets/reftest-12/$PROJECT/src -name "*.java" -not -path "*/test/*" 2>/dev/null >> /tmp/files_${PROJECT}.txt

TOTAL=$(wc -l < /tmp/files_${PROJECT}.txt)

echo "=== Resuming $PROJECT ==="
echo "Total files: $TOTAL"
echo "Starting from file #$START_FROM"
echo "Results dir: $RESULTS"

COUNT=0

# Skip the first (START_FROM - 1) files, then process the rest
tail -n +${START_FROM} /tmp/files_${PROJECT}.txt | while IFS= read -r f; do
  [ -z "$f" ] && continue
  COUNT=$((COUNT+1))
  ABS_INDEX=$((START_FROM + COUNT - 1))
  RUN_ID=$(printf "run_%04d" "$ABS_INDEX")

  # Skip if already completed
  if [ -f "$RESULTS/$RUN_ID/result.json" ]; then
    echo "[$ABS_INDEX/$TOTAL] ⏭️ SKIP (already done): $(basename $f)"
    continue
  fi

  REL=$(echo "$f" | sed 's|datasets/reftest-12/'"$PROJECT"'/||')

  # Find project root (dir with pom.xml or build.gradle)
  PROJ_ROOT=$(dirname "$f")
  while [ "$PROJ_ROOT" != "/" ] && [ ! -f "$PROJ_ROOT/pom.xml" ] && [ ! -f "$PROJ_ROOT/build.gradle" ]; do
    PROJ_ROOT=$(dirname "$PROJ_ROOT")
  done

  echo "[$ABS_INDEX/$TOTAL] 🏗️ Generating $REL"
  START_TS=$(date +%s)

  mkdir -p "$RESULTS/$RUN_ID"

  # main.py generate creates the test file but NOT result.json
  # Capture stdout to check for generated test
  GEN_OUTPUT=$(timeout 180 python main.py generate "$f" "$PROJ_ROOT" \
    --provider nvidia --model $MODEL \
    --output "$RESULTS/$RUN_ID" 2>/dev/null)

  EXIT_CODE=$?
  END_TS=$(date +%s)
  DURATION=$((END_TS - START_TS))

  # Find the generated test file
  GEN_FILE=$(ls "$RESULTS/$RUN_ID/"*Test.java 2>/dev/null | head -1)
  if [ -n "$GEN_FILE" ] && [ $EXIT_CODE -eq 0 ]; then
    # Count test methods and assertions
    TEST_COUNT=$(grep -c "@Test\|@ParameterizedTest\|@TestFactory" "$GEN_FILE" 2>/dev/null || echo 0)
    ASSERT_COUNT=$(grep -c "assert\|Assertions\.\|Assert\.\|verify(" "$GEN_FILE" 2>/dev/null || echo 0)
    CODE_LENGTH=$(wc -c < "$GEN_FILE" 2>/dev/null || echo 0)

    # Write result.json
    cat > "$RESULTS/$RUN_ID/result.json" << EOF
{
  "run_id": "$RUN_ID",
  "status": "success",
  "latency_ms": $((DURATION * 1000)),
  "generation_time_ms": $((DURATION * 1000)),
  "test_count": $TEST_COUNT,
  "assertion_count": $ASSERT_COUNT,
  "file": "$REL",
  "test_code_length": $CODE_LENGTH,
  "success": true
}
EOF
    echo "[$ABS_INDEX/$TOTAL] ✅ DONE in ${DURATION}s ($TEST_COUNT tests, $ASSERT_COUNT assertions): $REL"
  else
    echo "[$ABS_INDEX/$TOTAL] ❌ FAILED (exit=$EXIT_CODE, ${DURATION}s): $REL"
  fi
done

echo "=== $PROJECT done ==="
