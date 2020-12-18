package io.fluidsonic.pdf

import com.github.kklisura.cdt.launch.*


internal object DefaultChromiumEnvironment : ChromeLauncher.Environment {

	override fun getEnv(name: String?): String? =
		null
}
