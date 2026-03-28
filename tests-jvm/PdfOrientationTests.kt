package tests

import io.fluidsonic.pdf.*
import kotlin.test.*


class PdfOrientationTests {

	@Test
	fun hasExactlyTwoEntries() {
		assertEquals(2, PdfOrientation.entries.size)
	}

	@Test
	fun landscapeExists() {
		assertNotNull(PdfOrientation.landscape)
	}

	@Test
	fun portraitExists() {
		assertNotNull(PdfOrientation.portrait)
	}

	@Test
	fun valueOfLandscape() {
		assertEquals(PdfOrientation.landscape, PdfOrientation.valueOf("landscape"))
	}

	@Test
	fun valueOfPortrait() {
		assertEquals(PdfOrientation.portrait, PdfOrientation.valueOf("portrait"))
	}
}
