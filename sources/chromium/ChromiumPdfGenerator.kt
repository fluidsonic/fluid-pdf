package io.fluidsonic.pdf

import com.github.kklisura.cdt.launch.*
import com.github.kklisura.cdt.launch.config.*
import com.github.kklisura.cdt.launch.support.impl.*
import kotlinx.coroutines.*
import java.io.*
import java.nio.file.*


interface ChromiumPdfGenerator : PdfGenerator, Closeable {

	companion object {

		private fun checkBinaryFile(file: Path) {
			require(file.isAbsolute) { "'binaryFile' must be absolute: $file" }
			require(Files.exists(file)) { "'binaryFile' does not exist: $file" }
			require(Files.isReadable(file)) { "'binaryFile' is not a regular file: $file" }
			require(Files.isReadable(file) && Files.isExecutable(file)) { "'binaryFile' is not executable: $file" }
		}


		suspend fun launch(
			binaryFile: Path,
			configure: ConfigurationBuilder.() -> Unit = {}
		): ChromiumPdfGenerator =
			launch(
				binaryFile = binaryFile,
				configuration = ConfigurationBuilder().apply(configure).build()
			)


		internal suspend fun launch(
			binaryFile: Path,
			configuration: ChromeLauncherConfiguration
		): ChromiumPdfGenerator = withContext(Dispatchers.Default) {
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

			DefaultChromiumPdfGenerator(
				launcher = launcher,
				service = service
			)
		}


		fun lazy(
			binaryFile: Path,
			configure: ConfigurationBuilder.() -> Unit = {}
		): LazyChromiumPdfGenerator =
			DefaultLazyChromiumPdfGenerator(
				binaryFile = binaryFile,
				configuration = ConfigurationBuilder().apply(configure).build()
			)
	}


	class ConfigurationBuilder internal constructor() {

		internal fun build() =
			ChromeLauncherConfiguration()
	}
}
