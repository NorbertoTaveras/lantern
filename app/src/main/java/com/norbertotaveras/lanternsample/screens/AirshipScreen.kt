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
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.notifications.NotificationChannelConfig
import com.norbertotaveras.lantern.notifications.NotificationChannelId
import com.norbertotaveras.lantern.notifications.NotificationChannelImportance
import com.norbertotaveras.lantern.notifications.NotificationToken
import com.norbertotaveras.lantern.notifications.airship.AirshipAudienceAttributeValue
import com.norbertotaveras.lantern.notifications.airship.AirshipAudienceGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipAudienceManager
import com.norbertotaveras.lantern.notifications.airship.AirshipContactGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipContactManager
import com.norbertotaveras.lantern.notifications.airship.AirshipContactSubscriptionScope
import com.norbertotaveras.lantern.notifications.airship.AirshipNotificationTokenProvider
import com.norbertotaveras.lantern.notifications.airship.AirshipPrivacyFeature
import com.norbertotaveras.lantern.notifications.airship.AirshipPrivacyGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipPrivacyManager
import com.norbertotaveras.lantern.notifications.airship.AirshipPushEvent
import com.norbertotaveras.lantern.notifications.airship.AirshipPushEventGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipPushEventType
import com.norbertotaveras.lantern.notifications.airship.AirshipPushEventsManager
import com.norbertotaveras.lantern.notifications.airship.AirshipPushGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipPushNotificationStatus
import com.norbertotaveras.lantern.notifications.airship.AirshipUserNotificationsManager
import com.norbertotaveras.lanternsample.components.DemoMetric
import com.norbertotaveras.lanternsample.components.DemoSection
import com.norbertotaveras.lanternsample.components.FeatureScreen
import com.norbertotaveras.lanternsample.components.InfoRow
import com.norbertotaveras.lanternsample.components.MetricRow
import com.norbertotaveras.lanternsample.components.PrimaryDemoButton
import com.norbertotaveras.lanternsample.components.SecondaryDemoButton
import com.norbertotaveras.lanternsample.components.StatusMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Composable
fun AirshipScreen() {
    val coroutineScope = rememberCoroutineScope()
    val gateway = remember { SampleAirshipGateway() }
    val tokenProvider = remember(gateway) { AirshipNotificationTokenProvider(gateway) }
    val notificationManager = remember(gateway) { AirshipUserNotificationsManager(gateway) }
    val audienceManager = remember(gateway) { AirshipAudienceManager(gateway) }
    val pushEventsManager = remember(gateway) { AirshipPushEventsManager(gateway) }
    val contactManager = remember(gateway) { AirshipContactManager(gateway) }
    val privacyManager = remember(gateway) { AirshipPrivacyManager(gateway) }
    val latestEvent by pushEventsManager.observePushEvents().collectAsState(initial = gateway.latestEvent)
    var message by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    FeatureScreen(
        title = "Airship",
        subtitle = "Exercise Lantern's Airship push, channel audience, contact, and privacy helpers without committing Airship credentials.",
        icon = Icons.Filled.Notifications,
        status = "Live"
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Channel ID", value = gateway.channelId?.shortValue() ?: "Pending"),
                DemoMetric(label = "Notifications", value = if (gateway.userNotificationsEnabled) "Enabled" else "Disabled"),
                DemoMetric(label = "Privacy features", value = gateway.enabledFeatures.size.toString())
            )
        )

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
                InfoRow(label = "App-owned setup", value = "Credentials, FCM, icons, campaigns")
                InfoRow(label = "Future modules", value = "Message Center, Preference Center, In-App Experiences")
            }
        }

        StatusMessage(message = message, errorMessage = errorMessage)
    }
}

private class SampleAirshipGateway :
    AirshipPushGateway,
    AirshipAudienceGateway,
    AirshipPushEventGateway,
    AirshipContactGateway,
    AirshipPrivacyGateway {

    var channelId by mutableStateOf<String?>("airship-channel-demo-123456")
    var userNotificationsEnabled by mutableStateOf(true)
    var foregroundDisplayEnabled by mutableStateOf(true)
    var createdChannelId by mutableStateOf<String?>(null)
    var tags by mutableStateOf(setOf("beta"))
    var channelAttributes by mutableStateOf<Map<String, AirshipAudienceAttributeValue>>(emptyMap())
    var channelSubscriptionLists by mutableStateOf(setOf("weekly-updates"))
    var namedUserId by mutableStateOf<String?>(null)
    var contactAttributes by mutableStateOf<Map<String, AirshipAudienceAttributeValue>>(emptyMap())
    var contactSubscriptionLists by mutableStateOf<Map<AirshipContactSubscriptionScope, Set<String>>>(emptyMap())
    var enabledFeatures by mutableStateOf(
        setOf(
            AirshipPrivacyFeature.Push,
            AirshipPrivacyFeature.TagsAndAttributes,
            AirshipPrivacyFeature.Contacts
        )
    )
    var latestEvent by mutableStateOf(
        AirshipPushEvent(
            type = AirshipPushEventType.StatusChanged,
            alert = "Airship demo gateway is ready.",
            status = currentPushStatus()
        )
    )

    private val eventState = MutableStateFlow(latestEvent)

    override suspend fun getChannelId(): String? = channelId

    override suspend fun areUserNotificationsEnabled(): Boolean = userNotificationsEnabled

    override suspend fun setUserNotificationsEnabled(enabled: Boolean) {
        userNotificationsEnabled = enabled
        publishEvent(
            AirshipPushEvent(
                type = AirshipPushEventType.StatusChanged,
                alert = "User notifications ${if (enabled) "enabled" else "disabled"}.",
                status = currentPushStatus()
            )
        )
    }

    override suspend fun getTags(): Set<String> = tags

    override suspend fun addTags(tags: Set<String>) {
        this.tags = this.tags + tags
    }

    override suspend fun removeTags(tags: Set<String>) {
        this.tags = this.tags - tags
    }

    override suspend fun clearTags() {
        tags = emptySet()
    }

    override suspend fun setAttribute(
        name: String,
        value: AirshipAudienceAttributeValue
    ) {
        channelAttributes = channelAttributes + (name to value)
        contactAttributes = contactAttributes + (name to value)
    }

    override suspend fun removeAttribute(name: String) {
        channelAttributes = channelAttributes - name
        contactAttributes = contactAttributes - name
    }

    override suspend fun subscribeToLists(listIds: Set<String>) {
        channelSubscriptionLists = channelSubscriptionLists + listIds
    }

    override suspend fun unsubscribeFromLists(listIds: Set<String>) {
        channelSubscriptionLists = channelSubscriptionLists - listIds
    }

    override fun observePushEvents(): Flow<AirshipPushEvent> = eventState.asStateFlow()

    override suspend fun getPushNotificationStatus(): AirshipPushNotificationStatus = currentPushStatus()

    override suspend fun createNotificationChannel(config: NotificationChannelConfig) {
        createdChannelId = config.id.value
    }

    override suspend fun setForegroundNotificationDisplayEnabled(enabled: Boolean) {
        foregroundDisplayEnabled = enabled
    }

    override suspend fun getNamedUserId(): String? = namedUserId

    override suspend fun identify(namedUserId: String) {
        this.namedUserId = namedUserId
    }

    override suspend fun reset() {
        namedUserId = null
        contactAttributes = emptyMap()
        contactSubscriptionLists = emptyMap()
    }

    override suspend fun subscribeToLists(
        listIds: Set<String>,
        scope: AirshipContactSubscriptionScope
    ) {
        contactSubscriptionLists = contactSubscriptionLists + (
            scope to (contactSubscriptionLists[scope].orEmpty() + listIds)
        )
    }

    override suspend fun unsubscribeFromLists(
        listIds: Set<String>,
        scope: AirshipContactSubscriptionScope
    ) {
        contactSubscriptionLists = contactSubscriptionLists + (
            scope to (contactSubscriptionLists[scope].orEmpty() - listIds)
        )
    }

    override suspend fun getEnabledFeatures(): Set<AirshipPrivacyFeature> = enabledFeatures

    override suspend fun setEnabledFeatures(features: Set<AirshipPrivacyFeature>) {
        enabledFeatures = features
    }

    override suspend fun enableFeatures(features: Set<AirshipPrivacyFeature>) {
        enabledFeatures = enabledFeatures + features
    }

    override suspend fun disableFeatures(features: Set<AirshipPrivacyFeature>) {
        enabledFeatures = enabledFeatures - features
    }

    fun emitSampleEvent() {
        publishEvent(
            AirshipPushEvent(
                type = AirshipPushEventType.Received,
                title = "Lantern",
                alert = "Sample Airship push payload.",
                summary = "Demo event",
                sendId = "sample-send-id",
                metadata = "campaign=demo",
                notificationPosted = foregroundDisplayEnabled
            )
        )
    }

    private fun currentPushStatus(): AirshipPushNotificationStatus {
        return AirshipPushNotificationStatus(
            userNotificationsEnabled = userNotificationsEnabled,
            notificationsAllowed = true,
            pushPrivacyFeatureEnabled = AirshipPrivacyFeature.Push in enabledFeatures,
            pushTokenRegistered = !channelId.isNullOrBlank(),
            optedIn = userNotificationsEnabled && AirshipPrivacyFeature.Push in enabledFeatures
        )
    }

    private fun publishEvent(event: AirshipPushEvent) {
        latestEvent = event
        eventState.value = event
    }
}

private val sampleAirshipChannel = NotificationChannelConfig(
    id = NotificationChannelId.unsafe("airship_updates"),
    name = "Airship updates",
    description = "Lantern sample Airship notifications.",
    importance = NotificationChannelImportance.Default
)

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
