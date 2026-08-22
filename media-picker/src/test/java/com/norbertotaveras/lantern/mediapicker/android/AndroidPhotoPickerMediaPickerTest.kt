package com.norbertotaveras.lantern.mediapicker.android

import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.mediapicker.MediaPickRequest
import com.norbertotaveras.lantern.mediapicker.MediaPickerErrorCodes
import com.norbertotaveras.lantern.mediapicker.MediaPickerResult
import com.norbertotaveras.lantern.mediapicker.MediaPickerStatus
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AndroidPhotoPickerMediaPickerTest {

    @Test
    fun pickReturnsLauncherResultWhenRequestIsValid() = runBlocking {
        val picker = AndroidPhotoPickerMediaPicker(
            launcher = AndroidPhotoPickerLauncher {
                MediaPickerResult.Cancelled
            }
        )

        val result = picker.pick(MediaPickRequest())

        assertTrue(result is SdkResult.Success)
        assertEquals(MediaPickerStatus.Cancelled, (result as SdkResult.Success).data.status)
    }

    @Test
    fun pickReturnsValidationFailureBeforeLaunching() = runBlocking {
        var launchCount = 0
        val picker = AndroidPhotoPickerMediaPicker(
            launcher = AndroidPhotoPickerLauncher {
                launchCount += 1
                MediaPickerResult.Empty
            }
        )

        val result = picker.pick(MediaPickRequest(mediaTypes = emptySet()))

        assertTrue(result is SdkResult.Failure)
        assertEquals(MediaPickerErrorCodes.INVALID_REQUEST, (result as SdkResult.Failure).error.code)
        assertEquals(0, launchCount)
    }
}
