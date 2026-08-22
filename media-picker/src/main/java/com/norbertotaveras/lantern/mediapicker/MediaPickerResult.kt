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
