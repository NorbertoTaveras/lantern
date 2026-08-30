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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.norbertotaveras.lantern.appversioning.AndroidAppVersionProvider
import com.norbertotaveras.lantern.appversioning.AppUpdatePolicy
import com.norbertotaveras.lantern.appversioning.AppUpdateRequirement
import com.norbertotaveras.lantern.appversioning.AppUpdateState
import com.norbertotaveras.lantern.appversioning.AppVersion
import com.norbertotaveras.lantern.appversioning.DefaultAppUpdatePolicyEvaluator
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
fun AppVersioningScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val provider = remember(context) { AndroidAppVersionProvider(context) }
    val evaluator = remember { DefaultAppUpdatePolicyEvaluator() }
    var minimumVersionInput by remember { mutableStateOf("1.0.0") }
    var latestVersionInput by remember { mutableStateOf("1.1.0") }
    var currentVersion by remember { mutableStateOf<AppVersion?>(null) }
    var updateState by remember { mutableStateOf<AppUpdateState?>(null) }
    var message by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun parseOptionalVersion(label: String, value: String): AppVersion? {
        if (value.isBlank()) return null

        return when (val result = AppVersion.parse(value)) {
            is SdkResult.Success -> result.data
            is SdkResult.Failure -> {
                errorMessage = "$label: ${result.error.message}"
                null
            }
        }
    }

    fun evaluatePolicy(version: AppVersion) {
        errorMessage = null
        val minimumVersion = parseOptionalVersion("Minimum version", minimumVersionInput)
        if (errorMessage != null) return

        val latestVersion = parseOptionalVersion("Latest version", latestVersionInput)
        if (errorMessage != null) return

        when (
            val policyResult = evaluator.evaluate(
                version,
                AppUpdatePolicy(
                    minimumSupportedVersion = minimumVersion,
                    latestVersion = latestVersion
                )
            )
        ) {
            is SdkResult.Success -> {
                updateState = policyResult.data
                message = "Version policy evaluated."
            }
            is SdkResult.Failure -> {
                message = null
                errorMessage = policyResult.error.message
            }
        }
    }

    suspend fun refreshCurrentVersion() {
        when (val result = provider.getCurrentVersion()) {
            is SdkResult.Success -> {
                currentVersion = result.data
                evaluatePolicy(result.data)
            }
            is SdkResult.Failure -> {
                message = null
                errorMessage = result.error.message
            }
        }
    }

    LaunchedEffect(provider) {
        refreshCurrentVersion()
    }

    val requirement = updateState?.requirement ?: AppUpdateRequirement.None

    FeatureScreen(
        title = "App Versioning",
        subtitle = "Read the installed app version and evaluate update policy with SDK models.",
        icon = Icons.Filled.SystemUpdate,
        status = requirement.label()
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Current", value = currentVersion?.toString() ?: "Loading"),
                DemoMetric(label = "Minimum", value = updateState?.minimumSupportedVersion?.toString() ?: "None"),
                DemoMetric(label = "Latest", value = updateState?.latestVersion?.toString() ?: "None")
            )
        )

        DemoSection(
            title = "Policy controls",
            description = "Change policy inputs and re-run the SDK evaluator against the installed sample app version.",
            leadingIcon = Icons.Filled.SystemUpdate
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = minimumVersionInput,
                    onValueChange = { minimumVersionInput = it },
                    label = { Text(text = "Minimum supported version") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = latestVersionInput,
                    onValueChange = { latestVersionInput = it },
                    label = { Text(text = "Latest available version") },
                    singleLine = true
                )

                PrimaryDemoButton(
                    text = "Evaluate policy",
                    icon = Icons.Filled.Verified,
                    enabled = currentVersion != null,
                    onClick = {
                        currentVersion?.let(::evaluatePolicy)
                    }
                )

                SecondaryDemoButton(
                    text = "Refresh current version",
                    icon = Icons.Filled.Refresh,
                    onClick = {
                        coroutineScope.launch {
                            refreshCurrentVersion()
                        }
                    }
                )
            }
        }

        DemoSection(
            title = "Policy result",
            description = "The app decides how to display required or recommended updates.",
            leadingIcon = Icons.Filled.Verified
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "Current version", value = currentVersion?.toString() ?: "Loading")
                InfoRow(label = "Requirement", value = requirement.label())
                InfoRow(label = "Policy evaluator", value = "DefaultAppUpdatePolicyEvaluator")
                InfoRow(label = "App owns", value = "Update prompt UI")
            }
        }

        StatusMessage(message = message, errorMessage = errorMessage)
    }
}

private fun AppUpdateRequirement.label(): String {
    return when (this) {
        AppUpdateRequirement.None -> "Current"
        AppUpdateRequirement.SoftUpdate -> "Recommended"
        AppUpdateRequirement.ForceUpdate -> "Required"
    }
}
