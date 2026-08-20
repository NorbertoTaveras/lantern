package com.norbertotaveras.mobilefoundation.consumer.smoke

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.norbertotaveras.mobilefoundation.analytics.AnalyticsEvent
import com.norbertotaveras.mobilefoundation.analytics.AnalyticsEventName
import com.norbertotaveras.mobilefoundation.analytics.AnalyticsProvider
import com.norbertotaveras.mobilefoundation.analytics.AnalyticsValue
import com.norbertotaveras.mobilefoundation.analytics.firebase.FirebaseAnalyticsProvider
import com.norbertotaveras.mobilefoundation.appversioning.AndroidAppVersionProvider
import com.norbertotaveras.mobilefoundation.appversioning.AppUpdatePolicy
import com.norbertotaveras.mobilefoundation.appversioning.AppVersion
import com.norbertotaveras.mobilefoundation.appversioning.DefaultAppUpdatePolicyEvaluator
import com.norbertotaveras.mobilefoundation.auth.core.AuthProvider
import com.norbertotaveras.mobilefoundation.auth.firebase.FirebaseAuthProvider
import com.norbertotaveras.mobilefoundation.auth.firebasegoogle.FirebaseGoogleAuthConfig
import com.norbertotaveras.mobilefoundation.auth.firebasegoogle.FirebaseGoogleAuthProvider
import com.norbertotaveras.mobilefoundation.auth.google.CredentialManagerGoogleAuthProvider
import com.norbertotaveras.mobilefoundation.auth.google.GoogleAuthConfig
import com.norbertotaveras.mobilefoundation.backgroundwork.BackgroundWorkName
import com.norbertotaveras.mobilefoundation.backgroundwork.BackgroundWorkRequest
import com.norbertotaveras.mobilefoundation.backgroundwork.BackgroundWorkType
import com.norbertotaveras.mobilefoundation.backgroundwork.WorkManagerBackgroundWorkScheduler
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.deeplinks.DeepLinkConfig
import com.norbertotaveras.mobilefoundation.deeplinks.DefaultDeepLinkParser
import com.norbertotaveras.mobilefoundation.featureflags.FeatureFlag
import com.norbertotaveras.mobilefoundation.featureflags.FeatureFlagKey
import com.norbertotaveras.mobilefoundation.featureflags.FeatureFlagValue
import com.norbertotaveras.mobilefoundation.featureflags.RemoteConfigFeatureFlagProvider
import com.norbertotaveras.mobilefoundation.logging.AndroidSdkLogger
import com.norbertotaveras.mobilefoundation.mediapicker.MediaPickRequest
import com.norbertotaveras.mobilefoundation.mediapicker.MediaPicker
import com.norbertotaveras.mobilefoundation.mediapicker.MediaSelectionMode
import com.norbertotaveras.mobilefoundation.mediapicker.MediaType
import com.norbertotaveras.mobilefoundation.network.okhttp.NetworkConfig
import com.norbertotaveras.mobilefoundation.network.okhttp.NetworkLoggingLevel
import com.norbertotaveras.mobilefoundation.network.okhttp.NetworkRetryConfig
import com.norbertotaveras.mobilefoundation.network.okhttp.OkHttpNetworkClientFactory
import com.norbertotaveras.mobilefoundation.network.okhttp.TokenProvider
import com.norbertotaveras.mobilefoundation.notifications.DefaultNotificationPayloadParser
import com.norbertotaveras.mobilefoundation.notifications.firebase.FirebaseMessagingTokenProvider
import com.norbertotaveras.mobilefoundation.permissions.AndroidPermissionManager
import com.norbertotaveras.mobilefoundation.permissions.PermissionRequestLauncher
import com.norbertotaveras.mobilefoundation.permissions.SdkPermission
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigDefaults
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigKey
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigProvider
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigValue
import com.norbertotaveras.mobilefoundation.remoteconfig.firebase.FirebaseRemoteConfigProvider
import com.norbertotaveras.mobilefoundation.securestorage.DataStoreSecureKeyValueStore
import com.norbertotaveras.mobilefoundation.securestorage.SecureStorageKey

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
