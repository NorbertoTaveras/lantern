package com.norbertotaveras.lantern.appversioning.internal

import com.norbertotaveras.lantern.appversioning.AppVersion
import com.norbertotaveras.lantern.appversioning.AppVersionErrorCodes
import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult

internal object AppVersionParser {
    private val versionRegex = Regex("""^(\d+)\.(\d+)\.(\d+)(?:-([A-Za-z0-9][A-Za-z0-9._-]*))?$""")

    fun parse(value: String): SdkResult<AppVersion> {
        val normalizedValue = value.trim()
        val match = versionRegex.matchEntire(normalizedValue)
            ?: return invalidVersion("App version must use major.minor.patch format.")

        val major = match.groupValues[1].toIntOrNull()
        val minor = match.groupValues[2].toIntOrNull()
        val patch = match.groupValues[3].toIntOrNull()
        if (major == null || minor == null || patch == null) {
            return invalidVersion("App version number is too large.")
        }

        return SdkResult.Success(
            AppVersion(
                major = major,
                minor = minor,
                patch = patch,
                qualifier = match.groupValues[4].ifBlank { null }
            )
        )
    }

    private fun invalidVersion(message: String): SdkResult.Failure {
        return SdkResult.Failure(
            SdkError(
                code = AppVersionErrorCodes.INVALID_VERSION,
                message = message
            )
        )
    }
}
