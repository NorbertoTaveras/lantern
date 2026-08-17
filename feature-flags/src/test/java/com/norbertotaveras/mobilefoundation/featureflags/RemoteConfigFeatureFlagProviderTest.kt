package com.norbertotaveras.mobilefoundation.featureflags

import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigDefaults
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigFetchStatus
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigKey
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigProvider
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigSnapshot
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigValue
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
