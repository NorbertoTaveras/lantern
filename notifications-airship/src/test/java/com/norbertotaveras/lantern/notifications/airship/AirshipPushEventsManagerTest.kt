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
import com.norbertotaveras.lantern.notifications.NotificationChannelConfig
import com.norbertotaveras.lantern.notifications.NotificationChannelId
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class AirshipPushEventsManagerTest {

    @Test
    fun observePushEventsReturnsGatewayEvents() = runBlocking {
        val event = AirshipPushEvent(
            type = AirshipPushEventType.Opened,
            title = "Welcome"
        )
        val manager = AirshipPushEventsManager(
            FakeAirshipPushEventGateway(events = listOf(event))
        )

        val events = manager.observePushEvents().toList()

        assertEquals(listOf(event), events)
    }

    @Test
    fun getPushNotificationStatusReturnsGatewayStatus() = runBlocking {
        val status = AirshipPushNotificationStatus(
            userNotificationsEnabled = true,
            notificationsAllowed = true,
            pushPrivacyFeatureEnabled = true,
            pushTokenRegistered = true,
            optedIn = true
        )
        val manager = AirshipPushEventsManager(FakeAirshipPushEventGateway(status = status))

        val result = manager.getPushNotificationStatus()

        assertEquals(status, (result as SdkResult.Success).data)
    }

    @Test
    fun createNotificationChannelDelegatesToGateway() = runBlocking {
        val gateway = FakeAirshipPushEventGateway()
        val manager = AirshipPushEventsManager(gateway)
        val config = NotificationChannelConfig(
            id = NotificationChannelId.unsafe("updates"),
            name = "Updates"
        )

        manager.createNotificationChannel(config)

        assertEquals(config, gateway.createdChannelConfig)
    }

    @Test
    fun setForegroundNotificationDisplayDelegatesToGateway() = runBlocking {
        val gateway = FakeAirshipPushEventGateway()
        val manager = AirshipPushEventsManager(gateway)

        manager.setForegroundNotificationDisplayEnabled(true)

        assertTrue(gateway.foregroundNotificationDisplayEnabled == true)
    }

    @Test
    fun operationFailurePreservesCauseAndOperation() = runBlocking {
        val failure = IllegalStateException("Airship unavailable")
        val manager = AirshipPushEventsManager(FakeAirshipPushEventGateway(failure = failure))

        val result = manager.getPushNotificationStatus()

        val error = (result as SdkResult.Failure).error
        assertEquals(AirshipNotificationErrorCodes.PUSH_OPERATION_FAILED, error.code)
        assertEquals("get_push_notification_status", error.metadata["operation"])
        assertSame(failure, error.cause)
    }
}
