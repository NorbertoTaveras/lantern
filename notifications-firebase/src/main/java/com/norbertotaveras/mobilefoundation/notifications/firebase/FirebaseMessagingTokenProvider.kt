package com.norbertotaveras.mobilefoundation.notifications.firebase

import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.notifications.NotificationToken
import com.norbertotaveras.mobilefoundation.notifications.NotificationTokenProvider
import com.norbertotaveras.mobilefoundation.notifications.NotificationTokenProviderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

/**
 * [NotificationTokenProvider] implementation backed by Firebase Messaging.
 */
class FirebaseMessagingTokenProvider private constructor(
    private val firebaseMessaging: FirebaseMessaging = FirebaseMessaging.getInstance(),
    private val firebaseInstallations: FirebaseInstallations = FirebaseInstallations.getInstance(),
    private val errorMapper: FirebaseMessagingErrorMapper = FirebaseMessagingErrorMapper()
) : NotificationTokenProvider {

    /**
     * Creates a Firebase Messaging token provider with default Firebase instances.
     */
    constructor() : this(
        firebaseMessaging = FirebaseMessaging.getInstance(),
        firebaseInstallations = FirebaseInstallations.getInstance()
    )

    /**
     * Creates a Firebase Messaging token provider with injectable Firebase dependencies.
     */
    constructor(
        firebaseMessaging: FirebaseMessaging = FirebaseMessaging.getInstance(),
        firebaseInstallations: FirebaseInstallations = FirebaseInstallations.getInstance()
    ) : this(
        firebaseMessaging = firebaseMessaging,
        firebaseInstallations = firebaseInstallations,
        errorMapper = FirebaseMessagingErrorMapper()
    )

    private val tokenState = MutableStateFlow<NotificationToken?>(null)

    override val tokenUpdates: Flow<NotificationToken?> = tokenState.asStateFlow()

    override suspend fun getToken(): SdkResult<NotificationToken> {
        return try {
            firebaseMessaging.register().await()
            val installationId = firebaseInstallations.id.await()
            val token = NotificationToken(
                value = firebaseMessaging.token.await(),
                provider = NotificationTokenProviderType.FirebaseCloudMessaging,
                createdAtMillis = System.currentTimeMillis(),
                metadata = mapOf("firebase_installation_id" to installationId)
            )
            tokenState.value = token
            SdkResult.Success(token)
        } catch (throwable: Throwable) {
            SdkResult.Failure(errorMapper.map(FirebaseMessagingErrorMapper.Operation.GetToken, throwable))
        }
    }

    override suspend fun deleteToken(): SdkResult<Unit> {
        return try {
            firebaseMessaging.unregister().await()
            tokenState.value = null
            SdkResult.Success(Unit)
        } catch (throwable: Throwable) {
            SdkResult.Failure(errorMapper.map(FirebaseMessagingErrorMapper.Operation.DeleteToken, throwable))
        }
    }
}
