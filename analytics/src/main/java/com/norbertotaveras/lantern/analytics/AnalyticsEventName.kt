package com.norbertotaveras.lantern.analytics

import com.norbertotaveras.lantern.analytics.internal.AnalyticsNameValidator
import com.norbertotaveras.lantern.core.SdkResult

/**
 * Validated analytics event name.
 */
@JvmInline
value class AnalyticsEventName private constructor(val value: String) {
    companion object {
        /**
         * Creates an [AnalyticsEventName] after trimming and validating [value].
         */
        @JvmStatic
        fun from(value: String): SdkResult<AnalyticsEventName> {
            return when (val result = AnalyticsNameValidator.validateEventName(value)) {
                is SdkResult.Success -> SdkResult.Success(AnalyticsEventName(result.data))
                is SdkResult.Failure -> result
            }
        }

        /**
         * Creates an [AnalyticsEventName] without validation for trusted constants.
         */
        fun unsafe(value: String): AnalyticsEventName {
            return AnalyticsEventName(value)
        }
    }
}
