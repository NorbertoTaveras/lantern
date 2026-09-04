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

internal class FakeAirshipAudienceGateway(
    initialTags: Set<String> = emptySet(),
    private val lookupFailure: Throwable? = null,
    private val updateFailure: Throwable? = null
) : AirshipAudienceGateway {
    val tags = initialTags.toMutableSet()
    val attributes = mutableMapOf<String, AirshipAudienceAttributeValue>()
    val subscribedLists = mutableSetOf<String>()

    override suspend fun getTags(): Set<String> {
        lookupFailure?.let { throw it }
        return tags.toSet()
    }

    override suspend fun addTags(tags: Set<String>) {
        updateFailure?.let { throw it }
        this.tags.addAll(tags)
    }

    override suspend fun removeTags(tags: Set<String>) {
        updateFailure?.let { throw it }
        this.tags.removeAll(tags)
    }

    override suspend fun clearTags() {
        updateFailure?.let { throw it }
        tags.clear()
    }

    override suspend fun setAttribute(name: String, value: AirshipAudienceAttributeValue) {
        updateFailure?.let { throw it }
        attributes[name] = value
    }

    override suspend fun removeAttribute(name: String) {
        updateFailure?.let { throw it }
        attributes.remove(name)
    }

    override suspend fun subscribeToLists(listIds: Set<String>) {
        updateFailure?.let { throw it }
        subscribedLists.addAll(listIds)
    }

    override suspend fun unsubscribeFromLists(listIds: Set<String>) {
        updateFailure?.let { throw it }
        subscribedLists.removeAll(listIds)
    }
}
