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
		val tab = discovery.newTab()
		val connection = discovery.debugTab(tab.websocketDebuggerUrl)
		val result = try {
			val page = connection.page
			page.enable()
			page.setDocumentContent(page.getFrameTree().frame.id, source)
			page.loadEventFired().first()
			page.printToPdf(
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
		}
		finally {
			try {
				connection.close()
			}
			catch (e: Throwable) {
				logger.error("Cannot close DevTools connection.", e)
			}

			try {
				discovery.closeTab(tab.id)
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
