package com.norbertotaveras.lantern.mediapicker.android

import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.mediapicker.MediaPickRequest
import com.norbertotaveras.lantern.mediapicker.MediaPicker
import com.norbertotaveras.lantern.mediapicker.MediaPickerErrorCodes
import com.norbertotaveras.lantern.mediapicker.MediaPickerResult

/**
 * [MediaPicker] implementation backed by Android Photo Picker.
 *
 * The consuming app owns Activity Result registration through [AndroidPhotoPickerLauncher].
 */
class AndroidPhotoPickerMediaPicker private constructor(
    private val launcher: AndroidPhotoPickerLauncher,
    private val requestMapper: AndroidPhotoPickerRequestMapper = AndroidPhotoPickerRequestMapper()
) : MediaPicker {

    /**
     * Creates a picker using an app-provided [launcher].
     */
    constructor(
        launcher: AndroidPhotoPickerLauncher
    ) : this(
        launcher = launcher,
        requestMapper = AndroidPhotoPickerRequestMapper()
    )

    override suspend fun pick(request: MediaPickRequest): SdkResult<MediaPickerResult> {
        return when (val validation = request.validate()) {
            is SdkResult.Failure -> validation
            is SdkResult.Success -> launchPicker(validation.data)
        }
    }

    private suspend fun launchPicker(request: MediaPickRequest): SdkResult<MediaPickerResult> {
        return try {
            SdkResult.Success(launcher.launch(requestMapper.map(request)))
        } catch (throwable: Throwable) {
            SdkResult.Failure(
                SdkError(
                    code = MediaPickerErrorCodes.SELECTION_FAILED,
                    message = throwable.localizedMessage ?: "Unable to launch Android Photo Picker.",
                    cause = throwable
                )
            )
        }
    }
}
