# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/).


## [0.32.1] - 2026-05-18

### Added

- Debug logging at each CDP step in `DevToolsPdfGenerator`.


## [0.32.0] - 2026-03-28

### Added

- Unit tests for all pure-logic code.
- GitHub Actions CI workflow.
- KDoc for all public API.
- CHANGELOG.md.
- CLAUDE.md.

### Changed

- Migrated to fluid-gradle 3.0.0 (Kotlin 2.3.20, Gradle 9.4.1, JDK 21+).
- Updated Ktor from 3.3.1 to 3.4.1.
- Updated kotlinx-serialization from 1.9.0 to 1.10.0.
- Updated PDFBox from 3.0.6 to 3.0.7.
- Replaced `error()` with `check()` for state violation assertions in `AbstractPdfGeneratorService`.
- Renamed default branch from `master` to `main`.
