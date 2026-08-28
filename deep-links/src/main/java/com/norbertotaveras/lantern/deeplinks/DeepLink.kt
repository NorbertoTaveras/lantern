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
 * Parsed deep-link URI.
 */
data class DeepLink(
    val rawValue: String,
    val scheme: String,
    val host: String?,
    val pathSegments: List<String> = emptyList(),
    val queryParameters: Map<String, List<String>> = emptyMap(),
    val fragment: String? = null
) {
    /**
     * Returns the first query parameter value for [name], or `null` when absent.
     */
    fun firstQueryParameter(name: String): String? {
        return queryParameters[name]?.firstOrNull()
    }
}
