package com.norbertotaveras.mobilefoundation.appversioning

import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult

/**
 * Default [AppUpdatePolicyEvaluator] implementation.
 */
class DefaultAppUpdatePolicyEvaluator : AppUpdatePolicyEvaluator {
    override fun evaluate(
        currentVersion: AppVersion,
        policy: AppUpdatePolicy
    ): SdkResult<AppUpdateState> {
        val minimumSupportedVersion = policy.minimumSupportedVersion
        val latestVersion = policy.latestVersion

        if (
            minimumSupportedVersion != null &&
            latestVersion != null &&
            minimumSupportedVersion > latestVersion
        ) {
            return SdkResult.Failure(
                SdkError(
                    code = AppVersionErrorCodes.INVALID_POLICY,
                    message = "Minimum supported version cannot be newer than latest version."
                )
            )
        }

        val requirement = when {
            minimumSupportedVersion != null && currentVersion < minimumSupportedVersion -> {
                AppUpdateRequirement.ForceUpdate
            }
            latestVersion != null && currentVersion < latestVersion -> {
                AppUpdateRequirement.SoftUpdate
            }
            else -> AppUpdateRequirement.None
        }

        return SdkResult.Success(
            AppUpdateState(
                currentVersion = currentVersion,
                requirement = requirement,
                minimumSupportedVersion = minimumSupportedVersion,
                latestVersion = latestVersion
            )
        )
    }
}
