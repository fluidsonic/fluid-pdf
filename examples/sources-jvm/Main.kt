import io.fluidsonic.pdf.*
import java.nio.file.*
import java.time.*
import kotlinx.coroutines.*


suspend fun main() = withContext(Dispatchers.Default) {
	// TODO Change the binary file path to your local Chromium or Google Chrome installation.

	val sourceFile = Path.of("examples/data/example.html").toAbsolutePath()
	val destinationFile = Path.of("examples/data/example.pdf").toAbsolutePath()

	Files.deleteIfExists(destinationFile)

	ChromiumPdfGenerator.launch(
		binaryFile = Path.of("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome")
	).use { generator ->
		generator.generate(
			source = PdfGenerationSource.HtmlFile(sourceFile),
			settings = PdfGenerationSettings.default.copy(
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

	println()
	println("PDF has been generated at $destinationFile")
}
