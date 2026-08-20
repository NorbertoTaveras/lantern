package com.norbertotaveras.mobilefoundation.network.okhttp

/**
 * Supplies access tokens for network requests without coupling the network module to an auth provider.
 */
interface TokenProvider {
    /**
     * Returns the current access token, or `null` when the request should be sent unauthenticated.
     *
     * Blank tokens are ignored by [AuthHeaderInterceptor].
     */
    fun getAccessToken(): String?
}
