package io.fluidsonic.pdf

import com.github.kklisura.cdt.launch.*
import com.github.kklisura.cdt.protocol.types.page.*
import com.github.kklisura.cdt.services.*
import kotlinx.coroutines.*
import org.apache.pdfbox.cos.*
import org.apache.pdfbox.pdmodel.*
import java.io.*
import java.nio.file.*
import java.util.*
import kotlin.coroutines.*


internal class DefaultChromiumPdfGenerator constructor(
	private val launcher: ChromeLauncher,
	private val service: ChromeService
) : ChromiumPdfGenerator {

	@Volatile
	private var isClosed = false


	override fun close() {
		synchronized(this) {
			if (isClosed)
				return

			isClosed = true

			launcher.close()
		}
	}


	override suspend fun generate(source: PdfGenerationSource, settings: PdfGenerationSettings): PdfGenerationOutput {
		// TODO We may either want to block the close() call until all generations have been completed or abort pending generations.
		check(!isClosed) { "Cannot use a ChromiumPdfGenerator that has already been closed." }

		return withContext(Dispatchers.IO) {
			when (source) {
				is PdfGenerationSource.Html ->
					withTemporaryHtmlFile { sourceFile ->
						sourceFile.toFile().writeText(source.source, charset = source.charset)

						generate(sourceFile = sourceFile, settings = settings)
					}

				is PdfGenerationSource.HtmlFile -> {
					val sourceFile = source.file

					require(sourceFile.isAbsolute) { "'sourceFile' must be absolute: $sourceFile" }
					require(Files.exists(sourceFile)) { "'sourceFile' does not exist: $sourceFile" }
					require(Files.isReadable(sourceFile)) { "'sourceFile' is not readable: $sourceFile" }
					require(Files.isRegularFile(sourceFile)) { "'sourceFile' is not a regular file: $sourceFile" }
					require(Files.size(sourceFile) > 0L) { "'sourceFile' must not be empty: $sourceFile" }

					generate(sourceFile = source.file, settings = settings)
				}

				is PdfGenerationSource.HtmlStream ->
					withTemporaryHtmlFile { sourceFile ->
						sourceFile.toFile().outputStream().use { outputStream ->
							source.stream.copyTo(outputStream)
						}

						generate(sourceFile = sourceFile, settings = settings)
					}
			}
		}
	}


	private suspend fun generate(sourceFile: Path, settings: PdfGenerationSettings): PdfGenerationOutput =
		withContext(Dispatchers.Default) {
			val tab = service.createTab()
			val result = try {
				service.createDevToolsService(tab).use { devToolsService ->
					val page = devToolsService.page
					page.enable()
					page.navigate(sourceFile.toUri().toString())

					suspendCancellableCoroutine<Unit> { continuation ->
						val listener = page.onLoadEventFired { continuation.resume(Unit) }

						continuation.invokeOnCancellation { listener.unsubscribe() }
					}

					page.printToPDF(
						when (settings.pageOrientation) {
							PdfOrientation.landscape -> true
							PdfOrientation.portrait -> false
						},
						false,
						settings.includeBackgrounds,
						1.0,
						settings.pageSize.width.inch,
						settings.pageSize.height.inch,
						settings.pageMargins.top.inch,
						settings.pageMargins.bottom.inch,
						settings.pageMargins.left.inch,
						settings.pageMargins.right.inch,
						"",
						false,
						"",
						"",
						settings.preferCssPageSize,
						PrintToPDFTransferMode.RETURN_AS_BASE_64
					)
				}
			}
			finally {
				service.closeTab(tab)
			}

			var outputData = Base64.getDecoder().wrap(result.data.byteInputStream(Charsets.US_ASCII)).readBytes()

			settings.metadata?.let { metadata ->
				val outputStream = ByteArrayOutputStream()

				PDDocument.load(outputData).use { document ->
					document.documentInformation = PDDocumentInformation().apply {
						author = metadata.author
						creationDate = metadata.creationDate?.toCalendar()
						creator = metadata.creator
						keywords = metadata.keywords
						modificationDate = metadata.modificationDate?.toCalendar()
						producer = metadata.producer
						subject = metadata.subject
						title = metadata.title
					}
					metadata.documentId?.let { documentId ->
						document.document.documentID = COSArray().apply {
							add(COSString(documentId.initial))
							add(COSString(documentId.revision))
						}
					}
					document.save(outputStream)
				}

				outputData = outputStream.toByteArray()
			}

			PdfGenerationOutput(data = outputData)
		}


	private inline fun <Result> withTemporaryHtmlFile(action: (temporaryFile: Path) -> Result): Result {
		val file = Files.createTempFile("fluid-pdf-", ".html")
		try {
			return action(file)
		}
		finally {
			try {
				Files.deleteIfExists(file)
			}
			catch (e: Throwable) {
				file.toFile().deleteOnExit()
			}
		}
	}
}
