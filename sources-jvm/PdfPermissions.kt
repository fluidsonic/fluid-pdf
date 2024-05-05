package io.fluidsonic.pdf


/** Permissions that apply to the user if the PDF is encrypted. */
public data class PdfPermissions(
	/** Allows modifying annotations, filling in form fields, and (if [contentModificationAllowed] is also `true`) modifying form fields.
	 *
	 * Permission bit 6 will be set.
	 *
	 * @see contentModificationAllowed
	 * @see formFieldFillingAllowed
	 */
	val annotationAndFormFieldModificationAllowed: Boolean,

	/** Allows assembling the document (insert, rotate, or delete pages and create bookmarks or thumbnail images).
	 *
	 * Permission bit 11 will be set.
	 *
	 * @see annotationAndFormFieldModificationAllowed
	 */
	val assemblyAllowed: Boolean,

	/** Allows extracting content for anything except accessibility purposes.
	 *
	 * Permission bit 5 will be set.
	 *
	 * @see contentExtractionForAccessibilityAllowed
	 */
	val contentExtractionAllowed: Boolean,

	/** Allows extracting content for accessibility purposes.
	 *
	 * Permission bit 10 will be set.
	 *
	 * @see contentExtractionAllowed
	 */
	val contentExtractionForAccessibilityAllowed: Boolean,

	/** Allows modifying content except modifying annotations, modifying form fields, and assembling the document (insert, rotate, or delete pages and create
	 * bookmarks or thumbnail images).
	 *
	 * Permission bit 4 will be set.
	 *
	 * @see annotationAndFormFieldModificationAllowed
	 * @see assemblyAllowed
	 * @see formFieldFillingAllowed
	 */
	val contentModificationAllowed: Boolean,

	/** Allows filling in form fields, even if [annotationAndFormFieldModificationAllowed] is `false`.
	 *
	 * Permission bit 9 will be set.
	 *
	 * @see annotationAndFormFieldModificationAllowed
	 */
	val formFieldFillingAllowed: Boolean,

	/** Allows printing the document.
	 *
	 * Either permission bit 3 and/or 12 will be set if the value isn't [none][PrintQuality.none].
	 *
	 * @see PrintQuality
	 */
	val printQuality: PrintQuality,
) {

	public enum class PrintQuality {
		/** Allows printing in high quality.
		 *
		 * Permission bits 3 and 12 will be set.
		 */
		high,

		/** Allows printing in low quality.
		 *
		 * Permission bit 3 will be set.
		 */
		low,

		/** Doesn't allow printing.
		 *
		 * Permission bits 3 and 12 won't be set.
		 */
		none,
	}


	public companion object {

		/** All configurable permissions are given. */
		public val all: PdfPermissions = PdfPermissions(
			annotationAndFormFieldModificationAllowed = true,
			assemblyAllowed = true,
			contentExtractionAllowed = true,
			contentExtractionForAccessibilityAllowed = true,
			contentModificationAllowed = true,
			formFieldFillingAllowed = true,
			printQuality = PrintQuality.high,
		)

		/** No configurable permissions are given. */
		public val none: PdfPermissions = PdfPermissions(
			annotationAndFormFieldModificationAllowed = false,
			assemblyAllowed = false,
			contentExtractionAllowed = false,
			contentExtractionForAccessibilityAllowed = false,
			contentModificationAllowed = false,
			formFieldFillingAllowed = false,
			printQuality = PrintQuality.none,
		)
	}
}
