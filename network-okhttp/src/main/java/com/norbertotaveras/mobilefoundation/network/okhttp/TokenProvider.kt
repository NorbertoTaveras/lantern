package com.norbertotaveras.mobilefoundation.network.okhttp

/**
 * Supplies access tokens for network requests without coupling the network module to an auth provider.
 */
interface TokenProvider {
    fun getAccessToken(): String?
}
