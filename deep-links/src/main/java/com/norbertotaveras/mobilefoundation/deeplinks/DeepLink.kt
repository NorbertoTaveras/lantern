package com.norbertotaveras.mobilefoundation.deeplinks

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
