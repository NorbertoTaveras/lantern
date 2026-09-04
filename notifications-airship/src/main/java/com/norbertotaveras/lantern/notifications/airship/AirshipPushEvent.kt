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
 * Provider-neutral projection of an Airship push notification event.
 */
data class AirshipPushEvent(
    /**
     * Lifecycle event type.
     */
    val type: AirshipPushEventType,
    /**
     * Notification title, when present in the push payload.
     */
    val title: String? = null,
    /**
     * Notification alert/body, when present in the push payload.
     */
    val alert: String? = null,
    /**
     * Notification summary, when present in the push payload.
     */
    val summary: String? = null,
    /**
     * Airship send identifier, when present.
     */
    val sendId: String? = null,
    /**
     * Airship metadata string, when present.
     */
    val metadata: String? = null,
    /**
     * Posted Android notification ID, when known.
     */
    val notificationId: Int? = null,
    /**
     * Posted Android notification tag, when known.
     */
    val notificationTag: String? = null,
    /**
     * Whether Airship posted a notification for a received push.
     */
    val notificationPosted: Boolean? = null,
    /**
     * Airship action metadata, when the event is an action callback.
     */
    val action: AirshipNotificationAction? = null,
    /**
     * Push token value for token update events.
     */
    val pushToken: String? = null,
    /**
     * Push notification status for status change events.
     */
    val status: AirshipPushNotificationStatus? = null
)
