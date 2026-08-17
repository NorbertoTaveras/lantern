package com.norbertotaveras.mobilefoundation.deeplinks

data class DeepLink(
    val rawValue: String,
    val scheme: String,
    val host: String?,
    val pathSegments: List<String> = emptyList(),
    val queryParameters: Map<String, List<String>> = emptyMap(),
    val fragment: String? = null
) {
    fun firstQueryParameter(name: String): String? {
        return queryParameters[name]?.firstOrNull()
    }
}
