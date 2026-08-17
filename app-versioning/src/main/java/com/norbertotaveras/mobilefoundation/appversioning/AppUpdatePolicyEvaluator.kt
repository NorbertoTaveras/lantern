package com.norbertotaveras.mobilefoundation.appversioning

import com.norbertotaveras.mobilefoundation.core.SdkResult

interface AppUpdatePolicyEvaluator {
    fun evaluate(
        currentVersion: AppVersion,
        policy: AppUpdatePolicy
    ): SdkResult<AppUpdateState>
}
