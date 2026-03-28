package tests

import io.fluidsonic.pdf.*
import kotlin.test.*


class PdfMarginsTests {

	@Test
	fun singleValueConstructorSetsAllSidesEqual() {
		val unit = PdfLengthUnit.mm(5.0)
		val margins = PdfMargins(unit)
		assertEquals(unit, margins.top)
		assertEquals(unit, margins.right)
		assertEquals(unit, margins.bottom)
		assertEquals(unit, margins.left)
	}

	@Test
	fun twoValueConstructorSetsHorizontalAndVertical() {
		val horizontal = PdfLengthUnit.mm(10.0)
		val vertical = PdfLengthUnit.mm(20.0)
		val margins = PdfMargins(horizontal, vertical)
		assertEquals(vertical, margins.top)
		assertEquals(horizontal, margins.right)
		assertEquals(vertical, margins.bottom)
		assertEquals(horizontal, margins.left)
	}

	@Test
	fun fourValueConstructorSetsEachSideIndependently() {
		val top = PdfLengthUnit.mm(1.0)
		val right = PdfLengthUnit.mm(2.0)
		val bottom = PdfLengthUnit.mm(3.0)
		val left = PdfLengthUnit.mm(4.0)
		val margins = PdfMargins(top = top, right = right, bottom = bottom, left = left)
		assertEquals(top, margins.top)
		assertEquals(right, margins.right)
		assertEquals(bottom, margins.bottom)
		assertEquals(left, margins.left)
	}

	@Test
	fun cmAllFactory() {
		val margins = PdfMargins.cm(1.0)
		assertEquals(10.0, margins.top.mm)
		assertEquals(10.0, margins.right.mm)
		assertEquals(10.0, margins.bottom.mm)
		assertEquals(10.0, margins.left.mm)
	}

	@Test
	fun cmTwoArgFactory() {
		val margins = PdfMargins.cm(vertical = 1.0, horizontal = 2.0)
		assertEquals(10.0, margins.top.mm)
		assertEquals(20.0, margins.right.mm)
		assertEquals(10.0, margins.bottom.mm)
		assertEquals(20.0, margins.left.mm)
	}

	@Test
	fun cmFourArgFactory() {
		val margins = PdfMargins.cm(top = 1.0, right = 2.0, bottom = 3.0, left = 4.0)
		assertEquals(10.0, margins.top.mm)
		assertEquals(20.0, margins.right.mm)
		assertEquals(30.0, margins.bottom.mm)
		assertEquals(40.0, margins.left.mm)
	}

	@Test
	fun mmAllFactory() {
		val margins = PdfMargins.mm(5.0)
		assertEquals(5.0, margins.top.mm)
		assertEquals(5.0, margins.right.mm)
		assertEquals(5.0, margins.bottom.mm)
		assertEquals(5.0, margins.left.mm)
	}

	@Test
	fun inchAllFactory() {
		val margins = PdfMargins.inch(1.0)
		assertEquals(25.4, margins.top.mm, 0.01)
		assertEquals(25.4, margins.right.mm, 0.01)
		assertEquals(25.4, margins.bottom.mm, 0.01)
		assertEquals(25.4, margins.left.mm, 0.01)
	}

	@Test
	fun dataClassEqualityAndCopy() {
		val margins = PdfMargins.mm(top = 1.0, right = 2.0, bottom = 3.0, left = 4.0)
		val same = PdfMargins.mm(top = 1.0, right = 2.0, bottom = 3.0, left = 4.0)
		assertEquals(margins, same)

		val copied = margins.copy(top = PdfLengthUnit.mm(99.0))
		assertEquals(99.0, copied.top.mm)
		assertEquals(2.0, copied.right.mm)
	}
}
