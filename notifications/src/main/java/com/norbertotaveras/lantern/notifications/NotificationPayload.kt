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

package com.norbertotaveras.lantern.notifications

/**
 * Normalized notification payload data.
 */
data class NotificationPayload(
    /**
     * Optional notification title.
     */
    val title: String? = null,
    /**
     * Optional notification body.
     */
    val body: String? = null,
    /**
     * Optional deep-link data included in the notification.
     */
    val deepLink: NotificationDeepLink? = null,
    /**
     * Original key-value payload.
     */
    val data: Map<String, String> = emptyMap()
)
