#!/usr/bin/env bash
set -euo pipefail

local_maven="${1:-build/local-maven}"
asset_dir="${2:-build/release-assets}"
version="${MOBILE_FOUNDATION_VERSION:?MOBILE_FOUNDATION_VERSION is required}"

rm -rf "$asset_dir"
mkdir -p "$asset_dir"

python3 - "$local_maven" "$asset_dir" "$version" <<'PY'
from pathlib import Path
import shutil
import sys
import xml.etree.ElementTree as ET

local_maven = Path(sys.argv[1])
asset_dir = Path(sys.argv[2])
expected_version = sys.argv[3]

if not local_maven.is_dir():
    raise SystemExit(f"Local Maven repository does not exist: {local_maven}")

def pom_text(root, name):
    namespace = {"m": root.tag.split("}")[0].strip("{")} if root.tag.startswith("{") else {}
    path = f"m:{name}" if namespace else name
    return root.findtext(path, namespaces=namespace) or ""

coordinates = []
for pom in sorted(local_maven.rglob("*.pom")):
    root = ET.parse(pom).getroot()
    group_id = pom_text(root, "groupId")
    artifact_id = pom_text(root, "artifactId")
    version = pom_text(root, "version")

    if version != expected_version:
        continue

    if not group_id or not artifact_id or not version:
        raise SystemExit(f"Invalid POM metadata: {pom}")

    coordinate = (group_id, artifact_id, version)
    if coordinate not in coordinates:
        coordinates.append(coordinate)

if not coordinates:
    raise SystemExit(f"No Maven POM files found under {local_maven}")

failures = []
copied = 0

for group_id, artifact_id, version in coordinates:
    module_dir = local_maven.joinpath(*group_id.split("."), artifact_id, version)
    required_files = [
        module_dir / f"{artifact_id}-{version}.aar",
        module_dir / f"{artifact_id}-{version}.pom",
        module_dir / f"{artifact_id}-{version}.module",
    ]

    missing = [path for path in required_files if not path.is_file()]
    if missing:
        failures.extend(str(path) for path in missing)
        continue

    for source in required_files:
        target = asset_dir / source.name
        if target.exists():
            raise SystemExit(f"Release asset name collision: {target.name}")
        shutil.copy2(source, target)
        copied += 1

if failures:
    print("Missing expected release assets:")
    for failure in failures:
        print(f" - {failure}")
    raise SystemExit(1)

print(f"Staged {copied} release assets for {len(coordinates)} Maven modules in {asset_dir}.")
PY
