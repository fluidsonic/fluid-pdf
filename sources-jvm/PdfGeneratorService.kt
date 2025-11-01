package io.fluidsonic.pdf

import com.github.kklisura.cdt.launch.config.*
import java.nio.file.*
import kotlinx.coroutines.*
import org.slf4j.*


public interface PdfGeneratorService : PdfGenerator {

	public suspend fun startIn(scope: CoroutineScope)
	public suspend fun stop()


	public companion object {

		public fun chromiumLauncher(
			path: Path,
			logger: Logger = DefaultLogger,
			configure: ChromiumBinaryConfigurationBuilder.() -> Unit = {},
		): PdfGeneratorService {
			val builder = ChromiumBinaryConfigurationBuilder().apply(configure)

			return ChromiumLaunchingPdfGeneratorService(
				arguments = builder.buildArguments(),
				configuration = builder.build(),
				logger = logger,
				path = path,
			)
		}


		public fun devTools(
			host: String,
			port: Int,
			logger: Logger = DefaultLogger,
			fakeLocalhost: Boolean = !DevToolsUrlMapper.isAllowedHost(host),
		): PdfGeneratorService =
			DevToolsPdfGeneratorService(
				fakeLocalhost = fakeLocalhost,
				host = host,
				logger = logger,
				port = port
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
