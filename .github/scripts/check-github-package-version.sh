#!/usr/bin/env bash
set -euo pipefail

version="${LANTERN_VERSION:?LANTERN_VERSION is required}"
owner="${GITHUB_REPOSITORY_OWNER:?GITHUB_REPOSITORY_OWNER is required}"

owner_type="$(gh api "/users/$owner" --jq .type)"
if [[ "$owner_type" == "Organization" ]]; then
  package_scope="/orgs/$owner"
else
  package_scope="/users/$owner"
fi

duplicates=()

while IFS=':' read -r group_id artifact_id; do
  [[ -n "$group_id" && -n "$artifact_id" ]] || continue

  package_names=("$group_id.$artifact_id" "$artifact_id")
  for package_name in "${package_names[@]}"; do
    encoded_package_name="${package_name//./%2E}"
    endpoint="$package_scope/packages/maven/$encoded_package_name/versions?per_page=100"

    if versions="$(gh api "$endpoint" --jq '.[].name' 2>/dev/null)"; then
      if grep -Fxq "$version" <<< "$versions"; then
        duplicates+=("$package_name:$version")
      fi
      break
    fi
  done
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
    coordinate = (pom_text(root, "groupId"), pom_text(root, "artifactId"))
    if coordinate not in seen:
        seen.add(coordinate)
        print(f"{coordinate[0]}:{coordinate[1]}")
PY
)

if (( ${#duplicates[@]} > 0 )); then
  echo "Package version already exists. Refusing to publish:"
  printf ' - %s\n' "${duplicates[@]}"
  exit 1
fi
