package io.fluidsonic.pdf

import com.github.kklisura.cdt.launch.config.*
import java.nio.file.*
import kotlinx.coroutines.*
import org.slf4j.*


/** A [PdfGenerator] that manages the lifecycle of an underlying browser connection. */
public interface PdfGeneratorService : PdfGenerator {

	/** Starts the service within the given coroutine [scope]. */
	public suspend fun startIn(scope: CoroutineScope)

	/** Stops the service and releases all resources. */
	public suspend fun stop()


	/** Factory methods for creating [PdfGeneratorService] instances. */
	public companion object {

		/** Creates a service that launches a local Chromium binary at the given [path]. */
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


		/** Creates a service that connects to a remote Chrome DevTools instance at the given [host] and [port]. */
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


	/** Builder for configuring Chromium binary launch arguments. */
	public class ChromiumBinaryConfigurationBuilder internal constructor() {

		private val arguments = mutableMapOf<String, String>()


		/** Adds a command-line argument to pass to the Chromium binary. */
		public fun argument(name: String, value: String) {
			arguments[name] = value
		}


		internal fun build() =
			ChromeLauncherConfiguration()


		internal fun buildArguments() =
			arguments.toMap()
	}
}
