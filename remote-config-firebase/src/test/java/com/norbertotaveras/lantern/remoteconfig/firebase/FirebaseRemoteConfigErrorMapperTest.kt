package com.norbertotaveras.lantern.remoteconfig.firebase

import org.junit.Assert.assertEquals
import org.junit.Test

class FirebaseRemoteConfigErrorMapperTest {

    @Test
    fun mapUsesOperationErrorCodeAndThrowableMessage() {
        val error = FirebaseRemoteConfigErrorMapper().map(
            operation = FirebaseRemoteConfigErrorMapper.Operation.Fetch,
            throwable = IllegalStateException("Fetch failed")
        )

        assertEquals(FirebaseRemoteConfigErrorCodes.FETCH_FAILED, error.code)
        assertEquals("Fetch failed", error.message)
    }
}
