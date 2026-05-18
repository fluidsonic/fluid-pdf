package io.fluidsonic.pdf

import com.github.kklisura.cdt.protocol.types.page.*
import java.io.*
import kotlin.io.encoding.*
import kotlin.io.path.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.apache.pdfbox.*
import org.apache.pdfbox.cos.*
import org.apache.pdfbox.pdmodel.*
import org.apache.pdfbox.pdmodel.encryption.*
import org.slf4j.*


internal class DevToolsPdfGenerator(
	private val discovery: DevToolsService,
	private val logger: Logger,
) : PdfGenerator {

	override suspend fun generate(input: PdfGenerationInput): PdfGenerationOutput =
		generate(
			settings = input.settings,
			source = when (val source = input.source) {
				is PdfGenerationSource.Html -> source.code
				is PdfGenerationSource.HtmlFile -> source.file.readText()
				is PdfGenerationSource.HtmlStream -> source.stream.readBytes().decodeToString()
			},
		)


	private suspend fun generate(source: String, settings: PdfGenerationSettings): PdfGenerationOutput {
		logger.debug("Creating new tab.")
		val tab = discovery.newTab()
		logger.debug("Tab created: {}", tab.id)

		logger.debug("Opening DevTools connection.")
		val connection = discovery.debugTab(tab.websocketDebuggerUrl)
		logger.debug("DevTools connection opened.")

		val result = try {
			val page = connection.page

			logger.debug("Enabling page protocol.")
			page.enable()
			logger.debug("Page protocol enabled.")

			logger.debug("Fetching frame tree.")
			val frameId = page.getFrameTree().frame.id
			logger.debug("Frame tree fetched: {}", frameId)

			logger.debug("Setting document content ({} chars).", source.length)
			page.setDocumentContent(frameId, source)
			logger.debug("Document content set.")

			logger.debug("Waiting for load event.")
			page.loadEventFired().first()
			logger.debug("Load event received.")

			logger.debug("Calling printToPdf.")
			val printToPdfResult = page.printToPdf(
				landscape = when (settings.pageOrientation) {
					PdfOrientation.landscape -> true
					PdfOrientation.portrait -> false
				},
				displayHeaderFooter = true,
				printBackground = settings.includeBackgrounds,
				scale = 1.0,
				paperWidth = settings.pageSize.width.inch,
				paperHeight = settings.pageSize.height.inch,
				marginTop = settings.pageMargins.top.inch,
				marginBottom = settings.pageMargins.bottom.inch,
				marginLeft = settings.pageMargins.left.inch,
				marginRight = settings.pageMargins.right.inch,
				pageRanges = "",
				ignoreInvalidPageRanges = false,
				headerTemplate = settings.headerHtml.orEmpty().ifEmpty { " " },
				footerTemplate = settings.footerHtml.orEmpty().ifEmpty { " " },
				preferCSSPageSize = settings.preferCssPageSize,
				transferMode = PrintToPDFTransferMode.RETURN_AS_BASE_64,
			)
			logger.debug("printToPdf returned ({} chars base64).", printToPdfResult.data.length)
			printToPdfResult
		}
		finally {
			logger.debug("Closing DevTools connection.")
			try {
				connection.close()
				logger.debug("DevTools connection closed.")
			}
			catch (e: Throwable) {
				logger.error("Cannot close DevTools connection.", e)
			}

			logger.debug("Closing tab {}.", tab.id)
			try {
				discovery.closeTab(tab.id)
				logger.debug("Tab {} closed.", tab.id)
			}
			catch (e: Throwable) {
				logger.error("Cannot close tab.", e)
			}
		}

		val outputData = withContext(Dispatchers.Default) {
			Base64.decode(result.data.toByteArray())
		}

		return PdfGenerationOutput.withByteArray(data = postprocess(outputData, settings))
	}


	private suspend fun postprocess(data: ByteArray, settings: PdfGenerationSettings): ByteArray {
		val encryption = settings.encryption
		val metadata = settings.metadata
		if (encryption == null && metadata == null)
			return data

		return withContext(Dispatchers.IO) {
			val outputStream = ByteArrayOutputStream()

			Loader.loadPDF(data).use { document ->
				if (encryption != null) {
					document.protect(
						StandardProtectionPolicy(
							encryption.ownerPassword,
							encryption.userPassword,
							AccessPermission().apply {
								val permissions = encryption.permissions

								setCanAssembleDocument(permissions.assemblyAllowed)
								setCanExtractContent(permissions.contentExtractionAllowed)
								setCanExtractForAccessibility(permissions.contentExtractionForAccessibilityAllowed)
								setCanFillInForm(permissions.formFieldFillingAllowed)
								setCanModify(permissions.contentModificationAllowed)
								setCanModifyAnnotations(permissions.annotationAndFormFieldModificationAllowed)
								setCanPrint(permissions.printQuality != PdfPermissions.PrintQuality.none)
								setCanPrintFaithful(permissions.printQuality == PdfPermissions.PrintQuality.high)
							},
						)
					)
				}

				if (metadata != null) {
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
				}

				document.save(outputStream)
			}

			outputStream.toByteArray()
		}
	}
}
