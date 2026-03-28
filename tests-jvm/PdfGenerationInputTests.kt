package tests

import io.fluidsonic.pdf.*
import kotlin.test.*


class PdfGenerationInputTests {

	@Test
	fun constructionWithSourceOnlyUsesDefaultSettings() {
		val source = PdfGenerationSource.Html("<html></html>")
		val input = PdfGenerationInput(source = source)
		assertSame(PdfGenerationSettings.default, input.settings)
	}

	@Test
	fun constructionWithSourceAndCustomSettings() {
		val source = PdfGenerationSource.Html("<html></html>")
		val settings = PdfGenerationSettings.default.copy(includeBackgrounds = false)
		val input = PdfGenerationInput(source = source, settings = settings)
		assertSame(source, input.source)
		assertSame(settings, input.settings)
	}

	@Test
	fun dataClassEquality() {
		val source = PdfGenerationSource.Html("<html></html>")
		val input1 = PdfGenerationInput(source = source)
		val input2 = PdfGenerationInput(source = source)
		assertEquals(input1, input2)
	}
}
