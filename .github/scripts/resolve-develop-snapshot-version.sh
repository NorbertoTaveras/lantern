#!/usr/bin/env bash
set -euo pipefail

if [[ "${GITHUB_REF_NAME}" != "develop" ]]; then
  echo "Develop snapshots must be run from the develop branch."
  exit 1
fi

snapshot_version="${LANTERN_BASE_VERSION:-0.1.2-SNAPSHOT}"

if [[ ! "$snapshot_version" =~ ^[0-9]+\.[0-9]+\.[0-9]+(-[0-9A-Za-z][0-9A-Za-z.-]*)?-SNAPSHOT$ ]]; then
  echo "Snapshot base versions must use x.y.z-SNAPSHOT or x.y.z-prerelease-SNAPSHOT."
  exit 1
fi

echo "LANTERN_VERSION=$snapshot_version" >> "$GITHUB_ENV"
echo "lantern-version=$snapshot_version" >> "$GITHUB_OUTPUT"
