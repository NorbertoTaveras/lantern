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

package com.norbertotaveras.lantern.notifications.firebase

import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.notifications.NotificationToken
import com.norbertotaveras.lantern.notifications.NotificationTokenProvider
import com.norbertotaveras.lantern.notifications.NotificationTokenProviderType
import com.norbertotaveras.lantern.notifications.firebase.internal.FirebaseMessagingErrorMapper
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
