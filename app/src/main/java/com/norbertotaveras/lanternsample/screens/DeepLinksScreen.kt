package com.norbertotaveras.lanternsample.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Route
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.deeplinks.DeepLinkConfig
import com.norbertotaveras.lantern.deeplinks.DefaultDeepLinkParser
import com.norbertotaveras.lanternsample.components.DemoMetric
import com.norbertotaveras.lanternsample.components.DemoSection
import com.norbertotaveras.lanternsample.components.FeatureScreen
import com.norbertotaveras.lanternsample.components.InfoRow
import com.norbertotaveras.lanternsample.components.MetricRow

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
    val validResult = remember { parser.parse("mf://open/profile/42?tab=settings&source=sample") }
    val invalidResult = remember { parser.parse("https://example.com/profile/42") }

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
            title = "Accepted link",
            description = "The parser returns a typed model with path segments and query parameters.",
            leadingIcon = Icons.Filled.Route
        ) {
            when (validResult) {
                is SdkResult.Success -> {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        InfoRow(label = "Raw", value = validResult.data.rawValue)
                        InfoRow(label = "Scheme", value = validResult.data.scheme)
                        InfoRow(label = "Host", value = validResult.data.host ?: "None")
                        InfoRow(label = "Path", value = validResult.data.pathSegments.joinToString("/"))
                        InfoRow(label = "Source", value = validResult.data.firstQueryParameter("source") ?: "None")
                    }
                }
                is SdkResult.Failure -> {
                    InfoRow(label = "Error", value = validResult.error.message)
                }
            }
        }

        DemoSection(
            title = "Rejected link",
            description = "Unexpected schemes and hosts are rejected before they reach navigation.",
            leadingIcon = Icons.Filled.Link
        ) {
            when (invalidResult) {
                is SdkResult.Success -> InfoRow(label = "Parsed", value = invalidResult.data.rawValue)
                is SdkResult.Failure -> InfoRow(label = "Rejected", value = invalidResult.error.code)
            }
        }
    }
}
