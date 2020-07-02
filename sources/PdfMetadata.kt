package io.fluidsonic.pdf

import java.time.*


data class PdfMetadata(
	val author: String? = null,
	val creationDate: Instant? = null,
	val creator: String? = null,
	val keywords: String? = null,
	val modificationDate: Instant? = null,
	val producer: String? = null,
	val subject: String? = null,
	val title: String? = null
)
