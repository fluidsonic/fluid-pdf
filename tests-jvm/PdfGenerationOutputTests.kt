package tests

import io.fluidsonic.pdf.*
import java.io.*
import kotlin.io.path.*
import kotlin.test.*
import kotlinx.coroutines.test.*


class PdfGenerationOutputTests {

	@Test
	fun withByteArrayCreatesOutput() {
		val data = byteArrayOf(1, 2, 3)
		val output = PdfGenerationOutput.withByteArray(data)
		assertNotNull(output)
	}

	@Test
	fun toByteArrayReturnsSameData() = runTest {
		val data = byteArrayOf(10, 20, 30, 40)
		val output = PdfGenerationOutput.withByteArray(data)
		assertContentEquals(data, output.toByteArray())
	}

	@Test
	fun writeToOutputStreamWritesCorrectData() = runTest {
		val data = byteArrayOf(5, 6, 7, 8, 9)
		val output = PdfGenerationOutput.withByteArray(data)
		val baos = ByteArrayOutputStream()
		output.writeTo(baos)
		assertContentEquals(data, baos.toByteArray())
	}

	@Test
	fun writeToPathWithNonAbsolutePathThrows() = runTest {
		val data = byteArrayOf(1, 2, 3)
		val output = PdfGenerationOutput.withByteArray(data)
		val relativePath = Path("relative/file.pdf")
		val exception = assertFailsWith<IllegalArgumentException> {
			output.writeTo(relativePath)
		}
		assertTrue(exception.message!!.contains("absolute"))
	}

	@Test
	fun writeToPathWithExistingFileThrows() = runTest {
		val tempDir = createTempDirectory("pdf-test")
		try {
			val file = tempDir.resolve("existing.pdf")
			file.writeBytes(byteArrayOf(0))

			val data = byteArrayOf(1, 2, 3)
			val output = PdfGenerationOutput.withByteArray(data)
			val exception = assertFailsWith<IllegalArgumentException> {
				output.writeTo(file)
			}
			assertTrue(exception.message!!.contains("must not already exist"))
		}
		finally {
			tempDir.toFile().deleteRecursively()
		}
	}

	@Test
	fun writeToPathWithAbsoluteTempPathSucceeds() = runTest {
		val tempDir = createTempDirectory("pdf-test")
		try {
			val file = tempDir.resolve("output.pdf")
			val data = byteArrayOf(11, 22, 33, 44)
			val output = PdfGenerationOutput.withByteArray(data)
			output.writeTo(file)
			assertContentEquals(data, file.readBytes())
		}
		finally {
			tempDir.toFile().deleteRecursively()
		}
	}
}
