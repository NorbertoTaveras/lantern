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

/**
 * Normalized media item selected by a picker.
 */
data class PickedMediaItem(
    /**
     * String representation of the selected item URI.
     */
    val uri: String,
    /**
     * Best-known media category for the item.
     */
    val mediaType: MediaType,
    /**
     * Best-known MIME type for the item, when available.
     */
    val mimeType: MediaMimeType? = null,
    /**
     * Display name for the selected item, when available.
     */
    val displayName: String? = null,
    /**
     * File size in bytes, when available.
     */
    val sizeBytes: Long? = null,
    /**
     * Optional lightweight platform or provider metadata.
     */
    val metadata: Map<String, String> = emptyMap()
)
