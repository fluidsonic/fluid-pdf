package io.fluidsonic.pdf


/**
 * Input for PDF generation.
 *
 * @property source The HTML source to generate the PDF from.
 * @property settings The settings to apply during PDF generation.
 */
public data class PdfGenerationInput(
	val source: PdfGenerationSource,
	val settings: PdfGenerationSettings = PdfGenerationSettings.default,
)
