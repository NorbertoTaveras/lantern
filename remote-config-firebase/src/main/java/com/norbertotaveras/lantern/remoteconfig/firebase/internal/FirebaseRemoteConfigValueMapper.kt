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

package com.norbertotaveras.lantern.remoteconfig.firebase.internal

import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigKey
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigValue
import com.norbertotaveras.lantern.remoteconfig.firebase.FirebaseRemoteConfigValueType

internal class FirebaseRemoteConfigValueMapper(
    private val valueTypes: Map<RemoteConfigKey, FirebaseRemoteConfigValueType> = emptyMap()
) {

    fun map(
        key: RemoteConfigKey,
        value: FirebaseRemoteConfigValue
    ): RemoteConfigValue {
        return when (valueTypes[key]) {
            FirebaseRemoteConfigValueType.Boolean -> RemoteConfigValue.BooleanValue(value.asBoolean())
            FirebaseRemoteConfigValueType.Double -> RemoteConfigValue.DoubleValue(value.asDouble())
            FirebaseRemoteConfigValueType.Long -> RemoteConfigValue.LongValue(value.asLong())
            FirebaseRemoteConfigValueType.String,
            null -> RemoteConfigValue.StringValue(value.asString())
        }
    }
}
