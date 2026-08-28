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
 * Result returned by a media picker.
 */
data class MediaPickerResult(
    /**
     * Selected media items.
     */
    val items: List<PickedMediaItem>,
    /**
     * Selection status.
     */
    val status: MediaPickerStatus = if (items.isEmpty()) MediaPickerStatus.Empty else MediaPickerStatus.Selected
) {
    /**
     * True when the result contains selected items and has [MediaPickerStatus.Selected].
     */
    val hasSelection: Boolean = items.isNotEmpty() && status == MediaPickerStatus.Selected

    companion object {
        /**
         * Empty result for a picker that completed without selected items.
         */
        val Empty = MediaPickerResult(items = emptyList())
        /**
         * Result for a picker that was cancelled by the user or platform.
         */
        val Cancelled = MediaPickerResult(items = emptyList(), status = MediaPickerStatus.Cancelled)
    }
}
