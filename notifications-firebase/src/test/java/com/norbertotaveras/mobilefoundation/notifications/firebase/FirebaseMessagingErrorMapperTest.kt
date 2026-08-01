package com.norbertotaveras.mobilefoundation.notifications.firebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class FirebaseMessagingErrorMapperTest {

    @Test
    fun mapUsesOperationErrorCodeAndThrowableMessage() {
        val throwable = IllegalStateException("Token failed")

        val error = FirebaseMessagingErrorMapper().map(
            operation = FirebaseMessagingErrorMapper.Operation.GetToken,
            throwable = throwable
        )

        assertEquals(FirebaseMessagingErrorCodes.TOKEN_UNAVAILABLE, error.code)
        assertEquals("Token failed", error.message)
        assertSame(throwable, error.cause)
    }

    @Test
    fun mapUsesOperationFallbackMessageWhenThrowableMessageIsMissing() {
        val error = FirebaseMessagingErrorMapper().map(
            operation = FirebaseMessagingErrorMapper.Operation.SubscribeTopic,
            throwable = Throwable()
        )

        assertEquals(FirebaseMessagingErrorCodes.TOPIC_SUBSCRIPTION_FAILED, error.code)
        assertEquals("Unable to subscribe to Firebase Messaging topic.", error.message)
    }
}
