package com.norbertotaveras.lantern.network.okhttp

import org.junit.Assert.assertThrows
import org.junit.Test

class NetworkConfigTest {

    @Test
    fun initRejectsNegativeTimeouts() {
        assertThrows(IllegalArgumentException::class.java) {
            NetworkConfig(connectTimeoutMillis = -1)
        }
    }

    @Test
    fun initRejectsBlankHeaderNames() {
        assertThrows(IllegalArgumentException::class.java) {
            NetworkConfig(defaultHeaders = mapOf(" " to "value"))
        }
    }

    @Test
    fun initRejectsMalformedHeaderNames() {
        assertThrows(IllegalArgumentException::class.java) {
            NetworkConfig(defaultHeaders = mapOf("X Trace Id" to "value"))
        }
    }

    @Test
    fun initRejectsDuplicateHeaderNamesIgnoringCase() {
        assertThrows(IllegalArgumentException::class.java) {
            NetworkConfig(defaultHeaders = mapOf("X-Trace-Id" to "first", "x-trace-id" to "second"))
        }
    }

    @Test
    fun initRejectsHeaderValuesWithControlCharacters() {
        assertThrows(IllegalArgumentException::class.java) {
            NetworkConfig(defaultHeaders = mapOf("X-Trace-Id" to "first\nsecond"))
        }
    }
}
