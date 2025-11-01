package io.fluidsonic.pdf

import io.ktor.http.*


internal class DevToolsUrlMapper(
	private val host: String,
	private val port: Int,
) {

	fun map(tab: BrowserTab) =
		tab.copy(
			devtoolsFrontendUrl = mapDevtoolsFrontendUrl(tab.devtoolsFrontendUrl),
			websocketDebuggerUrl = mapWebsocketDebuggerUrl(tab.websocketDebuggerUrl),
		)


	fun map(tabs: List<BrowserTab>) =
		tabs.map(::map)


	fun map(version: BrowserVersion) =
		version.copy(
			websocketDebuggerUrl = mapWebsocketDebuggerUrl(version.websocketDebuggerUrl),
		)


	private fun mapDevtoolsFrontendUrl(url: Url) =
		buildUrl {
			takeFrom(url)
			encodedParameters["ws"] = encodedParameters["ws"]
				.let { ws ->
					checkNotNull(ws) { "Missing parameter 'ws' in URL: $url" }
					buildUrl {
						takeFrom("ws://$ws")
						host = this@DevToolsUrlMapper.host
						port = this@DevToolsUrlMapper.port
					}
				}
				.toString()
				.removePrefix("ws://")
		}


	private fun mapWebsocketDebuggerUrl(url: Url) =
		buildUrl {
			takeFrom(url)
			host = this@DevToolsUrlMapper.host
			port = this@DevToolsUrlMapper.port
		}


	companion object {

		private val ipv4Regex = Regex("(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}")

		// DevTools (depending on browser implementation) only allow connections that use a local hostname or an IP address,
		// for security reasons. In such cases, we fake a `localhost` hostname and map the now incorrect returned URLs back to the
		// original hostname.
		fun isAllowedHost(host: String) =
			host.equals("localhost", ignoreCase = true) ||
				host.equals("localhost.", ignoreCase = true) ||
				host.endsWith(".localhost", ignoreCase = true) ||
				(host.startsWith("[") && host.endsWith("]")) ||
				host == "::1" ||
				ipv4Regex.matches(host)
	}
}
