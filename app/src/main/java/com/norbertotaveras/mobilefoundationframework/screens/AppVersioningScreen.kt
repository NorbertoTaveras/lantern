package com.norbertotaveras.mobilefoundationframework.screens

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
import com.norbertotaveras.mobilefoundation.appversioning.AndroidAppVersionProvider
import com.norbertotaveras.mobilefoundation.appversioning.AppUpdatePolicy
import com.norbertotaveras.mobilefoundation.appversioning.AppUpdateRequirement
import com.norbertotaveras.mobilefoundation.appversioning.AppUpdateState
import com.norbertotaveras.mobilefoundation.appversioning.AppVersion
import com.norbertotaveras.mobilefoundation.appversioning.DefaultAppUpdatePolicyEvaluator
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundationframework.components.DemoMetric
import com.norbertotaveras.mobilefoundationframework.components.DemoSection
import com.norbertotaveras.mobilefoundationframework.components.FeatureScreen
import com.norbertotaveras.mobilefoundationframework.components.InfoRow
import com.norbertotaveras.mobilefoundationframework.components.MetricRow
import com.norbertotaveras.mobilefoundationframework.components.StatusMessage

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
