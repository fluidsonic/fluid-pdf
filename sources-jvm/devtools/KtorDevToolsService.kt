package io.fluidsonic.pdf

import com.github.kklisura.cdt.services.*
import com.github.kklisura.cdt.services.config.*
import com.github.kklisura.cdt.services.impl.*
import com.github.kklisura.cdt.services.invocation.*
import com.github.kklisura.cdt.services.utils.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import java.lang.reflect.*
import java.util.concurrent.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*


internal class KtorDevToolsService(
	fakeLocalhost: Boolean,
	host: String,
	port: Int,
) : DevToolsService {

	private val client = HttpClient(CIO) {
		install(ContentNegotiation) {
			json(Json { ignoreUnknownKeys = true })
		}

		defaultRequest {
			if (fakeLocalhost) header("Host", "localhost")
			url("http", host, port)
		}
	}

	private val urlMapper = if (fakeLocalhost) DevToolsUrlMapper(host, port) else null


	override suspend fun activateTab(tabId: String): Unit =
		request("activate", tabId)


	override suspend fun closeTab(tabId: String): Unit =
		request("close", tabId)


	// No Ktor yet…
	override suspend fun debugTab(debuggerUrl: Url): DevToolsConnection =
		withContext(Dispatchers.IO) {
			val websocketService = WebSocketServiceImpl.create(debuggerUrl.toURI())
			val commandInvocationHandler = CommandInvocationHandler()
			val commandsCache = ConcurrentHashMap<Method, Any>()

			val legacyService = ProxyUtils.createProxyFromAbstract(
				ChromeDevToolsServiceImpl::class.java,
				arrayOf(WebSocketService::class.java, ChromeDevToolsServiceConfiguration::class.java),
				arrayOf(websocketService, ChromeDevToolsServiceConfiguration())
			) { _, method, _ ->
				commandsCache.computeIfAbsent(method) {
					ProxyUtils.createProxy(method.returnType, commandInvocationHandler)
				}
			}

			commandInvocationHandler.setChromeDevToolsService(legacyService)

			DevToolsConnection(legacyService)
		}


	override suspend fun listTabs(): List<BrowserTab> =
		request<List<BrowserTab>>("list")
			.let { urlMapper?.map(it) ?: it }


	override suspend fun newTab(url: String): BrowserTab =
		request<BrowserTab>("new", url)
			.let { urlMapper?.map(it) ?: it }


	override suspend fun version(): BrowserVersion =
		request<BrowserVersion>("version")
			.let { urlMapper?.map(it) ?: it }


	private suspend inline fun <reified Result> request(
		path: String,
		query: String? = null,
	): Result =
		client
			.put {
				url {
					appendPathSegments("json", path)
					if (query != null) encodedParameters.appendAll(query, emptyList())
				}
			}
			.body<Result>()
}
