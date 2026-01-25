#!/bin/sh
set -e

echo "Downloading release JAR..."

# https://github.com/AlexeyZarechnev/system-analyses/releases/download/v0.0.1-SNAPSHOT/autoservice-0.0.1-SNAPSHOT.jar
link="https://github.com/${GITHUB_REPO}/releases/download/${APP_VERSION}/${JAR_NAME}"

echo "From: $link"

curl -L \
  -o app.jar \
  "$link"
echo "Starting application..."

exec java \
  -Dspring.datasource.url=jdbc:postgresql://db:5432/${POSTGRES_DB} \
  -Dspring.datasource.username=${POSTGRES_USER} \
  -Dspring.datasource.password=${POSTGRES_PASSWORD} \
  -jar app.jar
