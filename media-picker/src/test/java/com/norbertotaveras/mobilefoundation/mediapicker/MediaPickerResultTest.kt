package com.norbertotaveras.mobilefoundation.mediapicker

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaPickerResultTest {

    @Test
    fun hasSelectionIsTrueOnlyForSelectedItems() {
        val result = MediaPickerResult(
            items = listOf(
                PickedMediaItem(
                    uri = "content://media/image/1",
                    mediaType = MediaType.Image
                )
            )
        )

        assertTrue(result.hasSelection)
    }

    @Test
    fun cancelledDoesNotHaveSelection() {
        assertFalse(MediaPickerResult.Cancelled.hasSelection)
    }
}
