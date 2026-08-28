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

import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class SecureTokenTest {

    @Test
    fun `init rejects blank token value`() {
        assertThrows(IllegalArgumentException::class.java) {
            SecureToken(value = " ")
        }
    }

    @Test
    fun `isExpired is false when token has no expiration`() {
        val token = SecureToken(value = "token")

        assertFalse(token.isExpired(currentTimeMillis = 100L))
    }

    @Test
    fun `isExpired is false before expiration time`() {
        val token = SecureToken(
            value = "token",
            expiresAtEpochMillis = 200L
        )

        assertFalse(token.isExpired(currentTimeMillis = 199L))
    }

    @Test
    fun `isExpired is true at expiration time`() {
        val token = SecureToken(
            value = "token",
            expiresAtEpochMillis = 200L
        )

        assertTrue(token.isExpired(currentTimeMillis = 200L))
    }
}
