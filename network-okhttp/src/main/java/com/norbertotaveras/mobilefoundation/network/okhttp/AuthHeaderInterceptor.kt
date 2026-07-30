package com.norbertotaveras.mobilefoundation.network.okhttp

import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor(
    private val tokenProvider: TokenProvider,
    private val headerName: String = DEFAULT_AUTH_HEADER,
    private val scheme: String = DEFAULT_AUTH_SCHEME,
    private val replaceExisting: Boolean = false
) : Interceptor {

    init {
        require(headerName.isNotBlank()) { "headerName cannot be blank." }
        require(scheme.isNotBlank()) { "scheme cannot be blank." }
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

        val authorizedRequest = request.newBuilder()
            .header(headerName, "${scheme.trim()} $accessToken")
            .build()

        return chain.proceed(authorizedRequest)
    }

    companion object {
        const val DEFAULT_AUTH_HEADER = "Authorization"
        const val DEFAULT_AUTH_SCHEME = "Bearer"
    }
}
