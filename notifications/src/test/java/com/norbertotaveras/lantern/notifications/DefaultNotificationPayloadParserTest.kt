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

package com.norbertotaveras.lantern.notifications

import com.norbertotaveras.lantern.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultNotificationPayloadParserTest {

    private val parser = DefaultNotificationPayloadParser()

    @Test
    fun parseMapsTitleBodyAndDeepLink() {
        val result = parser.parse(
            mapOf(
                "title" to "Sale",
                "body" to "Open the new deal",
                "deep_link" to "app://deals/42",
                "route" to "deals",
                "dl_param_id" to "42"
            )
        )

        assertTrue(result is SdkResult.Success)
        val payload = (result as SdkResult.Success).data
        assertEquals("Sale", payload.title)
        assertEquals("Open the new deal", payload.body)
        assertEquals("app://deals/42", payload.deepLink?.uri)
        assertEquals("deals", payload.deepLink?.route)
        assertEquals(mapOf("id" to "42"), payload.deepLink?.parameters)
    }

    @Test
    fun parseKeepsOriginalDataWhenNoKnownKeysExist() {
        val data = mapOf("custom" to "value")

        val result = parser.parse(data)

        assertTrue(result is SdkResult.Success)
        val payload = (result as SdkResult.Success).data
        assertEquals(null, payload.title)
        assertEquals(null, payload.body)
        assertEquals(null, payload.deepLink)
        assertEquals(data, payload.data)
    }
}
