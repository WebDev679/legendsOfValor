#!/bin/bash
set -e

echo "Compiling Legends Game..."

rm -rf out
mkdir -p out

javac --release 8 -d out $(find . -name "*.java")

echo "Build complete."