package io.fluidsonic.pdf

import java.io.*
import org.slf4j.*


internal object DefaultLogger : Logger {

	override fun error(format: String?, vararg arguments: Any?) {
		System.err.println(format?.format(*arguments))
	}


	override fun error(msg: String?, t: Throwable?) {
		when (t) {
			null -> System.err.println(msg)
			else -> when (msg) {
				null -> t.printStackTrace(System.err)
				else -> StringWriter().use { writer ->
					writer.append(msg).append('\n')
					t.printStackTrace(PrintWriter(writer))
					System.err.print(writer)
				}
			}
		}
	}


	override fun debug(format: String?, arg1: Any?, arg2: Any?) {}
	override fun debug(format: String?, arg: Any?) {}
	override fun debug(format: String?, vararg arguments: Any?) {}
	override fun debug(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) {}
	override fun debug(marker: Marker?, format: String?, arg: Any?) {}
	override fun debug(marker: Marker?, format: String?, vararg arguments: Any?) {}
	override fun debug(marker: Marker?, msg: String?) {}
	override fun debug(marker: Marker?, msg: String?, t: Throwable?) {}
	override fun debug(msg: String?) {}
	override fun debug(msg: String?, t: Throwable?) {}
	override fun error(format: String?, arg1: Any?, arg2: Any?) = error(format, *arrayOf(arg1, arg2))
	override fun error(format: String?, arg: Any?) = error(format, *arrayOf(arg))
	override fun error(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) = error(format, *arrayOf(arg1, arg2))
	override fun error(marker: Marker?, format: String?, arg: Any?) = error(format, *arrayOf(arg))
	override fun error(marker: Marker?, format: String?, vararg arguments: Any?) = error(format, *arguments)
	override fun error(marker: Marker?, msg: String?) = error(msg, null)
	override fun error(marker: Marker?, msg: String?, t: Throwable?) = error(msg, t)
	override fun error(msg: String?) = error(msg, null)
	override fun getName(): String = "io.fluidsonic.pdf"
	override fun info(format: String?, arg1: Any?, arg2: Any?) {}
	override fun info(format: String?, arg: Any?) {}
	override fun info(format: String?, vararg arguments: Any?) {}
	override fun info(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) {}
	override fun info(marker: Marker?, format: String?, arg: Any?) {}
	override fun info(marker: Marker?, format: String?, vararg arguments: Any?) {}
	override fun info(marker: Marker?, msg: String?) {}
	override fun info(marker: Marker?, msg: String?, t: Throwable?) {}
	override fun info(msg: String?) {}
	override fun info(msg: String?, t: Throwable?) {}
	override fun isDebugEnabled(): Boolean = false
	override fun isDebugEnabled(marker: Marker?): Boolean = false
	override fun isErrorEnabled(): Boolean = true
	override fun isErrorEnabled(marker: Marker?): Boolean = true
	override fun isInfoEnabled(): Boolean = false
	override fun isInfoEnabled(marker: Marker?): Boolean = false
	override fun isTraceEnabled(): Boolean = false
	override fun isTraceEnabled(marker: Marker?): Boolean = false
	override fun isWarnEnabled(): Boolean = false
	override fun isWarnEnabled(marker: Marker?): Boolean = false
	override fun trace(format: String?, arg1: Any?, arg2: Any?) {}
	override fun trace(format: String?, arg: Any?) {}
	override fun trace(format: String?, vararg arguments: Any?) {}
	override fun trace(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) {}
	override fun trace(marker: Marker?, format: String?, arg: Any?) {}
	override fun trace(marker: Marker?, format: String?, vararg argArray: Any?) {}
	override fun trace(marker: Marker?, msg: String?) {}
	override fun trace(marker: Marker?, msg: String?, t: Throwable?) {}
	override fun trace(msg: String?) {}
	override fun trace(msg: String?, t: Throwable?) {}
	override fun warn(format: String?, arg1: Any?, arg2: Any?) {}
	override fun warn(format: String?, arg: Any?) {}
	override fun warn(format: String?, vararg arguments: Any?) {}
	override fun warn(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) {}
	override fun warn(marker: Marker?, format: String?, arg: Any?) {}
	override fun warn(marker: Marker?, format: String?, vararg arguments: Any?) {}
	override fun warn(marker: Marker?, msg: String?) {}
	override fun warn(marker: Marker?, msg: String?, t: Throwable?) {}
	override fun warn(msg: String?) {}
	override fun warn(msg: String?, t: Throwable?) {}
}
