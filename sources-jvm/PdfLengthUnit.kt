package io.fluidsonic.pdf


/** A length unit stored internally as millimeters, with conversion to centimeters and inches. */
@JvmInline
public value class PdfLengthUnit private constructor(private val _mm: Double) {

	/** The length in centimeters. */
	public val cm: Double
		get() = _mm / mmPerCm


	/** The length in millimeters. */
	public val mm: Double
		get() = _mm


	/** The length in inches. */
	public val inch: Double
		get() = _mm * inchPerMm


	override fun toString(): String =
		"$_mm mm"


	/** Factory methods for creating [PdfLengthUnit] instances from different units. */
	public companion object {

		private const val inchPerMm = 0.03937
		private const val mmPerCm = 10.0


		/** Creates a length from the given [value] in centimeters. */
		public fun cm(value: Double): PdfLengthUnit =
			mm(value * mmPerCm)


		/** Creates a length from the given [value] in millimeters. */
		public fun mm(value: Double): PdfLengthUnit =
			PdfLengthUnit(value)


		/** Creates a length from the given [value] in inches. */
		public fun inch(value: Double): PdfLengthUnit =
			mm(value / inchPerMm)
	}
}
