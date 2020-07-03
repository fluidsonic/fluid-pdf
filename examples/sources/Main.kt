import io.fluidsonic.pdf.*
import java.nio.file.*


suspend fun main() {
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
				pageMargins = PdfMargins.cm(top = 2.0, right = 2.0, bottom = 1.0, left = 2.0)
			)
		).writeTo(destinationFile)
	}

	println()
	println("PDF has been generated at $destinationFile")
}
