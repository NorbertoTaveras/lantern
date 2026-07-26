package com.norbertotaveras.mobilefoundationframework.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FirebaseAuthScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(text = "Firebase Auth")

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Email") }
        )

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Password") }
        )

        Button(onClick = {}) {
            Text(text = "Sign in with email")
        }

        Button(onClick = {}) {
            Text(text = "Sign in anonymously")
        }
    }
}