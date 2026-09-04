/*
 * Copyright (C) 2026 Norberto Taveras
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.norbertotaveras.lantern.notifications.firebase

import com.norbertotaveras.lantern.notifications.firebase.internal.FirebaseMessagingErrorMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class FirebaseMessagingErrorMapperTest {

    @Test
    fun mapUsesOperationErrorCodeFallbackMessageAndCause() {
        val throwable = IllegalStateException("Token failed")

        val error = FirebaseMessagingErrorMapper().map(
            operation = FirebaseMessagingErrorMapper.Operation.GetToken,
            throwable = throwable
        )

        assertEquals(FirebaseMessagingErrorCodes.TOKEN_UNAVAILABLE, error.code)
        assertEquals("Unable to register Firebase Messaging and retrieve the FCM token.", error.message)
        assertSame(throwable, error.cause)
    }

    @Test
    fun mapUsesOperationFallbackMessage() {
        val error = FirebaseMessagingErrorMapper().map(
            operation = FirebaseMessagingErrorMapper.Operation.SubscribeTopic,
            throwable = Throwable()
        )

        assertEquals(FirebaseMessagingErrorCodes.TOPIC_SUBSCRIPTION_FAILED, error.code)
        assertEquals("Unable to subscribe to Firebase Messaging topic.", error.message)
    }
}
