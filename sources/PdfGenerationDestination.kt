package io.fluidsonic.pdf

import java.io.*
import java.nio.file.*


sealed class PdfGenerationDestination {

	class File(val file: Path) : PdfGenerationDestination()
	class Stream(val stream: OutputStream) : PdfGenerationDestination()
}
