package io.fluidsonic.pdf

import java.io.*
import java.nio.file.*


/** The HTML source for PDF generation. */
public sealed interface PdfGenerationSource {

	/** An HTML source provided as a [code] string. */
	public class Html(public val code: String) : PdfGenerationSource

	/** An HTML source provided as a [file] path. */
	public class HtmlFile(public val file: Path) : PdfGenerationSource

	/** An HTML source provided as an input [stream]. */
	public class HtmlStream(public val stream: InputStream) : PdfGenerationSource
}
