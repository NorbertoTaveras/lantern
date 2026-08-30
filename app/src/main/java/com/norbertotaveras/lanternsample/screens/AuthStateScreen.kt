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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.norbertotaveras.lantern.auth.core.AuthSession
import com.norbertotaveras.lantern.auth.core.AuthState
import com.norbertotaveras.lantern.auth.firebase.FirebaseAuthProvider
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lanternsample.components.DemoMetric
import com.norbertotaveras.lanternsample.components.DemoSection
import com.norbertotaveras.lanternsample.components.FeatureScreen
import com.norbertotaveras.lanternsample.components.InfoRow
import com.norbertotaveras.lanternsample.components.MetricRow
import com.norbertotaveras.lanternsample.components.SecondaryDemoButton
import com.norbertotaveras.lanternsample.components.StatusMessage
import kotlinx.coroutines.launch

@Composable
fun AuthStateScreen() {
    val coroutineScope = rememberCoroutineScope()
    val authProvider = remember { FirebaseAuthProvider() }
    val authStateFlow = remember(authProvider) { authProvider.observeAuthState() }
    val observedState by authStateFlow.collectAsState(initial = AuthState.Loading)
    var refreshedSession by remember { mutableStateOf<AuthSession?>(null) }
    var message by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val activeSession = (observedState as? AuthState.Authenticated)?.session ?: refreshedSession

    FeatureScreen(
        title = "Auth State",
        subtitle = "Observe the live Firebase Auth provider and display provider-neutral auth-core session state.",
        icon = Icons.Filled.AccountCircle,
        status = observedState.label()
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Contract", value = "auth-core"),
                DemoMetric(label = "State", value = observedState.label()),
                DemoMetric(label = "Provider", value = activeSession?.provider?.name ?: "None")
            )
        )

        DemoSection(
            title = "Live session",
            description = "This readout updates from FirebaseAuthProvider.observeAuthState.",
            leadingIcon = Icons.Filled.Security
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "Observed state", value = observedState.label())
                InfoRow(label = "User ID", value = activeSession?.userId ?: "None")
                InfoRow(label = "Provider", value = activeSession?.provider?.name ?: "None")
                InfoRow(label = "Email", value = activeSession?.userProfile?.email ?: "None")
                InfoRow(label = "Display name", value = activeSession?.userProfile?.displayName ?: "None")
            }
        }

        DemoSection(
            title = "State controls",
            description = "Refresh reads the current provider session without changing sign-in state.",
            leadingIcon = Icons.Filled.Refresh
        ) {
            SecondaryDemoButton(
                text = "Refresh current session",
                icon = Icons.Filled.Refresh,
                onClick = {
                    coroutineScope.launch {
                        when (val result = authProvider.getCurrentSession()) {
                            is SdkResult.Success -> {
                                refreshedSession = result.data
                                errorMessage = null
                                message = result.data?.let { "Current session loaded." }
                                    ?: "No authenticated session."
                            }
                            is SdkResult.Failure -> {
                                message = null
                                errorMessage = result.error.message
                            }
                        }
                    }
                }
            )
        }

        StatusMessage(message = message, errorMessage = errorMessage)
    }
}

private fun AuthState.label(): String {
    return when (this) {
        AuthState.Loading -> "Loading"
        AuthState.Unauthenticated -> "Signed out"
        is AuthState.Authenticated -> "Signed in"
    }
}
