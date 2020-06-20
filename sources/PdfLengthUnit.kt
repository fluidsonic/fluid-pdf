package io.fluidsonic.pdf


@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
inline class PdfLengthUnit @PublishedApi internal constructor(val _mm: Double) {

	val cm: Double
		get() = _mm / mmPerCm


	val mm: Double
		get() = _mm


	val inch: Double
		get() = _mm * inchPerMm


	override fun toString(): String =
		"$_mm mm"


	companion object {

		private const val inchPerMm = 0.03937
		private const val mmPerCm = 10.0


		fun cm(value: Double): PdfLengthUnit =
			mm(value * mmPerCm)


		fun mm(value: Double): PdfLengthUnit =
			PdfLengthUnit(value)


		fun inch(value: Double): PdfLengthUnit =
			mm(value / inchPerMm)
	}
}
