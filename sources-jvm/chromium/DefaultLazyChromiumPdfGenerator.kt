package io.fluidsonic.pdf

import com.github.kklisura.cdt.launch.config.*
import java.nio.file.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.*


internal class DefaultLazyChromiumPdfGenerator(
	private val arguments: Map<String, String>,
	private val binaryFile: Path,
	private val dispatcher: CoroutineDispatcher,
	private val configuration: ChromeLauncherConfiguration,
) : LazyChromiumPdfGenerator {

	private var delegate: ChromiumPdfGenerator? = null
	private var isClosed = false
	private val mutex = Mutex()


	override fun close() {
		runBlocking {
			mutex.withLock {
				if (isClosed)
					return@withLock

				isClosed = true

				delegate?.close()
			}
		}
	}


	private suspend fun ensureDelegate(): ChromiumPdfGenerator {
		start()

		return checkNotNull(delegate)
	}


	override suspend fun generate(input: PdfGenerationInput) =
		ensureDelegate().generate(input)


	override suspend fun start() {
		mutex.withLock {
			check(!isClosed) { "Cannot use a ChromiumPdfGenerator that has already been closed." }

			if (delegate == null)
				delegate = ChromiumPdfGenerator.launch(
					arguments = arguments,
					binaryFile = binaryFile,
					configuration = configuration,
					dispatcher = dispatcher,
				)
		}
	}
}
