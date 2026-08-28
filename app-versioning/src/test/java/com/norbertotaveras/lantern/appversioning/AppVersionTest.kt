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

package com.norbertotaveras.lantern.appversioning

import com.norbertotaveras.lantern.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppVersionTest {

    @Test
    fun parseReturnsSemanticVersion() {
        val result = AppVersion.parse("1.2.3-beta.1")

        assertTrue(result is SdkResult.Success)
        val version = (result as SdkResult.Success).data
        assertEquals(1, version.major)
        assertEquals(2, version.minor)
        assertEquals(3, version.patch)
        assertEquals("beta.1", version.qualifier)
        assertEquals("1.2.3-beta.1", version.toString())
    }

    @Test
    fun parseRejectsInvalidVersion() {
        val result = AppVersion.parse("1.2")

        assertTrue(result is SdkResult.Failure)
        assertEquals(AppVersionErrorCodes.INVALID_VERSION, (result as SdkResult.Failure).error.code)
    }

    @Test
    fun compareUsesMajorMinorPatch() {
        assertTrue(AppVersion(2, 0, 0) > AppVersion(1, 9, 9))
        assertTrue(AppVersion(1, 2, 4) > AppVersion(1, 2, 3))
        assertEquals(0, AppVersion(1, 2, 3).compareTo(AppVersion(1, 2, 3, "beta")))
    }
}
