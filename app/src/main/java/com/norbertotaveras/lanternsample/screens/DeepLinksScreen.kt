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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.norbertotaveras.lantern.deeplinks.DeepLink
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.deeplinks.DeepLinkConfig
import com.norbertotaveras.lantern.deeplinks.DefaultDeepLinkParser
import com.norbertotaveras.lanternsample.components.DemoMetric
import com.norbertotaveras.lanternsample.components.DemoSection
import com.norbertotaveras.lanternsample.components.FeatureScreen
import com.norbertotaveras.lanternsample.components.InfoRow
import com.norbertotaveras.lanternsample.components.MetricRow
import com.norbertotaveras.lanternsample.components.PrimaryDemoButton
import com.norbertotaveras.lanternsample.components.SecondaryDemoButton
import com.norbertotaveras.lanternsample.components.StatusMessage

@Composable
fun DeepLinksScreen() {
    val parser = remember {
        DefaultDeepLinkParser(
            DeepLinkConfig(
                allowedSchemes = setOf("mf"),
                allowedHosts = setOf("open")
            )
        )
    }
    var inputValue by remember { mutableStateOf("mf://open/profile/42?tab=settings&source=sample") }
    var parsedResult by remember { mutableStateOf<SdkResult<DeepLink>?>(null) }
    var message by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun parseValue(value: String) {
        when (val result = parser.parse(value)) {
            is SdkResult.Success -> {
                parsedResult = result
                message = "Deep link accepted."
                errorMessage = null
            }
            is SdkResult.Failure -> {
                parsedResult = result
                message = null
                errorMessage = result.error.message
            }
        }
    }

    fun parseCurrentInput() {
        parseValue(inputValue)
    }

    FeatureScreen(
        title = "Deep Links",
        subtitle = "Parse URI strings into typed deep-link models with scheme and host allow-listing.",
        icon = Icons.Filled.Link,
        status = "Live"
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Schemes", value = "mf"),
                DemoMetric(label = "Hosts", value = "open"),
                DemoMetric(label = "Parser", value = "Default")
            )
        )

        DemoSection(
            title = "Parser controls",
            description = "Try an accepted Lantern-style URI or a rejected external URI.",
            leadingIcon = Icons.Filled.Route
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = { Text(text = "Deep link URI") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                PrimaryDemoButton(
                    text = "Parse deep link",
                    icon = Icons.Filled.Route,
                    onClick = ::parseCurrentInput
                )

                SecondaryDemoButton(
                    text = "Use rejected example",
                    icon = Icons.Filled.Link,
                    onClick = {
                        val rejectedExample = "https://example.com/profile/42"
                        inputValue = rejectedExample
                        parseValue(rejectedExample)
                    }
                )
            }
        }

        DemoSection(
            title = "Parsed result",
            description = "The parser returns a typed model with path segments and query parameters.",
            leadingIcon = Icons.Filled.Route
        ) {
            when (val result = parsedResult) {
                is SdkResult.Success -> {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        InfoRow(label = "Raw", value = result.data.rawValue)
                        InfoRow(label = "Scheme", value = result.data.scheme)
                        InfoRow(label = "Host", value = result.data.host ?: "None")
                        InfoRow(label = "Path", value = result.data.pathSegments.joinToString("/"))
                        InfoRow(label = "Source", value = result.data.firstQueryParameter("source") ?: "None")
                    }
                }
                is SdkResult.Failure -> {
                    InfoRow(label = "Rejected", value = result.error.code)
                }
                null -> InfoRow(label = "Status", value = "Not parsed yet")
            }
        }

        StatusMessage(message = message, errorMessage = errorMessage)
    }
}
