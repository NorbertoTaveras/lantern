package com.norbertotaveras.mobilefoundation.analytics

import com.norbertotaveras.mobilefoundation.analytics.internal.AnalyticsNameValidator
import com.norbertotaveras.mobilefoundation.core.SdkResult

@JvmInline
value class AnalyticsUserId private constructor(val value: String) {
    companion object {
        fun from(value: String): SdkResult<AnalyticsUserId> {
            return when (val result = AnalyticsNameValidator.validateUserId(value)) {
                is SdkResult.Success -> SdkResult.Success(AnalyticsUserId(result.data))
                is SdkResult.Failure -> result
            }
        }

        fun unsafe(value: String): AnalyticsUserId {
            return AnalyticsUserId(value)
        }
    }
}
