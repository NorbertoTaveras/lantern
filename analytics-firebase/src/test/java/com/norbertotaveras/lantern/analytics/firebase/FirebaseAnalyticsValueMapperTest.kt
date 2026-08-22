package com.norbertotaveras.lantern.analytics.firebase

import com.norbertotaveras.lantern.analytics.AnalyticsValue
import org.junit.Assert.assertEquals
import org.junit.Test

class FirebaseAnalyticsValueMapperTest {
    @Test
    fun toUserPropertyValueConvertsValuesToFirebaseStrings() {
        assertEquals("true", FirebaseAnalyticsValueMapper.toUserPropertyValue(AnalyticsValue.BooleanValue(true)))
        assertEquals("12.5", FirebaseAnalyticsValueMapper.toUserPropertyValue(AnalyticsValue.DoubleValue(12.5)))
        assertEquals("42", FirebaseAnalyticsValueMapper.toUserPropertyValue(AnalyticsValue.LongValue(42)))
        assertEquals("demo", FirebaseAnalyticsValueMapper.toUserPropertyValue(AnalyticsValue.StringValue("demo")))
    }
}
