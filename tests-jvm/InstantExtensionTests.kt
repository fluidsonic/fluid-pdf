package io.fluidsonic.pdf

import java.time.*
import java.util.*
import kotlin.test.*


class InstantExtensionTests {

	@Test
	fun epochInstantConvertsCorrectly() {
		val instant = Instant.EPOCH
		val calendar = instant.toCalendar()
		assertEquals(0L, calendar.timeInMillis)
	}

	@Test
	fun specificInstantPreservesEpochMillis() {
		val millis = 1_700_000_000_000L
		val instant = Instant.ofEpochMilli(millis)
		val calendar = instant.toCalendar()
		assertEquals(millis, calendar.timeInMillis)
	}

	@Test
	fun calendarTimeZoneIsDefault() {
		val instant = Instant.EPOCH
		val calendar = instant.toCalendar()
		assertEquals(TimeZone.getDefault(), calendar.timeZone)
	}
}
