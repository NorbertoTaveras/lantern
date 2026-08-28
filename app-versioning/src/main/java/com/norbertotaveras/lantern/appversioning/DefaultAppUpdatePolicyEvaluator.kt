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

package com.norbertotaveras.lantern.appversioning

import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult

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
