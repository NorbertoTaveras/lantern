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

package com.norbertotaveras.lantern.consumer.smoke

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.norbertotaveras.lantern.analytics.AnalyticsEvent
import com.norbertotaveras.lantern.analytics.AnalyticsEventName
import com.norbertotaveras.lantern.analytics.AnalyticsProvider
import com.norbertotaveras.lantern.analytics.AnalyticsValue
import com.norbertotaveras.lantern.analytics.firebase.FirebaseAnalyticsProvider
import com.norbertotaveras.lantern.appversioning.AndroidAppVersionProvider
import com.norbertotaveras.lantern.appversioning.AppUpdatePolicy
import com.norbertotaveras.lantern.appversioning.AppVersion
import com.norbertotaveras.lantern.appversioning.DefaultAppUpdatePolicyEvaluator
import com.norbertotaveras.lantern.auth.core.AuthProvider
import com.norbertotaveras.lantern.auth.firebase.FirebaseAuthProvider
import com.norbertotaveras.lantern.auth.firebasegoogle.FirebaseGoogleAuthConfig
import com.norbertotaveras.lantern.auth.firebasegoogle.FirebaseGoogleAuthProvider
import com.norbertotaveras.lantern.auth.google.CredentialManagerGoogleAuthProvider
import com.norbertotaveras.lantern.auth.google.GoogleAuthConfig
import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkName
import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkRequest
import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkType
import com.norbertotaveras.lantern.backgroundwork.WorkManagerBackgroundWorkScheduler
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.deeplinks.DeepLinkConfig
import com.norbertotaveras.lantern.deeplinks.DefaultDeepLinkParser
import com.norbertotaveras.lantern.featureflags.FeatureFlag
import com.norbertotaveras.lantern.featureflags.FeatureFlagKey
import com.norbertotaveras.lantern.featureflags.FeatureFlagValue
import com.norbertotaveras.lantern.featureflags.RemoteConfigFeatureFlagProvider
import com.norbertotaveras.lantern.logging.AndroidSdkLogger
import com.norbertotaveras.lantern.mediapicker.MediaPickRequest
import com.norbertotaveras.lantern.mediapicker.MediaPicker
import com.norbertotaveras.lantern.mediapicker.MediaSelectionMode
import com.norbertotaveras.lantern.mediapicker.MediaType
import com.norbertotaveras.lantern.network.okhttp.NetworkConfig
import com.norbertotaveras.lantern.network.okhttp.NetworkLoggingLevel
import com.norbertotaveras.lantern.network.okhttp.NetworkRetryConfig
import com.norbertotaveras.lantern.network.okhttp.OkHttpNetworkClientFactory
import com.norbertotaveras.lantern.network.okhttp.TokenProvider
import com.norbertotaveras.lantern.notifications.DefaultNotificationPayloadParser
import com.norbertotaveras.lantern.notifications.airship.AirshipAudienceAttributeValue
import com.norbertotaveras.lantern.notifications.airship.AirshipAudienceGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipAudienceManager
import com.norbertotaveras.lantern.notifications.airship.AirshipConfigOptionsFactory
import com.norbertotaveras.lantern.notifications.airship.AirshipContactGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipContactManager
import com.norbertotaveras.lantern.notifications.airship.AirshipContactSubscriptionScope
import com.norbertotaveras.lantern.notifications.airship.AirshipNotificationConfig
import com.norbertotaveras.lantern.notifications.airship.AirshipNotificationSite
import com.norbertotaveras.lantern.notifications.airship.AirshipNotificationTokenProvider
import com.norbertotaveras.lantern.notifications.airship.AirshipPrivacyFeature
import com.norbertotaveras.lantern.notifications.airship.AirshipPrivacyGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipPrivacyManager
import com.norbertotaveras.lantern.notifications.airship.AirshipPushEvent
import com.norbertotaveras.lantern.notifications.airship.AirshipPushEventGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipPushEventType
import com.norbertotaveras.lantern.notifications.airship.AirshipPushEventsManager
import com.norbertotaveras.lantern.notifications.airship.AirshipPushNotificationStatus
import com.norbertotaveras.lantern.notifications.airship.AirshipPushGateway
import com.norbertotaveras.lantern.notifications.airship.AirshipUserNotificationsManager
import com.norbertotaveras.lantern.notifications.firebase.FirebaseMessagingTokenProvider
import com.norbertotaveras.lantern.notifications.NotificationChannelConfig
import com.norbertotaveras.lantern.notifications.NotificationChannelId
import com.norbertotaveras.lantern.permissions.AndroidPermissionManager
import com.norbertotaveras.lantern.permissions.PermissionRequestLauncher
import com.norbertotaveras.lantern.permissions.SdkPermission
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigDefaults
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigKey
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigProvider
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigValue
import com.norbertotaveras.lantern.remoteconfig.firebase.FirebaseRemoteConfigProvider
import com.norbertotaveras.lantern.securestorage.DataStoreSecureKeyValueStore
import com.norbertotaveras.lantern.securestorage.SecureStorageKey
import kotlinx.coroutines.flow.flowOf

@Suppress("UNUSED_VARIABLE", "unused")
internal object ReadmeUsageSmoke {
    suspend fun auth() {
        val authProvider: AuthProvider = FirebaseAuthProvider()

        when (val result = authProvider.signIn()) {
            is SdkResult.Success -> {
                val session = result.data
                val userId = session.userId
            }
            is SdkResult.Failure -> {
                val error = result.error
            }
        }

        val firebaseAuth = FirebaseAuthProvider()

        firebaseAuth.signInAnonymously()
        firebaseAuth.signInWithEmailAndPassword("user@example.com", "password")
        firebaseAuth.createUserWithEmailAndPassword("user@example.com", "password")
        firebaseAuth.getCurrentSession()
        firebaseAuth.observeAuthState()
        firebaseAuth.signOut()
    }

    suspend fun googleAuth(
        context: Context,
        webOAuthClientId: String
    ) {
        val googleAuthProvider = CredentialManagerGoogleAuthProvider()

        val result = googleAuthProvider.signIn(
            context = context,
            config = GoogleAuthConfig(
                serverClientId = webOAuthClientId,
                filterByAuthorizedAccounts = false,
                autoSelectEnabled = false
            )
        )
    }

    suspend fun firebaseGoogleAuth(
        context: Context,
        webOAuthClientId: String
    ) {
        val firebaseGoogleAuthProvider = FirebaseGoogleAuthProvider(
            context = context,
            config = FirebaseGoogleAuthConfig(
                serverClientId = webOAuthClientId,
                filterByAuthorizedAccounts = false,
                autoSelectEnabled = false
            )
        )

        val result = firebaseGoogleAuthProvider.signIn()
    }

    suspend fun permissions(
        context: Context,
        permissionRequestLauncher: PermissionRequestLauncher
    ) {
        val permissionManager = AndroidPermissionManager(
            context = context,
            requestLauncher = permissionRequestLauncher
        )

        val cameraState = permissionManager.check(SdkPermission.Camera)
        val requestResult = permissionManager.request(SdkPermission.Camera)
    }

    suspend fun secureStorage(context: Context) {
        val keyResult = SecureStorageKey.from("session:access_token")

        if (keyResult is SdkResult.Success) {
            val store = DataStoreSecureKeyValueStore(context)
            store.putString(keyResult.data, "token-value")
            val token = store.getString(keyResult.data)
        }
    }

    fun networking(currentAccessToken: String?) {
        val tokenProvider = object : TokenProvider {
            override fun getAccessToken(): String? = currentAccessToken
        }

        val client = OkHttpNetworkClientFactory(
            config = NetworkConfig(
                defaultHeaders = mapOf("Accept" to "application/json")
            )
        ).create(
            tokenProvider = tokenProvider,
            retryConfig = NetworkRetryConfig(maxRetries = 2)
        )

        val loggingClient = OkHttpNetworkClientFactory()
            .createWithLogging(
                logger = AndroidSdkLogger(isEnabled = true),
                loggingLevel = NetworkLoggingLevel.Basic
            )
    }

    suspend fun remoteConfig() {
        val remoteConfigProvider: RemoteConfigProvider = FirebaseRemoteConfigProvider()

        remoteConfigProvider.setDefaults(
            RemoteConfigDefaults(
                values = mapOf(
                    RemoteConfigKey.unsafe("new_home") to RemoteConfigValue.BooleanValue(false)
                )
            )
        )

        remoteConfigProvider.fetchAndActivate()
        val snapshot = remoteConfigProvider.getSnapshot()
    }

    suspend fun featureFlags(remoteConfigProvider: RemoteConfigProvider) {
        val featureFlagProvider = RemoteConfigFeatureFlagProvider(remoteConfigProvider)

        val enabled = featureFlagProvider.isEnabled(
            FeatureFlag(
                key = FeatureFlagKey.unsafe("new_home"),
                defaultValue = FeatureFlagValue.BooleanValue(false)
            )
        )
    }

    suspend fun notifications(remoteMessageData: Map<String, String>) {
        val parser = DefaultNotificationPayloadParser()
        val payload = parser.parse(data = remoteMessageData)

        val tokenProvider = FirebaseMessagingTokenProvider()
        val token = tokenProvider.getToken()
    }

    suspend fun airshipNotifications() {
        val airshipConfigOptions = AirshipConfigOptionsFactory.create(
            AirshipNotificationConfig(
                appKey = "airship-app-key",
                appSecret = "airship-app-secret",
                site = AirshipNotificationSite.US,
                notificationIconResId = 1,
                notificationAccentColor = 0xFF1A73E8.toInt(),
                notificationChannel = "default",
                userNotificationsEnabled = true
            )
        )

        val gateway = object : AirshipPushGateway {
            override suspend fun getChannelId(): String? = "airship-channel-id"

            override suspend fun areUserNotificationsEnabled(): Boolean = true

            override suspend fun setUserNotificationsEnabled(enabled: Boolean) = Unit
        }

        val tokenProvider = AirshipNotificationTokenProvider(gateway)
        val userNotificationsManager = AirshipUserNotificationsManager(gateway)
        val audienceGateway = object : AirshipAudienceGateway {
            override suspend fun getTags(): Set<String> = setOf("beta")

            override suspend fun addTags(tags: Set<String>) = Unit

            override suspend fun removeTags(tags: Set<String>) = Unit

            override suspend fun clearTags() = Unit

            override suspend fun setAttribute(
                name: String,
                value: AirshipAudienceAttributeValue
            ) = Unit

            override suspend fun removeAttribute(name: String) = Unit

            override suspend fun subscribeToLists(listIds: Set<String>) = Unit

            override suspend fun unsubscribeFromLists(listIds: Set<String>) = Unit
        }
        val audienceManager = AirshipAudienceManager(audienceGateway)
        val pushEventGateway = object : AirshipPushEventGateway {
            override fun observePushEvents() = flowOf(
                AirshipPushEvent(type = AirshipPushEventType.Received, alert = "Hello")
            )

            override suspend fun getPushNotificationStatus() = AirshipPushNotificationStatus(
                userNotificationsEnabled = true,
                notificationsAllowed = true,
                pushPrivacyFeatureEnabled = true,
                pushTokenRegistered = true,
                optedIn = true
            )

            override suspend fun createNotificationChannel(config: NotificationChannelConfig) = Unit

            override suspend fun setForegroundNotificationDisplayEnabled(enabled: Boolean) = Unit
        }
        val contactGateway = object : AirshipContactGateway {
            override suspend fun getNamedUserId(): String? = "user-123"

            override suspend fun identify(namedUserId: String) = Unit

            override suspend fun reset() = Unit

            override suspend fun setAttribute(
                name: String,
                value: AirshipAudienceAttributeValue
            ) = Unit

            override suspend fun removeAttribute(name: String) = Unit

            override suspend fun subscribeToLists(
                listIds: Set<String>,
                scope: AirshipContactSubscriptionScope
            ) = Unit

            override suspend fun unsubscribeFromLists(
                listIds: Set<String>,
                scope: AirshipContactSubscriptionScope
            ) = Unit
        }
        val privacyGateway = object : AirshipPrivacyGateway {
            override suspend fun getEnabledFeatures() = setOf(AirshipPrivacyFeature.Push)

            override suspend fun setEnabledFeatures(features: Set<AirshipPrivacyFeature>) = Unit

            override suspend fun enableFeatures(features: Set<AirshipPrivacyFeature>) = Unit

            override suspend fun disableFeatures(features: Set<AirshipPrivacyFeature>) = Unit
        }
        val pushEventsManager = AirshipPushEventsManager(pushEventGateway)
        val contactManager = AirshipContactManager(contactGateway)
        val privacyManager = AirshipPrivacyManager(privacyGateway)

        val token = tokenProvider.getToken()
        val status = userNotificationsManager.getStatus()
        val tags = audienceManager.getTags()
        val tagUpdate = audienceManager.addTags(setOf("premium", "beta"))
        val attributeUpdate = audienceManager.setAttribute(
            name = "plan",
            value = AirshipAudienceAttributeValue.StringValue("premium")
        )
        val listUpdate = audienceManager.subscribeToLists(setOf("weekly-updates"))
        val pushStatus = pushEventsManager.getPushNotificationStatus()
        val channelUpdate = pushEventsManager.createNotificationChannel(
            NotificationChannelConfig(
                id = NotificationChannelId.unsafe("updates"),
                name = "Updates"
            )
        )
        val foregroundDisplay = pushEventsManager.setForegroundNotificationDisplayEnabled(true)
        val namedUserId = contactManager.getNamedUserId()
        val identify = contactManager.identify("user-123")
        val contactListUpdate = contactManager.subscribeToLists(
            listIds = setOf("weekly-updates"),
            scope = AirshipContactSubscriptionScope.Email
        )
        val privacy = privacyManager.setEnabledFeatures(
            setOf(
                AirshipPrivacyFeature.Push,
                AirshipPrivacyFeature.TagsAndAttributes,
                AirshipPrivacyFeature.Contacts
            )
        )
    }

    suspend fun mediaPicker(mediaPicker: MediaPicker) {
        val request = MediaPickRequest(
            mediaTypes = setOf(MediaType.Image),
            selectionMode = MediaSelectionMode.Single
        )

        val result = mediaPicker.pick(request)
    }

    suspend fun analytics(context: Context) {
        val analyticsProvider: AnalyticsProvider = FirebaseAnalyticsProvider(context)

        analyticsProvider.track(
            AnalyticsEvent(
                name = AnalyticsEventName.unsafe("screen_view"),
                parameters = mapOf("screen" to AnalyticsValue.StringValue("home"))
            )
        )
    }

    fun deepLinks() {
        val parser = DefaultDeepLinkParser(
            config = DeepLinkConfig(
                allowedSchemes = setOf("myapp"),
                allowedHosts = setOf("open")
            )
        )

        val deepLink = parser.parse("myapp://open/profile?id=123")
    }

    suspend fun backgroundWork(context: Context) {
        val workName = BackgroundWorkName.unsafe("sync-profile")
        val scheduler = WorkManagerBackgroundWorkScheduler(
            context = context,
            workerClasses = mapOf(workName to SyncProfileWorker::class.java)
        )

        scheduler.enqueue(
            BackgroundWorkRequest(
                name = workName,
                type = BackgroundWorkType.OneTime
            )
        )
    }

    suspend fun appVersioning(context: Context) {
        val evaluator = DefaultAppUpdatePolicyEvaluator()

        when (val currentVersion = AndroidAppVersionProvider(context).getCurrentVersion()) {
            is SdkResult.Success -> {
                val state = evaluator.evaluate(
                    currentVersion = currentVersion.data,
                    policy = AppUpdatePolicy(
                        minimumSupportedVersion = AppVersion(major = 1, minor = 0, patch = 0),
                        latestVersion = AppVersion(major = 1, minor = 2, patch = 0)
                    )
                )
            }
            is SdkResult.Failure -> {
                val error = currentVersion.error
            }
        }
    }
}

internal class SyncProfileWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    override fun doWork(): Result = Result.success()
}
