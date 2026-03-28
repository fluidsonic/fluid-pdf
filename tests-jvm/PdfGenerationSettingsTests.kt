package tests

import io.fluidsonic.pdf.*
import kotlin.test.*


class PdfGenerationSettingsTests {

	@Test
	fun defaultIncludeBackgroundsIsTrue() {
		assertTrue(PdfGenerationSettings.default.includeBackgrounds)
	}

	@Test
	fun defaultEncryptionIsNull() {
		assertNull(PdfGenerationSettings.default.encryption)
	}

	@Test
	fun defaultMetadataIsNull() {
		assertNull(PdfGenerationSettings.default.metadata)
	}

	@Test
	fun defaultPageMarginsIs1Cm() {
		val margins = PdfGenerationSettings.default.pageMargins
		assertEquals(10.0, margins.top.mm)
		assertEquals(10.0, margins.right.mm)
		assertEquals(10.0, margins.bottom.mm)
		assertEquals(10.0, margins.left.mm)
	}

	@Test
	fun defaultPageOrientationIsPortrait() {
		assertEquals(PdfOrientation.portrait, PdfGenerationSettings.default.pageOrientation)
	}

	@Test
	fun defaultPageSizeIsA4() {
		assertEquals(PdfSize.A4, PdfGenerationSettings.default.pageSize)
	}

	@Test
	fun defaultPreferCssPageSizeIsTrue() {
		assertTrue(PdfGenerationSettings.default.preferCssPageSize)
	}

	@Test
	fun defaultHeaderHtmlIsNull() {
		assertNull(PdfGenerationSettings.default.headerHtml)
	}

	@Test
	fun defaultFooterHtmlIsNull() {
		assertNull(PdfGenerationSettings.default.footerHtml)
	}

	@Test
	fun copyPreservesValues() {
		val original = PdfGenerationSettings.default
		val copy = original.copy(includeBackgrounds = false)
		assertFalse(copy.includeBackgrounds)
		assertEquals(original.pageOrientation, copy.pageOrientation)
		assertEquals(original.pageSize, copy.pageSize)
		assertEquals(original.pageMargins, copy.pageMargins)
		assertEquals(original.preferCssPageSize, copy.preferCssPageSize)
	}
}
