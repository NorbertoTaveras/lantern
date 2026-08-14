package com.norbertotaveras.mobilefoundation.mediapicker

import com.norbertotaveras.mobilefoundation.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaPickRequestTest {

    @Test
    fun validateAcceptsMultipleSelectionWithPositiveMaxItems() {
        val request = MediaPickRequest(
            mediaTypes = setOf(MediaType.Image, MediaType.Video),
            selectionMode = MediaSelectionMode.Multiple,
            maxItems = 3
        )

        val result = request.validate()

        assertTrue(result is SdkResult.Success)
        assertSame(request, (result as SdkResult.Success).data)
    }

    @Test
    fun validateAcceptsMimeTypesThatMatchRequestedMediaTypes() {
        val request = MediaPickRequest(
            mediaTypes = setOf(MediaType.Image, MediaType.Video),
            selectionMode = MediaSelectionMode.Multiple,
            maxItems = 2,
            mimeTypes = setOf(
                MediaMimeType.unsafe("image/jpeg"),
                MediaMimeType.unsafe("video/mp4")
            )
        )

        val result = request.validate()

        assertTrue(result is SdkResult.Success)
        assertSame(request, (result as SdkResult.Success).data)
    }

    @Test
    fun validateRejectsEmptyMediaTypes() {
        val result = MediaPickRequest(mediaTypes = emptySet()).validate()

        assertTrue(result is SdkResult.Failure)
        assertEquals(MediaPickerErrorCodes.INVALID_REQUEST, (result as SdkResult.Failure).error.code)
    }

    @Test
    fun validateRejectsSingleSelectionWithMultipleMaxItems() {
        val result = MediaPickRequest(
            selectionMode = MediaSelectionMode.Single,
            maxItems = 2
        ).validate()

        assertTrue(result is SdkResult.Failure)
        assertEquals(MediaPickerErrorCodes.INVALID_REQUEST, (result as SdkResult.Failure).error.code)
    }

    @Test
    fun validateRejectsMimeTypeThatDoesNotMatchRequestedMediaTypes() {
        val result = MediaPickRequest(
            mediaTypes = setOf(MediaType.Image),
            mimeTypes = setOf(MediaMimeType.unsafe("video/mp4"))
        ).validate()

        assertTrue(result is SdkResult.Failure)
        assertEquals(MediaPickerErrorCodes.INVALID_REQUEST, (result as SdkResult.Failure).error.code)
    }

    @Test
    fun validateRejectsUnsupportedMimeTypeCategory() {
        val result = MediaPickRequest(
            mediaTypes = setOf(MediaType.Image, MediaType.Video),
            mimeTypes = setOf(MediaMimeType.unsafe("application/pdf"))
        ).validate()

        assertTrue(result is SdkResult.Failure)
        assertEquals(MediaPickerErrorCodes.INVALID_REQUEST, (result as SdkResult.Failure).error.code)
    }
}
