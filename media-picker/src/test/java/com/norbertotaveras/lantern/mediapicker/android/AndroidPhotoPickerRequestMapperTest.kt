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
