package com.norbertotaveras.lantern.mediapicker.android

import com.norbertotaveras.lantern.mediapicker.MediaMimeType
import com.norbertotaveras.lantern.mediapicker.MediaPickRequest
import com.norbertotaveras.lantern.mediapicker.MediaSelectionMode
import com.norbertotaveras.lantern.mediapicker.MediaType
import com.norbertotaveras.lantern.mediapicker.android.internal.AndroidPhotoPickerRequestMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class AndroidPhotoPickerRequestMapperTest {

    private val mapper = AndroidPhotoPickerRequestMapper()

    @Test
    fun mapUsesMultipleContractForMultipleSelection() {
        val request = MediaPickRequest(
            mediaTypes = setOf(MediaType.Image, MediaType.Video),
            selectionMode = MediaSelectionMode.Multiple,
            maxItems = 4
        )

        val mapped = mapper.map(request)

        assertEquals(AndroidPhotoPickerContractType.Multiple, mapped.contractType)
        assertEquals(4, mapped.maxItems)
        assertEquals(request, mapped.sourceRequest)
    }

    @Test
    fun mapUsesSingleContractForSingleSelection() {
        val request = MediaPickRequest(
            mediaTypes = setOf(MediaType.Image),
            mimeTypes = setOf(MediaMimeType.unsafe("image/png"))
        )

        val mapped = mapper.map(request)

        assertEquals(AndroidPhotoPickerContractType.Single, mapped.contractType)
        assertEquals(1, mapped.maxItems)
        assertEquals(request, mapped.sourceRequest)
    }
}
