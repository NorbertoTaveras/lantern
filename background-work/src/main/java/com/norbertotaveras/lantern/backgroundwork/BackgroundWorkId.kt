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

package com.norbertotaveras.lantern.backgroundwork

import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult

/**
 * Identifier for enqueued background work.
 */
@JvmInline
value class BackgroundWorkId private constructor(val value: String) {
    companion object {
        /**
         * Creates a [BackgroundWorkId] after validating [value].
         */
        @JvmStatic
        fun from(value: String): SdkResult<BackgroundWorkId> {
            return if (value.isBlank()) {
                SdkResult.Failure(
                    SdkError(
                        code = BackgroundWorkErrorCodes.INVALID_WORK_ID,
                        message = "Background work id cannot be blank."
                    )
                )
            } else {
                SdkResult.Success(BackgroundWorkId(value))
            }
        }

        /**
         * Creates a [BackgroundWorkId] without validation for trusted constants.
         */
        fun unsafe(value: String): BackgroundWorkId {
            return BackgroundWorkId(value)
        }
    }
}
