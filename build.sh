#!/usr/bin/env bash
set -e

echo "Building Frontend..."
cd csrm-frontend
npm install
npm run build

echo "Copying Frontend to Backend static folder..."
cd ../csrm-backend
mkdir -p src/main/resources/static
cp -r ../csrm-frontend/dist/* src/main/resources/static/

echo "Building Backend..."
chmod +x ./mvnw
./mvnw clean package -DskipTests
echo "Build complete!"
