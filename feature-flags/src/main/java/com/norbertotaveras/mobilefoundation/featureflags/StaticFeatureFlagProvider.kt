package com.norbertotaveras.mobilefoundation.featureflags

import com.norbertotaveras.mobilefoundation.core.SdkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * In-memory [FeatureFlagProvider] useful for tests, demos, and local-only flag sets.
 */
class StaticFeatureFlagProvider(
    initialValues: Map<FeatureFlagKey, FeatureFlagValue> = emptyMap()
) : FeatureFlagProvider {
    private val snapshotState = MutableStateFlow(FeatureFlagSnapshot(initialValues))

    override val updates: Flow<FeatureFlagSnapshot> = snapshotState.asStateFlow()

    /**
     * Replaces the active in-memory values and emits a new snapshot.
     */
    fun update(values: Map<FeatureFlagKey, FeatureFlagValue>) {
        snapshotState.value = FeatureFlagSnapshot(values)
    }

    override suspend fun evaluate(flag: FeatureFlag): SdkResult<FeatureFlagEvaluation> {
        val value = snapshotState.value.values[flag.key]
        return SdkResult.Success(
            FeatureFlagEvaluation(
                flag = flag,
                value = value ?: flag.defaultValue,
                source = if (value == null) FeatureFlagValueSource.Default else FeatureFlagValueSource.Provider
            )
        )
    }

    override suspend fun getSnapshot(): SdkResult<FeatureFlagSnapshot> {
        return SdkResult.Success(snapshotState.value)
    }
}
