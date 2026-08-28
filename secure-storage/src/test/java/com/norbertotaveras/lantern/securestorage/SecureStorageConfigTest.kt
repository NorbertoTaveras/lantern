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

package com.norbertotaveras.lantern.securestorage

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class SecureStorageConfigTest {

    @Test
    fun `default namespace uses lantern branding`() {
        assertEquals("lantern_secure_storage", SecureStorageConfig.DEFAULT_NAMESPACE)
    }

    @Test
    fun `legacy namespace remains available for migration`() {
        assertEquals(
            "mobile_foundation_secure_storage",
            SecureStorageConfig.LEGACY_MOBILE_FOUNDATION_NAMESPACE
        )
    }

    @Test
    fun `init accepts valid namespace`() {
        SecureStorageConfig(namespace = "sample.secure_storage-1")
    }

    @Test
    fun `init rejects blank namespace`() {
        assertThrows(IllegalArgumentException::class.java) {
            SecureStorageConfig(namespace = " ")
        }
    }

    @Test
    fun `init rejects namespace with unsupported characters`() {
        assertThrows(IllegalArgumentException::class.java) {
            SecureStorageConfig(namespace = "sample secure storage")
        }
    }
}
