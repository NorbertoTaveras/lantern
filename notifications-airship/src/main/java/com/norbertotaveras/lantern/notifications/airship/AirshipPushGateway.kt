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

/**
 * Minimal Airship push surface consumed by Lantern notification adapters.
 *
 * Apps own Airship SDK initialization, credentials, push provider setup, and runtime
 * configuration. Implement this gateway with Airship APIs, then pass it to Lantern adapters.
 */
interface AirshipPushGateway {
    /**
     * Returns the current Airship channel ID, or `null` when Airship has not registered one yet.
     */
    suspend fun getChannelId(): String?

    /**
     * Returns whether Airship user-visible notifications are enabled.
     */
    suspend fun areUserNotificationsEnabled(): Boolean

    /**
     * Enables or disables Airship user-visible notifications.
     */
    suspend fun setUserNotificationsEnabled(enabled: Boolean)
}
