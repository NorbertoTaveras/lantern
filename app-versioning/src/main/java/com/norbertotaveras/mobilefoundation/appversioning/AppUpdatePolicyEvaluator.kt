package com.norbertotaveras.mobilefoundation.appversioning

import com.norbertotaveras.mobilefoundation.core.SdkResult

/**
 * Evaluates a current app version against an update policy.
 */
interface AppUpdatePolicyEvaluator {
    /**
     * Returns the update state for [currentVersion] and [policy].
     */
    fun evaluate(
        currentVersion: AppVersion,
        policy: AppUpdatePolicy
    ): SdkResult<AppUpdateState>
}
