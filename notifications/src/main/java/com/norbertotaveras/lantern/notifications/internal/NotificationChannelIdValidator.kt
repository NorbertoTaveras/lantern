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

package com.norbertotaveras.lantern.notifications.internal

import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.notifications.NotificationErrorCodes

internal object NotificationChannelIdValidator {
    private val channelIdPattern = Regex("^[A-Za-z][A-Za-z0-9_.-]{0,127}$")

    fun validate(value: String): SdkResult<String> {
        val normalized = value.trim()
        if (channelIdPattern.matches(normalized)) {
            return SdkResult.Success(normalized)
        }

        return SdkResult.Failure(
            SdkError(
                code = NotificationErrorCodes.INVALID_CHANNEL_ID,
                message = "Notification channel id must start with a letter and contain only letters, numbers, '_', '-', or '.'."
            )
        )
    }
}
