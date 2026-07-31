package com.norbertotaveras.mobilefoundation.featureflags

import com.norbertotaveras.mobilefoundation.core.SdkResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StaticFeatureFlagProviderTest {

    @Test
    fun evaluateReturnsProviderValueWhenPresent() = runBlocking {
        val key = FeatureFlagKey.unsafe("new_checkout")
        val flag = FeatureFlag(key, defaultValue = FeatureFlagValue.BooleanValue(false))
        val provider = StaticFeatureFlagProvider(
            mapOf(key to FeatureFlagValue.BooleanValue(true))
        )

        val result = provider.evaluate(flag)

        assertTrue(result is SdkResult.Success)
        val evaluation = (result as SdkResult.Success).data
        assertTrue(evaluation.isEnabled())
        assertEquals(FeatureFlagValueSource.Provider, evaluation.source)
    }

    @Test
    fun evaluateFallsBackToFlagDefault() = runBlocking {
        val flag = FeatureFlag(
            FeatureFlagKey.unsafe("new_checkout"),
            defaultValue = FeatureFlagValue.BooleanValue(false)
        )
        val provider = StaticFeatureFlagProvider()

        val result = provider.evaluate(flag)

        assertTrue(result is SdkResult.Success)
        val evaluation = (result as SdkResult.Success).data
        assertFalse(evaluation.isEnabled())
        assertEquals(FeatureFlagValueSource.Default, evaluation.source)
    }
}
