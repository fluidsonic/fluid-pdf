package io.fluidsonic.pdf


public interface PdfGenerator {

	public suspend fun generate(
		source: PdfGenerationSource,
		settings: PdfGenerationSettings = PdfGenerationSettings.default
	): PdfGenerationOutput
}
