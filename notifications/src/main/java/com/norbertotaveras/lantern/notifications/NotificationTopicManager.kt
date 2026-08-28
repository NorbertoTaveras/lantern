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
 * Subscribes and unsubscribes this app instance from provider notification topics.
 */
interface NotificationTopicManager {
    /**
     * Subscribes this app instance to [topic].
     */
    suspend fun subscribe(topic: NotificationTopic): SdkResult<Unit>

    /**
     * Unsubscribes this app instance from [topic].
     */
    suspend fun unsubscribe(topic: NotificationTopic): SdkResult<Unit>
}
