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

package com.norbertotaveras.lantern.notifications.airship

import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.notifications.NotificationTokenProviderType
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test

class AirshipNotificationTokenProviderTest {

    @Test
    fun getTokenReturnsAirshipChannelIdAsNotificationToken() = runBlocking {
        val provider = AirshipNotificationTokenProvider(
            gateway = FakeAirshipPushGateway(channelId = "airship-channel-123")
        )

        val result = provider.getToken()

        val token = (result as SdkResult.Success).data
        assertEquals("airship-channel-123", token.value)
        assertEquals(NotificationTokenProviderType.Airship, token.provider)
        assertEquals("airship", token.metadata["provider"])
    }

    @Test
    fun getTokenReturnsFailureWhenChannelIdIsMissing() = runBlocking {
        val provider = AirshipNotificationTokenProvider(
            gateway = FakeAirshipPushGateway(channelId = null)
        )

        val result = provider.getToken()

        val error = (result as SdkResult.Failure).error
        assertEquals(AirshipNotificationErrorCodes.CHANNEL_ID_UNAVAILABLE, error.code)
        assertEquals("Airship channel ID is not available.", error.message)
        assertNull(error.cause)
    }

    @Test
    fun getTokenPreservesCauseWhenGatewayFails() = runBlocking {
        val failure = IllegalStateException("Airship unavailable")
        val provider = AirshipNotificationTokenProvider(
            gateway = FakeAirshipPushGateway(channelIdFailure = failure)
        )

        val result = provider.getToken()

        val error = (result as SdkResult.Failure).error
        assertEquals(AirshipNotificationErrorCodes.CHANNEL_LOOKUP_FAILED, error.code)
        assertEquals("Unable to load Airship channel ID.", error.message)
        assertSame(failure, error.cause)
    }

    @Test
    fun deleteTokenReturnsUnsupportedFailure() = runBlocking {
        val provider = AirshipNotificationTokenProvider(
            gateway = FakeAirshipPushGateway(channelId = "airship-channel-123")
        )

        val result = provider.deleteToken()

        val error = (result as SdkResult.Failure).error
        assertEquals(AirshipNotificationErrorCodes.CHANNEL_DELETE_UNSUPPORTED, error.code)
        assertEquals("Airship channel deletion is not supported.", error.message)
        assertNull(error.cause)
    }
}
