package io.fluidsonic.pdf


public interface PdfGenerator {

	public suspend fun generate(input: PdfGenerationInput): PdfGenerationOutput
}


public suspend fun PdfGenerator.generate(
	source: PdfGenerationSource,
	settings: PdfGenerationSettings = PdfGenerationSettings.default,
): PdfGenerationOutput =
	generate(PdfGenerationInput(settings = settings, source = source))
