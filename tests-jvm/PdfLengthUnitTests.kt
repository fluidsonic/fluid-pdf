package tests

import io.fluidsonic.pdf.*
import kotlin.test.*


class PdfLengthUnitTests {

	@Test
	fun mmFactoryReturnsMmValue() {
		assertEquals(10.0, PdfLengthUnit.mm(10.0).mm)
	}

	@Test
	fun cmFactoryConvertsToMm() {
		assertEquals(10.0, PdfLengthUnit.cm(1.0).mm)
	}

	@Test
	fun inchFactoryConvertsToMm() {
		assertEquals(25.4, PdfLengthUnit.inch(1.0).mm, 0.01)
	}

	@Test
	fun cmPropertyConvertsMmToCm() {
		assertEquals(1.0, PdfLengthUnit.mm(10.0).cm)
	}

	@Test
	fun inchPropertyConvertsMmToInch() {
		assertEquals(1.0, PdfLengthUnit.mm(25.4).inch, 0.001)
	}

	@Test
	fun roundTripMmToCmToMm() {
		val original = PdfLengthUnit.mm(42.0)
		val roundTripped = PdfLengthUnit.cm(original.cm)
		assertEquals(original.mm, roundTripped.mm)
	}

	@Test
	fun roundTripMmToInchToMm() {
		val original = PdfLengthUnit.mm(25.4)
		val roundTripped = PdfLengthUnit.inch(original.inch)
		assertEquals(original.mm, roundTripped.mm, 0.0001)
	}

	@Test
	fun equalityForSameMmValue() {
		assertEquals(PdfLengthUnit.mm(10.0), PdfLengthUnit.mm(10.0))
	}

	@Test
	fun toStringFormat() {
		assertEquals("10.0 mm", PdfLengthUnit.mm(10.0).toString())
	}
}
