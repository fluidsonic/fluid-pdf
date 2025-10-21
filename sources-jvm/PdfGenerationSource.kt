package io.fluidsonic.pdf

import java.io.*
import java.nio.file.*


public sealed interface PdfGenerationSource {

	public class Html(public val code: String) : PdfGenerationSource
	public class HtmlFile(public val file: Path) : PdfGenerationSource
	public class HtmlStream(public val stream: InputStream) : PdfGenerationSource
}
