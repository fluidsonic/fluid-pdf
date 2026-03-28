package tests

import io.fluidsonic.pdf.*
import kotlin.test.*


class PdfDocumentIdTests {

	@Test
	fun constructionWithBothValues() {
		val id = PdfDocumentId(initial = "abc", revision = "def")
		assertEquals("abc", id.initial)
		assertEquals("def", id.revision)
	}

	@Test
	fun revisionDefaultsToInitial() {
		val id = PdfDocumentId(initial = "abc")
		assertEquals("abc", id.revision)
	}

	@Test
	fun dataClassEquality() {
		val a = PdfDocumentId(initial = "abc", revision = "def")
		val b = PdfDocumentId(initial = "abc", revision = "def")
		assertEquals(a, b)
	}

	@Test
	fun dataClassCopy() {
		val original = PdfDocumentId(initial = "abc", revision = "def")
		val copied = original.copy(revision = "ghi")
		assertEquals("abc", copied.initial)
		assertEquals("ghi", copied.revision)
	}
}
