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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.notifications.NotificationToken
import com.norbertotaveras.lantern.notifications.airship.AirshipAudienceAttributeValue
import com.norbertotaveras.lantern.notifications.airship.AirshipAudienceManager
import com.norbertotaveras.lantern.notifications.airship.AirshipContactManager
import com.norbertotaveras.lantern.notifications.airship.AirshipContactSubscriptionScope
import com.norbertotaveras.lantern.notifications.airship.AirshipNotificationTokenProvider
import com.norbertotaveras.lantern.notifications.airship.AirshipPrivacyFeature
import com.norbertotaveras.lantern.notifications.airship.AirshipPrivacyManager
import com.norbertotaveras.lantern.notifications.airship.AirshipPushEventsManager
import com.norbertotaveras.lantern.notifications.airship.AirshipUserNotificationsManager
import com.norbertotaveras.lanternsample.airship.createAirshipSampleGateway
import com.norbertotaveras.lanternsample.airship.sampleAirshipChannel
import com.norbertotaveras.lanternsample.components.DemoMetric
import com.norbertotaveras.lanternsample.components.DemoSection
import com.norbertotaveras.lanternsample.components.FeatureScreen
import com.norbertotaveras.lanternsample.components.InfoRow
import com.norbertotaveras.lanternsample.components.MetricRow
import com.norbertotaveras.lanternsample.components.PrimaryDemoButton
import com.norbertotaveras.lanternsample.components.SecondaryDemoButton
import com.norbertotaveras.lanternsample.components.StatusMessage
import kotlinx.coroutines.launch

@Composable
fun AirshipScreen() {
    val application = LocalContext.current.applicationContext as android.app.Application
    val coroutineScope = rememberCoroutineScope()
    val gateway = remember { createAirshipSampleGateway(application) }
    val tokenProvider = remember(gateway) { AirshipNotificationTokenProvider(gateway) }
    val notificationManager = remember(gateway) { AirshipUserNotificationsManager(gateway) }
    val audienceManager = remember(gateway) { AirshipAudienceManager(gateway) }
    val pushEventsManager = remember(gateway) { AirshipPushEventsManager(gateway) }
    val contactManager = remember(gateway) { AirshipContactManager(gateway) }
    val privacyManager = remember(gateway) { AirshipPrivacyManager(gateway) }
    val latestEvent by pushEventsManager.observePushEvents().collectAsState(initial = gateway.latestEvent)
    var message by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(gateway) {
        gateway.refreshState()
    }

    FeatureScreen(
        title = "Airship",
        subtitle = gateway.description,
        icon = Icons.Filled.Notifications,
        status = gateway.statusLabel
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Channel ID", value = if (gateway.channelId == null) "Pending" else "Ready"),
                DemoMetric(label = "Notifications", value = if (gateway.userNotificationsEnabled) "Enabled" else "Disabled"),
                DemoMetric(label = "Privacy features", value = gateway.enabledFeatures.size.toString())
            )
        )

        DemoSection(
            title = "Sample setup",
            description = "The screen runs in demo mode by default. Add Airship credentials to local.properties to switch to the real Airship SDK gateways.",
            leadingIcon = Icons.Filled.Cloud
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                PrimaryDemoButton(
                    text = "Refresh Airship state",
                    icon = Icons.Filled.Refresh,
                    onClick = {
                        coroutineScope.launch {
                            runCatching { gateway.refreshState() }
                                .onSuccess {
                                    errorMessage = null
                                    message = "Airship sample state refreshed."
                                }
                                .onFailure {
                                    message = null
                                    errorMessage = it.message ?: "Unable to refresh Airship sample state."
                                }
                        }
                    }
                )

                InfoRow(label = "Runtime mode", value = gateway.runtimeMode)
                InfoRow(label = "App key", value = if (gateway.setupStatus.appKeyConfigured) "Configured" else "Missing")
                InfoRow(label = "App secret", value = if (gateway.setupStatus.appSecretConfigured) "Configured" else "Missing")
                InfoRow(label = "Site", value = gateway.setupStatus.site)
                InfoRow(label = "Airship initialized", value = if (gateway.setupStatus.initialized) "Yes" else "No")
                gateway.setupStatus.initializationError?.let { error ->
                    InfoRow(label = "Initialization error", value = error)
                }
            }
        }

        DemoSection(
            title = "Push and channel",
            description = "Airship credentials, FCM provider setup, icons, and campaign sending stay app-owned. Lantern reads and updates Airship state through gateways.",
            leadingIcon = Icons.Filled.Notifications
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                PrimaryDemoButton(
                    text = "Fetch Airship channel token",
                    icon = Icons.Filled.Cloud,
                    onClick = {
                        coroutineScope.launch {
                            when (val result = tokenProvider.getToken()) {
                                is SdkResult.Success -> {
                                    errorMessage = null
                                    message = "Airship token loaded: ${result.data.shortValue()}."
                                }
                                is SdkResult.Failure -> {
                                    message = null
                                    errorMessage = result.error.message
                                }
                            }
                        }
                    }
                )

                SecondaryDemoButton(
                    text = if (gateway.userNotificationsEnabled) {
                        "Disable user notifications"
                    } else {
                        "Enable user notifications"
                    },
                    icon = Icons.Filled.Notifications,
                    onClick = {
                        coroutineScope.launch {
                            val result = if (gateway.userNotificationsEnabled) {
                                notificationManager.disableUserNotifications()
                            } else {
                                notificationManager.enableUserNotifications()
                            }
                            result.report(
                                successMessage = "User notifications are ${if (gateway.userNotificationsEnabled) "enabled" else "disabled"}.",
                                onMessage = {
                                    errorMessage = null
                                    message = it
                                },
                                onError = {
                                    message = null
                                    errorMessage = it
                                }
                            )
                            if (result is SdkResult.Success) {
                                errorMessage = null
                            }
                        }
                    }
                )

                SecondaryDemoButton(
                    text = "Create Airship sample channel",
                    icon = Icons.Filled.Tune,
                    onClick = {
                        coroutineScope.launch {
                            pushEventsManager.createNotificationChannel(sampleAirshipChannel)
                                .report(
                                    successMessage = "Airship channel '${sampleAirshipChannel.id.value}' is ready.",
                                    onMessage = {
                                        errorMessage = null
                                        message = it
                                    },
                                    onError = {
                                        message = null
                                        errorMessage = it
                                    }
                                )
                        }
                    }
                )

                SecondaryDemoButton(
                    text = if (gateway.foregroundDisplayEnabled) {
                        "Disable foreground display"
                    } else {
                        "Enable foreground display"
                    },
                    icon = Icons.Filled.Tune,
                    onClick = {
                        coroutineScope.launch {
                            val enabled = !gateway.foregroundDisplayEnabled
                            pushEventsManager.setForegroundNotificationDisplayEnabled(enabled)
                                .report(
                                    successMessage = "Foreground notification display is ${if (enabled) "enabled" else "disabled"}.",
                                    onMessage = {
                                        errorMessage = null
                                        message = it
                                    },
                                    onError = {
                                        message = null
                                        errorMessage = it
                                    }
                                )
                        }
                    }
                )

                SecondaryDemoButton(
                    text = "Simulate push event",
                    icon = Icons.Filled.Add,
                    onClick = {
                        gateway.emitSampleEvent()
                        errorMessage = null
                        message = "Sample Airship push event emitted."
                    }
                )

                InfoRow(label = "Channel ID", value = gateway.channelId ?: "Pending")
                InfoRow(label = "Created channel", value = gateway.createdChannelId ?: "None")
                InfoRow(label = "Foreground display", value = if (gateway.foregroundDisplayEnabled) "Enabled" else "Disabled")
                InfoRow(label = "Latest event", value = latestEvent.type.name)
                InfoRow(label = "Event alert", value = latestEvent.alert ?: "None")
            }
        }

        DemoSection(
            title = "Channel audience",
            description = "Use channel tags, attributes, and subscription lists for push audience targeting.",
            leadingIcon = Icons.Filled.Tune
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SecondaryDemoButton(
                    text = "Add channel audience data",
                    icon = Icons.Filled.Add,
                    onClick = {
                        coroutineScope.launch {
                            val tagResult = audienceManager.addTags(setOf("premium", "android"))
                            val attributeResult = audienceManager.setAttribute(
                                name = "plan",
                                value = AirshipAudienceAttributeValue.StringValue("premium")
                            )
                            val listResult = audienceManager.subscribeToLists(setOf("weekly-updates"))
                            listOf(tagResult, attributeResult, listResult).firstFailure()?.let {
                                message = null
                                errorMessage = it.error.message
                            } ?: run {
                                errorMessage = null
                                message = "Channel tags, attributes, and subscription lists were updated."
                            }
                        }
                    }
                )

                SecondaryDemoButton(
                    text = "Clear channel tags",
                    icon = Icons.Filled.Delete,
                    onClick = {
                        coroutineScope.launch {
                            audienceManager.clearTags()
                                .report(
                                    successMessage = "Channel tags cleared.",
                                    onMessage = {
                                        errorMessage = null
                                        message = it
                                    },
                                    onError = {
                                        message = null
                                        errorMessage = it
                                    }
                                )
                        }
                    }
                )

                InfoRow(label = "Tags", value = gateway.tags.displaySet())
                InfoRow(label = "Attributes", value = gateway.channelAttributes.size.toString())
                InfoRow(label = "Subscription lists", value = gateway.channelSubscriptionLists.displaySet())
            }
        }

        DemoSection(
            title = "Contact identity",
            description = "Contact helpers let an app connect its signed-in user model to Airship named users, attributes, and scoped subscription lists.",
            leadingIcon = Icons.Filled.AccountCircle
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SecondaryDemoButton(
                    text = "Identify sample contact",
                    icon = Icons.Filled.AccountCircle,
                    onClick = {
                        coroutineScope.launch {
                            val identifyResult = contactManager.identify("sample-user-123")
                            val attributeResult = contactManager.setAttribute(
                                name = "tier",
                                value = AirshipAudienceAttributeValue.StringValue("gold")
                            )
                            val listResult = contactManager.subscribeToLists(
                                listIds = setOf("weekly-updates"),
                                scope = AirshipContactSubscriptionScope.Email
                            )
                            listOf(identifyResult, attributeResult, listResult).firstFailure()?.let {
                                message = null
                                errorMessage = it.error.message
                            } ?: run {
                                errorMessage = null
                                message = "Contact identity, attributes, and email list subscription were updated."
                            }
                        }
                    }
                )

                SecondaryDemoButton(
                    text = "Reset contact",
                    icon = Icons.Filled.Delete,
                    onClick = {
                        coroutineScope.launch {
                            contactManager.reset()
                                .report(
                                    successMessage = "Contact reset to anonymous state.",
                                    onMessage = {
                                        errorMessage = null
                                        message = it
                                    },
                                    onError = {
                                        message = null
                                        errorMessage = it
                                    }
                                )
                        }
                    }
                )

                InfoRow(label = "Named user", value = gateway.namedUserId ?: "Anonymous")
                InfoRow(label = "Contact attributes", value = gateway.contactAttributes.size.toString())
                InfoRow(label = "Email lists", value = gateway.contactSubscriptionLists[AirshipContactSubscriptionScope.Email].orEmpty().displaySet())
            }
        }

        DemoSection(
            title = "Privacy and data collection",
            description = "Apps can connect their own consent flow to Airship feature toggles without leaking Airship APIs into provider-neutral code.",
            leadingIcon = Icons.Filled.PrivacyTip
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SecondaryDemoButton(
                    text = "Enable messaging consent set",
                    icon = Icons.Filled.PrivacyTip,
                    onClick = {
                        coroutineScope.launch {
                            privacyManager.setEnabledFeatures(
                                setOf(
                                    AirshipPrivacyFeature.Push,
                                    AirshipPrivacyFeature.TagsAndAttributes,
                                    AirshipPrivacyFeature.Contacts,
                                    AirshipPrivacyFeature.MessageCenter,
                                    AirshipPrivacyFeature.InAppAutomation
                                )
                            ).report(
                                successMessage = "Messaging-related Airship privacy features are enabled.",
                                onMessage = {
                                    errorMessage = null
                                    message = it
                                },
                                onError = {
                                    message = null
                                    errorMessage = it
                                }
                            )
                        }
                    }
                )

                SecondaryDemoButton(
                    text = "Disable analytics collection",
                    icon = Icons.Filled.Delete,
                    onClick = {
                        coroutineScope.launch {
                            privacyManager.disableFeatures(setOf(AirshipPrivacyFeature.Analytics))
                                .report(
                                    successMessage = "Airship analytics collection disabled.",
                                    onMessage = {
                                        errorMessage = null
                                        message = it
                                    },
                                    onError = {
                                        message = null
                                        errorMessage = it
                                    }
                                )
                        }
                    }
                )

                InfoRow(label = "Enabled features", value = gateway.enabledFeatures.displaySet())
                InfoRow(label = "Runtime mode", value = gateway.runtimeMode)
                InfoRow(label = "Future modules", value = "Message Center, Preference Center, In-App Experiences")
            }
        }

        StatusMessage(message = message, errorMessage = errorMessage)
    }
}

private fun SdkResult<Unit>.report(
    successMessage: String,
    onMessage: (String) -> Unit,
    onError: (String) -> Unit
) {
    when (this) {
        is SdkResult.Success -> onMessage(successMessage)
        is SdkResult.Failure -> onError(error.message)
    }
}

private fun List<SdkResult<Unit>>.firstFailure(): SdkResult.Failure? {
    return firstOrNull { it is SdkResult.Failure } as? SdkResult.Failure
}

private fun Set<*>.displaySet(): String {
    return if (isEmpty()) {
        "None"
    } else {
        joinToString()
    }
}

private fun NotificationToken.shortValue(): String = value.shortValue()

private fun String.shortValue(): String {
    return if (length <= AIRSHIP_TOKEN_PREVIEW_LENGTH * 2) {
        this
    } else {
        "${take(AIRSHIP_TOKEN_PREVIEW_LENGTH)}...${takeLast(AIRSHIP_TOKEN_PREVIEW_LENGTH)}"
    }
}

private const val AIRSHIP_TOKEN_PREVIEW_LENGTH = 8
