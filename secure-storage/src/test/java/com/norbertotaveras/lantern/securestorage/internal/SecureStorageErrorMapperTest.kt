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
        assertEquals("Read failed", error.message)
        assertSame(throwable, error.cause)
    }

    @Test
    fun `map returns write error for write operation`() {
        val throwable = IllegalStateException("Write failed")

        val error = mapper.map(SecureStorageErrorMapper.Operation.Write, throwable)

        assertEquals(SecureStorageErrorCodes.WRITE_FAILED, error.code)
        assertEquals("Write failed", error.message)
        assertSame(throwable, error.cause)
    }

    @Test
    fun `map returns remove error for remove operation`() {
        val throwable = IllegalStateException("Remove failed")

        val error = mapper.map(SecureStorageErrorMapper.Operation.Remove, throwable)

        assertEquals(SecureStorageErrorCodes.REMOVE_FAILED, error.code)
        assertEquals("Remove failed", error.message)
        assertSame(throwable, error.cause)
    }

    @Test
    fun `map returns clear error for clear operation`() {
        val throwable = IllegalStateException("Clear failed")

        val error = mapper.map(SecureStorageErrorMapper.Operation.Clear, throwable)

        assertEquals(SecureStorageErrorCodes.CLEAR_FAILED, error.code)
        assertEquals("Clear failed", error.message)
        assertSame(throwable, error.cause)
    }
}
