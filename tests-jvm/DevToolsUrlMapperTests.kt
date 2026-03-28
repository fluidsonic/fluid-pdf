package io.fluidsonic.pdf

import kotlin.test.*


class DevToolsUrlMapperTests {

	@Test
	fun localhostIsAllowed() {
		assertTrue(DevToolsUrlMapper.isAllowedHost("localhost"))
	}

	@Test
	fun localhostDotIsAllowed() {
		assertTrue(DevToolsUrlMapper.isAllowedHost("localhost."))
	}

	@Test
	fun subLocalhostIsAllowed() {
		assertTrue(DevToolsUrlMapper.isAllowedHost("sub.localhost"))
	}

	@Test
	fun localhostIsCaseInsensitive() {
		assertTrue(DevToolsUrlMapper.isAllowedHost("LOCALHOST"))
	}

	@Test
	fun ipv4LoopbackIsAllowed() {
		assertTrue(DevToolsUrlMapper.isAllowedHost("127.0.0.1"))
	}

	@Test
	fun ipv4PrivateIsAllowed() {
		assertTrue(DevToolsUrlMapper.isAllowedHost("192.168.1.1"))
	}

	@Test
	fun ipv4AllZerosIsAllowed() {
		assertTrue(DevToolsUrlMapper.isAllowedHost("0.0.0.0"))
	}

	@Test
	fun ipv4AllMaxIsAllowed() {
		assertTrue(DevToolsUrlMapper.isAllowedHost("255.255.255.255"))
	}

	@Test
	fun bracketedIpv6LoopbackIsAllowed() {
		assertTrue(DevToolsUrlMapper.isAllowedHost("[::1]"))
	}

	@Test
	fun bracketedIpv6LinkLocalIsAllowed() {
		assertTrue(DevToolsUrlMapper.isAllowedHost("[fe80::1]"))
	}

	@Test
	fun unbracketedIpv6LoopbackIsAllowed() {
		assertTrue(DevToolsUrlMapper.isAllowedHost("::1"))
	}

	@Test
	fun regularHostnameIsNotAllowed() {
		assertFalse(DevToolsUrlMapper.isAllowedHost("example.com"))
	}

	@Test
	fun anotherRegularHostnameIsNotAllowed() {
		assertFalse(DevToolsUrlMapper.isAllowedHost("google.com"))
	}

	@Test
	fun emptyStringIsNotAllowed() {
		assertFalse(DevToolsUrlMapper.isAllowedHost(""))
	}

	@Test
	fun localhostxIsNotAllowed() {
		assertFalse(DevToolsUrlMapper.isAllowedHost("localhostx"))
	}
}
