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
 * Minimal Airship audience surface consumed by Lantern notification adapters.
 */
interface AirshipAudienceGateway {
    /**
     * Returns currently cached Airship channel tags.
     */
    suspend fun getTags(): Set<String>

    /**
     * Adds tags to the Airship channel.
     */
    suspend fun addTags(tags: Set<String>)

    /**
     * Removes tags from the Airship channel.
     */
    suspend fun removeTags(tags: Set<String>)

    /**
     * Clears all tags from the Airship channel.
     */
    suspend fun clearTags()

    /**
     * Sets an Airship channel attribute.
     */
    suspend fun setAttribute(name: String, value: AirshipAudienceAttributeValue)

    /**
     * Removes an Airship channel attribute.
     */
    suspend fun removeAttribute(name: String)

    /**
     * Subscribes the Airship channel to subscription lists.
     */
    suspend fun subscribeToLists(listIds: Set<String>)

    /**
     * Unsubscribes the Airship channel from subscription lists.
     */
    suspend fun unsubscribeFromLists(listIds: Set<String>)
}
