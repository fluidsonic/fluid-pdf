import io.fluidsonic.pdf.*
import java.nio.file.*
import java.time.*
import kotlin.io.encoding.*
import kotlin.io.path.*
import kotlinx.coroutines.*


suspend fun main() = withContext(Dispatchers.Default) {
	// TODO Change the binary file path to your local Chromium or Google Chrome installation.

	val destinationFile = Path.of("./data/example.pdf").toAbsolutePath()

	withContext(Dispatchers.IO) {
		destinationFile.deleteIfExists()
	}

	val service = PdfGeneratorService.chromiumLauncher(
		path = Path.of("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome")
	)
	service.startIn(this)

	try {
		service.generate(
			source = PdfGenerationSource.Html(html),
			settings = PdfGenerationSettings.default.copy(
				encryption = PdfEncryption(
					ownerPassword = "secret",
					permissions = PdfPermissions.none.copy(
						contentExtractionAllowed = true,
						contentExtractionForAccessibilityAllowed = true,
						printQuality = PdfPermissions.PrintQuality.high,
					),
				),
				pageMargins = PdfMargins.cm(top = 2.0, right = 2.0, bottom = 1.0, left = 2.0),
				metadata = PdfMetadata(
					author = "Marc Knaup",
					creationDate = Instant.parse("2021-01-01T00:00:00Z"),
					creator = "Marc Knaup",
					documentId = PdfDocumentId("example"),
					keywords = "pdf,pdf-generation,example",
					modificationDate = Instant.parse("2021-01-01T00:00:00Z"),
					producer = "fluid-pdf",
					subject = "Example for PDF generation",
					title = "fluid-pdf Example",
				)
			)
		).writeTo(destinationFile)
	}
	finally {
		service.stop()
	}

	println()
	println("PDF has been generated at $destinationFile")
}


private val squirrel = Path("./data/squirrel.svg")
	.readBytes()
	.let(Base64::encode)
	.let { "data:image/svg+xml;base64,$it" }

private val html = Path("./data/example.html")
	.readText()
	.replace("squirrel.svg", squirrel)
