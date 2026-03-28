package tests

import io.fluidsonic.pdf.*
import java.io.*
import java.nio.file.*
import kotlin.test.*


class PdfGenerationSourceTests {

	@Test
	fun htmlHoldsCodeString() {
		val code = "<html><body>Hello</body></html>"
		val source = PdfGenerationSource.Html(code)
		assertEquals(code, source.code)
	}

	@Test
	fun htmlFileHoldsPath() {
		val path = Path.of("/tmp/test.html")
		val source = PdfGenerationSource.HtmlFile(path)
		assertEquals(path, source.file)
	}

	@Test
	fun htmlStreamHoldsStream() {
		val stream = ByteArrayInputStream(byteArrayOf(1, 2, 3))
		val source = PdfGenerationSource.HtmlStream(stream)
		assertSame(stream, source.stream)
	}

	@Test
	fun htmlIsSubtypeOfPdfGenerationSource() {
		val source: PdfGenerationSource = PdfGenerationSource.Html("<html></html>")
		assertIs<PdfGenerationSource.Html>(source)
	}

	@Test
	fun htmlFileIsSubtypeOfPdfGenerationSource() {
		val source: PdfGenerationSource = PdfGenerationSource.HtmlFile(Path.of("/tmp/test.html"))
		assertIs<PdfGenerationSource.HtmlFile>(source)
	}

	@Test
	fun htmlStreamIsSubtypeOfPdfGenerationSource() {
		val source: PdfGenerationSource = PdfGenerationSource.HtmlStream(ByteArrayInputStream(byteArrayOf()))
		assertIs<PdfGenerationSource.HtmlStream>(source)
	}
}
