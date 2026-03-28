# fluid-pdf

JVM-only Kotlin library for PDF generation using Chrome DevTools Protocol.

## Build

```sh
./gradlew build    # full build
./gradlew jvmTest  # run tests
```

## Conventions

- Tab indentation in Kotlin and Gradle files.
- Release tags have no `v` prefix (e.g. `0.31.0`).
- Source directory: `sources-jvm/`, test directory: `tests-jvm/`.
- Test package: `tests`.
- All public API must have KDoc.
- Use `check()` for state violations, `require()` for input validation.
