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

package com.norbertotaveras.lantern.backgroundwork.internal

import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkErrorCodes
import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkRequest
import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkType
import com.norbertotaveras.lantern.core.SdkError

internal object BackgroundWorkRequestValidator {
    fun validate(request: BackgroundWorkRequest): SdkError? {
        if (request.name.value.isBlank()) {
            return SdkError(
                code = BackgroundWorkErrorCodes.INVALID_WORK_NAME,
                message = "Background work name cannot be blank."
            )
        }

        if (request.initialDelayMillis < 0L) {
            return SdkError(
                code = BackgroundWorkErrorCodes.INVALID_INITIAL_DELAY,
                message = "Background work initial delay cannot be negative."
            )
        }

        val type = request.type
        if (type is BackgroundWorkType.Periodic && type.repeatIntervalMillis <= 0L) {
            return SdkError(
                code = BackgroundWorkErrorCodes.INVALID_INTERVAL,
                message = "Periodic background work interval must be greater than zero."
            )
        }

        return null
    }
}
