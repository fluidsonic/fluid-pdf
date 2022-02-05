package io.fluidsonic.pdf


@JvmInline
public value class PdfLengthUnit private constructor(private val _mm: Double) {

	public val cm: Double
		get() = _mm / mmPerCm


	public val mm: Double
		get() = _mm


	public val inch: Double
		get() = _mm * inchPerMm


	override fun toString(): String =
		"$_mm mm"


	public companion object {

		private const val inchPerMm = 0.03937
		private const val mmPerCm = 10.0


		public fun cm(value: Double): PdfLengthUnit =
			mm(value * mmPerCm)


		public fun mm(value: Double): PdfLengthUnit =
			PdfLengthUnit(value)


		public fun inch(value: Double): PdfLengthUnit =
			mm(value / inchPerMm)
	}
}
