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

package com.norbertotaveras.lantern.remoteconfig.firebase

/**
 * Stable error codes returned by Firebase Remote Config integration.
 */
object FirebaseRemoteConfigErrorCodes {
    const val UNKNOWN = "firebase_remote_config_unknown"
    const val FETCH_FAILED = "firebase_remote_config_fetch_failed"
    const val ACTIVATE_FAILED = "firebase_remote_config_activate_failed"
    const val DEFAULTS_FAILED = "firebase_remote_config_defaults_failed"
    const val SETTINGS_FAILED = "firebase_remote_config_settings_failed"
    const val VALUE_NOT_FOUND = "firebase_remote_config_value_not_found"
}
