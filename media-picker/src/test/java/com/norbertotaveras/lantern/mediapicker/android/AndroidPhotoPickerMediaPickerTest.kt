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
