package io.fluidsonic.pdf


internal class ChromiumPdfGenerator(
	private val service: ChromiumPdfGenerationService,
) : PdfGenerator {

	override suspend fun generate(input: PdfGenerationInput): PdfGenerationOutput =
		service.generate(input)
}
