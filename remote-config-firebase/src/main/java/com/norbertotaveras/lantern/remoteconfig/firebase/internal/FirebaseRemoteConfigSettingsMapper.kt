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

import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigSettings

/**
 * Converts SDK remote config settings into Firebase Remote Config settings.
 */
fun RemoteConfigSettings.toFirebase(): FirebaseRemoteConfigSettings {
    return FirebaseRemoteConfigSettings.Builder()
        .setMinimumFetchIntervalInSeconds(minimumFetchIntervalMillis / MILLIS_PER_SECOND)
        .setFetchTimeoutInSeconds(fetchTimeoutMillis / MILLIS_PER_SECOND)
        .build()
}

private const val MILLIS_PER_SECOND = 1_000L
