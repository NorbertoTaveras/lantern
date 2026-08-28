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

import com.google.firebase.messaging.FirebaseMessaging
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.notifications.NotificationTopic
import com.norbertotaveras.lantern.notifications.NotificationTopicManager
import com.norbertotaveras.lantern.notifications.firebase.internal.FirebaseMessagingErrorMapper
import kotlinx.coroutines.tasks.await

/**
 * [NotificationTopicManager] implementation backed by Firebase Messaging topics.
 */
class FirebaseMessagingTopicManager private constructor(
    private val firebaseMessaging: FirebaseMessaging = FirebaseMessaging.getInstance(),
    private val errorMapper: FirebaseMessagingErrorMapper = FirebaseMessagingErrorMapper()
) : NotificationTopicManager {

    /**
     * Creates a Firebase Messaging topic manager with the default Firebase instance.
     */
    constructor() : this(
        firebaseMessaging = FirebaseMessaging.getInstance()
    )

    /**
     * Creates a Firebase Messaging topic manager with an injectable Firebase instance.
     */
    constructor(
        firebaseMessaging: FirebaseMessaging = FirebaseMessaging.getInstance()
    ) : this(
        firebaseMessaging = firebaseMessaging,
        errorMapper = FirebaseMessagingErrorMapper()
    )

    override suspend fun subscribe(topic: NotificationTopic): SdkResult<Unit> {
        return try {
            firebaseMessaging.subscribeToTopic(topic.value).await()
            SdkResult.Success(Unit)
        } catch (throwable: Throwable) {
            SdkResult.Failure(errorMapper.map(FirebaseMessagingErrorMapper.Operation.SubscribeTopic, throwable))
        }
    }

    override suspend fun unsubscribe(topic: NotificationTopic): SdkResult<Unit> {
        return try {
            firebaseMessaging.unsubscribeFromTopic(topic.value).await()
            SdkResult.Success(Unit)
        } catch (throwable: Throwable) {
            SdkResult.Failure(errorMapper.map(FirebaseMessagingErrorMapper.Operation.UnsubscribeTopic, throwable))
        }
    }
}
