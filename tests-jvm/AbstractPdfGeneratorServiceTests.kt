package io.fluidsonic.pdf

import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlin.test.*


class AbstractPdfGeneratorServiceTests {

	private class TestPdfGeneratorService : AbstractPdfGeneratorService<String>() {

		override suspend fun generateWithInstance(instance: String, input: PdfGenerationInput): PdfGenerationOutput =
			PdfGenerationOutput.withByteArray("test".toByteArray())

		override suspend fun startInstance(): String = "test-instance"

		override suspend fun stopInstance(instance: String) {}
	}


	@Test
	fun generateBeforeStartThrows() = runTest {
		val service = TestPdfGeneratorService()
		val input = PdfGenerationInput(source = PdfGenerationSource.Html("<html></html>"))
		assertFailsWith<IllegalStateException> {
			service.generate(input)
		}
	}

	@Test
	fun startThenGenerateSucceeds() = runTest {
		val service = TestPdfGeneratorService()
		service.startIn(this)
		val input = PdfGenerationInput(source = PdfGenerationSource.Html("<html></html>"))
		val output = service.generate(input)
		val bytes = output.toByteArray()
		assertEquals("test", bytes.decodeToString())
		service.stop()
	}

	@Test
	fun startTwiceThrows() = runTest {
		val service = TestPdfGeneratorService()
		service.startIn(this)
		assertFailsWith<IllegalStateException> {
			service.startIn(this)
		}
		service.stop()
	}

	@Test
	fun stopBeforeStartThrows() = runTest {
		val service = TestPdfGeneratorService()
		assertFailsWith<IllegalStateException> {
			service.stop()
		}
	}

	@Test
	fun fullLifecycle() = runTest {
		val service = TestPdfGeneratorService()
		service.startIn(this)
		val input = PdfGenerationInput(source = PdfGenerationSource.Html("<html></html>"))
		val output = service.generate(input)
		assertContentEquals("test".toByteArray(), output.toByteArray())
		service.stop()
	}

	@Test
	fun generateAfterStopThrows() = runTest {
		val service = TestPdfGeneratorService()
		service.startIn(this)
		service.stop()
		val input = PdfGenerationInput(source = PdfGenerationSource.Html("<html></html>"))
		assertFailsWith<IllegalStateException> {
			service.generate(input)
		}
	}

	@Test
	fun stopTwiceThrows() = runTest {
		val service = TestPdfGeneratorService()
		service.startIn(this)
		service.stop()
		assertFailsWith<IllegalStateException> {
			service.stop()
		}
	}
}
