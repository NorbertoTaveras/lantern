package com.norbertotaveras.lantern.network.okhttp

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adds an authorization header when [tokenProvider] returns a non-blank access token.
 *
 * Existing authorization headers are preserved by default so callers can override auth per request.
 */
class AuthHeaderInterceptor(
    private val tokenProvider: TokenProvider,
    private val headerName: String = DEFAULT_AUTH_HEADER,
    private val scheme: String = DEFAULT_AUTH_SCHEME,
    private val replaceExisting: Boolean = false
) : Interceptor {

    init {
        require(headerName.isNotBlank()) { "headerName cannot be blank." }
        require(headerName.isValidHeaderName()) { "headerName must be a valid HTTP header name." }
        require(scheme.isNotBlank()) { "scheme cannot be blank." }
        require(scheme.trim().isValidAuthScheme()) { "scheme must be a valid HTTP auth scheme." }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = tokenProvider.getAccessToken()?.trim()

        if (accessToken.isNullOrEmpty()) {
            return chain.proceed(request)
        }

        if (!replaceExisting && request.header(headerName) != null) {
            return chain.proceed(request)
        }

        val authorizationHeaderValue = "${scheme.trim()} $accessToken"
        if (!authorizationHeaderValue.isValidHeaderValue()) {
            return chain.proceed(request)
        }

        val authorizedRequest = request.newBuilder()
            .header(headerName, authorizationHeaderValue)
            .build()

        return chain.proceed(authorizedRequest)
    }

    companion object {
        const val DEFAULT_AUTH_HEADER = "Authorization"
        const val DEFAULT_AUTH_SCHEME = "Bearer"

        private val headerNamePattern = Regex("^[!#$%&'*+.^_`|~0-9A-Za-z-]+$")
        private val authSchemePattern = Regex("^[A-Za-z][A-Za-z0-9+.-]*$")
    }

    private fun String.isValidHeaderName(): Boolean {
        return headerNamePattern.matches(this)
    }

    private fun String.isValidAuthScheme(): Boolean {
        return authSchemePattern.matches(this)
    }

    private fun String.isValidHeaderValue(): Boolean {
        return all { character ->
            character == '\t' || !character.isISOControl()
        }
    }
}
