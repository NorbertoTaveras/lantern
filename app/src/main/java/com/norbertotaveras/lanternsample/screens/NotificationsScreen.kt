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

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Topic
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.notifications.AndroidNotificationChannelManager
import com.norbertotaveras.lantern.notifications.AndroidNotificationPermissionManager
import com.norbertotaveras.lantern.notifications.DefaultNotificationPayloadParser
import com.norbertotaveras.lantern.notifications.NotificationChannelConfig
import com.norbertotaveras.lantern.notifications.NotificationChannelId
import com.norbertotaveras.lantern.notifications.NotificationChannelImportance
import com.norbertotaveras.lantern.notifications.NotificationTopic
import com.norbertotaveras.lantern.notifications.NotificationToken
import com.norbertotaveras.lantern.notifications.firebase.FirebaseMessagingTokenProvider
import com.norbertotaveras.lantern.notifications.firebase.FirebaseMessagingTopicManager
import com.norbertotaveras.lantern.permissions.AndroidPermissionManager
import com.norbertotaveras.lantern.permissions.PermissionRequestLauncher
import com.norbertotaveras.lanternsample.components.DemoMetric
import com.norbertotaveras.lanternsample.components.DemoSection
import com.norbertotaveras.lanternsample.components.FeatureScreen
import com.norbertotaveras.lanternsample.components.InfoRow
import com.norbertotaveras.lanternsample.components.MetricRow
import com.norbertotaveras.lanternsample.components.PrimaryDemoButton
import com.norbertotaveras.lanternsample.components.SecondaryDemoButton
import com.norbertotaveras.lanternsample.components.StatusMessage
import kotlinx.coroutines.launch
import kotlin.coroutines.resume

@Composable
fun NotificationsScreen() {
    val context = LocalContext.current
    val activity = context as? Activity
    val coroutineScope = rememberCoroutineScope()
    val permissionLauncher = rememberNotificationPermissionLauncher()
    val permissionManager = remember(context, activity, permissionLauncher) {
        AndroidPermissionManager(
            context = context,
            requestLauncher = permissionLauncher,
            rationaleProvider = { manifestPermission ->
                activity?.let {
                    ActivityCompat.shouldShowRequestPermissionRationale(it, manifestPermission)
                } ?: false
            }
        )
    }
    val notificationPermissionManager = remember(permissionManager) {
        AndroidNotificationPermissionManager(permissionManager)
    }
    val channelManager = remember(context) { AndroidNotificationChannelManager(context) }
    val tokenProvider = remember { FirebaseMessagingTokenProvider() }
    val topicManager = remember { FirebaseMessagingTopicManager() }
    val token by tokenProvider.tokenUpdates.collectAsState(initial = null)
    val parser = remember { DefaultNotificationPayloadParser() }
    val payload = remember {
        parser.parse(
            mapOf(
                "title" to "Lantern",
                "body" to "Notification payload parsed by the SDK.",
                "deep_link" to "mf://open/notifications?id=42",
                "dl_param_campaign" to "sample"
            )
        )
    }
    val topicResult = remember { NotificationTopic.from("product-updates") }
    val topic = (topicResult as? SdkResult.Success)?.data
    var message by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    FeatureScreen(
        title = "Notifications",
        subtitle = "Parse payloads, create channels, model topics, and reuse permission helpers.",
        icon = Icons.Filled.Notifications,
        status = notificationPermissionManager.check().status.name
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Payload parser", value = "Live"),
                DemoMetric(label = "Channel API", value = "Live"),
                DemoMetric(label = "FCM token", value = token?.shortValue() ?: "Not loaded")
            )
        )

        DemoSection(
            title = "Channel and permission",
            description = "Creates a sample channel and checks notification permission state.",
            leadingIcon = Icons.Filled.Notifications
        ) {
            PrimaryDemoButton(
                text = "Create sample channel",
                icon = Icons.Filled.Tune,
                onClick = {
                    coroutineScope.launch {
                        when (val result = channelManager.createChannel(sampleChannelConfig)) {
                            is SdkResult.Success -> {
                                errorMessage = null
                                message = "Channel '${sampleChannelConfig.id.value}' is ready."
                            }
                            is SdkResult.Failure -> {
                                errorMessage = result.error.message
                            }
                        }
                    }
                }
            )

            SecondaryDemoButton(
                text = "Request notification permission",
                icon = Icons.Filled.Notifications,
                onClick = {
                    coroutineScope.launch {
                        when (val result = notificationPermissionManager.request()) {
                            is SdkResult.Success -> {
                                errorMessage = null
                                message = "Notification permission: ${result.data.status.name}."
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
            title = "Parsed payload",
            description = "The SDK maps common FCM data keys into provider-neutral notification models.",
            leadingIcon = Icons.Filled.Topic
        ) {
            when (payload) {
                is SdkResult.Success -> {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        InfoRow(label = "Title", value = payload.data.title ?: "None")
                        InfoRow(label = "Body", value = payload.data.body ?: "None")
                        InfoRow(label = "Deep link", value = payload.data.deepLink?.uri ?: "None")
                        InfoRow(label = "Route", value = payload.data.deepLink?.route ?: "None")
                    }
                }
                is SdkResult.Failure -> {
                    InfoRow(label = "Payload", value = payload.error.message)
                }
            }
        }

        DemoSection(
            title = "Firebase Messaging boundary",
            description = "Fetch this device's FCM token and manage a sample topic through the Firebase-backed SDK module.",
            leadingIcon = Icons.Filled.Topic
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                PrimaryDemoButton(
                    text = "Fetch FCM token",
                    icon = Icons.Filled.Notifications,
                    onClick = {
                        coroutineScope.launch {
                            when (val result = tokenProvider.getToken()) {
                                is SdkResult.Success -> {
                                    errorMessage = null
                                    message = "FCM token loaded: ${result.data.shortValue()}."
                                }
                                is SdkResult.Failure -> {
                                    errorMessage = result.error.message
                                }
                            }
                        }
                    }
                )

                SecondaryDemoButton(
                    text = "Delete FCM token",
                    icon = Icons.Filled.Delete,
                    onClick = {
                        coroutineScope.launch {
                            when (val result = tokenProvider.deleteToken()) {
                                is SdkResult.Success -> {
                                    errorMessage = null
                                    message = "FCM token deleted for this app instance."
                                }
                                is SdkResult.Failure -> {
                                    errorMessage = result.error.message
                                }
                            }
                        }
                    }
                )

                SecondaryDemoButton(
                    text = "Subscribe sample topic",
                    icon = Icons.Filled.Topic,
                    enabled = topic != null,
                    onClick = {
                        coroutineScope.launch {
                            val notificationTopic = topic ?: return@launch
                            when (val result = topicManager.subscribe(notificationTopic)) {
                                is SdkResult.Success -> {
                                    errorMessage = null
                                    message = "Subscribed to '${notificationTopic.value}'."
                                }
                                is SdkResult.Failure -> {
                                    errorMessage = result.error.message
                                }
                            }
                        }
                    }
                )

                SecondaryDemoButton(
                    text = "Unsubscribe sample topic",
                    icon = Icons.Filled.Topic,
                    enabled = topic != null,
                    onClick = {
                        coroutineScope.launch {
                            val notificationTopic = topic ?: return@launch
                            when (val result = topicManager.unsubscribe(notificationTopic)) {
                                is SdkResult.Success -> {
                                    errorMessage = null
                                    message = "Unsubscribed from '${notificationTopic.value}'."
                                }
                                is SdkResult.Failure -> {
                                    errorMessage = result.error.message
                                }
                            }
                        }
                    }
                )

                InfoRow(label = "Topic", value = topicResult.topicName())
                InfoRow(label = "Token", value = token?.shortValue() ?: "Not loaded")
                InfoRow(label = "Token provider", value = token?.provider?.name ?: "FirebaseCloudMessaging")
                InfoRow(label = "Provider module", value = "notifications-firebase")
                InfoRow(label = "Push sending", value = "Backend-owned")
            }
        }

        StatusMessage(message = message, errorMessage = errorMessage)
    }
}

@Composable
private fun rememberNotificationPermissionLauncher(): PermissionRequestLauncher {
    var callback by remember { mutableStateOf<((Map<String, Boolean>) -> Unit)?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        callback?.invoke(result)
        callback = null
    }

    return remember {
        PermissionRequestLauncher { manifestPermissions ->
            if (manifestPermissions.isEmpty()) {
                emptyMap()
            } else {
                kotlinx.coroutines.suspendCancellableCoroutine { continuation ->
                    callback = { result ->
                        if (continuation.isActive) {
                            continuation.resume(result)
                        }
                    }
                    launcher.launch(manifestPermissions.toTypedArray())
                    continuation.invokeOnCancellation { callback = null }
                }
            }
        }
    }
}

private val sampleChannelConfig = NotificationChannelConfig(
    id = NotificationChannelId.unsafe("sample_updates"),
    name = "Sample updates",
    description = "Lantern sample notifications.",
    importance = NotificationChannelImportance.Default
)

private fun SdkResult<NotificationTopic>.topicName(): String {
    return when (this) {
        is SdkResult.Success -> data.value
        is SdkResult.Failure -> error.message
    }
}

private fun NotificationToken.shortValue(): String {
    return if (value.length <= TOKEN_PREVIEW_LENGTH * 2) {
        value
    } else {
        "${value.take(TOKEN_PREVIEW_LENGTH)}...${value.takeLast(TOKEN_PREVIEW_LENGTH)}"
    }
}

private const val TOKEN_PREVIEW_LENGTH = 8
