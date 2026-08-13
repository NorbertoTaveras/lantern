package com.norbertotaveras.mobilefoundation.analytics

import com.norbertotaveras.mobilefoundation.analytics.internal.AnalyticsNameValidator
import com.norbertotaveras.mobilefoundation.core.SdkResult

@JvmInline
value class AnalyticsUserPropertyName private constructor(val value: String) {
    companion object {
        @JvmStatic
        fun from(value: String): SdkResult<AnalyticsUserPropertyName> {
            return when (val result = AnalyticsNameValidator.validatePropertyName(value)) {
                is SdkResult.Success -> SdkResult.Success(AnalyticsUserPropertyName(result.data))
                is SdkResult.Failure -> result
            }
        }

        fun unsafe(value: String): AnalyticsUserPropertyName {
            return AnalyticsUserPropertyName(value)
        }
    }
}
