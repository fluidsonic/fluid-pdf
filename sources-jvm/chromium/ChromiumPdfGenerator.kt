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
		): ChromiumPdfGenerator =
			launch(
				binaryFile = binaryFile,
				configuration = ConfigurationBuilder().apply(configure).build(),
				dispatcher = dispatcher,
			)


		internal suspend fun launch(
			binaryFile: Path,
			configuration: ChromeLauncherConfiguration,
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
					.build()
			)

			DefaultChromiumPdfGenerator(dispatcher = dispatcher, launcher = launcher, service = service)
		}


		public fun lazy(
			binaryFile: Path,
			configure: ConfigurationBuilder.() -> Unit = {},
		): LazyChromiumPdfGenerator =
			DefaultLazyChromiumPdfGenerator(
				binaryFile = binaryFile,
				configuration = ConfigurationBuilder().apply(configure).build()
			)
	}


	public class ConfigurationBuilder internal constructor() {

		internal fun build() =
			ChromeLauncherConfiguration()
	}
}
