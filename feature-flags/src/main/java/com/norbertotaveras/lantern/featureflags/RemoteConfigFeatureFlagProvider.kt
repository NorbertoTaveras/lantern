package com.norbertotaveras.lantern.featureflags

import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigProvider
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Feature flag provider backed by [RemoteConfigProvider].
 *
 * Remote config keys are matched to feature flag keys by their raw string value.
 */
class RemoteConfigFeatureFlagProvider(
    private val remoteConfigProvider: RemoteConfigProvider
) : FeatureFlagProvider {

    override val updates: Flow<FeatureFlagSnapshot> = remoteConfigProvider.updates.map { snapshot ->
        FeatureFlagSnapshot(
            values = snapshot.values.mapKeys { (key, _) ->
                FeatureFlagKey.unsafe(key.value)
            }.mapValues { (_, value) ->
                when (value) {
                    is RemoteConfigValue.BooleanValue -> FeatureFlagValue.BooleanValue(value.value)
                    is RemoteConfigValue.DoubleValue -> FeatureFlagValue.DoubleValue(value.value)
                    is RemoteConfigValue.LongValue -> FeatureFlagValue.LongValue(value.value)
                    is RemoteConfigValue.StringValue -> FeatureFlagValue.StringValue(value.value)
                }
            }
        )
    }

    override suspend fun evaluate(flag: FeatureFlag): SdkResult<FeatureFlagEvaluation> {
        return when (val result = remoteConfigProvider.getSnapshot()) {
            is SdkResult.Failure -> result
            is SdkResult.Success -> {
                val remoteValue = result.data.values.entries.firstOrNull { (key, _) ->
                    key.value == flag.key.value
                }?.value
                val value = when (remoteValue) {
                    is RemoteConfigValue.BooleanValue -> FeatureFlagValue.BooleanValue(remoteValue.value)
                    is RemoteConfigValue.DoubleValue -> FeatureFlagValue.DoubleValue(remoteValue.value)
                    is RemoteConfigValue.LongValue -> FeatureFlagValue.LongValue(remoteValue.value)
                    is RemoteConfigValue.StringValue -> FeatureFlagValue.StringValue(remoteValue.value)
                    null -> flag.defaultValue
                }
                SdkResult.Success(
                    FeatureFlagEvaluation(
                        flag = flag,
                        value = value,
                        source = if (remoteValue == null) {
                            FeatureFlagValueSource.Default
                        } else {
                            FeatureFlagValueSource.Provider
                        }
                    )
                )
            }
        }
    }

    override suspend fun getSnapshot(): SdkResult<FeatureFlagSnapshot> {
        return when (val result = remoteConfigProvider.getSnapshot()) {
            is SdkResult.Failure -> result
            is SdkResult.Success -> SdkResult.Success(
                FeatureFlagSnapshot(
                    values = result.data.values.mapKeys { (key, _) ->
                        FeatureFlagKey.unsafe(key.value)
                    }.mapValues { (_, value) ->
                        when (value) {
                            is RemoteConfigValue.BooleanValue -> FeatureFlagValue.BooleanValue(value.value)
                            is RemoteConfigValue.DoubleValue -> FeatureFlagValue.DoubleValue(value.value)
                            is RemoteConfigValue.LongValue -> FeatureFlagValue.LongValue(value.value)
                            is RemoteConfigValue.StringValue -> FeatureFlagValue.StringValue(value.value)
                        }
                    }
                )
            )
        }
    }
}
