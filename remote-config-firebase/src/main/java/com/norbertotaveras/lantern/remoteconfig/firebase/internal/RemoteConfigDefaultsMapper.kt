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

import com.norbertotaveras.lantern.remoteconfig.RemoteConfigDefaults
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigValue

/**
 * Converts SDK remote config defaults into Firebase-supported default values.
 */
fun RemoteConfigDefaults.toFirebaseDefaults(): Map<String, Any> {
    return values.mapKeys { (key, _) -> key.value }
        .mapValues { (_, value) ->
            when (value) {
                is RemoteConfigValue.BooleanValue -> value.value
                is RemoteConfigValue.DoubleValue -> value.value
                is RemoteConfigValue.LongValue -> value.value
                is RemoteConfigValue.StringValue -> value.value
            }
        }
}
