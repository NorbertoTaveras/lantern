package com.norbertotaveras.mobilefoundation.mediapicker

/**
 * Selection cardinality requested from a [MediaPicker].
 */
enum class MediaSelectionMode {
    /**
     * Select one item.
     */
    Single,
    /**
     * Select multiple items up to [MediaPickRequest.maxItems].
     */
    Multiple
}
