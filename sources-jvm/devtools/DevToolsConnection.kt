package io.fluidsonic.pdf

import com.github.kklisura.cdt.protocol.commands.Page as LegacyPage
import com.github.kklisura.cdt.protocol.events.page.*
import com.github.kklisura.cdt.protocol.support.types.*
import com.github.kklisura.cdt.protocol.types.page.*
import com.github.kklisura.cdt.services.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*


internal class DevToolsConnection(
	private val legacyService: ChromeDevToolsService,
) {

	val page = Page()


	suspend fun close() {
		withContext(Dispatchers.IO) {
			legacyService.close()
		}
	}


	inner class Page : Wrapper<LegacyPage>(legacyService.page) {

		suspend fun enable() {
			command { enable() }
		}


		suspend fun getFrameTree(): FrameTree =
			command { frameTree }


		fun loadEventFired(): Flow<LoadEventFired> =
			events { onLoadEventFired(it) }


		suspend fun printToPdf(
			landscape: Boolean,
			displayHeaderFooter: Boolean,
			printBackground: Boolean,
			scale: Double,
			paperWidth: Double,
			paperHeight: Double,
			marginTop: Double,
			marginBottom: Double,
			marginLeft: Double,
			marginRight: Double,
			pageRanges: String,
			ignoreInvalidPageRanges: Boolean,
			headerTemplate: String,
			footerTemplate: String,
			preferCSSPageSize: Boolean,
			transferMode: PrintToPDFTransferMode,
		): PrintToPDF =
			command {
				printToPDF(
					landscape,
					displayHeaderFooter,
					printBackground,
					scale,
					paperWidth,
					paperHeight,
					marginTop,
					marginBottom,
					marginLeft,
					marginRight,
					pageRanges,
					ignoreInvalidPageRanges,
					headerTemplate,
					footerTemplate,
					preferCSSPageSize,
					transferMode,
				)
			}


		suspend fun setDocumentContent(frameId: String, html: String) {
			command { setDocumentContent(frameId, html) }
		}
	}


	abstract class Wrapper<Domain>(
		private val domain: Domain,
	) {

		protected suspend inline fun <Result> command(crossinline command: Domain.() -> Result): Result =
			withContext(Dispatchers.IO) {
				domain.command()
			}


		protected inline fun <Event : Any> events(
			crossinline listen: Domain.(EventHandler<Event>) -> EventListener,
		): Flow<Event> =
			callbackFlow {
				val listener = domain.listen { event ->
					runBlocking { channel.send(event) }
				}

				awaitClose { listener.unsubscribe() }
			}
	}
}
