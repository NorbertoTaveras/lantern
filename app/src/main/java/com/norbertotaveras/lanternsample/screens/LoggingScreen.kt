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
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.norbertotaveras.lantern.logging.AndroidSdkLogger
import com.norbertotaveras.lanternsample.components.DemoMetric
import com.norbertotaveras.lanternsample.components.DemoSection
import com.norbertotaveras.lanternsample.components.FeatureScreen
import com.norbertotaveras.lanternsample.components.InfoRow
import com.norbertotaveras.lanternsample.components.MetricRow
import com.norbertotaveras.lanternsample.components.PrimaryDemoButton

@Composable
fun LoggingScreen() {
    val logger = AndroidSdkLogger(isEnabled = true)

    FeatureScreen(
        title = "Logging",
        subtitle = "Exercise the SDK logger abstraction from the sample app.",
        icon = Icons.Filled.BugReport,
        status = "Ready"
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Implementation", value = "Android"),
                DemoMetric(label = "Levels", value = "4"),
                DemoMetric(label = "UI dependency", value = "None")
            )
        )

        DemoSection(
            title = "Logger action",
            description = "Tap once to send debug, info, warning, and error messages through AndroidSdkLogger.",
            leadingIcon = Icons.AutoMirrored.Filled.Send
        ) {
            PrimaryDemoButton(
                text = "Send test logs",
                icon = Icons.AutoMirrored.Filled.Send,
                onClick = {
                    logger.debug("Debug log from sample app")
                    logger.info("Info log from sample app")
                    logger.warning("Warning log from sample app")
                    logger.error("Error log from sample app")
                }
            )
        }

        DemoSection(
            title = "Logger details",
            description = "This screen stays intentionally small while the SDK logger module grows.",
            leadingIcon = Icons.Filled.BugReport
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "Logger", value = "AndroidSdkLogger")
                InfoRow(label = "Enabled", value = "true")
                InfoRow(label = "Levels", value = "Debug, info, warning, error")
            }
        }
    }
}
