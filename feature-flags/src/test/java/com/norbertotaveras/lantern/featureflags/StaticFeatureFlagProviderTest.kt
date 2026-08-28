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

package com.norbertotaveras.lantern.featureflags

import com.norbertotaveras.lantern.core.SdkResult
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
