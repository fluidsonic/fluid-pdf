package io.fluidsonic.pdf

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.*


internal class ChromiumPdfGenerationService(
	private val beginSession: suspend () -> ChromiumPdfGenerationSession,
	private val dispatcher: CoroutineDispatcher,
) : PdfGenerationService {

	private val mutex = Mutex()
	private var session: ChromiumPdfGenerationSession? = null

	override val generator = ChromiumPdfGenerator(this)


	suspend fun generate(input: PdfGenerationInput): PdfGenerationOutput =
		mutex.withLock {
			val session = checkNotNull(session) { "Can't generate a PDF before starting or after stopping the Chromium PDF generation service." }

			return withContext(dispatcher) {
				session.generate(input)
			}
		}


	override suspend fun start() {
		mutex.withLock {
			if (session != null) return

			session = withContext(dispatcher) {
				beginSession()
			}
		}
	}


	override suspend fun stop() {
		mutex.withLock {
			val session = session ?: return

			withContext(dispatcher) {
				session.end()
			}

			this.session = null
		}
	}
}
