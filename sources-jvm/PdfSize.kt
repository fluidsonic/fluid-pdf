package io.fluidsonic.pdf


/**
 * The page size of a PDF document.
 *
 * @property width The page width.
 * @property height The page height.
 */
public data class PdfSize(
	val width: PdfLengthUnit,
	val height: PdfLengthUnit
) {

	/** Predefined ISO 216 page sizes and factory methods for creating custom sizes. */
	public companion object {

		/** ISO 216 A0 size (841 x 1189 mm). */
		public val A0: PdfSize = mm(width = 841.0, height = 1189.0)

		/** ISO 216 A1 size (594 x 841 mm). */
		public val A1: PdfSize = mm(width = 594.0, height = 841.0)

		/** ISO 216 A2 size (420 x 594 mm). */
		public val A2: PdfSize = mm(width = 420.0, height = 594.0)

		/** ISO 216 A3 size (297 x 420 mm). */
		public val A3: PdfSize = mm(width = 297.0, height = 420.0)

		/** ISO 216 A4 size (210 x 297 mm). */
		public val A4: PdfSize = mm(width = 210.0, height = 297.0)

		/** ISO 216 A5 size (148 x 210 mm). */
		public val A5: PdfSize = mm(width = 148.0, height = 210.0)

		/** ISO 216 A6 size (105 x 148 mm). */
		public val A6: PdfSize = mm(width = 105.0, height = 148.0)

		/** ISO 216 A7 size (74 x 105 mm). */
		public val A7: PdfSize = mm(width = 74.0, height = 105.0)

		/** ISO 216 A8 size (52 x 74 mm). */
		public val A8: PdfSize = mm(width = 52.0, height = 74.0)

		/** ISO 216 A9 size (37 x 52 mm). */
		public val A9: PdfSize = mm(width = 37.0, height = 52.0)

		/** ISO 216 A10 size (26 x 37 mm). */
		public val A10: PdfSize = mm(width = 26.0, height = 37.0)


		/** Creates a page size with dimensions specified in centimeters. */
		public fun cm(width: Double, height: Double): PdfSize =
			PdfSize(width = PdfLengthUnit.cm(width), height = PdfLengthUnit.cm(height))


		/** Creates a page size with dimensions specified in inches. */
		public fun inch(width: Double, height: Double): PdfSize =
			PdfSize(width = PdfLengthUnit.inch(width), height = PdfLengthUnit.inch(height))


		/** Creates a page size with dimensions specified in millimeters. */
		public fun mm(width: Double, height: Double): PdfSize =
			PdfSize(width = PdfLengthUnit.mm(width), height = PdfLengthUnit.mm(height))
	}
}
