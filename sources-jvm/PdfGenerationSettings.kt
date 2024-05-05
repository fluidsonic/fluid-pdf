package io.fluidsonic.pdf


public data class PdfGenerationSettings(
	val encryption: PdfEncryption? = null,
	val footerHtml: String? = null,
	val headerHtml: String? = null,
	val includeBackgrounds: Boolean,
	val metadata: PdfMetadata? = null,
	val pageMargins: PdfMargins,
	val pageOrientation: PdfOrientation,
	val pageSize: PdfSize,
	val preferCssPageSize: Boolean,
) {

	public companion object {

		public val default: PdfGenerationSettings = PdfGenerationSettings(
			includeBackgrounds = true,
			pageMargins = PdfMargins.cm(all = 1.0),
			pageOrientation = PdfOrientation.portrait,
			pageSize = PdfSize.A4,
			preferCssPageSize = true
		)
	}
}
