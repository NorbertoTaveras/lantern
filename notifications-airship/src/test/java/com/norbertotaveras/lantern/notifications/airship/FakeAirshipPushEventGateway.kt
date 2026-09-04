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

import com.norbertotaveras.lantern.notifications.NotificationChannelConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class FakeAirshipPushEventGateway(
    private val events: List<AirshipPushEvent> = emptyList(),
    private val status: AirshipPushNotificationStatus = AirshipPushNotificationStatus(
        userNotificationsEnabled = false,
        notificationsAllowed = false,
        pushPrivacyFeatureEnabled = false,
        pushTokenRegistered = false,
        optedIn = false
    ),
    private val failure: Throwable? = null
) : AirshipPushEventGateway {
    var createdChannelConfig: NotificationChannelConfig? = null
    var foregroundNotificationDisplayEnabled: Boolean? = null

    override fun observePushEvents(): Flow<AirshipPushEvent> {
        return flowOf(*events.toTypedArray())
    }

    override suspend fun getPushNotificationStatus(): AirshipPushNotificationStatus {
        failure?.let { throw it }
        return status
    }

    override suspend fun createNotificationChannel(config: NotificationChannelConfig) {
        failure?.let { throw it }
        createdChannelConfig = config
    }

    override suspend fun setForegroundNotificationDisplayEnabled(enabled: Boolean) {
        failure?.let { throw it }
        foregroundNotificationDisplayEnabled = enabled
    }
}
