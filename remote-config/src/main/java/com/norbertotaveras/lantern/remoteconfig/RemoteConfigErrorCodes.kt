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

package com.norbertotaveras.lantern.remoteconfig

/**
 * Stable error codes returned by remote config APIs.
 */
object RemoteConfigErrorCodes {
    /**
     * Fallback code for unexpected remote config failures.
     */
    const val UNKNOWN = "remote_config_unknown"
    /**
     * A remote config key failed validation.
     */
    const val INVALID_KEY = "remote_config_invalid_key"
    /**
     * Fetching values from the backing provider failed.
     */
    const val FETCH_FAILED = "remote_config_fetch_failed"
    /**
     * Activating fetched values failed.
     */
    const val ACTIVATE_FAILED = "remote_config_activate_failed"
    /**
     * Applying provider defaults failed.
     */
    const val DEFAULTS_FAILED = "remote_config_defaults_failed"
    /**
     * A requested value was not found.
     */
    const val VALUE_NOT_FOUND = "remote_config_value_not_found"
    /**
     * A value exists but cannot be read as the requested type.
     */
    const val TYPE_MISMATCH = "remote_config_type_mismatch"
}
