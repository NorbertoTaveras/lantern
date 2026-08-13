package com.norbertotaveras.mobilefoundation.analytics.firebase

import android.os.Bundle
import com.norbertotaveras.mobilefoundation.analytics.AnalyticsValue

object FirebaseAnalyticsValueMapper {
    fun toBundle(parameters: Map<String, AnalyticsValue>): Bundle {
        return Bundle().apply {
            parameters.forEach { (key, value) ->
                when (value) {
                    is AnalyticsValue.BooleanValue -> putString(key, value.value.toString())
                    is AnalyticsValue.DoubleValue -> putDouble(key, value.value)
                    is AnalyticsValue.LongValue -> putLong(key, value.value)
                    is AnalyticsValue.StringValue -> putString(key, value.value)
                }
            }
        }
    }

    fun toUserPropertyValue(value: AnalyticsValue): String {
        return when (value) {
            is AnalyticsValue.BooleanValue -> value.value.toString()
            is AnalyticsValue.DoubleValue -> value.value.toString()
            is AnalyticsValue.LongValue -> value.value.toString()
            is AnalyticsValue.StringValue -> value.value
        }
    }
}
