package com.norbertotaveras.lantern.appversioning

import com.norbertotaveras.lantern.appversioning.internal.AppVersionParser
import com.norbertotaveras.lantern.core.SdkResult

/**
 * Semantic app version with optional qualifier.
 */
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

    /**
     * Compares major, minor, and patch values. Qualifiers do not affect ordering.
     */
    override fun compareTo(other: AppVersion): Int {
        return compareValuesBy(this, other, AppVersion::major, AppVersion::minor, AppVersion::patch)
    }

    override fun toString(): String {
        val baseVersion = "$major.$minor.$patch"
        return if (qualifier == null) baseVersion else "$baseVersion-$qualifier"
    }

    companion object {
        /**
         * Parses a version string in `major.minor.patch` or `major.minor.patch-qualifier` form.
         */
        @JvmStatic
        fun parse(value: String): SdkResult<AppVersion> {
            return AppVersionParser.parse(value)
        }
    }
}
