package com.norbertotaveras.lanternsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.norbertotaveras.lanternsample.components.SampleNavigationDrawer
import com.norbertotaveras.lanternsample.ui.theme.LanternTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LanternTheme {
                SampleNavigationDrawer()
            }
        }
    }
}