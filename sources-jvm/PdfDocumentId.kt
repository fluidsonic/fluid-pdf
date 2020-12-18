package io.fluidsonic.pdf


public data class PdfDocumentId(
	val initial: String,
	val revision: String = initial,
)
