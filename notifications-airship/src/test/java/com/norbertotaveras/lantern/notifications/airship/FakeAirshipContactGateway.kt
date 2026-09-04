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

internal class FakeAirshipContactGateway(
    var namedUserId: String? = null,
    private val failure: Throwable? = null
) : AirshipContactGateway {
    val attributes = mutableMapOf<String, AirshipAudienceAttributeValue>()
    val subscribedLists = mutableMapOf<AirshipContactSubscriptionScope, MutableSet<String>>()

    override suspend fun getNamedUserId(): String? {
        failure?.let { throw it }
        return namedUserId
    }

    override suspend fun identify(namedUserId: String) {
        failure?.let { throw it }
        this.namedUserId = namedUserId
    }

    override suspend fun reset() {
        failure?.let { throw it }
        namedUserId = null
    }

    override suspend fun setAttribute(name: String, value: AirshipAudienceAttributeValue) {
        failure?.let { throw it }
        attributes[name] = value
    }

    override suspend fun removeAttribute(name: String) {
        failure?.let { throw it }
        attributes.remove(name)
    }

    override suspend fun subscribeToLists(
        listIds: Set<String>,
        scope: AirshipContactSubscriptionScope
    ) {
        failure?.let { throw it }
        subscribedLists.getOrPut(scope) { mutableSetOf() }.addAll(listIds)
    }

    override suspend fun unsubscribeFromLists(
        listIds: Set<String>,
        scope: AirshipContactSubscriptionScope
    ) {
        failure?.let { throw it }
        subscribedLists.getOrPut(scope) { mutableSetOf() }.removeAll(listIds)
    }
}
