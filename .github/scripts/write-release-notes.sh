#!/usr/bin/env bash
set -euo pipefail

version="${MOBILE_FOUNDATION_VERSION:?MOBILE_FOUNDATION_VERSION is required}"
notes_file="${1:-build/release-notes.md}"
current_tag="v${version}"
previous_tag="$(git tag --list 'v[0-9]*' --sort=-v:refname | grep -v "^${current_tag}$" | head -n 1 || true)"

mkdir -p "$(dirname "$notes_file")"

coordinates=()
while IFS= read -r coordinate; do
  coordinates+=("$coordinate")
done < <(
python3 - <<'PY'
from pathlib import Path
import xml.etree.ElementTree as ET

local_maven = Path("build/local-maven")

def pom_text(root, name):
    namespace = {"m": root.tag.split("}")[0].strip("{")} if root.tag.startswith("{") else {}
    path = f"m:{name}" if namespace else name
    return root.findtext(path, namespaces=namespace) or ""

seen = set()
for pom in sorted(local_maven.rglob("*.pom")):
    root = ET.parse(pom).getroot()
    coordinate = (pom_text(root, "groupId"), pom_text(root, "artifactId"), pom_text(root, "version"))
    if coordinate not in seen:
        seen.add(coordinate)
        print(":".join(coordinate))
PY
)

{
  echo "## Packages"
  echo
  echo '```kotlin'
  for coordinate in "${coordinates[@]}"; do
    echo "implementation(\"$coordinate\")"
  done
  echo '```'
  echo
  echo "Published modules:"
  echo
  for coordinate in "${coordinates[@]}"; do
    echo "- \`$coordinate\`"
  done
  echo

  if [[ -n "$previous_tag" ]]; then
    echo "## Changes Since $previous_tag"
    echo
    git log --pretty=format:'- %s (%h)' "$previous_tag..HEAD"
  else
    echo "## Initial Release"
    echo
    git log --pretty=format:'- %s (%h)'
  fi
  echo
} > "$notes_file"
