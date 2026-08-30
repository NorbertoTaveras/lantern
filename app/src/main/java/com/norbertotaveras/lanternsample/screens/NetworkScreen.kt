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

package com.norbertotaveras.lanternsample.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Http
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.norbertotaveras.lantern.logging.AndroidSdkLogger
import com.norbertotaveras.lantern.network.okhttp.NetworkConfig
import com.norbertotaveras.lantern.network.okhttp.NetworkLoggingLevel
import com.norbertotaveras.lantern.network.okhttp.NetworkRetryConfig
import com.norbertotaveras.lantern.network.okhttp.OkHttpNetworkClientFactory
import com.norbertotaveras.lantern.network.okhttp.TokenProvider
import com.norbertotaveras.lanternsample.components.DemoMetric
import com.norbertotaveras.lanternsample.components.DemoSection
import com.norbertotaveras.lanternsample.components.FeatureScreen
import com.norbertotaveras.lanternsample.components.InfoRow
import com.norbertotaveras.lanternsample.components.MetricRow
import com.norbertotaveras.lanternsample.components.PrimaryDemoButton
import com.norbertotaveras.lanternsample.components.SecondaryDemoButton
import com.norbertotaveras.lanternsample.components.StatusMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

@Composable
fun NetworkScreen() {
    val coroutineScope = rememberCoroutineScope()
    val config = remember {
        NetworkConfig(
            defaultHeaders = mapOf(
                "Accept" to "application/json",
                "X-Sample-App" to "lantern"
            )
        )
    }
    val retryConfig = remember { NetworkRetryConfig(maxRetries = 2) }
    val tokenProvider = remember {
        object : TokenProvider {
            override fun getAccessToken(): String = "sample-token"
        }
    }
    val factory = remember(config) { OkHttpNetworkClientFactory(config) }
    var message by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var responseCode by remember { mutableStateOf<Int?>(null) }
    var responseBody by remember { mutableStateOf<String?>(null) }
    var echoedHeaders by remember { mutableStateOf("None") }
    var isLoading by remember { mutableStateOf(false) }

    FeatureScreen(
        title = "Network OkHttp",
        subtitle = "Build configured OkHttp clients with SDK headers, auth, retry, and logging helpers.",
        icon = Icons.Filled.SyncAlt,
        status = "Live"
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Headers", value = config.defaultHeaders.size.toString()),
                DemoMetric(label = "Retries", value = retryConfig.maxRetries.toString()),
                DemoMetric(label = "Timeout", value = "${config.readTimeoutMillis / 1000}s")
            )
        )

        DemoSection(
            title = "Client factory",
            description = "Creates an OkHttpClient without making a network request.",
            leadingIcon = Icons.Filled.Http
        ) {
            PrimaryDemoButton(
                text = "Create logged client",
                icon = Icons.Filled.Refresh,
                onClick = {
                    val client = factory.createWithLogging(
                        logger = AndroidSdkLogger(isEnabled = true),
                        loggingLevel = NetworkLoggingLevel.Basic,
                        tokenProvider = tokenProvider,
                        retryConfig = retryConfig
                    )
                    errorMessage = null
                    message = "Client ready with ${client.interceptors.size} application interceptors."
                }
            )

            SecondaryDemoButton(
                text = "Run mock request",
                icon = Icons.Filled.Http,
                enabled = !isLoading,
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        message = null
                        errorMessage = null

                        try {
                            val client = factory.createWithLogging(
                                logger = AndroidSdkLogger(isEnabled = true),
                                loggingLevel = NetworkLoggingLevel.Basic,
                                tokenProvider = tokenProvider,
                                retryConfig = retryConfig,
                                interceptors = listOf(sampleMockResponseInterceptor())
                            )
                            val request = Request.Builder()
                                .url("https://sample.lantern.local/sdk/status")
                                .get()
                                .build()
                            val response = withContext(Dispatchers.IO) {
                                client.newCall(request).execute()
                            }

                            response.use {
                                responseCode = it.code
                                responseBody = it.body.string()
                                echoedHeaders = listOfNotNull(
                                    it.header("X-Echo-Accept")?.let { value -> "Accept=$value" },
                                    it.header("X-Echo-Auth")?.let { value -> "Auth=$value" }
                                ).joinToString()
                                message = "Mock request completed with HTTP ${it.code}."
                            }
                        } catch (throwable: Throwable) {
                            responseCode = null
                            responseBody = null
                            echoedHeaders = "None"
                            errorMessage = throwable.message
                        } finally {
                            isLoading = false
                        }
                    }
                }
            )
        }

        DemoSection(
            title = "Latest response",
            description = "The request travels through Lantern headers, auth, retry, and logging before the sample mock responds.",
            leadingIcon = Icons.Filled.Http
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "Status code", value = responseCode?.toString() ?: "None")
                InfoRow(label = "Echoed headers", value = echoedHeaders)
                InfoRow(label = "Body", value = responseBody ?: "No response yet")
            }
        }

        DemoSection(
            title = "Configuration",
            description = "The sample app owns API endpoints and tokens; the SDK owns reusable client wiring.",
            leadingIcon = Icons.Filled.SyncAlt
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "Module", value = "network-okhttp")
                InfoRow(label = "Default headers", value = config.defaultHeaders.keys.joinToString())
                InfoRow(label = "Retry statuses", value = retryConfig.retryStatusCodes.joinToString())
                InfoRow(label = "Body logging", value = "Not enabled")
            }
        }

        StatusMessage(message = message, errorMessage = errorMessage)
    }
}

private fun sampleMockResponseInterceptor(): Interceptor {
    return Interceptor { chain ->
        val request = chain.request()
        Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .header("Content-Type", "application/json")
            .header("X-Echo-Accept", request.header("Accept").orEmpty())
            .header("X-Echo-Auth", request.header("Authorization")?.take(14).orEmpty())
            .body(sampleNetworkBody(request).toResponseBody())
            .build()
    }
}

private fun sampleNetworkBody(request: Request): String {
    return """
        {
          "module": "network-okhttp",
          "path": "${request.url.encodedPath}",
          "mock": true
        }
    """.trimIndent()
}
