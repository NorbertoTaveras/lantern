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
