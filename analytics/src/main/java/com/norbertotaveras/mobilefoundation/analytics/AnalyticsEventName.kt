package com.norbertotaveras.mobilefoundation.analytics

import com.norbertotaveras.mobilefoundation.analytics.internal.AnalyticsNameValidator
import com.norbertotaveras.mobilefoundation.core.SdkResult

@JvmInline
value class AnalyticsEventName private constructor(val value: String) {
    companion object {
        fun from(value: String): SdkResult<AnalyticsEventName> {
            return when (val result = AnalyticsNameValidator.validateEventName(value)) {
                is SdkResult.Success -> SdkResult.Success(AnalyticsEventName(result.data))
                is SdkResult.Failure -> result
            }
        }

        fun unsafe(value: String): AnalyticsEventName {
            return AnalyticsEventName(value)
        }
    }
}
