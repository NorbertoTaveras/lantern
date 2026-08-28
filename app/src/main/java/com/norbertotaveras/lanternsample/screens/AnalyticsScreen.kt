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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.norbertotaveras.lantern.analytics.AnalyticsEvent
import com.norbertotaveras.lantern.analytics.AnalyticsEventName
import com.norbertotaveras.lantern.analytics.AnalyticsUserId
import com.norbertotaveras.lantern.analytics.AnalyticsUserProperty
import com.norbertotaveras.lantern.analytics.AnalyticsUserPropertyName
import com.norbertotaveras.lantern.analytics.AnalyticsValue
import com.norbertotaveras.lantern.analytics.NoOpAnalyticsProvider
import com.norbertotaveras.lantern.analytics.firebase.FirebaseAnalyticsProvider
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lanternsample.components.DemoMetric
import com.norbertotaveras.lanternsample.components.DemoSection
import com.norbertotaveras.lanternsample.components.FeatureScreen
import com.norbertotaveras.lanternsample.components.InfoRow
import com.norbertotaveras.lanternsample.components.MetricRow
import com.norbertotaveras.lanternsample.components.PrimaryDemoButton
import com.norbertotaveras.lanternsample.components.SecondaryDemoButton
import com.norbertotaveras.lanternsample.components.StatusMessage
import kotlinx.coroutines.launch

@Composable
fun AnalyticsScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val noOpProvider = remember { NoOpAnalyticsProvider() }
    val firebaseProvider = remember(context) { FirebaseAnalyticsProvider(context) }
    val event = remember {
        AnalyticsEvent(
            name = AnalyticsEventName.unsafe("sample_screen_view"),
            parameters = mapOf(
                "screen" to AnalyticsValue.StringValue("analytics"),
                "sample" to AnalyticsValue.BooleanValue(true)
            )
        )
    }
    var message by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    FeatureScreen(
        title = "Analytics",
        subtitle = "Track typed analytics events through no-op and Firebase provider implementations.",
        icon = Icons.Filled.Analytics,
        status = "Live"
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Parameters", value = event.parameters.size.toString()),
                DemoMetric(label = "Providers", value = "2"),
                DemoMetric(label = "Privacy owner", value = "App")
            )
        )

        DemoSection(
            title = "Track events",
            description = "No-op is safe for demos; Firebase uses the app's Firebase configuration.",
            leadingIcon = Icons.AutoMirrored.Filled.Send
        ) {
            PrimaryDemoButton(
                text = "Track no-op event",
                icon = Icons.AutoMirrored.Filled.Send,
                onClick = {
                    coroutineScope.launch {
                        when (val result = noOpProvider.track(event)) {
                            is SdkResult.Success -> {
                                errorMessage = null
                                message = "No-op provider accepted ${event.name.value}."
                            }
                            is SdkResult.Failure -> errorMessage = result.error.message
                        }
                    }
                }
            )

            SecondaryDemoButton(
                text = "Track Firebase event",
                icon = Icons.Filled.Analytics,
                onClick = {
                    coroutineScope.launch {
                        when (val result = firebaseProvider.track(event)) {
                            is SdkResult.Success -> {
                                errorMessage = null
                                message = "Firebase provider accepted ${event.name.value}."
                            }
                            is SdkResult.Failure -> errorMessage = result.error.message
                        }
                    }
                }
            )
        }

        DemoSection(
            title = "User state",
            description = "User identifiers and properties are typed before reaching provider SDKs.",
            leadingIcon = Icons.Filled.Person
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "Event name", value = event.name.value)
                InfoRow(label = "User ID model", value = AnalyticsUserId.unsafe("sample-user").value)
                InfoRow(
                    label = "Property model",
                    value = AnalyticsUserProperty(
                        name = AnalyticsUserPropertyName.unsafe("plan"),
                        value = AnalyticsValue.StringValue("demo")
                    ).name.value
                )
                InfoRow(label = "Provider module", value = "analytics-firebase")
            }
        }

        StatusMessage(message = message, errorMessage = errorMessage)
    }
}
