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

package com.norbertotaveras.lanternsample.airship

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.norbertotaveras.lantern.notifications.NotificationChannelConfig
import com.norbertotaveras.lantern.notifications.NotificationChannelId
import com.norbertotaveras.lantern.notifications.NotificationChannelImportance
import com.norbertotaveras.lantern.notifications.airship.AirshipAudienceAttributeValue
import com.norbertotaveras.lantern.notifications.airship.AirshipAudienceGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipConfigOptionsFactory
import com.norbertotaveras.lantern.notifications.airship.AirshipContactGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipContactSubscriptionScope
import com.norbertotaveras.lantern.notifications.airship.AirshipNotificationConfig
import com.norbertotaveras.lantern.notifications.airship.AirshipNotificationSite
import com.norbertotaveras.lantern.notifications.airship.AirshipPrivacyFeature
import com.norbertotaveras.lantern.notifications.airship.AirshipPrivacyGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipPushEvent
import com.norbertotaveras.lantern.notifications.airship.AirshipPushEventGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipPushEventType
import com.norbertotaveras.lantern.notifications.airship.AirshipPushGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipPushNotificationStatus
import com.norbertotaveras.lantern.notifications.airship.AirshipSdkAudienceGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipSdkContactGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipSdkPrivacyGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipSdkPushEventGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipSdkPushGateway
import com.norbertotaveras.lanternsample.BuildConfig
import com.urbanairship.Airship
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach

internal val sampleAirshipChannel = NotificationChannelConfig(
    id = NotificationChannelId.unsafe("airship_updates"),
    name = "Airship updates",
    description = "Lantern sample Airship notifications.",
    importance = NotificationChannelImportance.Default
)

internal interface AirshipSampleGateway :
    AirshipPushGateway,
    AirshipAudienceGateway,
    AirshipPushEventGateway,
    AirshipContactGateway,
    AirshipPrivacyGateway {
    val statusLabel: String
    val description: String
    val runtimeMode: String
    val channelId: String?
    val userNotificationsEnabled: Boolean
    val foregroundDisplayEnabled: Boolean
    val createdChannelId: String?
    val tags: Set<String>
    val channelAttributes: Map<String, AirshipAudienceAttributeValue>
    val channelSubscriptionLists: Set<String>
    val namedUserId: String?
    val contactAttributes: Map<String, AirshipAudienceAttributeValue>
    val contactSubscriptionLists: Map<AirshipContactSubscriptionScope, Set<String>>
    val enabledFeatures: Set<AirshipPrivacyFeature>
    val latestEvent: AirshipPushEvent

    fun emitSampleEvent()
}

internal fun createAirshipSampleGateway(application: Application): AirshipSampleGateway {
    val appKey = BuildConfig.AIRSHIP_APP_KEY.trim()
    val appSecret = BuildConfig.AIRSHIP_APP_SECRET.trim()
    if (appKey.isEmpty() || appSecret.isEmpty()) {
        return DemoAirshipGateway()
    }

    runCatching {
        if (!Airship.isFlyingOrTakingOff) {
            Airship.takeOff(
                application,
                AirshipConfigOptionsFactory.create(
                    AirshipNotificationConfig(
                        appKey = appKey,
                        appSecret = appSecret,
                        site = BuildConfig.AIRSHIP_SITE.toAirshipNotificationSite(),
                        notificationChannel = sampleAirshipChannel.id.value,
                        userNotificationsEnabled = true
                    )
                )
            )
        }
    }

    return if (Airship.isFlyingOrTakingOff) {
        RealAirshipGateway()
    } else {
        DemoAirshipGateway()
    }
}

private class DemoAirshipGateway : AirshipSampleGateway {
    override val statusLabel: String = "Demo"
    override val description: String =
        "Preview Lantern's Airship push, channel audience, contact, and privacy helpers with a credential-free demo gateway."
    override val runtimeMode: String = "Demo gateway"

    override var channelId by mutableStateOf<String?>("airship-channel-demo-123456")
    override var userNotificationsEnabled by mutableStateOf(true)
    override var foregroundDisplayEnabled by mutableStateOf(true)
    override var createdChannelId by mutableStateOf<String?>(null)
    override var tags by mutableStateOf(setOf("beta"))
    override var channelAttributes by mutableStateOf<Map<String, AirshipAudienceAttributeValue>>(emptyMap())
    override var channelSubscriptionLists by mutableStateOf(setOf("weekly-updates"))
    override var namedUserId by mutableStateOf<String?>(null)
    override var contactAttributes by mutableStateOf<Map<String, AirshipAudienceAttributeValue>>(emptyMap())
    override var contactSubscriptionLists by mutableStateOf<Map<AirshipContactSubscriptionScope, Set<String>>>(emptyMap())
    override var enabledFeatures by mutableStateOf(
        setOf(
            AirshipPrivacyFeature.Push,
            AirshipPrivacyFeature.TagsAndAttributes,
            AirshipPrivacyFeature.Contacts
        )
    )
    override var latestEvent by mutableStateOf(
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

    override suspend fun setAttribute(name: String, value: AirshipAudienceAttributeValue) {
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

    override fun emitSampleEvent() {
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

private class RealAirshipGateway(
    private val pushGateway: AirshipSdkPushGateway = AirshipSdkPushGateway(),
    private val audienceGateway: AirshipSdkAudienceGateway = AirshipSdkAudienceGateway(),
    private val pushEventGateway: AirshipSdkPushEventGateway = AirshipSdkPushEventGateway(),
    private val contactGateway: AirshipSdkContactGateway = AirshipSdkContactGateway(),
    private val privacyGateway: AirshipSdkPrivacyGateway = AirshipSdkPrivacyGateway()
) : AirshipSampleGateway {
    override val statusLabel: String = "Real"
    override val description: String =
        "Exercise Lantern's Airship helpers against the Airship SDK using local app-owned credentials."
    override val runtimeMode: String = "Airship SDK"

    override var channelId by mutableStateOf<String?>(null)
    override var userNotificationsEnabled by mutableStateOf(false)
    override var foregroundDisplayEnabled by mutableStateOf(true)
    override var createdChannelId by mutableStateOf<String?>(null)
    override var tags by mutableStateOf(emptySet<String>())
    override var channelAttributes by mutableStateOf<Map<String, AirshipAudienceAttributeValue>>(emptyMap())
    override var channelSubscriptionLists by mutableStateOf(emptySet<String>())
    override var namedUserId by mutableStateOf<String?>(null)
    override var contactAttributes by mutableStateOf<Map<String, AirshipAudienceAttributeValue>>(emptyMap())
    override var contactSubscriptionLists by mutableStateOf<Map<AirshipContactSubscriptionScope, Set<String>>>(emptyMap())
    override var enabledFeatures by mutableStateOf(emptySet<AirshipPrivacyFeature>())
    override var latestEvent by mutableStateOf(
        AirshipPushEvent(
            type = AirshipPushEventType.StatusChanged,
            alert = "Airship SDK gateway is ready."
        )
    )

    override suspend fun getChannelId(): String? {
        channelId = pushGateway.getChannelId()
        return channelId
    }

    override suspend fun areUserNotificationsEnabled(): Boolean {
        userNotificationsEnabled = pushGateway.areUserNotificationsEnabled()
        return userNotificationsEnabled
    }

    override suspend fun setUserNotificationsEnabled(enabled: Boolean) {
        pushGateway.setUserNotificationsEnabled(enabled)
        userNotificationsEnabled = enabled
    }

    override suspend fun getTags(): Set<String> {
        tags = audienceGateway.getTags()
        return tags
    }

    override suspend fun addTags(tags: Set<String>) {
        audienceGateway.addTags(tags)
        this.tags = this.tags + tags
    }

    override suspend fun removeTags(tags: Set<String>) {
        audienceGateway.removeTags(tags)
        this.tags = this.tags - tags
    }

    override suspend fun clearTags() {
        audienceGateway.clearTags()
        tags = emptySet()
    }

    override suspend fun setAttribute(name: String, value: AirshipAudienceAttributeValue) {
        audienceGateway.setAttribute(name, value)
        channelAttributes = channelAttributes + (name to value)
    }

    override suspend fun removeAttribute(name: String) {
        audienceGateway.removeAttribute(name)
        channelAttributes = channelAttributes - name
    }

    override suspend fun subscribeToLists(listIds: Set<String>) {
        audienceGateway.subscribeToLists(listIds)
        channelSubscriptionLists = channelSubscriptionLists + listIds
    }

    override suspend fun unsubscribeFromLists(listIds: Set<String>) {
        audienceGateway.unsubscribeFromLists(listIds)
        channelSubscriptionLists = channelSubscriptionLists - listIds
    }

    override fun observePushEvents(): Flow<AirshipPushEvent> {
        return pushEventGateway.observePushEvents()
            .onEach { event -> latestEvent = event }
    }

    override suspend fun getPushNotificationStatus(): AirshipPushNotificationStatus {
        return pushEventGateway.getPushNotificationStatus()
            .also { status -> userNotificationsEnabled = status.userNotificationsEnabled }
    }

    override suspend fun createNotificationChannel(config: NotificationChannelConfig) {
        pushEventGateway.createNotificationChannel(config)
        createdChannelId = config.id.value
    }

    override suspend fun setForegroundNotificationDisplayEnabled(enabled: Boolean) {
        pushEventGateway.setForegroundNotificationDisplayEnabled(enabled)
        foregroundDisplayEnabled = enabled
    }

    override suspend fun getNamedUserId(): String? {
        namedUserId = contactGateway.getNamedUserId()
        return namedUserId
    }

    override suspend fun identify(namedUserId: String) {
        contactGateway.identify(namedUserId)
        this.namedUserId = namedUserId
    }

    override suspend fun reset() {
        contactGateway.reset()
        namedUserId = null
        contactAttributes = emptyMap()
        contactSubscriptionLists = emptyMap()
    }

    override suspend fun subscribeToLists(
        listIds: Set<String>,
        scope: AirshipContactSubscriptionScope
    ) {
        contactGateway.subscribeToLists(listIds, scope)
        contactSubscriptionLists = contactSubscriptionLists + (
            scope to (contactSubscriptionLists[scope].orEmpty() + listIds)
        )
    }

    override suspend fun unsubscribeFromLists(
        listIds: Set<String>,
        scope: AirshipContactSubscriptionScope
    ) {
        contactGateway.unsubscribeFromLists(listIds, scope)
        contactSubscriptionLists = contactSubscriptionLists + (
            scope to (contactSubscriptionLists[scope].orEmpty() - listIds)
        )
    }

    override suspend fun getEnabledFeatures(): Set<AirshipPrivacyFeature> {
        enabledFeatures = privacyGateway.getEnabledFeatures()
        return enabledFeatures
    }

    override suspend fun setEnabledFeatures(features: Set<AirshipPrivacyFeature>) {
        privacyGateway.setEnabledFeatures(features)
        enabledFeatures = features
    }

    override suspend fun enableFeatures(features: Set<AirshipPrivacyFeature>) {
        privacyGateway.enableFeatures(features)
        enabledFeatures = enabledFeatures + features
    }

    override suspend fun disableFeatures(features: Set<AirshipPrivacyFeature>) {
        privacyGateway.disableFeatures(features)
        enabledFeatures = enabledFeatures - features
    }

    override fun emitSampleEvent() {
        latestEvent = AirshipPushEvent(
            type = AirshipPushEventType.Received,
            alert = "Waiting for a real Airship push event."
        )
    }
}

private fun String.toAirshipNotificationSite(): AirshipNotificationSite {
    return when (uppercase()) {
        "EU" -> AirshipNotificationSite.EU
        else -> AirshipNotificationSite.US
    }
}
