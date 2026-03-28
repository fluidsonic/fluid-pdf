package io.fluidsonic.pdf


/**
 * A unique identifier for a PDF document, consisting of an initial ID and a revision ID.
 *
 * @property initial The permanent identifier assigned when the document was first created.
 * @property revision The identifier for the current revision, defaults to [initial].
 */
public data class PdfDocumentId(
	val initial: String,
	val revision: String = initial,
)
