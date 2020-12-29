package io.fluidsonic.pdf

import java.io.*
import java.nio.file.*
import kotlin.coroutines.*
import kotlinx.coroutines.*


public interface PdfGenerationOutput {

	public suspend fun toByteArray(context: CoroutineContext = Dispatchers.Default): ByteArray
	public suspend fun writeTo(output: OutputStream, context: CoroutineContext = Dispatchers.IO)
	public suspend fun writeTo(file: Path, vararg options: OpenOption, context: CoroutineContext = Dispatchers.IO)


	public companion object {

		public fun withByteArray(data: ByteArray): PdfGenerationOutput =
			WithByteArray(data = data)
	}


	private class WithByteArray(private val data: ByteArray) : PdfGenerationOutput {

		override suspend fun toByteArray(context: CoroutineContext): ByteArray =
			data


		override suspend fun writeTo(output: OutputStream, context: CoroutineContext) {
			withContext(context) {
				output.write(data)
			}
		}


		override suspend fun writeTo(file: Path, vararg options: OpenOption, context: CoroutineContext) {
			withContext(context) {
				require(file.isAbsolute) { "'file' must be absolute: $file" }
				require(!Files.exists(file)) { "'file' must not already exist: $file" }
				require(file.parent?.let(Files::isWritable) ?: false) { "'file' parent is not writable: $file" }

				Files.write(file, data, *options)
			}
		}
	}
}
