package com.norbertotaveras.mobilefoundationframework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.norbertotaveras.mobilefoundation.core.Enviroment
import com.norbertotaveras.mobilefoundation.core.SdkConfig
import com.norbertotaveras.mobilefoundation.logging.AndroidSdkLogger
import com.norbertotaveras.mobilefoundationframework.ui.theme.MobileFoundationFrameworkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = SdkConfig(
            environment = Enviroment.Development,
            isDebugLoggingEnabled = true
        )
        val logger = AndroidSdkLogger(
            isEnabled = config.isDebugLoggingEnabled
        )
        logger.debug("Mobile Foundation Framework initialized")
        enableEdgeToEdge()
        setContent {
            MobileFoundationFrameworkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MobileFoundationFrameworkTheme {
        Greeting("Android")
    }
}