package com.norbertotaveras.mobilefoundationframework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.norbertotaveras.mobilefoundationframework.components.SampleNavigationDrawer
import com.norbertotaveras.mobilefoundationframework.ui.theme.MobileFoundationFrameworkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileFoundationFrameworkTheme {
                SampleNavigationDrawer()
            }
        }
    }
}