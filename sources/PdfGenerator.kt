package io.fluidsonic.pdf


interface PdfGenerator {

	suspend fun generate(
		source: PdfGenerationSource,
		destination: PdfGenerationDestination,
		settings: PdfGenerationSettings = PdfGenerationSettings.default
	)
}
