package io.fluidsonic.pdf


/**
 * Page margins for a PDF document.
 *
 * @property top The top margin.
 * @property right The right margin.
 * @property bottom The bottom margin.
 * @property left The left margin.
 */
public data class PdfMargins(
	val top: PdfLengthUnit,
	val right: PdfLengthUnit,
	val bottom: PdfLengthUnit,
	val left: PdfLengthUnit
) {

	/** Creates margins with equal size on all sides. */
	public constructor(all: PdfLengthUnit) :
		this(top = all, right = all, bottom = all, left = all)


	/** Creates margins with equal [horizontal] (left/right) and [vertical] (top/bottom) sizes. */
	public constructor(horizontal: PdfLengthUnit, vertical: PdfLengthUnit) :
		this(top = vertical, right = horizontal, bottom = vertical, left = horizontal)


	/** Factory methods for creating [PdfMargins] from centimeters, millimeters, or inches. */
	public companion object {

		/** Creates margins with equal size on all sides, specified in centimeters. */
		public fun cm(all: Double): PdfMargins =
			cm(top = all, right = all, bottom = all, left = all)


		/** Creates margins with equal horizontal and vertical sizes, specified in centimeters. */
		public fun cm(vertical: Double, horizontal: Double): PdfMargins =
			cm(top = vertical, right = horizontal, bottom = vertical, left = horizontal)


		/** Creates margins with individual sizes per side, specified in centimeters. */
		public fun cm(top: Double, right: Double, bottom: Double, left: Double): PdfMargins =
			PdfMargins(
				top = PdfLengthUnit.cm(top),
				right = PdfLengthUnit.cm(right),
				bottom = PdfLengthUnit.cm(bottom),
				left = PdfLengthUnit.cm(left)
			)


		/** Creates margins with equal size on all sides, specified in inches. */
		public fun inch(all: Double): PdfMargins =
			inch(top = all, right = all, bottom = all, left = all)


		/** Creates margins with equal horizontal and vertical sizes, specified in inches. */
		public fun inch(vertical: Double, horizontal: Double): PdfMargins =
			inch(top = vertical, right = horizontal, bottom = vertical, left = horizontal)


		/** Creates margins with individual sizes per side, specified in inches. */
		public fun inch(top: Double, right: Double, bottom: Double, left: Double): PdfMargins =
			PdfMargins(
				top = PdfLengthUnit.inch(top),
				right = PdfLengthUnit.inch(right),
				bottom = PdfLengthUnit.inch(bottom),
				left = PdfLengthUnit.inch(left)
			)


		/** Creates margins with equal size on all sides, specified in millimeters. */
		public fun mm(all: Double): PdfMargins =
			mm(top = all, right = all, bottom = all, left = all)


		/** Creates margins with equal horizontal and vertical sizes, specified in millimeters. */
		public fun mm(vertical: Double, horizontal: Double): PdfMargins =
			mm(top = vertical, right = horizontal, bottom = vertical, left = horizontal)


		/** Creates margins with individual sizes per side, specified in millimeters. */
		public fun mm(top: Double, right: Double, bottom: Double, left: Double): PdfMargins =
			PdfMargins(
				top = PdfLengthUnit.mm(top),
				right = PdfLengthUnit.mm(right),
				bottom = PdfLengthUnit.mm(bottom),
				left = PdfLengthUnit.mm(left)
			)
	}
}
