package com.norbertotaveras.mobilefoundation.network.okhttp

import java.util.concurrent.TimeUnit
import com.norbertotaveras.mobilefoundation.network.okhttp.internal.DefaultHeadersInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient

class OkHttpNetworkClientFactory(
    private val config: NetworkConfig = NetworkConfig()
) {

    fun create(
        tokenProvider: TokenProvider? = null,
        interceptors: List<Interceptor> = emptyList(),
        networkInterceptors: List<Interceptor> = emptyList()
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(config.connectTimeoutMillis, TimeUnit.MILLISECONDS)
            .readTimeout(config.readTimeoutMillis, TimeUnit.MILLISECONDS)
            .writeTimeout(config.writeTimeoutMillis, TimeUnit.MILLISECONDS)
            .callTimeout(config.callTimeoutMillis, TimeUnit.MILLISECONDS)
            .followRedirects(config.followRedirects)
            .followSslRedirects(config.followSslRedirects)
            .retryOnConnectionFailure(config.retryOnConnectionFailure)
            .apply {
                if (config.defaultHeaders.isNotEmpty()) {
                    addInterceptor(DefaultHeadersInterceptor(config.defaultHeaders))
                }
                if (tokenProvider != null) {
                    addInterceptor(AuthHeaderInterceptor(tokenProvider))
                }
                interceptors.forEach(::addInterceptor)
                networkInterceptors.forEach(::addNetworkInterceptor)
            }
            .build()
    }
}
