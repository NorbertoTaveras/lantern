package com.norbertotaveras.mobilefoundation.mediapicker

/**
 * Status of a media picker result.
 */
enum class MediaPickerStatus {
    /**
     * One or more items were selected.
     */
    Selected,
    /**
     * Picker completed without selected items.
     */
    Empty,
    /**
     * Picker was cancelled.
     */
    Cancelled
}
