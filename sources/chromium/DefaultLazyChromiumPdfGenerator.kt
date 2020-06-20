package io.fluidsonic.pdf

import com.github.kklisura.cdt.launch.config.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.*
import java.nio.file.*


internal class DefaultLazyChromiumPdfGenerator(
	private val binaryFile: Path,
	private val configuration: ChromeLauncherConfiguration
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


	override suspend fun generate(source: PdfGenerationSource, destination: PdfGenerationDestination, settings: PdfGenerationSettings) {
		ensureDelegate().generate(source = source, destination = destination, settings = settings)
	}


	override suspend fun start() {
		mutex.withLock {
			check(!isClosed) { "Cannot use a ChromiumPdfGenerator that has already been closed." }

			if (delegate == null)
				delegate = ChromiumPdfGenerator.launch(
					binaryFile = binaryFile,
					configuration = configuration
				)
		}
	}
}
