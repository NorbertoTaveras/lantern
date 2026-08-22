#!/usr/bin/env bash
set -euo pipefail

output_path="${1:-app/google-services.json}"

mkdir -p "$(dirname "$output_path")"

cat > "$output_path" <<'JSON'
{
  "project_info": {
    "project_number": "000000000000",
    "project_id": "lantern-ci",
    "storage_bucket": "lantern-ci.appspot.com"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "1:000000000000:android:0000000000000000000000",
        "android_client_info": {
          "package_name": "com.norbertotaveras.lanternsample"
        }
      },
      "oauth_client": [],
      "api_key": [
        {
          "current_key": "ci-placeholder"
        }
      ],
      "services": {
        "appinvite_service": {
          "other_platform_oauth_client": []
        }
      }
    }
  ],
  "configuration_version": "1"
}
JSON
