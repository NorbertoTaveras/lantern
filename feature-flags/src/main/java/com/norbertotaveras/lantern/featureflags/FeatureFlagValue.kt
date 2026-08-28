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
 * Typed feature flag value exposed by provider-neutral APIs.
 */
sealed interface FeatureFlagValue {
    /**
     * Boolean flag value.
     */
    data class BooleanValue(val value: Boolean) : FeatureFlagValue
    /**
     * Double flag value.
     */
    data class DoubleValue(val value: Double) : FeatureFlagValue
    /**
     * Long flag value.
     */
    data class LongValue(val value: Long) : FeatureFlagValue
    /**
     * String flag value.
     */
    data class StringValue(val value: String) : FeatureFlagValue
}
