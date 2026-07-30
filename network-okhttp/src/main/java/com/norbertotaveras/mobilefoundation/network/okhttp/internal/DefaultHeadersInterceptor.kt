package com.norbertotaveras.mobilefoundation.network.okhttp.internal

import okhttp3.Interceptor
import okhttp3.Response

internal class DefaultHeadersInterceptor(
    private val headers: Map<String, String>
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        headers.forEach { (name, value) ->
            requestBuilder.header(name, value)
        }

        return chain.proceed(requestBuilder.build())
    }
}
