package com.norbertotaveras.lantern.mediapicker

import com.norbertotaveras.lantern.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaMimeTypeTest {

    @Test
    fun fromReturnsNormalizedMimeTypeForValidValue() {
        val result = MediaMimeType.from(" Image/JPEG ")

        assertTrue(result is SdkResult.Success)
        assertEquals("image/jpeg", (result as SdkResult.Success).data.value)
    }

    @Test
    fun fromRejectsInvalidMimeType() {
        val result = MediaMimeType.from("image")

        assertTrue(result is SdkResult.Failure)
        assertEquals(MediaPickerErrorCodes.INVALID_MIME_TYPE, (result as SdkResult.Failure).error.code)
    }
}
