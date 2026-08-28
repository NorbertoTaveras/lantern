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

import com.norbertotaveras.lantern.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AnalyticsUserPropertyNameTest {

    @Test
    fun fromAcceptsValidPropertyName() {
        val result = AnalyticsUserPropertyName.from("subscription_tier")

        assertTrue(result is SdkResult.Success)
        assertEquals("subscription_tier", (result as SdkResult.Success).data.value)
    }

    @Test
    fun fromRejectsBlankPropertyName() {
        val result = AnalyticsUserPropertyName.from(" ")

        assertTrue(result is SdkResult.Failure)
        assertEquals(AnalyticsErrorCodes.INVALID_PROPERTY_NAME, (result as SdkResult.Failure).error.code)
    }
}
