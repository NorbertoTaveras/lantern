package com.norbertotaveras.mobilefoundation.network.okhttp.internal

import okhttp3.Interceptor
import okhttp3.Response

internal class DefaultHeadersInterceptor(
    private val headers: Map<String, String>
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        headers.forEach { (name, value) ->
            if (request.header(name) == null) {
                requestBuilder.header(name, value)
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}
