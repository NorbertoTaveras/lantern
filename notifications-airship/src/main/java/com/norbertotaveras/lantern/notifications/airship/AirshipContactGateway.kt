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
 * Minimal Airship contact surface consumed by Lantern helpers.
 */
interface AirshipContactGateway {
    /**
     * Returns the current Airship named user ID, or `null` when the contact is anonymous.
     */
    suspend fun getNamedUserId(): String?

    /**
     * Identifies the Airship contact with an app-owned named user ID.
     */
    suspend fun identify(namedUserId: String)

    /**
     * Resets the Airship contact back to anonymous state.
     */
    suspend fun reset()

    /**
     * Sets an Airship contact attribute.
     */
    suspend fun setAttribute(name: String, value: AirshipAudienceAttributeValue)

    /**
     * Removes an Airship contact attribute.
     */
    suspend fun removeAttribute(name: String)

    /**
     * Subscribes the contact to scoped subscription lists.
     */
    suspend fun subscribeToLists(
        listIds: Set<String>,
        scope: AirshipContactSubscriptionScope
    )

    /**
     * Unsubscribes the contact from scoped subscription lists.
     */
    suspend fun unsubscribeFromLists(
        listIds: Set<String>,
        scope: AirshipContactSubscriptionScope
    )
}
