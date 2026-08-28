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

import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigDefaults
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigFetchStatus
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigKey
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigProvider
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigSnapshot
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteConfigFeatureFlagProviderTest {

    @Test
    fun evaluateMapsRemoteConfigValueToFeatureFlagValue() = runBlocking {
        val key = FeatureFlagKey.unsafe("new_checkout")
        val remoteConfigProvider = FakeRemoteConfigProvider(
            values = mapOf(RemoteConfigKey.unsafe(key.value) to RemoteConfigValue.BooleanValue(true))
        )
        val provider = RemoteConfigFeatureFlagProvider(remoteConfigProvider)

        val result = provider.evaluate(FeatureFlag(key))

        assertTrue(result is SdkResult.Success)
        val evaluation = (result as SdkResult.Success).data
        assertEquals(FeatureFlagValue.BooleanValue(true), evaluation.value)
        assertEquals(FeatureFlagValueSource.Provider, evaluation.source)
    }

    @Test
    fun getSnapshotMapsRemoteConfigSnapshotToFeatureFlagSnapshot() = runBlocking {
        val key = RemoteConfigKey.unsafe("welcome_message")
        val provider = RemoteConfigFeatureFlagProvider(
            FakeRemoteConfigProvider(
                values = mapOf(key to RemoteConfigValue.StringValue("hello"))
            )
        )

        val result = provider.getSnapshot()

        assertTrue(result is SdkResult.Success)
        val snapshot = (result as SdkResult.Success).data
        assertEquals(
            FeatureFlagValue.StringValue("hello"),
            snapshot.values[FeatureFlagKey.unsafe("welcome_message")]
        )
    }

    @Test
    fun evaluateFallsBackToFlagDefaultWhenRemoteValueIsMissing() = runBlocking {
        val provider = RemoteConfigFeatureFlagProvider(FakeRemoteConfigProvider(values = emptyMap()))
        val flag = FeatureFlag(
            key = FeatureFlagKey.unsafe("new_checkout"),
            defaultValue = FeatureFlagValue.BooleanValue(false)
        )

        val result = provider.evaluate(flag)

        assertTrue(result is SdkResult.Success)
        val evaluation = (result as SdkResult.Success).data
        assertEquals(FeatureFlagValue.BooleanValue(false), evaluation.value)
        assertEquals(FeatureFlagValueSource.Default, evaluation.source)
    }

    private class FakeRemoteConfigProvider(
        values: Map<RemoteConfigKey, RemoteConfigValue>
    ) : RemoteConfigProvider {
        private val snapshot = RemoteConfigSnapshot(values)

        override val updates: Flow<RemoteConfigSnapshot> = MutableStateFlow(snapshot)

        override suspend fun setDefaults(defaults: RemoteConfigDefaults): SdkResult<Unit> {
            return SdkResult.Success(Unit)
        }

        override suspend fun fetch(): SdkResult<RemoteConfigFetchStatus> {
            return SdkResult.Success(RemoteConfigFetchStatus.Success)
        }

        override suspend fun activate(): SdkResult<Boolean> {
            return SdkResult.Success(true)
        }

        override suspend fun getValue(key: RemoteConfigKey): SdkResult<RemoteConfigValue> {
            return snapshot.values[key]?.let { SdkResult.Success(it) }
                ?: SdkResult.Failure(SdkError(code = "missing_value", message = "Missing value."))
        }

        override suspend fun getSnapshot(): SdkResult<RemoteConfigSnapshot> {
            return SdkResult.Success(snapshot)
        }
    }
}
