package com.norbertotaveras.mobilefoundationframework.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GoogleAuthScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(text = "Google Auth")
        Text(text = "This screen will test Google Sign-In through Credential Manager.")

        Button(onClick = {}) {
            Text(text = "Sign in with Google")
        }

        Button(onClick = {}) {
            Text(text = "Clear Google credential state")
        }
    }
}