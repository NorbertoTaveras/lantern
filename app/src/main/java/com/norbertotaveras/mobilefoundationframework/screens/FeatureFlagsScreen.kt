package com.norbertotaveras.mobilefoundationframework.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.featureflags.FeatureFlag
import com.norbertotaveras.mobilefoundation.featureflags.FeatureFlagEvaluation
import com.norbertotaveras.mobilefoundation.featureflags.FeatureFlagKey
import com.norbertotaveras.mobilefoundation.featureflags.FeatureFlagValue
import com.norbertotaveras.mobilefoundation.featureflags.FeatureFlagValueSource
import com.norbertotaveras.mobilefoundation.featureflags.StaticFeatureFlagProvider
import com.norbertotaveras.mobilefoundationframework.components.DemoMetric
import com.norbertotaveras.mobilefoundationframework.components.DemoSection
import com.norbertotaveras.mobilefoundationframework.components.FeatureScreen
import com.norbertotaveras.mobilefoundationframework.components.InfoRow
import com.norbertotaveras.mobilefoundationframework.components.MetricRow
import com.norbertotaveras.mobilefoundationframework.components.PrimaryDemoButton
import com.norbertotaveras.mobilefoundationframework.components.SecondaryDemoButton
import com.norbertotaveras.mobilefoundationframework.components.StatusMessage
import kotlinx.coroutines.launch

@Composable
fun FeatureFlagsScreen() {
    val coroutineScope = rememberCoroutineScope()
    var checkoutEnabled by remember { mutableStateOf(true) }
    var richHeaderEnabled by remember { mutableStateOf(false) }
    var pricingVariant by remember { mutableStateOf("control") }
    val provider = remember {
        StaticFeatureFlagProvider(
            initialValues = currentFeatureFlagValues(
                checkoutEnabled = checkoutEnabled,
                richHeaderEnabled = richHeaderEnabled,
                pricingVariant = pricingVariant
            )
        )
    }

    var evaluations by remember { mutableStateOf<List<FeatureFlagEvaluation>>(emptyList()) }
    var message by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun evaluateFlags() {
        coroutineScope.launch {
            val nextEvaluations = mutableListOf<FeatureFlagEvaluation>()
            for (flag in demoFeatureFlags) {
                when (val result = provider.evaluate(flag)) {
                    is SdkResult.Success -> nextEvaluations += result.data
                    is SdkResult.Failure -> errorMessage = result.error.message
                }
            }

            evaluations = nextEvaluations
            message = "Evaluated ${nextEvaluations.size} flags."
        }
    }

    fun updateProvider() {
        provider.update(
            currentFeatureFlagValues(
                checkoutEnabled = checkoutEnabled,
                richHeaderEnabled = richHeaderEnabled,
                pricingVariant = pricingVariant
            )
        )
        evaluateFlags()
    }

    LaunchedEffect(provider) {
        evaluateFlags()
    }

    FeatureScreen(
        title = "Feature Flags",
        subtitle = "Toggle and evaluate typed feature flags through SDK contracts while the sample app owns all UI state.",
        icon = Icons.Filled.Flag,
        status = "${evaluations.count { it.isEnabled() }}/${demoBooleanFlags.size} enabled"
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Flags", value = demoFeatureFlags.size.toString()),
                DemoMetric(label = "Boolean", value = demoBooleanFlags.size.toString()),
                DemoMetric(label = "Provider", value = "Static")
            )
        )

        DemoSection(
            title = "Boolean controls",
            description = "Switches update a StaticFeatureFlagProvider and then evaluate the SDK flag contracts.",
            leadingIcon = Icons.Filled.Flag
        ) {
            FeatureFlagToggleRow(
                title = "Checkout flow",
                description = "Controls checkout.new_flow",
                checked = checkoutEnabled,
                onCheckedChange = {
                    checkoutEnabled = it
                    updateProvider()
                }
            )

            FeatureFlagToggleRow(
                title = "Rich home header",
                description = "Controls home.rich_header",
                checked = richHeaderEnabled,
                onCheckedChange = {
                    richHeaderEnabled = it
                    updateProvider()
                }
            )
        }

        DemoSection(
            title = "Variant controls",
            description = "String variants use the same typed value contract as boolean flags.",
            leadingIcon = Icons.Filled.Tune
        ) {
            PrimaryDemoButton(
                text = "Use treatment variant",
                icon = Icons.Filled.Tune,
                onClick = {
                    pricingVariant = "treatment"
                    updateProvider()
                }
            )

            SecondaryDemoButton(
                text = "Use control variant",
                icon = Icons.Filled.Refresh,
                onClick = {
                    pricingVariant = "control"
                    updateProvider()
                }
            )
        }

        DemoSection(
            title = "Current evaluations",
            description = "Each row is produced by FeatureFlagProvider.evaluate.",
            leadingIcon = Icons.Filled.Flag
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                evaluations.forEach { evaluation ->
                    InfoRow(
                        label = evaluation.flag.key.value,
                        value = evaluation.value.displayValue(),
                        supportingText = evaluation.source.label()
                    )
                }
            }
        }

        DemoSection(
            title = "Module boundary",
            description = "The module owns keys, typed values, snapshots, and evaluation; the sample app owns toggles and display.",
            leadingIcon = Icons.Filled.Flag
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "SDK module", value = "feature-flags")
                InfoRow(label = "Provider", value = "StaticFeatureFlagProvider")
                InfoRow(label = "Remote bridge", value = "Future slice")
                InfoRow(label = "Compose in SDK", value = "None")
            }
        }

        StatusMessage(
            message = message,
            errorMessage = errorMessage
        )
    }
}

@Composable
private fun FeatureFlagToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.size(width = 52.dp, height = 32.dp)
        )
    }
}

private val checkoutFlag = FeatureFlag(
    key = FeatureFlagKey.unsafe("checkout.new_flow"),
    defaultValue = FeatureFlagValue.BooleanValue(false),
    description = "Enable the sample checkout flow."
)

private val richHeaderFlag = FeatureFlag(
    key = FeatureFlagKey.unsafe("home.rich_header"),
    defaultValue = FeatureFlagValue.BooleanValue(false),
    description = "Enable a richer home header treatment."
)

private val pricingVariantFlag = FeatureFlag(
    key = FeatureFlagKey.unsafe("pricing.variant"),
    defaultValue = FeatureFlagValue.StringValue("control"),
    description = "Select the sample pricing layout variant."
)

private val demoBooleanFlags = listOf(checkoutFlag, richHeaderFlag)

private val demoFeatureFlags = listOf(
    checkoutFlag,
    richHeaderFlag,
    pricingVariantFlag
)

private fun currentFeatureFlagValues(
    checkoutEnabled: Boolean,
    richHeaderEnabled: Boolean,
    pricingVariant: String
): Map<FeatureFlagKey, FeatureFlagValue> {
    return mapOf(
        checkoutFlag.key to FeatureFlagValue.BooleanValue(checkoutEnabled),
        richHeaderFlag.key to FeatureFlagValue.BooleanValue(richHeaderEnabled),
        pricingVariantFlag.key to FeatureFlagValue.StringValue(pricingVariant)
    )
}

private fun FeatureFlagValue.displayValue(): String {
    return when (this) {
        is FeatureFlagValue.BooleanValue -> value.toString()
        is FeatureFlagValue.DoubleValue -> value.toString()
        is FeatureFlagValue.LongValue -> value.toString()
        is FeatureFlagValue.StringValue -> value
    }
}

private fun FeatureFlagValueSource.label(): String {
    return when (this) {
        FeatureFlagValueSource.Default -> "Default value"
        FeatureFlagValueSource.Provider -> "Provider value"
    }
}
