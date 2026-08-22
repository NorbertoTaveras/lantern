#!/usr/bin/env bash
set -euo pipefail

repository="${GITHUB_PACKAGES_REPOSITORY:?GITHUB_PACKAGES_REPOSITORY is required}"
owner="${repository%%/*}"
target_version="${SNAPSHOT_VERSION:-}"
dry_run="${DRY_RUN:-true}"

if [[ -n "$target_version" && ! "$target_version" =~ ^[0-9A-Za-z._-]+-SNAPSHOT$ ]]; then
  echo "SNAPSHOT_VERSION must be empty or a snapshot version ending in -SNAPSHOT."
  exit 1
fi

if [[ "$dry_run" != "true" && "$dry_run" != "false" ]]; then
  echo "DRY_RUN must be true or false."
  exit 1
fi

owner_type="$(gh api "/users/$owner" --jq .type)"
if [[ "$owner_type" == "Organization" ]]; then
  package_scope="/orgs/$owner"
else
  package_scope="/users/$owner"
fi

artifacts=(
  "lantern-core"
  "lantern-logging"
  "lantern-auth-core"
  "lantern-auth-firebase"
  "lantern-auth-google"
  "lantern-auth-firebase-google"
  "lantern-permissions"
  "lantern-secure-storage"
  "lantern-network-okhttp"
  "lantern-remote-config"
  "lantern-remote-config-firebase"
  "lantern-feature-flags"
  "lantern-notifications"
  "lantern-notifications-firebase"
  "lantern-media-picker"
  "lantern-analytics"
  "lantern-analytics-firebase"
  "lantern-deep-links"
  "lantern-background-work"
  "lantern-app-versioning"
)

deleted=0
matched=0
missing_packages=()

echo "Cleanup target owner: $owner"
echo "Cleanup mode: $([[ -n "$target_version" ]] && echo "$target_version" || echo "all -SNAPSHOT versions")"
echo "Dry run: $dry_run"

for artifact in "${artifacts[@]}"; do
  package_names=(
    "com.norbertotaveras.lantern.$artifact"
    "$artifact"
  )

  package_found="false"

  for package_name in "${package_names[@]}"; do
    encoded_package_name="${package_name//./%2E}"
    endpoint="$package_scope/packages/maven/$encoded_package_name/versions?per_page=100"

    if [[ -n "$target_version" ]]; then
      jq_filter=".[] | select(.name == \"$target_version\") | [.id, .name] | @tsv"
    else
      jq_filter='.[] | select(.name | endswith("-SNAPSHOT")) | [.id, .name] | @tsv'
    fi

    if versions="$(gh api "$endpoint" --paginate --jq "$jq_filter" 2>/dev/null)"; then
      package_found="true"
      if [[ -z "$versions" ]]; then
        break
      fi

      while IFS=$'\t' read -r version_id version_name; do
        [[ -n "$version_id" && -n "$version_name" ]] || continue
        matched=$((matched + 1))

        if [[ "$dry_run" == "true" ]]; then
          echo "Would delete $package_name:$version_name (id $version_id)"
        else
          echo "Deleting $package_name:$version_name (id $version_id)"
          gh api \
            --method DELETE \
            "$package_scope/packages/maven/$encoded_package_name/versions/$version_id" >/dev/null
          deleted=$((deleted + 1))
        fi
      done <<< "$versions"

      break
    fi
  done

  if [[ "$package_found" != "true" ]]; then
    missing_packages+=("$artifact")
  fi
done

{
  echo "## Snapshot cleanup"
  echo
  echo "- Repository: $repository"
  echo "- Owner scope: $package_scope"
  echo "- Mode: $([[ -n "$target_version" ]] && echo "$target_version" || echo "all -SNAPSHOT versions")"
  echo "- Dry run: $dry_run"
  echo "- Matched versions: $matched"
  echo "- Deleted versions: $deleted"
  if (( ${#missing_packages[@]} > 0 )); then
    echo "- Packages not found:"
    printf '  - %s\n' "${missing_packages[@]}"
  fi
} >> "${GITHUB_STEP_SUMMARY:-/dev/stdout}"
