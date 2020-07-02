package io.fluidsonic.pdf

import kotlinx.coroutines.*
import java.io.*
import java.nio.file.*


class PdfGenerationOutput internal constructor(
	private val data: ByteArray
) {

	@Suppress("RedundantSuspendModifier") // 'suspend' in case we change the implementation later
	suspend fun toByteArray(): ByteArray =
		data


	suspend fun writeTo(output: OutputStream) {
		withContext(Dispatchers.IO) {
			output.write(data)
		}
	}


	suspend fun writeTo(file: Path, vararg options: OpenOption) {
		withContext(Dispatchers.IO) {
			require(file.isAbsolute) { "'file' must be absolute: $file" }
			require(!Files.exists(file)) { "'file' must not already exist: $file" }
			require(file.parent?.let(Files::isWritable) ?: false) { "'file' parent is not writable: $file" }

			Files.write(file, data, *options)
		}
	}
}
