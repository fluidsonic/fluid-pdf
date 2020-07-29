package io.fluidsonic.pdf


public data class PdfMargins(
	val top: PdfLengthUnit,
	val right: PdfLengthUnit,
	val bottom: PdfLengthUnit,
	val left: PdfLengthUnit
) {

	public constructor(all: PdfLengthUnit) :
		this(top = all, right = all, bottom = all, left = all)


	public constructor(horizontal: PdfLengthUnit, vertical: PdfLengthUnit) :
		this(top = vertical, right = horizontal, bottom = vertical, left = horizontal)


	public companion object {

		public fun cm(all: Double): PdfMargins =
			cm(top = all, right = all, bottom = all, left = all)


		public fun cm(vertical: Double, horizontal: Double): PdfMargins =
			cm(top = vertical, right = horizontal, bottom = vertical, left = horizontal)


		public fun cm(top: Double, right: Double, bottom: Double, left: Double): PdfMargins =
			PdfMargins(
				top = PdfLengthUnit.cm(top),
				right = PdfLengthUnit.cm(right),
				bottom = PdfLengthUnit.cm(bottom),
				left = PdfLengthUnit.cm(left)
			)


		public fun inch(all: Double): PdfMargins =
			inch(top = all, right = all, bottom = all, left = all)


		public fun inch(vertical: Double, horizontal: Double): PdfMargins =
			inch(top = vertical, right = horizontal, bottom = vertical, left = horizontal)


		public fun inch(top: Double, right: Double, bottom: Double, left: Double): PdfMargins =
			PdfMargins(
				top = PdfLengthUnit.inch(top),
				right = PdfLengthUnit.inch(right),
				bottom = PdfLengthUnit.inch(bottom),
				left = PdfLengthUnit.inch(left)
			)


		public fun mm(all: Double): PdfMargins =
			mm(top = all, right = all, bottom = all, left = all)


		public fun mm(vertical: Double, horizontal: Double): PdfMargins =
			mm(top = vertical, right = horizontal, bottom = vertical, left = horizontal)


		public fun mm(top: Double, right: Double, bottom: Double, left: Double): PdfMargins =
			PdfMargins(
				top = PdfLengthUnit.mm(top),
				right = PdfLengthUnit.mm(right),
				bottom = PdfLengthUnit.mm(bottom),
				left = PdfLengthUnit.mm(left)
			)
	}
}
