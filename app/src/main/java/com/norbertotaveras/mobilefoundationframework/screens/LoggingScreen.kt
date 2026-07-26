package com.norbertotaveras.mobilefoundationframework.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.norbertotaveras.mobilefoundation.logging.AndroidSdkLogger

@Composable
fun LoggingScreen() {
    val logger = AndroidSdkLogger(isEnabled = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(text = "Logging")

        Button(
            onClick = {
                logger.debug("Debug log from sample app")
                logger.info("Info log from sample app")
                logger.warning("Warning log from sample app")
                logger.error("Error log from sample app")
            }
        ) {
            Text(text = "Send test logs")
        }
    }
}