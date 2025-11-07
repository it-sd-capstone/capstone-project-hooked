#!/usr/bin/env bash
set -euo pipefail

LABEL="hooked-$(date +%Y%m%d-%H%M%S)"

echo "==> Cleaning & building (tests skipped) ..."
./mvnw clean package -DskipTests

if [ ! -f ./target/app.jar ]; then
  echo "Build failed: ./target/app.jar not found." >&2
  exit 1
fi

echo "==> Preparing files ..."
cp -f ./target/app.jar ./app.jar

# Ensure Procfile exists with correct content
if [ ! -f ./Procfile ] || ! grep -q "web: java -jar app.jar" ./Procfile ; then
  printf 'web: java -jar app.jar' > Procfile
fi

echo "==> Creating bundle.zip ..."
rm -f bundle.zip

# Prefer 'zip' if available; otherwise fall back to PowerShell's Compress-Archive on Windows Git Bash
if command -v zip >/dev/null 2>&1; then
  zip -q bundle.zip app.jar Procfile
else
  powershell.exe -NoProfile -Command \
    "Compress-Archive -Force -DestinationPath .\bundle.zip -Path .\app.jar, .\Procfile"
fi

echo "==> Done."
echo "Upload bundle.zip in Elastic Beanstalk → 'Upload and deploy'"
echo "Suggested Version label: $LABEL"
