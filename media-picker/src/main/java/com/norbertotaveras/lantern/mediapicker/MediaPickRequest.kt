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

package com.norbertotaveras.lantern.mediapicker

import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.mediapicker.internal.MediaPickRequestValidator

/**
 * Request describing what media the picker should return.
 */
data class MediaPickRequest(
    /**
     * Media categories accepted by the picker.
     */
    val mediaTypes: Set<MediaType> = setOf(MediaType.Image),
    /**
     * Whether the caller expects one item or multiple items.
     */
    val selectionMode: MediaSelectionMode = MediaSelectionMode.Single,
    /**
     * Maximum number of items for multiple selection.
     */
    val maxItems: Int = 1,
    /**
     * Optional MIME type filters. A single MIME type maps to Android Photo Picker MIME filtering.
     */
    val mimeTypes: Set<MediaMimeType> = emptySet(),
    /**
     * Whether the caller wants persistable URI permission when supported by an implementation.
     */
    val requirePersistablePermission: Boolean = false
) {
    /**
     * Validates request constraints before launch.
     */
    fun validate(): SdkResult<MediaPickRequest> {
        return MediaPickRequestValidator.validate(this)
    }
}
