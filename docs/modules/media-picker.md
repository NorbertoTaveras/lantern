# Media Picker

`media-picker` wraps Android Photo Picker requests and results in typed SDK models.

```kotlin
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-media-picker:$mobileFoundationVersion")
```

## Use It For

- Requesting images, videos, or mixed media.
- Choosing single or multiple selection.
- Mapping Android picker results into `PickedMediaItem`.
- Keeping picker request/result modeling separate from UI code.

## Basic Request

```kotlin
val request = MediaPickRequest(
    mediaTypes = setOf(MediaType.Image),
    selectionMode = MediaSelectionMode.Single
)

val result = mediaPicker.pick(request)
```

## Android Photo Picker

The Android implementation uses app-provided launcher wiring:

```kotlin
val picker = AndroidPhotoPickerMediaPicker(launcher)
```

Activity Result registration stays in the app because launchers are lifecycle-bound.

## Boundaries

The SDK does not own image loading, thumbnails, upload behavior, or Compose/View UI. It models and launches picker requests through app-owned lifecycle components.
