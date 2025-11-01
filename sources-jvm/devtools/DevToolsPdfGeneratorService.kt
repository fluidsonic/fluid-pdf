package io.fluidsonic.pdf

import org.slf4j.*


internal class DevToolsPdfGeneratorService(
	private val fakeLocalhost: Boolean,
	private val host: String,
	private val logger: Logger,
	private val port: Int,
) : AbstractPdfGeneratorService<DevToolsPdfGenerator>() {

	override suspend fun generateWithInstance(instance: DevToolsPdfGenerator, input: PdfGenerationInput): PdfGenerationOutput =
		instance.generate(input)


	override suspend fun startInstance(): DevToolsPdfGenerator =
		DevToolsPdfGenerator(
			discovery = KtorDevToolsService(fakeLocalhost, host, port),
			logger = logger,
		)


	override suspend fun stopInstance(instance: DevToolsPdfGenerator) {}
}
