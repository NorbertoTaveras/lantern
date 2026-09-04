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
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class AirshipUserNotificationsManagerTest {

    @Test
    fun getStatusReturnsChannelAndUserNotificationState() = runBlocking {
        val manager = AirshipUserNotificationsManager(
            gateway = FakeAirshipPushGateway(
                channelId = "airship-channel-123",
                userNotificationsEnabled = true
            )
        )

        val result = manager.getStatus()

        val status = (result as SdkResult.Success).data
        assertEquals("airship-channel-123", status.channelId)
        assertTrue(status.userNotificationsEnabled)
    }

    @Test
    fun enableAndDisableUserNotificationsUpdateGateway() = runBlocking {
        val gateway = FakeAirshipPushGateway(userNotificationsEnabled = false)
        val manager = AirshipUserNotificationsManager(gateway)

        manager.enableUserNotifications()
        assertTrue(gateway.userNotificationsEnabled)

        manager.disableUserNotifications()
        assertFalse(gateway.userNotificationsEnabled)
    }

    @Test
    fun getStatusPreservesCauseWhenGatewayFails() = runBlocking {
        val failure = IllegalStateException("Status unavailable")
        val manager = AirshipUserNotificationsManager(
            gateway = FakeAirshipPushGateway(statusFailure = failure)
        )

        val result = manager.getStatus()

        val error = (result as SdkResult.Failure).error
        assertEquals(AirshipNotificationErrorCodes.USER_NOTIFICATIONS_STATUS_FAILED, error.code)
        assertEquals("Unable to load Airship notification status.", error.message)
        assertSame(failure, error.cause)
    }

    @Test
    fun setUserNotificationsPreservesCauseWhenGatewayFails() = runBlocking {
        val failure = IllegalStateException("Toggle unavailable")
        val manager = AirshipUserNotificationsManager(
            gateway = FakeAirshipPushGateway(setUserNotificationsFailure = failure)
        )

        val result = manager.enableUserNotifications()

        val error = (result as SdkResult.Failure).error
        assertEquals(AirshipNotificationErrorCodes.USER_NOTIFICATIONS_ENABLE_FAILED, error.code)
        assertEquals("Unable to update Airship user notification setting.", error.message)
        assertSame(failure, error.cause)
    }
}
