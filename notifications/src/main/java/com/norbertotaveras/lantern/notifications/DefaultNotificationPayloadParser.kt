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

import com.norbertotaveras.lantern.core.SdkResult

/**
 * Default parser for common notification data payload keys.
 */
class DefaultNotificationPayloadParser : NotificationPayloadParser {
    override fun parse(data: Map<String, String>): SdkResult<NotificationPayload> {
        val title = data.firstValue(TITLE_KEYS)
        val body = data.firstValue(BODY_KEYS)
        val deepLink = data.toDeepLink()

        return SdkResult.Success(
            NotificationPayload(
                title = title,
                body = body,
                deepLink = deepLink,
                data = data
            )
        )
    }

    private fun Map<String, String>.toDeepLink(): NotificationDeepLink? {
        val uri = firstValue(DEEP_LINK_URI_KEYS) ?: return null
        val route = firstValue(DEEP_LINK_ROUTE_KEYS)
        val parameters = filterKeys { it.startsWith(DEEP_LINK_PARAMETER_PREFIX) }
            .mapKeys { (key, _) -> key.removePrefix(DEEP_LINK_PARAMETER_PREFIX) }
            .filterKeys { it.isNotBlank() }

        return NotificationDeepLink(
            uri = uri,
            route = route,
            parameters = parameters
        )
    }

    private fun Map<String, String>.firstValue(keys: Set<String>): String? {
        return keys.firstNotNullOfOrNull { key -> this[key]?.takeIf { it.isNotBlank() } }
    }

    private companion object {
        val TITLE_KEYS = setOf("title", "notification_title", "gcm.notification.title")
        val BODY_KEYS = setOf("body", "message", "notification_body", "gcm.notification.body")
        val DEEP_LINK_URI_KEYS = setOf("deep_link", "deepLink", "deeplink", "url")
        val DEEP_LINK_ROUTE_KEYS = setOf("route", "destination")
        const val DEEP_LINK_PARAMETER_PREFIX = "dl_param_"
    }
}
