#!/usr/bin/env bash
set -euo pipefail

if [[ "${GITHUB_REF_NAME}" != "develop" ]]; then
  echo "Develop snapshots must be run from the develop branch."
  exit 1
fi

base_version="${MOBILE_FOUNDATION_BASE_VERSION:-0.1.0-SNAPSHOT}"
base_version="${base_version%-SNAPSHOT}"

if [[ ! "$base_version" =~ ^[0-9]+\.[0-9]+\.[0-9]+(-[0-9A-Za-z][0-9A-Za-z.-]*)?$ ]]; then
  echo "Snapshot base versions must use x.y.z-SNAPSHOT or x.y.z-prerelease-SNAPSHOT."
  exit 1
fi

snapshot_version="${base_version}-dev.${GITHUB_RUN_NUMBER}-SNAPSHOT"

echo "MOBILE_FOUNDATION_VERSION=$snapshot_version" >> "$GITHUB_ENV"
echo "mobile-foundation-version=$snapshot_version" >> "$GITHUB_OUTPUT"
