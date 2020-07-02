package io.fluidsonic.pdf

import java.time.*
import java.util.*


internal fun Instant.toCalendar(): Calendar =
	Calendar.getInstance().also { it.time = Date.from(this) }
