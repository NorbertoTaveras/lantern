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

package com.norbertotaveras.lantern.featureflags

/**
 * Stable error codes returned by feature flag APIs.
 */
object FeatureFlagErrorCodes {
    /**
     * Fallback code for unexpected feature flag failures.
     */
    const val UNKNOWN = "feature_flag_unknown"
    /**
     * A feature flag key failed validation.
     */
    const val INVALID_KEY = "feature_flag_invalid_key"
    /**
     * A requested flag value was not found.
     */
    const val VALUE_NOT_FOUND = "feature_flag_value_not_found"
    /**
     * A flag value exists but cannot be read as the expected type.
     */
    const val TYPE_MISMATCH = "feature_flag_type_mismatch"
    /**
     * Evaluating a flag failed.
     */
    const val EVALUATION_FAILED = "feature_flag_evaluation_failed"
}
