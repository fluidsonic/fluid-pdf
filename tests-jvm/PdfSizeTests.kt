package tests

import io.fluidsonic.pdf.*
import kotlin.test.*


class PdfSizeTests {

	@Test
	fun a4Dimensions() {
		assertEquals(210.0, PdfSize.A4.width.mm)
		assertEquals(297.0, PdfSize.A4.height.mm)
	}

	@Test
	fun a3Dimensions() {
		assertEquals(297.0, PdfSize.A3.width.mm)
		assertEquals(420.0, PdfSize.A3.height.mm)
	}

	@Test
	fun successiveSizesFollowIsoPattern() {
		val sizes = listOf(
			PdfSize.A0, PdfSize.A1, PdfSize.A2, PdfSize.A3, PdfSize.A4,
			PdfSize.A5, PdfSize.A6, PdfSize.A7, PdfSize.A8, PdfSize.A9, PdfSize.A10,
		)
		for (i in 0 until sizes.size - 1) {
			val current = sizes[i]
			val next = sizes[i + 1]
			// ISO 216: next size width equals current size height halved (rounded to nearest mm)
			assertEquals(
				current.width.mm, next.height.mm,
				"A${i + 1} height should equal A$i width"
			)
		}
	}

	@Test
	fun cmFactory() {
		val size = PdfSize.cm(21.0, 29.7)
		assertEquals(210.0, size.width.mm)
		assertEquals(297.0, size.height.mm)
	}

	@Test
	fun mmFactory() {
		val size = PdfSize.mm(100.0, 200.0)
		assertEquals(100.0, size.width.mm)
		assertEquals(200.0, size.height.mm)
	}

	@Test
	fun inchFactory() {
		val size = PdfSize.inch(1.0, 2.0)
		assertEquals(25.4, size.width.mm, 0.01)
		assertEquals(50.8, size.height.mm, 0.01)
	}

	@Test
	fun dataClassEquality() {
		val a = PdfSize.mm(210.0, 297.0)
		val b = PdfSize.mm(210.0, 297.0)
		assertEquals(a, b)
	}
}
