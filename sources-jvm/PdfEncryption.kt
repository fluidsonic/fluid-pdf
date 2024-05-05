package io.fluidsonic.pdf


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
