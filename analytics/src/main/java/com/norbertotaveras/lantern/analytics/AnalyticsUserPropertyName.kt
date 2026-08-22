package com.norbertotaveras.lantern.analytics

import com.norbertotaveras.lantern.analytics.internal.AnalyticsNameValidator
import com.norbertotaveras.lantern.core.SdkResult

/**
 * Validated analytics user property name.
 */
@JvmInline
value class AnalyticsUserPropertyName private constructor(val value: String) {
    companion object {
        /**
         * Creates an [AnalyticsUserPropertyName] after trimming and validating [value].
         */
        @JvmStatic
        fun from(value: String): SdkResult<AnalyticsUserPropertyName> {
            return when (val result = AnalyticsNameValidator.validatePropertyName(value)) {
                is SdkResult.Success -> SdkResult.Success(AnalyticsUserPropertyName(result.data))
                is SdkResult.Failure -> result
            }
        }

        /**
         * Creates an [AnalyticsUserPropertyName] without validation for trusted constants.
         */
        fun unsafe(value: String): AnalyticsUserPropertyName {
            return AnalyticsUserPropertyName(value)
        }
    }
}
