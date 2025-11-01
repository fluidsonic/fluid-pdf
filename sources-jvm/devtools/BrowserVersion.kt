package io.fluidsonic.pdf

import io.ktor.http.*
import kotlinx.serialization.*


@Serializable
internal data class BrowserVersion(
	@SerialName("Android-Package")
	val androidPackage: String? = null,
	@SerialName("Browser")
	val browser: String,
	@SerialName("Protocol-Version")
	val protocolVersion: String,
	@SerialName("User-Agent")
	val userAgent: String,
	@SerialName("V8-Version")
	val v8Version: String,
	@SerialName("WebKit-Version")
	val webKitVersion: String,
	@SerialName("webSocketDebuggerUrl")
	val websocketDebuggerUrl: Url,
)
