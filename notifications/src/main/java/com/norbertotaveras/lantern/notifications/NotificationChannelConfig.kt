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
 * Provider-neutral notification channel definition.
 */
data class NotificationChannelConfig(
    /**
     * Stable channel identifier.
     */
    val id: NotificationChannelId,
    /**
     * User-visible channel name.
     */
    val name: String,
    /**
     * Optional user-visible channel description.
     */
    val description: String? = null,
    /**
     * Importance used when creating the platform channel.
     */
    val importance: NotificationChannelImportance = NotificationChannelImportance.Default,
    /**
     * Whether notifications in this channel may show an app icon badge.
     */
    val showBadge: Boolean = true,
    /**
     * Optional lightweight metadata for callers and provider implementations.
     */
    val metadata: Map<String, String> = emptyMap()
)
