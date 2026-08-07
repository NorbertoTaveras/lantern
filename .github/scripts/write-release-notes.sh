#!/usr/bin/env bash
set -euo pipefail

version="${MOBILE_FOUNDATION_VERSION:?MOBILE_FOUNDATION_VERSION is required}"
notes_file="${1:-build/release-notes.md}"
current_tag="v${version}"
previous_tag="$(git tag --list 'v[0-9]*' --sort=-v:refname | grep -v "^${current_tag}$" | head -n 1 || true)"
repository="${GITHUB_REPOSITORY:-NorbertoTaveras/android_mobilefoundation_framework}"
repository_url="https://github.com/${repository}"

if [[ -n "$previous_tag" ]]; then
  commit_range="${previous_tag}..HEAD"
  full_changelog_url="${repository_url}/compare/${previous_tag}...${current_tag}"
else
  commit_range="HEAD"
  full_changelog_url="${repository_url}/commits/${current_tag}"
fi

mkdir -p "$(dirname "$notes_file")"

coordinates=()
while IFS= read -r coordinate; do
  coordinates+=("$coordinate")
done < <(
python3 - "$version" <<'PY'
from pathlib import Path
import sys
import xml.etree.ElementTree as ET

local_maven = Path("build/local-maven")
expected_version = sys.argv[1]

def pom_text(root, name):
    namespace = {"m": root.tag.split("}")[0].strip("{")} if root.tag.startswith("{") else {}
    path = f"m:{name}" if namespace else name
    return root.findtext(path, namespaces=namespace) or ""

seen = set()
for pom in sorted(local_maven.rglob("*.pom")):
    root = ET.parse(pom).getroot()
    coordinate = (pom_text(root, "groupId"), pom_text(root, "artifactId"), pom_text(root, "version"))
    if coordinate[2] != expected_version:
        continue
    if coordinate not in seen:
        seen.add(coordinate)
        print(":".join(coordinate))
PY
)

commit_lines=()
while IFS= read -r line; do
  commit_lines+=("$line")
done < <(git log --format='%s%x09%an%x09%h' "$commit_range")

new_contributors=()
while IFS=$'\t' read -r author email; do
  [[ -n "$author" && -n "$email" ]] || continue

  if [[ -z "$previous_tag" ]] || ! git log --format='%ae' "${previous_tag}" | grep -Fxiq "$email"; then
    contributor="${author} <${email}>"
    existing_contributors=" ${new_contributors[*]-} "
    if [[ "$existing_contributors" != *" ${contributor} "* ]]; then
      new_contributors+=("$contributor")
    fi
  fi
done < <(git log --format='%an%x09%ae' "$commit_range")

{
  echo "## What's Changed"
  echo
  if (( ${#commit_lines[@]} > 0 )); then
    for line in "${commit_lines[@]}"; do
      IFS=$'\t' read -r subject author hash <<< "$line"
      echo "- ${subject} by ${author} in [${hash}](${repository_url}/commit/${hash})"
    done
  else
    echo "- No commit changes found."
  fi
  echo

  if (( ${#new_contributors[@]} > 0 )); then
    echo "## New Contributors"
    echo
    for contributor in "${new_contributors[@]}"; do
      echo "- ${contributor} made their first contribution in this release."
    done
    echo
  fi

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

  echo "**Full Changelog**: ${full_changelog_url}"
} > "$notes_file"
