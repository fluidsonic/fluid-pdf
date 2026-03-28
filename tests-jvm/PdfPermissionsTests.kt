package tests

import io.fluidsonic.pdf.*
import kotlin.test.*


class PdfPermissionsTests {

	@Test
	fun allHasAllFlagsTrue() {
		val all = PdfPermissions.all
		assertTrue(all.annotationAndFormFieldModificationAllowed)
		assertTrue(all.assemblyAllowed)
		assertTrue(all.contentExtractionAllowed)
		assertTrue(all.contentExtractionForAccessibilityAllowed)
		assertTrue(all.contentModificationAllowed)
		assertTrue(all.formFieldFillingAllowed)
		assertEquals(PdfPermissions.PrintQuality.high, all.printQuality)
	}

	@Test
	fun noneHasAllFlagsFalse() {
		val none = PdfPermissions.none
		assertFalse(none.annotationAndFormFieldModificationAllowed)
		assertFalse(none.assemblyAllowed)
		assertFalse(none.contentExtractionAllowed)
		assertFalse(none.contentExtractionForAccessibilityAllowed)
		assertFalse(none.contentModificationAllowed)
		assertFalse(none.formFieldFillingAllowed)
		assertEquals(PdfPermissions.PrintQuality.none, none.printQuality)
	}

	@Test
	fun printQualityEnumHasExactlyThreeValues() {
		assertEquals(3, PdfPermissions.PrintQuality.entries.size)
	}

	@Test
	fun dataClassCopyWorks() {
		val modified = PdfPermissions.none.copy(assemblyAllowed = true)
		assertTrue(modified.assemblyAllowed)
		assertFalse(modified.contentExtractionAllowed)
	}
}
