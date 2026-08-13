package com.norbertotaveras.mobilefoundation.appversioning

import com.norbertotaveras.mobilefoundation.appversioning.internal.AppVersionParser
import com.norbertotaveras.mobilefoundation.core.SdkResult

data class AppVersion(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val qualifier: String? = null
) : Comparable<AppVersion> {
    init {
        require(major >= 0) { "Major version cannot be negative." }
        require(minor >= 0) { "Minor version cannot be negative." }
        require(patch >= 0) { "Patch version cannot be negative." }
        require(qualifier?.isNotBlank() ?: true) { "Version qualifier cannot be blank." }
    }

    override fun compareTo(other: AppVersion): Int {
        return compareValuesBy(this, other, AppVersion::major, AppVersion::minor, AppVersion::patch)
    }

    override fun toString(): String {
        val baseVersion = "$major.$minor.$patch"
        return if (qualifier == null) baseVersion else "$baseVersion-$qualifier"
    }

    companion object {
        @JvmStatic
        fun parse(value: String): SdkResult<AppVersion> {
            return AppVersionParser.parse(value)
        }
    }
}
