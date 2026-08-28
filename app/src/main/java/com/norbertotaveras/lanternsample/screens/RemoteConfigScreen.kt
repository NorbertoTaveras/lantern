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
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigDefaults
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigFetchStatus
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigKey
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigSnapshot
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigValue
import com.norbertotaveras.lantern.remoteconfig.firebase.FirebaseRemoteConfigProvider
import com.norbertotaveras.lantern.remoteconfig.firebase.FirebaseRemoteConfigProviderConfig
import com.norbertotaveras.lantern.remoteconfig.firebase.FirebaseRemoteConfigValueType
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
fun RemoteConfigScreen() {
    val coroutineScope = rememberCoroutineScope()
    val provider = remember {
        FirebaseRemoteConfigProvider(
            config = FirebaseRemoteConfigProviderConfig(
                valueTypes = demoRemoteConfigTypes
            )
        )
    }

    val snapshot by provider.updates.collectAsState(initial = RemoteConfigSnapshot.Empty)
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun runRemoteConfigAction(block: suspend () -> Unit) {
        coroutineScope.launch {
            isLoading = true
            message = null
            errorMessage = null
            try {
                block()
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(provider) {
        when (val settingsResult = provider.applySettings()) {
            is SdkResult.Success -> Unit
            is SdkResult.Failure -> errorMessage = settingsResult.error.message
        }

        when (val defaultsResult = provider.setDefaults(demoRemoteConfigDefaults)) {
            is SdkResult.Success -> message = "Remote Config defaults loaded."
            is SdkResult.Failure -> errorMessage = defaultsResult.error.message
        }
    }

    FeatureScreen(
        title = "Remote Config",
        subtitle = "Fetch and inspect typed Firebase Remote Config values through provider-neutral SDK contracts.",
        icon = Icons.Filled.Tune,
        status = snapshot.fetchStatus?.label() ?: "Defaults"
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Default keys", value = demoRemoteConfigDefaults.values.size.toString()),
                DemoMetric(label = "Snapshot keys", value = snapshot.values.size.toString()),
                DemoMetric(label = "Provider", value = "Firebase")
            )
        )

        DemoSection(
            title = "Config controls",
            description = "The sample app owns Firebase setup while the SDK provider owns defaults, fetch, activate, and typed values.",
            leadingIcon = Icons.Filled.CloudSync
        ) {
            PrimaryDemoButton(
                text = "Fetch and activate",
                icon = Icons.Filled.CloudSync,
                enabled = !isLoading,
                onClick = {
                    runRemoteConfigAction {
                        when (val result = provider.fetchAndActivate()) {
                            is SdkResult.Success -> {
                                message = if (result.data) {
                                    "Fetched and activated updated values."
                                } else {
                                    "Fetch completed; no newly activated values."
                                }
                            }

                            is SdkResult.Failure -> {
                                errorMessage = result.error.message
                            }
                        }
                    }
                }
            )

            SecondaryDemoButton(
                text = "Apply defaults",
                icon = Icons.Filled.Save,
                enabled = !isLoading,
                onClick = {
                    runRemoteConfigAction {
                        when (val result = provider.setDefaults(demoRemoteConfigDefaults)) {
                            is SdkResult.Success -> message = "Defaults applied."
                            is SdkResult.Failure -> errorMessage = result.error.message
                        }
                    }
                }
            )

            SecondaryDemoButton(
                text = "Refresh snapshot",
                icon = Icons.Filled.Refresh,
                enabled = !isLoading,
                onClick = {
                    runRemoteConfigAction {
                        when (val result = provider.getSnapshot()) {
                            is SdkResult.Success -> message = "Snapshot refreshed with ${result.data.values.size} values."
                            is SdkResult.Failure -> errorMessage = result.error.message
                        }
                    }
                }
            )
        }

        DemoSection(
            title = "Current values",
            description = "Rows show the typed values returned by Firebase Remote Config after defaults, fetch, or activation.",
            leadingIcon = Icons.Filled.Tune
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                demoRemoteConfigDefaults.values.keys.forEach { key ->
                    InfoRow(
                        label = key.value,
                        value = snapshot.valueFor(key)?.displayValue() ?: "Not loaded"
                    )
                }
            }
        }

        DemoSection(
            title = "Module boundary",
            description = "Firebase remains an app-owned provider detail; the reusable contract stays provider-neutral.",
            leadingIcon = Icons.Filled.CloudSync
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "SDK contracts", value = "remote-config")
                InfoRow(label = "Provider module", value = "remote-config-firebase")
                InfoRow(label = "Fetch status", value = snapshot.fetchStatus?.label() ?: "No fetch yet")
                InfoRow(label = "Compose in SDK", value = "None")
            }
        }

        StatusMessage(
            message = message,
            errorMessage = errorMessage
        )
    }
}

private val welcomeMessageKey = RemoteConfigKey.unsafe("sample_welcome_message")
private val checkoutEnabledKey = RemoteConfigKey.unsafe("sample_checkout_enabled")
private val maxItemsKey = RemoteConfigKey.unsafe("sample_max_items")
private val rolloutPercentKey = RemoteConfigKey.unsafe("sample_rollout_percent")

private val demoRemoteConfigDefaults = RemoteConfigDefaults(
    values = mapOf(
        welcomeMessageKey to RemoteConfigValue.StringValue("Welcome to Lantern"),
        checkoutEnabledKey to RemoteConfigValue.BooleanValue(true),
        maxItemsKey to RemoteConfigValue.LongValue(12L),
        rolloutPercentKey to RemoteConfigValue.DoubleValue(0.25)
    )
)

private val demoRemoteConfigTypes = mapOf(
    welcomeMessageKey to FirebaseRemoteConfigValueType.String,
    checkoutEnabledKey to FirebaseRemoteConfigValueType.Boolean,
    maxItemsKey to FirebaseRemoteConfigValueType.Long,
    rolloutPercentKey to FirebaseRemoteConfigValueType.Double
)

private fun RemoteConfigFetchStatus.label(): String {
    return when (this) {
        RemoteConfigFetchStatus.Success -> "Fetched"
        RemoteConfigFetchStatus.Throttled -> "Throttled"
        RemoteConfigFetchStatus.NoChange -> "No change"
    }
}

private fun RemoteConfigValue.displayValue(): String {
    return when (this) {
        is RemoteConfigValue.BooleanValue -> value.toString()
        is RemoteConfigValue.DoubleValue -> value.toString()
        is RemoteConfigValue.LongValue -> value.toString()
        is RemoteConfigValue.StringValue -> value
    }
}
