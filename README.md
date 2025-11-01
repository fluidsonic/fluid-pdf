fluid-pdf
=========

[![Maven Central](https://img.shields.io/maven-central/v/io.fluidsonic.pdf/fluid-pdf?label=Maven%20Central)](https://search.maven.org/artifact/io.fluidsonic.pdf/fluid-pdf)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.20.0-blue.svg)](https://github.com/JetBrains/kotlin/releases/v1.8.22)
[![#fluid-libraries Slack Channel](https://img.shields.io/badge/slack-%23fluid--libraries-543951.svg)](https://kotlinlang.slack.com/messages/C7UDFSVT2/)

Easy PDF generation with HTML & CSS using Chromium or Google Chrome



Installation
------------

`build.gradle.kts`:

```kotlin
dependencies {
	implementation("io.fluidsonic.pdf:fluid-pdf:0.30.0")
}
```

Usage
-----

## HTML file to PDF file

```kotlin
import io.fluidsonic.pdf.*
import java.nio.file.*


suspend fun main() {
	// TODO Change the binary file path to your local Chromium or Google Chrome installation.

	val sourceFile = Path.of("input.html").toAbsolutePath()
	val destinationFile = Path.of("output.pdf").toAbsolutePath()

	coroutineScope {
		val service = PdfGeneratorService.chromiumLauncher(
			path = Path.of("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome")
		)
		service.startIn(this)

		try {
			service.generate(PdfGenerationSource.HtmlFile(sourceFile))
				.writeTo(destinationFile)
		}
		finally {
			service.stop()
		}
	}

	println("PDF has been generated at $destinationFile")
}
```

- Use `PdfGeneratorService.chromiumLauncher()` to launch a browser instance.
- Use `PdfGeneratorService.devTools()` to connect to an existing or a remote browser instance.
- Use `service.startIn(CoroutineScope)` and `service.stop()` to manage the service lifecycle.
- Use `PdfGeneratorService.generate()` to create any number of PDFs.
- Use `PdfGenerator` interface to hide service details, e.g. in dependency injection.

## HTML string to PDF file

```kotlin
service.generate(PdfGenerationSource.Html("<strong>Hello world!</strong>"))
	.writeTo(PdfGenerationDestination.File(destinationFile))
```

💡 Relative paths in HTML & CSS won't resolve. Using `<base href="…">` to specify the base path should help.

**Paths in HTML & CSS only work with a Chromium instance on the same machine.**

## HTML stream to PDF file

```kotlin
val sourceStream: InputStream = …

generator.generate(PdfGenerationSource.HtmlStream(sourceStream))
	.writeTo(PdfGenerationDestination.File(destinationFile))
```

💡 Relative paths in HTML & CSS won't resolve. Using `<base href="…">` to specify the base path should help.

**Paths in HTML & CSS only work with a Chromium instance on the same machine.**

## PDF generation settings

```kotlin
service.generate(
	source = PdfGenerationSource.Html("<strong>Hello world!</strong>"),
	settings = PdfGenerationSettings.default.copy(
		encryption = PdfEncryption(
			ownerPassword = "secret",
			permissions = PdfPermissions.none.copy(
				contentExtractionAllowed = true,
				contentExtractionForAccessibilityAllowed = true,
				printQuality = PdfPermissions.PrintQuality.high,
			),
		),
		includeBackgrounds = false,
		metadata = PdfMetadata(
			title = "My PDF"
		),
		pageMargins = PdfMargins.cm(top = 2.0, right = 2.0, bottom = 1.0, left = 2.0),
		pageOrientation = PdfOrientation.landscape,
		pageSize = PdfSize.A5,
		preferCssPageSize = false
	)
)
	.writeTo(destinationFile)
```

## Output to stream

```kotlin
service.generate(PdfGenerationSource.Html("<strong>Hello world!</strong>"))
	.writeTo(outputStream)
```

💡 Closing the output stream is the responsibility of the caller. It will not be closed automatically.



TO-DO
-----

Contributions welcome 🙏

- Add unit tests.
- Add KDoc to all public API.
- Write Chromium launcher & DevTools WebSocket client with Kotlin & Ktor, then remove dependency on `com.github.kklisura.cdt`.
- Test if `.generate()` works well with significant parallelism.

License
-------

Apache 2.0
