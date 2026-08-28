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

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.logging.NoOpSdkLogger
import com.norbertotaveras.lantern.logging.SdkLogger

/**
 * [AppVersionProvider] that reads the current app version from Android package info.
 */
class AndroidAppVersionProvider(
    context: Context,
    private val logger: SdkLogger = NoOpSdkLogger()
) : AppVersionProvider {
    private val appContext = context.applicationContext

    /**
     * Returns the current app version parsed from `versionName`.
     */
    override suspend fun getCurrentVersion(): SdkResult<AppVersion> {
        val packageName = appContext.packageName
        val packageInfo = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                appContext.packageManager.getPackageInfo(
                    packageName,
                    PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                appContext.packageManager.getPackageInfo(packageName, 0)
            }
        } catch (exception: PackageManager.NameNotFoundException) {
            logger.error("Unable to load package info for $packageName.", exception)
            return SdkResult.Failure(
                SdkError(
                    code = AppVersionErrorCodes.PACKAGE_INFO_UNAVAILABLE,
                    message = "Unable to load package info for $packageName.",
                    cause = exception
                )
            )
        }

        val versionName = packageInfo.versionName
        if (versionName.isNullOrBlank()) {
            return SdkResult.Failure(
                SdkError(
                    code = AppVersionErrorCodes.INVALID_VERSION,
                    message = "Package versionName is missing."
                )
            )
        }

        return AppVersion.parse(versionName)
    }
}
