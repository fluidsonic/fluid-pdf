package io.fluidsonic.pdf

import io.ktor.http.*


internal interface DevToolsService {

	suspend fun activateTab(tabId: String)
	suspend fun closeTab(tabId: String)
	suspend fun debugTab(debuggerUrl: Url): DevToolsConnection
	suspend fun listTabs(): List<BrowserTab>
	suspend fun newTab(url: String = "about:blank"): BrowserTab
	suspend fun version(): BrowserVersion
}
