package io.fluidsonic.pdf

import java.time.*


/**
 * Metadata embedded in a PDF document.
 *
 * @property author The author of the document.
 * @property creationDate The date and time the document was created.
 * @property creator The application that created the original document before PDF conversion.
 * @property documentId A unique identifier for the document.
 * @property keywords Keywords associated with the document.
 * @property modificationDate The date and time the document was last modified.
 * @property producer The application that produced the PDF.
 * @property subject The subject of the document.
 * @property title The title of the document.
 */
public data class PdfMetadata(
	val author: String? = null,
	val creationDate: Instant? = null,
	val creator: String? = null,
	val documentId: PdfDocumentId? = null,
	val keywords: String? = null,
	val modificationDate: Instant? = null,
	val producer: String? = null,
	val subject: String? = null,
	val title: String? = null
)
