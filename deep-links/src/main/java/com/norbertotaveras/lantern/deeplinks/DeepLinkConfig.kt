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
