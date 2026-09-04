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
import com.urbanairship.contacts.Scope

/**
 * [AirshipContactGateway] backed by the Airship Android SDK singleton.
 */
class AirshipSdkContactGateway : AirshipContactGateway {
    override suspend fun getNamedUserId(): String? {
        return Airship.contact.namedUserId
    }

    override suspend fun identify(namedUserId: String) {
        Airship.contact.identify(namedUserId)
    }

    override suspend fun reset() {
        Airship.contact.reset()
    }

    override suspend fun setAttribute(name: String, value: AirshipAudienceAttributeValue) {
        val editor = Airship.contact.editAttributes()
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
        Airship.contact.editAttributes()
            .removeAttribute(name)
            .apply()
    }

    override suspend fun subscribeToLists(
        listIds: Set<String>,
        scope: AirshipContactSubscriptionScope
    ) {
        Airship.contact.editSubscriptionLists()
            .subscribe(listIds, scope.toAirshipScope())
            .apply()
    }

    override suspend fun unsubscribeFromLists(
        listIds: Set<String>,
        scope: AirshipContactSubscriptionScope
    ) {
        Airship.contact.editSubscriptionLists()
            .unsubscribe(listIds, scope.toAirshipScope())
            .apply()
    }

    private fun AirshipContactSubscriptionScope.toAirshipScope(): Scope {
        return when (this) {
            AirshipContactSubscriptionScope.App -> Scope.APP
            AirshipContactSubscriptionScope.Web -> Scope.WEB
            AirshipContactSubscriptionScope.Email -> Scope.EMAIL
            AirshipContactSubscriptionScope.Sms -> Scope.SMS
        }
    }
}
