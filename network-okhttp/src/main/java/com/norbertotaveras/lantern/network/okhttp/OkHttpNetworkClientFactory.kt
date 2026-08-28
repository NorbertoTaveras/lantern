/*
 * Copyright (C) 2026 Norberto Taveras
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.norbertotaveras.lantern.network.okhttp

import java.util.concurrent.TimeUnit
import com.norbertotaveras.lantern.logging.SdkLogger
import com.norbertotaveras.lantern.network.okhttp.internal.DefaultHeadersInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient

/**
 * Creates OkHttp clients configured with Lantern defaults and optional SDK interceptors.
 */
class OkHttpNetworkClientFactory(
    private val config: NetworkConfig = NetworkConfig()
) {

    /**
     * Creates an [OkHttpClient] with configured timeouts, headers, auth, retry, and custom interceptors.
     *
     * Interceptors are added after Lantern default headers, auth, and retry interceptors.
     */
    fun create(
        tokenProvider: TokenProvider? = null,
        retryConfig: NetworkRetryConfig? = null,
        interceptors: List<Interceptor> = emptyList(),
        networkInterceptors: List<Interceptor> = emptyList()
    ): OkHttpClient {
        return createInternal(
            tokenProvider = tokenProvider,
            retryConfig = retryConfig,
            logger = null,
            loggingLevel = NetworkLoggingLevel.None,
            interceptors = interceptors,
            networkInterceptors = networkInterceptors
        )
    }

    /**
     * Creates an [OkHttpClient] with SDK network logging enabled.
     *
     * Request and response bodies are not logged. Sensitive headers are redacted by
     * [NetworkLoggingInterceptor].
     */
    fun createWithLogging(
        logger: SdkLogger,
        loggingLevel: NetworkLoggingLevel = NetworkLoggingLevel.Basic,
        tokenProvider: TokenProvider? = null,
        retryConfig: NetworkRetryConfig? = null,
        interceptors: List<Interceptor> = emptyList(),
        networkInterceptors: List<Interceptor> = emptyList()
    ): OkHttpClient {
        return createInternal(
            tokenProvider = tokenProvider,
            retryConfig = retryConfig,
            logger = logger,
            loggingLevel = loggingLevel,
            interceptors = interceptors,
            networkInterceptors = networkInterceptors
        )
    }

    private fun createInternal(
        tokenProvider: TokenProvider?,
        retryConfig: NetworkRetryConfig?,
        logger: SdkLogger?,
        loggingLevel: NetworkLoggingLevel,
        interceptors: List<Interceptor>,
        networkInterceptors: List<Interceptor>
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
                if (retryConfig != null) {
                    addInterceptor(RetryInterceptor(retryConfig))
                }
                if (logger != null && loggingLevel != NetworkLoggingLevel.None) {
                    addInterceptor(NetworkLoggingInterceptor(logger, loggingLevel))
                }
                interceptors.forEach(::addInterceptor)
                networkInterceptors.forEach(::addNetworkInterceptor)
            }
            .build()
    }
}
