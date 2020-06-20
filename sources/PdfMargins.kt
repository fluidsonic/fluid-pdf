package io.fluidsonic.pdf


data class PdfMargins(
	val top: PdfLengthUnit,
	val right: PdfLengthUnit,
	val bottom: PdfLengthUnit,
	val left: PdfLengthUnit
) {

	constructor(all: PdfLengthUnit) :
		this(top = all, right = all, bottom = all, left = all)


	constructor(horizontal: PdfLengthUnit, vertical: PdfLengthUnit) :
		this(top = vertical, right = horizontal, bottom = vertical, left = horizontal)


	companion object {

		fun cm(all: Double): PdfMargins =
			cm(top = all, right = all, bottom = all, left = all)


		fun cm(vertical: Double, horizontal: Double): PdfMargins =
			cm(top = vertical, right = horizontal, bottom = vertical, left = horizontal)


		fun cm(top: Double, right: Double, bottom: Double, left: Double): PdfMargins =
			PdfMargins(
				top = PdfLengthUnit.cm(top),
				right = PdfLengthUnit.cm(right),
				bottom = PdfLengthUnit.cm(bottom),
				left = PdfLengthUnit.cm(left)
			)


		fun inch(all: Double): PdfMargins =
			inch(top = all, right = all, bottom = all, left = all)


		fun inch(vertical: Double, horizontal: Double): PdfMargins =
			inch(top = vertical, right = horizontal, bottom = vertical, left = horizontal)


		fun inch(top: Double, right: Double, bottom: Double, left: Double): PdfMargins =
			PdfMargins(
				top = PdfLengthUnit.inch(top),
				right = PdfLengthUnit.inch(right),
				bottom = PdfLengthUnit.inch(bottom),
				left = PdfLengthUnit.inch(left)
			)


		fun mm(all: Double): PdfMargins =
			mm(top = all, right = all, bottom = all, left = all)


		fun mm(vertical: Double, horizontal: Double): PdfMargins =
			mm(top = vertical, right = horizontal, bottom = vertical, left = horizontal)


		fun mm(top: Double, right: Double, bottom: Double, left: Double): PdfMargins =
			PdfMargins(
				top = PdfLengthUnit.mm(top),
				right = PdfLengthUnit.mm(right),
				bottom = PdfLengthUnit.mm(bottom),
				left = PdfLengthUnit.mm(left)
			)
	}
}
