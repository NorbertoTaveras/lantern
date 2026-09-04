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
 * Snapshot of Airship push notification readiness.
 */
data class AirshipPushNotificationStatus(
    /**
     * Whether user-visible Airship notifications are enabled.
     */
    val userNotificationsEnabled: Boolean,
    /**
     * Whether Android currently allows notifications for the app.
     */
    val notificationsAllowed: Boolean,
    /**
     * Whether Airship push data collection is enabled.
     */
    val pushPrivacyFeatureEnabled: Boolean,
    /**
     * Whether Airship has a registered push token.
     */
    val pushTokenRegistered: Boolean,
    /**
     * Whether Airship considers this install opted in for push.
     */
    val optedIn: Boolean
)
