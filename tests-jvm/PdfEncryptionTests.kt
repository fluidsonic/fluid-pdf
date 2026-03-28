package tests

import io.fluidsonic.pdf.*
import kotlin.test.*


class PdfEncryptionTests {

	@Test
	fun validConstructionWithOwnerPasswordOnly() {
		val encryption = PdfEncryption(ownerPassword = "secret", permissions = PdfPermissions.all)
		assertEquals("secret", encryption.ownerPassword)
		assertNull(encryption.userPassword)
	}

	@Test
	fun validConstructionWithBothPasswords() {
		val encryption = PdfEncryption(ownerPassword = "owner", permissions = PdfPermissions.all, userPassword = "user")
		assertEquals("owner", encryption.ownerPassword)
		assertEquals("user", encryption.userPassword)
	}

	@Test
	fun emptyOwnerPasswordThrows() {
		assertFailsWith<IllegalArgumentException> {
			PdfEncryption(ownerPassword = "", permissions = PdfPermissions.all)
		}
	}

	@Test
	fun emptyUserPasswordThrows() {
		assertFailsWith<IllegalArgumentException> {
			PdfEncryption(ownerPassword = "owner", permissions = PdfPermissions.all, userPassword = "")
		}
	}

	@Test
	fun nullUserPasswordIsAllowed() {
		val encryption = PdfEncryption(ownerPassword = "owner", permissions = PdfPermissions.all, userPassword = null)
		assertNull(encryption.userPassword)
	}

	@Test
	fun dataClassEquality() {
		val a = PdfEncryption(ownerPassword = "owner", permissions = PdfPermissions.all, userPassword = "user")
		val b = PdfEncryption(ownerPassword = "owner", permissions = PdfPermissions.all, userPassword = "user")
		assertEquals(a, b)
	}
}
