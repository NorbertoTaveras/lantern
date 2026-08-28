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
 * Stable error codes returned by media picker APIs.
 */
object MediaPickerErrorCodes {
    /**
     * Fallback code for unexpected picker failures.
     */
    const val UNKNOWN = "media_picker_unknown"
    /**
     * The picker request failed validation.
     */
    const val INVALID_REQUEST = "media_picker_invalid_request"
    /**
     * A MIME type failed validation.
     */
    const val INVALID_MIME_TYPE = "media_picker_invalid_mime_type"
    /**
     * The requested picker is unavailable.
     */
    const val PICKER_UNAVAILABLE = "media_picker_unavailable"
    /**
     * Required media permission was denied.
     */
    const val PERMISSION_DENIED = "media_picker_permission_denied"
    /**
     * Media selection failed while launching or awaiting a result.
     */
    const val SELECTION_FAILED = "media_picker_selection_failed"
    /**
     * Mapping a platform result failed.
     */
    const val RESULT_MAPPING_FAILED = "media_picker_result_mapping_failed"
}
