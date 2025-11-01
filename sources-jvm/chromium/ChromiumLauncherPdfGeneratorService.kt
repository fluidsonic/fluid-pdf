package io.fluidsonic.pdf

import com.github.kklisura.cdt.launch.*
import com.github.kklisura.cdt.launch.config.*
import com.github.kklisura.cdt.launch.support.impl.*
import com.github.kklisura.cdt.services.impl.*
import io.fluidsonic.pdf.ChromiumLaunchingPdfGeneratorService.*
import java.nio.file.*
import kotlinx.coroutines.*
import org.slf4j.*


internal class ChromiumLaunchingPdfGeneratorService(
	private val path: Path,
	private val arguments: Map<String, String>,
	private val configuration: ChromeLauncherConfiguration,
	private val logger: Logger,
) : AbstractPdfGeneratorService<Instance>() {

	override suspend fun generateWithInstance(instance: Instance, input: PdfGenerationInput): PdfGenerationOutput =
		instance.devToolsGenerator.generate(input)


	override suspend fun startInstance(): Instance =
		withContext(Dispatchers.IO) {
			checkBinaryFile(path)

			val chromiumLauncher = ChromeLauncher(
				ProcessLauncherImpl(),
				Environment,
				ChromeLauncher.RuntimeShutdownHookRegistry(),
				configuration,
			)

			val chromeService = chromiumLauncher.launch(
				path,
				ChromeArguments.defaults(true)
					.incognito()
					.additionalArguments("font-render-hinting", "none") // https://github.com/puppeteer/puppeteer/issues/2410
					.additionalArguments("no-sandbox", true)
					.additionalArguments("remote-allow-origins", "*") // https://groups.google.com/g/chromedriver-users/c/xL5-13_qGaA
					.apply {
						for ((name, value) in arguments)
							additionalArguments(name, value)
					}
					.build()
			) as ChromeServiceImpl

			Instance(
				chromiumLauncher = chromiumLauncher,
				devToolsGenerator = DevToolsPdfGenerator(
					discovery = KtorDevToolsService(
						host = chromeService.host,
						port = chromeService.port,
						fakeLocalhost = false,
					),
					logger = logger,
				),
			)
		}


	override suspend fun stopInstance(instance: Instance) {
		withContext(Dispatchers.IO) {
			instance.chromiumLauncher.close()
		}
	}


	private object Environment : ChromeLauncher.Environment {

		override fun getEnv(name: String?): String? =
			null
	}


	data class Instance(
		val chromiumLauncher: ChromeLauncher,
		val devToolsGenerator: DevToolsPdfGenerator,
	)
}


private fun checkBinaryFile(path: Path) {
	require(path.isAbsolute) { "Chromium binary file must be an absolute path: $path" }
	require(Files.exists(path)) { "Chromium binary file does not exist: $path" }
	require(Files.isReadable(path)) { "Chromium binary file is not a regular file: $path" }
	require(Files.isReadable(path) && Files.isExecutable(path)) { "Chromium binary file is not executable: $path" }
}
