#!/bin/bash
# Validates all SKILL.md files have required frontmatter fields,
# checks marketplace.json consistency, and enforces spec limits.
# Usage: ./validate-skills.sh

set -euo pipefail

SKILLS_DIR="skills"
MARKETPLACE=".claude-plugin/marketplace.json"
TOTAL_ERRORS=0
CHECKED=0
MAX_NAME_LEN=64
MAX_DESC_LEN=1024

echo "Validating skills..."
echo "===================="

# Temp file for duplicate name detection (bash 3.x compatible)
SEEN_NAMES_FILE=$(mktemp)
trap 'rm -f "$SEEN_NAMES_FILE"' EXIT

for skill_dir in "$SKILLS_DIR"/*/; do
  dir_name=$(basename "$skill_dir")
  skill_file="$skill_dir/SKILL.md"
  skill_errors=0

  if [ ! -f "$skill_file" ]; then
    echo "FAIL: $dir_name — missing SKILL.md"
    TOTAL_ERRORS=$((TOTAL_ERRORS + 1))
    continue
  fi

  CHECKED=$((CHECKED + 1))

  # Check for opening frontmatter delimiter
  first_line=$(head -n 1 "$skill_file")
  if [ "$first_line" != "---" ]; then
    echo "FAIL: $dir_name — missing frontmatter (no opening ---)"
    TOTAL_ERRORS=$((TOTAL_ERRORS + 1))
    continue
  fi

  # Check for closing frontmatter delimiter
  closing_line=$(awk 'NR>1 && /^---$/{print NR; exit}' "$skill_file")
  if [ -z "$closing_line" ]; then
    echo "FAIL: $dir_name — missing closing frontmatter delimiter (---)"
    TOTAL_ERRORS=$((TOTAL_ERRORS + 1))
    continue
  fi

  # Extract frontmatter (between first and second ---)
  frontmatter=$(awk 'NR==1{next} /^---$/{exit} {print}' "$skill_file")

  # Check required field: name
  if ! echo "$frontmatter" | grep -q "^name:"; then
    echo "FAIL: $dir_name — missing 'name' field"
    skill_errors=$((skill_errors + 1))
  fi

  # Check required field: description
  if ! echo "$frontmatter" | grep -q "^description:"; then
    echo "FAIL: $dir_name — missing 'description' field"
    skill_errors=$((skill_errors + 1))
  fi

  # Extract name value
  yaml_name=$(echo "$frontmatter" | grep "^name:" | sed 's/^name: *//' || true)

  if [ -n "$yaml_name" ]; then
    # Check name matches directory
    if [ "$yaml_name" != "$dir_name" ]; then
      echo "FAIL: $dir_name — name field '$yaml_name' doesn't match directory name"
      skill_errors=$((skill_errors + 1))
    fi

    # Check name format (lowercase, hyphens, numbers only)
    if ! echo "$yaml_name" | grep -qE '^[a-z][a-z0-9-]*[a-z0-9]$'; then
      echo "FAIL: $dir_name — name must be lowercase letters, numbers, hyphens only"
      skill_errors=$((skill_errors + 1))
    fi

    # Check name length
    name_len=${#yaml_name}
    if [ "$name_len" -gt "$MAX_NAME_LEN" ]; then
      echo "FAIL: $dir_name — name is $name_len chars (max $MAX_NAME_LEN)"
      skill_errors=$((skill_errors + 1))
    fi

    # Check for duplicate names
    prev_dir=$(grep "^${yaml_name}=" "$SEEN_NAMES_FILE" 2>/dev/null | head -1 | cut -d= -f2 || true)
    if [ -n "$prev_dir" ]; then
      echo "FAIL: $dir_name — duplicate skill name '$yaml_name' (also in $prev_dir)"
      skill_errors=$((skill_errors + 1))
    else
      echo "${yaml_name}=${dir_name}" >> "$SEEN_NAMES_FILE"
    fi
  fi

  # Extract and check description length
  # Description may be on one line or span multiple lines with quotes
  desc_value=$(echo "$frontmatter" | grep "^description:" | sed 's/^description: *//' | sed 's/^"//' | sed 's/"$//' || true)
  if [ -n "$desc_value" ]; then
    desc_len=${#desc_value}
    if [ "$desc_len" -gt "$MAX_DESC_LEN" ]; then
      echo "FAIL: $dir_name — description is $desc_len chars (max $MAX_DESC_LEN)"
      skill_errors=$((skill_errors + 1))
    fi
  fi

  # Check for version in metadata
  if ! echo "$frontmatter" | grep -q "version:"; then
    echo "WARN: $dir_name — no version in metadata"
  fi

  # Check line count
  line_count=$(wc -l < "$skill_file")
  if [ "$line_count" -gt 500 ]; then
    echo "WARN: $dir_name — $line_count lines (recommended max: 500)"
  fi

  if [ "$skill_errors" -eq 0 ]; then
    echo "  OK: $dir_name"
  fi

  TOTAL_ERRORS=$((TOTAL_ERRORS + skill_errors))
done

# Cross-reference: directories vs marketplace.json
echo ""
echo "Checking marketplace.json consistency..."
echo "========================================="

if [ ! -f "$MARKETPLACE" ]; then
  echo "FAIL: $MARKETPLACE not found"
  TOTAL_ERRORS=$((TOTAL_ERRORS + 1))
else
  # Get skill paths from marketplace.json (strip quotes, commas, whitespace)
  marketplace_skills=$(grep -oE '\./skills/[a-z0-9-]+' "$MARKETPLACE" | sed 's|^\./skills/||' | sort)

  # Get actual directories
  actual_dirs=$(ls -1 "$SKILLS_DIR" | sort)

  # Check: every directory should be in marketplace.json
  for dir in $actual_dirs; do
    if ! echo "$marketplace_skills" | grep -qx "$dir"; then
      echo "FAIL: $dir — has a directory but missing from marketplace.json"
      TOTAL_ERRORS=$((TOTAL_ERRORS + 1))
    fi
  done

  # Check: every marketplace.json entry should have a directory
  for skill in $marketplace_skills; do
    if [ ! -d "$SKILLS_DIR/$skill" ]; then
      echo "FAIL: $skill — listed in marketplace.json but no directory found"
      TOTAL_ERRORS=$((TOTAL_ERRORS + 1))
    fi
  done

  echo "  Marketplace entries: $(echo "$marketplace_skills" | wc -l | tr -d ' ')"
  echo "  Skill directories:  $(echo "$actual_dirs" | wc -l | tr -d ' ')"
fi

echo ""
echo "===================="
echo "Checked: $CHECKED skills"
echo "Errors: $TOTAL_ERRORS"

if [ $TOTAL_ERRORS -gt 0 ]; then
  exit 1
else
  echo "All skills valid!"
fi
