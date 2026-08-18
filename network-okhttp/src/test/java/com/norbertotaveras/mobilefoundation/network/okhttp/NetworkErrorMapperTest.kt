package com.norbertotaveras.mobilefoundation.network.okhttp

import java.net.SocketTimeoutException
import java.net.UnknownHostException
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkErrorMapperTest {

    private val mapper = NetworkErrorMapper()

    @Test
    fun mapReturnsTimeoutForSocketTimeoutException() {
        val error = mapper.map(SocketTimeoutException("timed out"))

        assertEquals(NetworkErrorCodes.TIMEOUT, error.code)
        assertEquals("timed out", error.message)
    }

    @Test
    fun mapReturnsNoConnectionForUnknownHostException() {
        val error = mapper.map(UnknownHostException("offline"))

        assertEquals(NetworkErrorCodes.NO_CONNECTION, error.code)
        assertEquals("offline", error.message)
    }

    @Test
    fun mapUsesDefaultMessageWhenThrowableMessageIsBlank() {
        val error = mapper.map(RuntimeException(""))

        assertEquals(NetworkErrorCodes.UNKNOWN, error.code)
        assertEquals("Network request failed.", error.message)
    }

    @Test
    fun retryExhaustedIncludesAttemptMetadata() {
        val error = mapper.retryExhausted(attempts = 3)

        assertEquals(NetworkErrorCodes.RETRY_EXHAUSTED, error.code)
        assertEquals("3", error.metadata["attempts"])
    }
}
