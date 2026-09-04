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

/**
 * Airship push lifecycle surface consumed by Lantern notification helpers.
 */
interface AirshipPushEventGateway {
    /**
     * Observes Airship push, notification, token, and status events.
     */
    fun observePushEvents(): Flow<AirshipPushEvent>

    /**
     * Returns the current Airship push notification status.
     */
    suspend fun getPushNotificationStatus(): AirshipPushNotificationStatus

    /**
     * Creates an Airship notification channel.
     */
    suspend fun createNotificationChannel(config: NotificationChannelConfig)

    /**
     * Controls whether Airship should display foreground notifications.
     */
    suspend fun setForegroundNotificationDisplayEnabled(enabled: Boolean)
}
