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
 * Deep-link data carried by a notification payload.
 */
data class NotificationDeepLink(
    /**
     * URI to parse or route when the notification is opened.
     */
    val uri: String,
    /**
     * Optional app route hint from the payload.
     */
    val route: String? = null,
    /**
     * Optional deep-link parameters extracted from the payload.
     */
    val parameters: Map<String, String> = emptyMap()
)
