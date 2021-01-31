package io.fluidsonic.pdf


public data class PdfGenerationSettings(
	val footerHtml: String?,
	val headerHtml: String?,
	val includeBackgrounds: Boolean,
	val metadata: PdfMetadata?,
	val pageMargins: PdfMargins,
	val pageOrientation: PdfOrientation,
	val pageSize: PdfSize,
	val preferCssPageSize: Boolean,
) {

	public companion object {

		public val default: PdfGenerationSettings = PdfGenerationSettings(
			footerHtml = null,
			headerHtml = null,
			includeBackgrounds = true,
			metadata = null,
			pageMargins = PdfMargins.cm(all = 1.0),
			pageOrientation = PdfOrientation.portrait,
			pageSize = PdfSize.A4,
			preferCssPageSize = true
		)
	}
}
