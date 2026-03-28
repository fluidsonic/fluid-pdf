package io.fluidsonic.pdf


/** Generates PDF documents from HTML sources. */
public interface PdfGenerator {

	/** Generates a PDF document from the given [input]. */
	public suspend fun generate(input: PdfGenerationInput): PdfGenerationOutput
}


/** Generates a PDF document from the given [source] and optional [settings]. */
public suspend fun PdfGenerator.generate(
	source: PdfGenerationSource,
	settings: PdfGenerationSettings = PdfGenerationSettings.default,
): PdfGenerationOutput =
	generate(PdfGenerationInput(settings = settings, source = source))
