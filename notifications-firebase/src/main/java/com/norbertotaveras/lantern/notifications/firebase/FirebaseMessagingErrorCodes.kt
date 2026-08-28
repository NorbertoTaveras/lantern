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

package com.norbertotaveras.lantern.notifications.firebase

/**
 * Stable error codes returned by Firebase Messaging integration.
 */
object FirebaseMessagingErrorCodes {
    const val UNKNOWN = "firebase_messaging_unknown"
    const val TOKEN_UNAVAILABLE = "firebase_messaging_token_unavailable"
    const val UNREGISTER_FAILED = "firebase_messaging_unregister_failed"
    const val TOPIC_SUBSCRIPTION_FAILED = "firebase_messaging_topic_subscription_failed"
    const val TOPIC_UNSUBSCRIPTION_FAILED = "firebase_messaging_topic_unsubscription_failed"
}
