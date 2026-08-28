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

import androidx.activity.result.PickVisualMediaRequest
import com.norbertotaveras.lantern.mediapicker.MediaPickRequest

/**
 * Android Photo Picker request derived from a provider-neutral [MediaPickRequest].
 */
data class AndroidPhotoPickerRequest(
    /**
     * Original provider-neutral request.
     */
    val sourceRequest: MediaPickRequest,
    /**
     * Activity Result contract type required for the request.
     */
    val contractType: AndroidPhotoPickerContractType,
    /**
     * AndroidX visual media request.
     */
    val visualMediaRequest: PickVisualMediaRequest,
    /**
     * Maximum number of items requested.
     */
    val maxItems: Int
)
