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
import com.norbertotaveras.lantern.notifications.firebase.FirebaseMessagingTokenProvider
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
