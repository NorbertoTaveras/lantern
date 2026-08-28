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

package com.norbertotaveras.lantern.analytics.internal

import com.norbertotaveras.lantern.analytics.AnalyticsErrorCodes
import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult

internal object AnalyticsNameValidator {
    private const val MAX_EVENT_NAME_LENGTH = 64
    private const val MAX_PROPERTY_NAME_LENGTH = 64
    private const val MAX_USER_ID_LENGTH = 128
    private val namePattern = Regex("^[A-Za-z][A-Za-z0-9_]*$")

    fun validateEventName(value: String): SdkResult<String> {
        return validate(
            value = value,
            maxLength = MAX_EVENT_NAME_LENGTH,
            errorCode = AnalyticsErrorCodes.INVALID_EVENT_NAME,
            label = "Analytics event name"
        )
    }

    fun validatePropertyName(value: String): SdkResult<String> {
        return validate(
            value = value,
            maxLength = MAX_PROPERTY_NAME_LENGTH,
            errorCode = AnalyticsErrorCodes.INVALID_PROPERTY_NAME,
            label = "Analytics user property name"
        )
    }

    fun validateUserId(value: String): SdkResult<String> {
        val normalizedValue = value.trim()
        return when {
            normalizedValue.isEmpty() -> SdkResult.Failure(
                SdkError(
                    code = AnalyticsErrorCodes.INVALID_USER_ID,
                    message = "Analytics user ID cannot be blank."
                )
            )

            normalizedValue.length > MAX_USER_ID_LENGTH -> SdkResult.Failure(
                SdkError(
                    code = AnalyticsErrorCodes.INVALID_USER_ID,
                    message = "Analytics user ID cannot exceed $MAX_USER_ID_LENGTH characters."
                )
            )

            else -> SdkResult.Success(normalizedValue)
        }
    }

    private fun validate(
        value: String,
        maxLength: Int,
        errorCode: String,
        label: String
    ): SdkResult<String> {
        val normalizedValue = value.trim()
        return when {
            normalizedValue.isEmpty() -> SdkResult.Failure(
                SdkError(
                    code = errorCode,
                    message = "$label cannot be blank."
                )
            )

            normalizedValue.length > maxLength -> SdkResult.Failure(
                SdkError(
                    code = errorCode,
                    message = "$label cannot exceed $maxLength characters."
                )
            )

            !namePattern.matches(normalizedValue) -> SdkResult.Failure(
                SdkError(
                    code = errorCode,
                    message = "$label must start with a letter and contain only letters, numbers, and underscores."
                )
            )

            else -> SdkResult.Success(normalizedValue)
        }
    }
}
