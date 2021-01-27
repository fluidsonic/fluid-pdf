package io.fluidsonic.pdf


public data class PdfGenerationInput(
	val source: PdfGenerationSource,
	val settings: PdfGenerationSettings = PdfGenerationSettings.default,
)
