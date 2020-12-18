package io.fluidsonic.pdf

import java.io.*
import java.nio.charset.*
import java.nio.file.*


public sealed class PdfGenerationSource {

	public class Html(public val source: String, public val charset: Charset = Charsets.UTF_8) : PdfGenerationSource()
	public class HtmlFile(public val file: Path) : PdfGenerationSource()
	public class HtmlStream(public val stream: InputStream) : PdfGenerationSource()
}
