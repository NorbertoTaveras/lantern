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
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Verified
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.norbertotaveras.lanternsample.components.StatusMessage

@Composable
fun AppVersioningScreen() {
    val context = LocalContext.current
    val provider = remember(context) { AndroidAppVersionProvider(context) }
    val evaluator = remember { DefaultAppUpdatePolicyEvaluator() }
    var currentVersion by remember { mutableStateOf<AppVersion?>(null) }
    var updateState by remember { mutableStateOf<AppUpdateState?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(provider) {
        when (val result = provider.getCurrentVersion()) {
            is SdkResult.Success -> {
                currentVersion = result.data
                when (val policyResult = evaluator.evaluate(result.data, samplePolicy)) {
                    is SdkResult.Success -> updateState = policyResult.data
                    is SdkResult.Failure -> errorMessage = policyResult.error.message
                }
            }
            is SdkResult.Failure -> errorMessage = result.error.message
        }
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
                DemoMetric(label = "Minimum", value = samplePolicy.minimumSupportedVersion?.toString() ?: "None"),
                DemoMetric(label = "Latest", value = samplePolicy.latestVersion?.toString() ?: "None")
            )
        )

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

        StatusMessage(message = updateState?.let { "Version policy evaluated." }, errorMessage = errorMessage)
    }
}

private val samplePolicy = AppUpdatePolicy(
    minimumSupportedVersion = AppVersion(major = 1, minor = 0, patch = 0),
    latestVersion = AppVersion(major = 1, minor = 1, patch = 0)
)

private fun AppUpdateRequirement.label(): String {
    return when (this) {
        AppUpdateRequirement.None -> "Current"
        AppUpdateRequirement.SoftUpdate -> "Recommended"
        AppUpdateRequirement.ForceUpdate -> "Required"
    }
}
