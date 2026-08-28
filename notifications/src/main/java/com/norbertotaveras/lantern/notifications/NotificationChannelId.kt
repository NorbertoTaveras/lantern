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

package com.norbertotaveras.lantern.notifications

import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.notifications.internal.NotificationChannelIdValidator

/**
 * Validated notification channel identifier.
 */
@JvmInline
value class NotificationChannelId private constructor(val value: String) {
    companion object {
        /**
         * Creates a [NotificationChannelId] after trimming and validating [value].
         */
        @JvmStatic
        fun from(value: String): SdkResult<NotificationChannelId> {
            return when (val result = NotificationChannelIdValidator.validate(value)) {
                is SdkResult.Success -> SdkResult.Success(NotificationChannelId(result.data))
                is SdkResult.Failure -> result
            }
        }

        /**
         * Creates a [NotificationChannelId] without validation for trusted constants.
         */
        fun unsafe(value: String): NotificationChannelId {
            return NotificationChannelId(value)
        }
    }
}
