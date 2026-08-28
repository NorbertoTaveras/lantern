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

import com.norbertotaveras.lantern.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteConfigKeyTest {

    @Test
    fun fromReturnsNormalizedKeyForValidValue() {
        val result = RemoteConfigKey.from(" welcome_enabled ")

        assertTrue(result is SdkResult.Success)
        assertEquals("welcome_enabled", (result as SdkResult.Success).data.value)
    }

    @Test
    fun fromRejectsInvalidKey() {
        val result = RemoteConfigKey.from("1-welcome")

        assertTrue(result is SdkResult.Failure)
        assertEquals(RemoteConfigErrorCodes.INVALID_KEY, (result as SdkResult.Failure).error.code)
    }
}
