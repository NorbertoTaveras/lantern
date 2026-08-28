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

package com.norbertotaveras.lantern.deeplinks

/**
 * Allow-list configuration used while parsing deep links.
 */
data class DeepLinkConfig(
    val allowedSchemes: Set<String> = emptySet(),
    val allowedHosts: Set<String> = emptySet()
) {
    /**
     * Returns true when [scheme] is allowed, or when no scheme allow-list is configured.
     */
    fun allowsScheme(scheme: String): Boolean {
        return allowedSchemes.isEmpty() || allowedSchemes.any { it.equals(scheme, ignoreCase = true) }
    }

    /**
     * Returns true when [host] is allowed, or when no host allow-list is configured.
     */
    fun allowsHost(host: String?): Boolean {
        return allowedHosts.isEmpty() || (host != null && allowedHosts.any { it.equals(host, ignoreCase = true) })
    }

    companion object {
        /**
         * Configuration that accepts any scheme and host.
         */
        val AllowAny = DeepLinkConfig()
    }
}
