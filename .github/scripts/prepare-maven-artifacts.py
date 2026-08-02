#!/usr/bin/env python3
import argparse
import json
import os
import subprocess
from pathlib import Path
import xml.etree.ElementTree as ET


def pom_text(root, name):
    namespace = {"m": root.tag.split("}")[0].strip("{")} if root.tag.startswith("{") else {}
    path = f"m:{name}" if namespace else name
    return root.findtext(path, namespaces=namespace) or ""


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--dry-run", action="store_true")
    args = parser.parse_args()

    version = os.environ["MOBILE_FOUNDATION_VERSION"]
    local_maven = Path("build/local-maven")
    provenance_file = Path(
        "build/release-dry-run-provenance.json" if args.dry_run else "build/release-provenance.json"
    )
    bundle_name = f"mobile-foundation-maven-{version}{'-dry-run' if args.dry_run else ''}.tgz"

    subprocess.run(
        "find . -type f ! -name SHA256SUMS -print0 | sort -z | xargs -0 shasum -a 256 > SHA256SUMS",
        cwd=local_maven,
        shell=True,
        check=True,
    )

    modules = []
    for pom in sorted(local_maven.rglob("*.pom")):
        root = ET.parse(pom).getroot()
        modules.append(
            {
                "groupId": pom_text(root, "groupId"),
                "artifactId": pom_text(root, "artifactId"),
                "version": pom_text(root, "version"),
                "pom": str(pom.relative_to(local_maven)),
            }
        )

    provenance = {
        "name": "Mobile Foundation SDK",
        "version": version,
        "tag": f"v{version}",
        "branch": os.environ["GITHUB_REF_NAME"],
        "commit": os.environ["GITHUB_SHA"],
        "repository": os.environ["GITHUB_REPOSITORY"],
        "runId": os.environ["GITHUB_RUN_ID"],
        "runAttempt": os.environ["GITHUB_RUN_ATTEMPT"],
        "mavenRepository": "build/local-maven",
        "checksumFile": "SHA256SUMS",
        "bundleFile": bundle_name,
        "artifactFileCount": sum(1 for path in local_maven.rglob("*") if path.is_file()),
        "modules": modules,
    }

    if args.dry_run:
        provenance["dryRun"] = True

    provenance_file.write_text(json.dumps(provenance, indent=2) + "\n", encoding="utf-8")
    subprocess.run(
        ["tar", "-czf", f"build/{bundle_name}", "-C", "build/local-maven", "."],
        check=True,
    )


if __name__ == "__main__":
    main()
