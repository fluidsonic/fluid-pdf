package io.fluidsonic.pdf

import com.github.kklisura.cdt.launch.*
import com.github.kklisura.cdt.launch.config.*
import com.github.kklisura.cdt.launch.support.impl.*
import java.io.*
import java.nio.file.*
import kotlinx.coroutines.*


public interface ChromiumPdfGenerator : PdfGenerator, Closeable {

	public companion object {

		private fun checkBinaryFile(file: Path) {
			require(file.isAbsolute) { "'binaryFile' must be absolute: $file" }
			require(Files.exists(file)) { "'binaryFile' does not exist: $file" }
			require(Files.isReadable(file)) { "'binaryFile' is not a regular file: $file" }
			require(Files.isReadable(file) && Files.isExecutable(file)) { "'binaryFile' is not executable: $file" }
		}


		public suspend fun launch(
			binaryFile: Path,
			dispatcher: CoroutineDispatcher = Dispatchers.IO,
			configure: ConfigurationBuilder.() -> Unit = {},
		): ChromiumPdfGenerator {
			val builder = ConfigurationBuilder().apply(configure)

			return launch(
				arguments = builder.buildArguments(),
				binaryFile = binaryFile,
				configuration = builder.build(),
				dispatcher = dispatcher,
			)
		}


		internal suspend fun launch(
			binaryFile: Path,
			configuration: ChromeLauncherConfiguration,
			arguments: Map<String, String>,
			dispatcher: CoroutineDispatcher = Dispatchers.IO,
		): ChromiumPdfGenerator = withContext(dispatcher) {
			checkBinaryFile(binaryFile)

			val launcher = ChromeLauncher(
				ProcessLauncherImpl(),
				DefaultChromiumEnvironment,
				DefaultChromiumShutdownHookRegistry,
				configuration
			)

			val service = launcher.launch(
				binaryFile,
				ChromeArguments.defaults(true)
					.incognito()
					.userDataDir("/dev/null")
					.additionalArguments("font-render-hinting", "none") // https://github.com/puppeteer/puppeteer/issues/2410
					.additionalArguments("no-sandbox", true)
					.additionalArguments("remote-allow-origins", "*") // https://groups.google.com/g/chromedriver-users/c/xL5-13_qGaA
					.apply {
						for ((name, value) in arguments)
							additionalArguments(name, value)
					}
					.build()
			)

			DefaultChromiumPdfGenerator(dispatcher = dispatcher, launcher = launcher, service = service)
		}


		public fun lazy(
			binaryFile: Path,
			dispatcher: CoroutineDispatcher = Dispatchers.IO,
			configure: ConfigurationBuilder.() -> Unit = {},
		): LazyChromiumPdfGenerator {
			val builder = ConfigurationBuilder().apply(configure)

			return DefaultLazyChromiumPdfGenerator(
				arguments = builder.buildArguments(),
				binaryFile = binaryFile,
				configuration = builder.build(),
				dispatcher = dispatcher,
			)
		}
	}


	public class ConfigurationBuilder internal constructor() {

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
