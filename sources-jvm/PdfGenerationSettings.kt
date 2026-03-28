package io.fluidsonic.pdf


/**
 * Settings for PDF generation.
 *
 * @property encryption Encryption settings, or `null` for no encryption.
 * @property footerHtml HTML template for the page footer, or `null` for no footer.
 * @property headerHtml HTML template for the page header, or `null` for no header.
 * @property includeBackgrounds Whether to include CSS backgrounds in the PDF.
 * @property metadata PDF document metadata, or `null` for no metadata.
 * @property pageMargins Margins for each page.
 * @property pageOrientation Orientation of each page.
 * @property pageSize Size of each page.
 * @property preferCssPageSize Whether to prefer page sizes defined in CSS over [pageSize].
 */
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

	/** Predefined [PdfGenerationSettings] instances. */
	public companion object {

		/** Default settings: portrait A4, 1 cm margins, backgrounds included, CSS page size preferred. */
		public val default: PdfGenerationSettings = PdfGenerationSettings(
			includeBackgrounds = true,
			pageMargins = PdfMargins.cm(all = 1.0),
			pageOrientation = PdfOrientation.portrait,
			pageSize = PdfSize.A4,
			preferCssPageSize = true
		)
	}
}
