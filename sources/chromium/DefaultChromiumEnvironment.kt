package io.fluidsonic.pdf

import com.github.kklisura.cdt.launch.*
import com.github.kklisura.cdt.launch.config.*
import com.github.kklisura.cdt.services.*
import kotlinx.coroutines.*
import java.io.*
import java.nio.charset.*
import java.util.concurrent.*
import kotlin.time.*


internal object DefaultChromiumEnvironment : ChromeLauncher.Environment {

	override fun getEnv(name: String?): String? =
		null
}
