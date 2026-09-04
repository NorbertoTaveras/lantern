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

package com.norbertotaveras.lantern.securestorage.internal

import com.norbertotaveras.lantern.securestorage.SecureStorageErrorCodes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class SecureStorageErrorMapperTest {

    private val mapper = SecureStorageErrorMapper()

    @Test
    fun `map returns read error for read operation`() {
        val throwable = IllegalStateException("Read failed")

        val error = mapper.map(SecureStorageErrorMapper.Operation.Read, throwable)

        assertEquals(SecureStorageErrorCodes.READ_FAILED, error.code)
        assertEquals("Unable to read secure storage value.", error.message)
        assertSame(throwable, error.cause)
    }

    @Test
    fun `map returns write error for write operation`() {
        val throwable = IllegalStateException("Write failed")

        val error = mapper.map(SecureStorageErrorMapper.Operation.Write, throwable)

        assertEquals(SecureStorageErrorCodes.WRITE_FAILED, error.code)
        assertEquals("Unable to write secure storage value.", error.message)
        assertSame(throwable, error.cause)
    }

    @Test
    fun `map returns remove error for remove operation`() {
        val throwable = IllegalStateException("Remove failed")

        val error = mapper.map(SecureStorageErrorMapper.Operation.Remove, throwable)

        assertEquals(SecureStorageErrorCodes.REMOVE_FAILED, error.code)
        assertEquals("Unable to remove secure storage value.", error.message)
        assertSame(throwable, error.cause)
    }

    @Test
    fun `map returns clear error for clear operation`() {
        val throwable = IllegalStateException("Clear failed")

        val error = mapper.map(SecureStorageErrorMapper.Operation.Clear, throwable)

        assertEquals(SecureStorageErrorCodes.CLEAR_FAILED, error.code)
        assertEquals("Unable to clear secure storage values.", error.message)
        assertSame(throwable, error.cause)
    }
}
