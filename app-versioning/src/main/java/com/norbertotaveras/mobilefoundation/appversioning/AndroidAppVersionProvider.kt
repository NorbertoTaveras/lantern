package com.norbertotaveras.mobilefoundation.appversioning

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.logging.NoOpSdkLogger
import com.norbertotaveras.mobilefoundation.logging.SdkLogger

class AndroidAppVersionProvider(
    context: Context,
    private val logger: SdkLogger = NoOpSdkLogger()
) : AppVersionProvider {
    private val appContext = context.applicationContext

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
