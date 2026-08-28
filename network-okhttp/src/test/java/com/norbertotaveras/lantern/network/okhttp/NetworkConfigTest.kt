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

package com.norbertotaveras.lantern.network.okhttp

import org.junit.Assert.assertThrows
import org.junit.Test

class NetworkConfigTest {

    @Test
    fun initRejectsNegativeTimeouts() {
        assertThrows(IllegalArgumentException::class.java) {
            NetworkConfig(connectTimeoutMillis = -1)
        }
    }

    @Test
    fun initRejectsBlankHeaderNames() {
        assertThrows(IllegalArgumentException::class.java) {
            NetworkConfig(defaultHeaders = mapOf(" " to "value"))
        }
    }

    @Test
    fun initRejectsMalformedHeaderNames() {
        assertThrows(IllegalArgumentException::class.java) {
            NetworkConfig(defaultHeaders = mapOf("X Trace Id" to "value"))
        }
    }

    @Test
    fun initRejectsDuplicateHeaderNamesIgnoringCase() {
        assertThrows(IllegalArgumentException::class.java) {
            NetworkConfig(defaultHeaders = mapOf("X-Trace-Id" to "first", "x-trace-id" to "second"))
        }
    }

    @Test
    fun initRejectsHeaderValuesWithControlCharacters() {
        assertThrows(IllegalArgumentException::class.java) {
            NetworkConfig(defaultHeaders = mapOf("X-Trace-Id" to "first\nsecond"))
        }
    }
}
