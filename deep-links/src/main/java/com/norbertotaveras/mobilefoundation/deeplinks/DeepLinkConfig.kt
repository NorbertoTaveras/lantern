package com.norbertotaveras.mobilefoundation.deeplinks

data class DeepLinkConfig(
    val allowedSchemes: Set<String> = emptySet(),
    val allowedHosts: Set<String> = emptySet()
) {
    fun allowsScheme(scheme: String): Boolean {
        return allowedSchemes.isEmpty() || allowedSchemes.any { it.equals(scheme, ignoreCase = true) }
    }

    fun allowsHost(host: String?): Boolean {
        return allowedHosts.isEmpty() || (host != null && allowedHosts.any { it.equals(host, ignoreCase = true) })
    }

    companion object {
        val AllowAny = DeepLinkConfig()
    }
}
