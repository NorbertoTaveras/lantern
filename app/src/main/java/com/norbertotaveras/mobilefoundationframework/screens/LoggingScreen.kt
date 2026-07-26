package com.norbertotaveras.mobilefoundationframework.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.norbertotaveras.mobilefoundation.logging.AndroidSdkLogger
import com.norbertotaveras.mobilefoundationframework.components.DemoSection
import com.norbertotaveras.mobilefoundationframework.components.FeatureScreen
import com.norbertotaveras.mobilefoundationframework.components.InfoRow
import com.norbertotaveras.mobilefoundationframework.components.PrimaryDemoButton

@Composable
fun LoggingScreen() {
    val logger = AndroidSdkLogger(isEnabled = true)

    FeatureScreen(
        title = "Logging",
        subtitle = "Exercise the SDK logger abstraction from the sample app.",
        icon = Icons.Filled.BugReport,
        status = "Ready"
    ) {
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
