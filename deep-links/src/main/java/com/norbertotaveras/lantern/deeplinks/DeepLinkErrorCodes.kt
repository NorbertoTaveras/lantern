package com.norbertotaveras.lantern.deeplinks

/**
 * Stable error codes returned by deep-link APIs.
 */
object DeepLinkErrorCodes {
    const val INVALID_URI = "deep_link_invalid_uri"
    const val INVALID_SCHEME = "deep_link_invalid_scheme"
    const val INVALID_HOST = "deep_link_invalid_host"
    const val MISSING_INTENT_URI = "deep_link_missing_intent_uri"
}
