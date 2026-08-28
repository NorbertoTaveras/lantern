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
 * Stable error codes returned by notification APIs.
 */
object NotificationErrorCodes {
    /**
     * Fallback code for unexpected notification failures.
     */
    const val UNKNOWN = "notifications_unknown"
    /**
     * Notification permission is denied.
     */
    const val PERMISSION_DENIED = "notifications_permission_denied"
    /**
     * A provider token could not be returned.
     */
    const val TOKEN_UNAVAILABLE = "notifications_token_unavailable"
    /**
     * A topic failed validation.
     */
    const val INVALID_TOPIC = "notifications_invalid_topic"
    /**
     * Topic subscribe or unsubscribe failed.
     */
    const val TOPIC_SUBSCRIPTION_FAILED = "notifications_topic_subscription_failed"
    /**
     * A channel ID failed validation.
     */
    const val INVALID_CHANNEL_ID = "notifications_invalid_channel_id"
    /**
     * A channel create, update, or delete operation failed.
     */
    const val CHANNEL_OPERATION_FAILED = "notifications_channel_operation_failed"
    /**
     * A notification payload could not be parsed.
     */
    const val INVALID_PAYLOAD = "notifications_invalid_payload"
}
