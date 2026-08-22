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
