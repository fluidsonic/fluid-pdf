fluid-pdf
=========

[![Maven Central](https://img.shields.io/maven-central/v/io.fluidsonic.pdf/fluid-pdf?label=Maven%20Central)](https://search.maven.org/artifact/io.fluidsonic.pdf/fluid-pdf)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.6.10-blue.svg)](https://github.com/JetBrains/kotlin/releases/v1.6.10)
[![#fluid-libraries Slack Channel](https://img.shields.io/badge/slack-%23fluid--libraries-543951.svg)](https://kotlinlang.slack.com/messages/C7UDFSVT2/)

Easy PDF generation with HTML & CSS using Chromium or Google Chrome



Installation
------------

`build.gradle.kts`:

```kotlin
dependencies {
	implementation("io.fluidsonic.pdf:fluid-pdf:0.14.5")
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

	ChromiumPdfGenerator.launch(
		binaryFile = Path.of("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome")
	).use { generator ->
		generator.generate(PdfGenerationSource.HtmlFile(sourceFile))
			.writeTo(destinationFile)
	}

	println("PDF has been generated at $destinationFile")
}
```

- Use `ChromiumPdfGenerator.launch()` to launch a browser instance for PDF generation.
- Use `ChromiumPdfGenerator.lazy()` to launch the browser not immediately but automatically with the first PDF generation.
- Use `ChromiumPdfGenerator`'s `.close()` to shut down the browser when you are done generating PDFs.
- Use `PdfGenerator`'s `.generate()` to create any number of PDFs.
- Use `PdfGenerator` interface to hide implementation details (use of Chromium, `.close()`) as needed.

## HTML string to PDF file

```kotlin
generator.generate(PdfGenerationSource.Html("<strong>Hello world!</strong>"))
	.writeTo(PdfGenerationDestination.File(destinationFile))
```

💡 Relative paths in HTML & CSS won't resolve. Using `<base href="…">` to specify the base path should help.

## HTML stream to PDF file

```kotlin
val sourceStream: InputStream = …

generator.generate(PdfGenerationSource.HtmlStream(sourceStream))
	.writeTo(PdfGenerationDestination.File(destinationFile))
```

💡 Relative paths in HTML & CSS won't resolve. Using `<base href="…">` to specify the base path should help.

## PDF generation settings

```kotlin
generator.generate(
	source = PdfGenerationSource.Html("<strong>Hello world!</strong>"),
	settings = PdfGenerationSettings.default.copy(
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
generator.generate(PdfGenerationSource.Html("<strong>Hello world!</strong>"))
	.writeTo(outputStream)
```

💡 Closing the output stream is the responsibility of the caller. It will not be closed automatically.



TO-DO
-----
Contributions welcome 🙏

- Add unit tests.
- Add KDoc to all public API.
- Check if `.generate()` works well if used from multiple threads and document if that is the case.
- Add support for header & footer templates.

License
-------

Apache 2.0
