package com.norbertotaveras.mobilefoundation.featureflags

import com.norbertotaveras.mobilefoundation.core.SdkResult
import kotlinx.coroutines.flow.Flow

interface FeatureFlagProvider {
    val updates: Flow<FeatureFlagSnapshot>

    suspend fun evaluate(flag: FeatureFlag): SdkResult<FeatureFlagEvaluation>

    suspend fun isEnabled(flag: FeatureFlag): SdkResult<Boolean> {
        return when (val result = evaluate(flag)) {
            is SdkResult.Failure -> result
            is SdkResult.Success -> SdkResult.Success(result.data.isEnabled())
        }
    }

    suspend fun getSnapshot(): SdkResult<FeatureFlagSnapshot>
}
