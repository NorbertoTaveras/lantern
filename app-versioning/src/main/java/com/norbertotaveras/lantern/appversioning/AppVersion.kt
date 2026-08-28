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
