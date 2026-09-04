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

import com.urbanairship.Airship

/**
 * [AirshipAudienceGateway] backed by the Airship Android SDK singleton.
 */
class AirshipSdkAudienceGateway : AirshipAudienceGateway {
    override suspend fun getTags(): Set<String> {
        return Airship.channel.tags
    }

    override suspend fun addTags(tags: Set<String>) {
        Airship.channel.editTags()
            .addTags(tags)
            .apply()
    }

    override suspend fun removeTags(tags: Set<String>) {
        Airship.channel.editTags()
            .removeTags(tags)
            .apply()
    }

    override suspend fun clearTags() {
        Airship.channel.editTags()
            .clear()
            .apply()
    }

    override suspend fun setAttribute(name: String, value: AirshipAudienceAttributeValue) {
        val editor = Airship.channel.editAttributes()
        when (value) {
            is AirshipAudienceAttributeValue.StringValue -> editor.setAttribute(name, value.value)
            is AirshipAudienceAttributeValue.IntValue -> editor.setAttribute(name, value.value)
            is AirshipAudienceAttributeValue.LongValue -> editor.setAttribute(name, value.value)
            is AirshipAudienceAttributeValue.FloatValue -> editor.setAttribute(name, value.value)
            is AirshipAudienceAttributeValue.DoubleValue -> editor.setAttribute(name, value.value)
        }
        editor.apply()
    }

    override suspend fun removeAttribute(name: String) {
        Airship.channel.editAttributes()
            .removeAttribute(name)
            .apply()
    }

    override suspend fun subscribeToLists(listIds: Set<String>) {
        Airship.channel.editSubscriptionLists()
            .subscribe(listIds)
            .apply()
    }

    override suspend fun unsubscribeFromLists(listIds: Set<String>) {
        Airship.channel.editSubscriptionLists()
            .unsubscribe(listIds)
            .apply()
    }
}
