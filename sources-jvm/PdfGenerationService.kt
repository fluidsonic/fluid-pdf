package io.fluidsonic.pdf

import com.github.kklisura.cdt.launch.config.*
import java.nio.file.*
import kotlinx.coroutines.*


public interface PdfGenerationService {

	public val generator: PdfGenerator

	public suspend fun start()
	public suspend fun stop()


	public companion object {

		public fun chromiumBinary(
			path: Path,
			dispatcher: CoroutineDispatcher = Dispatchers.Default,
			configure: ChromiumBinaryConfigurationBuilder.() -> Unit = {},
		): PdfGenerationService {
			val builder = ChromiumBinaryConfigurationBuilder().apply(configure)

			return ChromiumPdfGenerationService(
				beginSession = {
					chromiumBinarySession(
						arguments = builder.buildArguments(),
						configuration = builder.build(),
						path = path,
					)
				},
				dispatcher = dispatcher,
			)
		}


		public fun chromiumRemote(
			host: String,
			port: Int,
			dispatcher: CoroutineDispatcher = Dispatchers.Default,
		): PdfGenerationService =
			ChromiumPdfGenerationService(
				beginSession = {
					chromiumRemoteSession(host = host, port = port)
				},
				dispatcher = dispatcher,
			)
	}


	public class ChromiumBinaryConfigurationBuilder internal constructor() {

		private val arguments = mutableMapOf<String, String>()


		public fun argument(name: String, value: String) {
			arguments[name] = value
		}


		internal fun build() =
			ChromeLauncherConfiguration()


		internal fun buildArguments() =
			arguments.toMap()
	}
}
