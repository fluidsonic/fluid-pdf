package io.fluidsonic.pdf

import com.github.kklisura.cdt.launch.*
import com.github.kklisura.cdt.launch.config.*
import com.github.kklisura.cdt.launch.support.impl.*
import com.github.kklisura.cdt.protocol.types.page.*
import com.github.kklisura.cdt.services.*
import com.github.kklisura.cdt.services.impl.*
import java.io.*
import java.net.*
import java.nio.file.*
import kotlin.coroutines.*
import kotlin.io.encoding.*
import kotlin.io.path.*
import kotlinx.coroutines.*
import org.apache.pdfbox.*
import org.apache.pdfbox.cos.*
import org.apache.pdfbox.pdmodel.*
import org.apache.pdfbox.pdmodel.encryption.*


internal data class ChromiumPdfGenerationSession(
	val end: suspend () -> Unit,
	val service: ChromeService,
) {

	suspend fun generate(input: PdfGenerationInput): PdfGenerationOutput =
		generate(
			source = when (val source = input.source) {
				is PdfGenerationSource.Html ->
					source.code

				is PdfGenerationSource.HtmlFile ->
					source.file.readText()

				is PdfGenerationSource.HtmlStream ->
					source.stream.readBytes().decodeToString()
			},
			settings = input.settings,
		)


	private suspend fun generate(source: String, settings: PdfGenerationSettings): PdfGenerationOutput {
		val tab = service.createTab()
		val result = try {
			service.createDevToolsService(tab).use { devToolsService ->
				val page = devToolsService.page
				page.enable()
				page.setDocumentContent(page.frameTree.frame.id, source)

				suspendCancellableCoroutine { continuation ->
					val listener = page.onLoadEventFired { continuation.resume(Unit) }

					continuation.invokeOnCancellation { listener.unsubscribe() }
				}

				page.printToPDF(
					when (settings.pageOrientation) {
						PdfOrientation.landscape -> true
						PdfOrientation.portrait -> false
					},
					true,
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
					settings.headerHtml.orEmpty().ifEmpty { " " },
					settings.footerHtml.orEmpty().ifEmpty { " " },
					settings.preferCssPageSize,
					PrintToPDFTransferMode.RETURN_AS_BASE_64,
				)
			}
		}
		finally {
			service.closeTab(tab)
		}

		var outputData = Base64.decode(result.data.toByteArray())

		val encryption = settings.encryption
		val metadata = settings.metadata
		if (encryption != null || metadata != null) {
			val outputStream = ByteArrayOutputStream()

			Loader.loadPDF(outputData).use { document ->
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

			outputData = outputStream.toByteArray()
		}

		return PdfGenerationOutput.withByteArray(data = outputData)
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


private fun checkBinaryFile(path: Path) {
	require(path.isAbsolute) { "Chromium binary file must be an absolute path: $path" }
	require(Files.exists(path)) { "Chromium binary file does not exist: $path" }
	require(Files.isReadable(path)) { "Chromium binary file is not a regular file: $path" }
	require(Files.isReadable(path) && Files.isExecutable(path)) { "Chromium binary file is not executable: $path" }
}


internal fun chromiumBinarySession(
	path: Path,
	configuration: ChromeLauncherConfiguration,
	arguments: Map<String, String>,
): ChromiumPdfGenerationSession {
	checkBinaryFile(path)

	val launcher = ChromeLauncher(
		ProcessLauncherImpl(),
		ChromiumEnvironment,
		ChromeLauncher.RuntimeShutdownHookRegistry(),
		configuration,
	)

	val service = launcher.launch(
		path,
		ChromeArguments.defaults(true)
			.incognito()
			.additionalArguments("font-render-hinting", "none") // https://github.com/puppeteer/puppeteer/issues/2410
			.additionalArguments("no-sandbox", true)
			.additionalArguments("remote-allow-origins", "*") // https://groups.google.com/g/chromedriver-users/c/xL5-13_qGaA
			.apply {
				for ((name, value) in arguments)
					additionalArguments(name, value)
			}
			.build()
	)

	return ChromiumPdfGenerationSession(
		end = launcher::close,
		service = service,
	)
}


internal fun chromiumRemoteSession(
	host: String,
	port: Int,
): ChromiumPdfGenerationSession {
	val address = try {
		InetAddress.getAllByName(host).first().hostAddress
	}
	catch (e: UnknownHostException) {
		throw Exception("Cannot resolve Chrome DevTools host '$host'.", e)
	}

	return ChromiumPdfGenerationSession(
		end = {},
		service = ChromeServiceImpl(encodeHostForUrl(address), port),
	)
}


private fun encodeHostForUrl(host: String) =
	if (':' in host && !host.startsWith('[') && !host.endsWith(']')) "[$host]" else host


private object ChromiumEnvironment : ChromeLauncher.Environment {

	override fun getEnv(name: String?): String? =
		null
}
