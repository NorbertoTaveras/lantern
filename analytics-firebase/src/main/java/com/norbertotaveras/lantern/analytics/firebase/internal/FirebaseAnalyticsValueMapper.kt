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

package com.norbertotaveras.lantern.analytics.firebase.internal

import android.os.Bundle
import com.norbertotaveras.lantern.analytics.AnalyticsValue

/**
 * Maps SDK analytics values to Firebase Analytics supported values.
 */
object FirebaseAnalyticsValueMapper {
    /**
     * Converts event parameters into a Firebase [Bundle].
     */
    fun toBundle(parameters: Map<String, AnalyticsValue>): Bundle {
        return Bundle().apply {
            parameters.forEach { (key, value) ->
                when (value) {
                    is AnalyticsValue.BooleanValue -> putString(key, value.value.toString())
                    is AnalyticsValue.DoubleValue -> putDouble(key, value.value)
                    is AnalyticsValue.LongValue -> putLong(key, value.value)
                    is AnalyticsValue.StringValue -> putString(key, value.value)
                }
            }
        }
    }

    /**
     * Converts a typed SDK value into a Firebase user property string.
     */
    fun toUserPropertyValue(value: AnalyticsValue): String {
        return when (value) {
            is AnalyticsValue.BooleanValue -> value.value.toString()
            is AnalyticsValue.DoubleValue -> value.value.toString()
            is AnalyticsValue.LongValue -> value.value.toString()
            is AnalyticsValue.StringValue -> value.value
        }
    }
}
