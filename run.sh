#!/bin/sh
# Compila e roda a Aventura Java (precisa do JDK 17 ou superior)
mkdir -p out && javac -encoding UTF-8 -d out src/*.java && java -cp out Main "$@"
