package io.fluidsonic.pdf


/**
 * Encryption settings for a PDF document.
 *
 * @property ownerPassword Password required to change permissions or encryption. Must not be empty.
 * @property permissions Permissions granted to the user when the PDF is opened with the [userPassword].
 * @property userPassword Password required to open the PDF, or `null` for no password.
 */
public data class PdfEncryption(
	val ownerPassword: String,
	val permissions: PdfPermissions,
	val userPassword: String? = null,
) {

	init {
		require(ownerPassword.isNotEmpty()) { "`ownerPassword` must not be empty." }
		require(userPassword == null || userPassword.isNotEmpty()) { "`userPassword` must not be empty." }
	}


	public companion object
}
