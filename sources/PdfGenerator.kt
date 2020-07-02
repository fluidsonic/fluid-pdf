package io.fluidsonic.pdf


interface PdfGenerator {

	suspend fun generate(
		source: PdfGenerationSource,
		settings: PdfGenerationSettings = PdfGenerationSettings.default
	): PdfGenerationOutput
}
