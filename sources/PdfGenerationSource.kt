package io.fluidsonic.pdf

import java.io.*
import java.nio.charset.*
import java.nio.file.*


sealed class PdfGenerationSource {

	class Html(val source: String, val charset: Charset = Charsets.UTF_8) : PdfGenerationSource()
	class HtmlFile(val file: Path) : PdfGenerationSource()
	class HtmlStream(val stream: InputStream) : PdfGenerationSource()
}
