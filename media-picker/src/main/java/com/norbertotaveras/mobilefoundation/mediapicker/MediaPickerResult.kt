package com.norbertotaveras.mobilefoundation.mediapicker

data class MediaPickerResult(
    val items: List<PickedMediaItem>,
    val status: MediaPickerStatus = if (items.isEmpty()) MediaPickerStatus.Empty else MediaPickerStatus.Selected
) {
    val hasSelection: Boolean = items.isNotEmpty() && status == MediaPickerStatus.Selected

    companion object {
        val Empty = MediaPickerResult(items = emptyList())
        val Cancelled = MediaPickerResult(items = emptyList(), status = MediaPickerStatus.Cancelled)
    }
}
