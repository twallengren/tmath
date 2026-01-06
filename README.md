# tmath

A comprehensive mathematics library built from scratch in Java.

## Overview

TMath is a collection of mathematical implementations including:
- Category Theory
- Group Theory
- ODE Solvers
- PDE Solvers
- Automatic Differentiation
- And more mathematical tools and algorithms

All implementations are built from scratch to provide deep understanding and control over the mathematical operations.

## Requirements

- Java 21 or higher

## Building

Build the project using Gradle:

```bash
./gradlew build
```

## Running

Run the application:

```bash
./gradlew run
```

## Other Gradle Tasks

```bash
# Clean build artifacts
./gradlew clean

# Run tests
./gradlew test

# Create a JAR file
./gradlew jar
```

## Project Structure

```
tmath/
├── src/
│   ├── main/
│   │   ├── java/io/github/torenwallengren/tmath/
│   │   │   └── TMath.java
│   │   └── resources/
│   └── test/
│       ├── java/io/github/torenwallengren/tmath/
│       │   └── TMathTest.java (placeholder)
│       └── resources/
└── build/            (generated)
    └── classes/      (compiled classes)
```

## License

See LICENSE file for details.