package com.norbertotaveras.lantern.analytics

import com.norbertotaveras.lantern.analytics.internal.AnalyticsNameValidator
import com.norbertotaveras.lantern.core.SdkResult

/**
 * Validated analytics user identifier.
 */
@JvmInline
value class AnalyticsUserId private constructor(val value: String) {
    companion object {
        /**
         * Creates an [AnalyticsUserId] after trimming and validating [value].
         */
        @JvmStatic
        fun from(value: String): SdkResult<AnalyticsUserId> {
            return when (val result = AnalyticsNameValidator.validateUserId(value)) {
                is SdkResult.Success -> SdkResult.Success(AnalyticsUserId(result.data))
                is SdkResult.Failure -> result
            }
        }

        /**
         * Creates an [AnalyticsUserId] without validation for trusted constants.
         */
        fun unsafe(value: String): AnalyticsUserId {
            return AnalyticsUserId(value)
        }
    }
}
