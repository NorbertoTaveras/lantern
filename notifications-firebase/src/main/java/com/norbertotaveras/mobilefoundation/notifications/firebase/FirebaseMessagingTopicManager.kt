package com.norbertotaveras.mobilefoundation.notifications.firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.notifications.NotificationTopic
import com.norbertotaveras.mobilefoundation.notifications.NotificationTopicManager
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
