package com.norbertotaveras.lantern.securestorage

import org.junit.Assert.assertThrows
import org.junit.Test

class SecureStorageConfigTest {

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
