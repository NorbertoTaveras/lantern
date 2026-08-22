#!/usr/bin/env bash
set -euo pipefail

version="${GITHUB_REF_NAME#release/}"

if [[ -z "$version" || "$version" == "$GITHUB_REF_NAME" ]]; then
  echo "Release workflows must run from release/{version} branches."
  exit 1
fi

if [[ "$version" == *SNAPSHOT* ]]; then
  echo "Release branch versions must not contain SNAPSHOT."
  exit 1
fi

if [[ ! "$version" =~ ^[0-9]+\.[0-9]+\.[0-9]+(-[0-9A-Za-z][0-9A-Za-z.-]*)?$ ]]; then
  echo "Release branch versions must use x.y.z or x.y.z-prerelease."
  exit 1
fi

if [[ -n "${LANTERN_VERSION:-}" && "$LANTERN_VERSION" != "$version" ]]; then
  echo "LANTERN_VERSION must match release branch version ${version}, but was ${LANTERN_VERSION}."
  exit 1
fi

tag="v${version}"

if git ls-remote --tags origin "refs/tags/${tag}" | grep -q .; then
  echo "Release tag ${tag} already exists."
  exit 1
fi

echo "LANTERN_VERSION=$version" >> "$GITHUB_ENV"
echo "lantern-version=$version" >> "$GITHUB_OUTPUT"
