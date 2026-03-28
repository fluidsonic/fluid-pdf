package io.fluidsonic.pdf

import java.io.*
import java.nio.file.*
import kotlinx.coroutines.*


/** The output of a PDF generation, providing access to the generated PDF data. */
public interface PdfGenerationOutput {

	/** Returns the generated PDF as a byte array. */
	public suspend fun toByteArray(dispatcher: CoroutineDispatcher = Dispatchers.IO): ByteArray

	/** Writes the generated PDF to the given [output] stream. */
	public suspend fun writeTo(output: OutputStream, dispatcher: CoroutineDispatcher = Dispatchers.IO)

	/** Writes the generated PDF to the given [file]. */
	public suspend fun writeTo(file: Path, vararg options: OpenOption, dispatcher: CoroutineDispatcher = Dispatchers.IO)


	/** Factory methods for creating [PdfGenerationOutput] instances. */
	public companion object {

		/** Creates an output backed by the given byte array [data]. */
		public fun withByteArray(data: ByteArray): PdfGenerationOutput =
			WithByteArray(data = data)
	}


	private class WithByteArray(private val data: ByteArray) : PdfGenerationOutput {

		override suspend fun toByteArray(dispatcher: CoroutineDispatcher): ByteArray =
			data


		override suspend fun writeTo(output: OutputStream, dispatcher: CoroutineDispatcher) {
			withContext(dispatcher) {
				output.write(data)
			}
		}


		override suspend fun writeTo(file: Path, vararg options: OpenOption, dispatcher: CoroutineDispatcher) {
			withContext(dispatcher) {
				require(file.isAbsolute) { "'file' must be absolute: $file" }
				require(!Files.exists(file)) { "'file' must not already exist: $file" }
				require(file.parent?.let(Files::isWritable) ?: false) { "'file' parent is not writable: $file" }

				Files.write(file, data, *options)
			}
		}
	}
}
