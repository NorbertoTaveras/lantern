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

package com.norbertotaveras.lantern.analytics

/**
 * Typed analytics parameter or user property value.
 *
 * Use these variants instead of passing raw platform values so provider modules can map analytics
 * data consistently and reject unsupported value types before reaching vendor SDKs.
 */
sealed interface AnalyticsValue {
    /**
     * Boolean analytics value.
     */
    data class BooleanValue(val value: Boolean) : AnalyticsValue

    /**
     * Double analytics value.
     */
    data class DoubleValue(val value: Double) : AnalyticsValue

    /**
     * Long analytics value.
     */
    data class LongValue(val value: Long) : AnalyticsValue

    /**
     * String analytics value.
     */
    data class StringValue(val value: String) : AnalyticsValue
}
