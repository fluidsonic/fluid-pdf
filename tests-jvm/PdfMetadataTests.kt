package tests

import io.fluidsonic.pdf.*
import java.time.*
import kotlin.test.*


class PdfMetadataTests {

	@Test
	fun defaultConstructionHasNullDefaults() {
		val metadata = PdfMetadata()
		assertNull(metadata.author)
		assertNull(metadata.creationDate)
		assertNull(metadata.creator)
		assertNull(metadata.documentId)
		assertNull(metadata.keywords)
		assertNull(metadata.modificationDate)
		assertNull(metadata.producer)
		assertNull(metadata.subject)
		assertNull(metadata.title)
	}

	@Test
	fun constructionWithAllFields() {
		val now = Instant.now()
		val docId = PdfDocumentId(initial = "id1", revision = "rev1")
		val metadata = PdfMetadata(
			author = "Author",
			creationDate = now,
			creator = "Creator",
			documentId = docId,
			keywords = "test pdf",
			modificationDate = now,
			producer = "Producer",
			subject = "Subject",
			title = "Title",
		)
		assertEquals("Author", metadata.author)
		assertEquals(now, metadata.creationDate)
		assertEquals("Creator", metadata.creator)
		assertEquals(docId, metadata.documentId)
		assertEquals("test pdf", metadata.keywords)
		assertEquals(now, metadata.modificationDate)
		assertEquals("Producer", metadata.producer)
		assertEquals("Subject", metadata.subject)
		assertEquals("Title", metadata.title)
	}

	@Test
	fun dataClassCopy() {
		val metadata = PdfMetadata(title = "Original")
		val copied = metadata.copy(title = "Updated", author = "New Author")
		assertEquals("Updated", copied.title)
		assertEquals("New Author", copied.author)
		assertNull(copied.subject)
	}
}
