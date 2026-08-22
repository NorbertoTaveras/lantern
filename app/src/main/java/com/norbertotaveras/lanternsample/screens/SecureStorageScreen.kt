package com.norbertotaveras.lanternsample.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Token
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.securestorage.DataStoreSecureKeyValueStore
import com.norbertotaveras.lantern.securestorage.DefaultSecureTokenStore
import com.norbertotaveras.lantern.securestorage.SecureStorageConfig
import com.norbertotaveras.lantern.securestorage.SecureStorageKey
import com.norbertotaveras.lantern.securestorage.SecureToken
import com.norbertotaveras.lantern.securestorage.SecureTokenType
import com.norbertotaveras.lanternsample.components.DemoMetric
import com.norbertotaveras.lanternsample.components.DemoSection
import com.norbertotaveras.lanternsample.components.DestructiveDemoButton
import com.norbertotaveras.lanternsample.components.FeatureScreen
import com.norbertotaveras.lanternsample.components.InfoRow
import com.norbertotaveras.lanternsample.components.MetricRow
import com.norbertotaveras.lanternsample.components.PrimaryDemoButton
import com.norbertotaveras.lanternsample.components.SecondaryDemoButton
import com.norbertotaveras.lanternsample.components.StatusMessage
import kotlinx.coroutines.launch

@Composable
fun SecureStorageScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val keyValueStore = remember(context) {
        DataStoreSecureKeyValueStore(
            context = context,
            config = SecureStorageConfig(namespace = "sample_secure_storage")
        )
    }
    val tokenStore = remember(keyValueStore) {
        DefaultSecureTokenStore(keyValueStore)
    }

    var keyInput by remember { mutableStateOf("sample.session") }
    var valueInput by remember { mutableStateOf("demo-token") }
    var storedValue by remember { mutableStateOf<String?>(null) }
    var tokenPreview by remember { mutableStateOf<SecureToken?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun withKey(block: suspend (SecureStorageKey) -> Unit) {
        when (val keyResult = SecureStorageKey.from(keyInput)) {
            is SdkResult.Success -> {
                coroutineScope.launch {
                    isLoading = true
                    message = null
                    errorMessage = null
                    try {
                        block(keyResult.data)
                    } finally {
                        isLoading = false
                    }
                }
            }

            is SdkResult.Failure -> {
                message = null
                errorMessage = keyResult.error.message
            }
        }
    }

    FeatureScreen(
        title = "Secure Storage",
        subtitle = "Exercise DataStore-backed key-value and token storage while keeping storage UI in the sample app.",
        icon = Icons.Filled.Lock,
        status = if (storedValue == null && tokenPreview == null) "Ready" else "Stored"
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Backend", value = "DataStore"),
                DemoMetric(label = "Token codec", value = "JSON"),
                DemoMetric(label = "SDK UI", value = "0")
            )
        )

        DemoSection(
            title = "Storage inputs",
            description = "Keys are validated by the SDK before the sample app writes to local storage.",
            leadingIcon = Icons.Filled.Key
        ) {
            OutlinedTextField(
                value = keyInput,
                onValueChange = { keyInput = it },
                label = { Text(text = "Storage key") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = valueInput,
                onValueChange = { valueInput = it },
                label = { Text(text = "Value or token") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        DemoSection(
            title = "Key-value controls",
            description = "Write, read, remove, and clear values through SecureKeyValueStore.",
            leadingIcon = Icons.Filled.Lock
        ) {
            PrimaryDemoButton(
                text = "Save value",
                icon = Icons.Filled.Save,
                enabled = !isLoading,
                onClick = {
                    withKey { key ->
                        when (val result = keyValueStore.putString(key, valueInput)) {
                            is SdkResult.Success -> {
                                storedValue = valueInput
                                message = "Value saved."
                            }

                            is SdkResult.Failure -> {
                                errorMessage = result.error.message
                            }
                        }
                    }
                }
            )

            SecondaryDemoButton(
                text = "Read value",
                icon = Icons.Filled.Refresh,
                enabled = !isLoading,
                onClick = {
                    withKey { key ->
                        when (val result = keyValueStore.getString(key)) {
                            is SdkResult.Success -> {
                                storedValue = result.data
                                message = result.data?.let { "Value loaded." } ?: "No value stored for this key."
                            }

                            is SdkResult.Failure -> {
                                errorMessage = result.error.message
                            }
                        }
                    }
                }
            )

            DestructiveDemoButton(
                text = "Remove value",
                icon = Icons.Filled.Delete,
                enabled = !isLoading,
                onClick = {
                    withKey { key ->
                        when (val result = keyValueStore.remove(key)) {
                            is SdkResult.Success -> {
                                storedValue = null
                                message = "Value removed."
                            }

                            is SdkResult.Failure -> {
                                errorMessage = result.error.message
                            }
                        }
                    }
                }
            )
        }

        DemoSection(
            title = "Token controls",
            description = "Persist a SecureToken through the SDK token store wrapper.",
            leadingIcon = Icons.Filled.Token
        ) {
            PrimaryDemoButton(
                text = "Save token",
                icon = Icons.Filled.Save,
                enabled = !isLoading,
                onClick = {
                    withKey { key ->
                        val token = SecureToken(
                            value = valueInput,
                            type = SecureTokenType.Bearer,
                            metadata = mapOf("source" to "sample")
                        )

                        when (val result = tokenStore.saveToken(key, token)) {
                            is SdkResult.Success -> {
                                tokenPreview = token
                                storedValue = valueInput
                                message = "Token saved."
                            }

                            is SdkResult.Failure -> {
                                errorMessage = result.error.message
                            }
                        }
                    }
                }
            )

            SecondaryDemoButton(
                text = "Read token",
                icon = Icons.Filled.Refresh,
                enabled = !isLoading,
                onClick = {
                    withKey { key ->
                        when (val result = tokenStore.getToken(key)) {
                            is SdkResult.Success -> {
                                tokenPreview = result.data
                                storedValue = result.data?.value
                                message = result.data?.let { "Token loaded." } ?: "No token stored for this key."
                            }

                            is SdkResult.Failure -> {
                                errorMessage = result.error.message
                            }
                        }
                    }
                }
            )

            DestructiveDemoButton(
                text = "Clear secure storage",
                icon = Icons.Filled.Delete,
                enabled = !isLoading,
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        message = null
                        errorMessage = null

                        when (val result = tokenStore.clearTokens()) {
                            is SdkResult.Success -> {
                                storedValue = null
                                tokenPreview = null
                                message = "Secure storage cleared."
                            }

                            is SdkResult.Failure -> {
                                errorMessage = result.error.message
                            }
                        }

                        isLoading = false
                    }
                }
            )
        }

        DemoSection(
            title = "Current stored state",
            description = "Values shown here come from the sample app's local DataStore file.",
            leadingIcon = Icons.Filled.Key
        ) {
            SecureStorageStateRow(
                label = "Raw value",
                value = storedValue ?: "None"
            )

            SecureStorageStateRow(
                label = "Token type",
                value = tokenPreview?.type?.name ?: "None"
            )

            SecureStorageStateRow(
                label = "Token metadata",
                value = tokenPreview?.metadata?.entries?.joinToString { "${it.key}=${it.value}" } ?: "None"
            )
        }

        DemoSection(
            title = "Module boundary",
            description = "The SDK owns storage contracts and DataStore-backed behavior; the sample app owns all Compose UI.",
            leadingIcon = Icons.Filled.Lock
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "SDK module", value = "secure-storage")
                InfoRow(label = "Storage backend", value = "DataStore Preferences")
                InfoRow(label = "Token format", value = "JSON")
                InfoRow(label = "Compose in SDK", value = "None")
            }
        }

        StatusMessage(
            message = message,
            errorMessage = errorMessage
        )
    }
}

@Composable
private fun SecureStorageStateRow(
    label: String,
    value: String
) {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Filled.Key,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
