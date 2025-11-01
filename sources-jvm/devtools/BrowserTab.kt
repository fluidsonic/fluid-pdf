package io.fluidsonic.pdf

import io.ktor.http.*
import kotlinx.serialization.*


@Serializable
internal data class BrowserTab(
	val description: String,
	val devtoolsFrontendUrl: Url,
	val faviconUrl: String? = null,
	val id: String,
	val parentId: String? = null,
	val title: String,
	val type: String,
	val url: String,
	@SerialName("webSocketDebuggerUrl")
	val websocketDebuggerUrl: Url,
)
