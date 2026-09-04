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

package com.norbertotaveras.lantern.notifications.airship

import android.app.NotificationManager
import com.norbertotaveras.lantern.notifications.NotificationChannelConfig
import com.norbertotaveras.lantern.notifications.NotificationChannelImportance
import com.urbanairship.Airship
import com.urbanairship.Predicate
import com.urbanairship.push.NotificationActionButtonInfo
import com.urbanairship.push.NotificationInfo
import com.urbanairship.push.NotificationListener
import com.urbanairship.push.PushListener
import com.urbanairship.push.PushMessage
import com.urbanairship.push.PushNotificationStatus
import com.urbanairship.push.PushNotificationStatusListener
import com.urbanairship.push.PushTokenListener
import com.urbanairship.push.notifications.NotificationChannelCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * [AirshipPushEventGateway] backed by the Airship Android SDK singleton.
 *
 * Airship allows one notification listener at a time. Observing notification opened, dismissed,
 * and action events through this gateway temporarily installs Lantern's listener and restores the
 * previous listener when collection is closed.
 */
class AirshipSdkPushEventGateway : AirshipPushEventGateway {
    override fun observePushEvents(): Flow<AirshipPushEvent> = callbackFlow {
        val previousNotificationListener = Airship.push.notificationListener
        val pushListener = PushListener { message, notificationPosted ->
            trySend(message.toEvent(AirshipPushEventType.Received, notificationPosted))
        }
        val tokenListener = PushTokenListener { token ->
            trySend(AirshipPushEvent(type = AirshipPushEventType.TokenUpdated, pushToken = token))
        }
        val statusListener = PushNotificationStatusListener { status ->
            trySend(
                AirshipPushEvent(
                    type = AirshipPushEventType.StatusChanged,
                    status = status.toLanternStatus()
                )
            )
        }
        val notificationListener = object : NotificationListener {
            override fun onNotificationPosted(notificationInfo: NotificationInfo) {
                trySend(notificationInfo.toEvent(AirshipPushEventType.Posted))
            }

            override fun onNotificationOpened(notificationInfo: NotificationInfo): Boolean {
                trySend(notificationInfo.toEvent(AirshipPushEventType.Opened))
                return false
            }

            override fun onNotificationForegroundAction(
                notificationInfo: NotificationInfo,
                actionButtonInfo: NotificationActionButtonInfo
            ): Boolean {
                trySend(
                    notificationInfo.toEvent(
                        type = AirshipPushEventType.ForegroundAction,
                        action = actionButtonInfo.toLanternAction()
                    )
                )
                return false
            }

            override fun onNotificationBackgroundAction(
                notificationInfo: NotificationInfo,
                actionButtonInfo: NotificationActionButtonInfo
            ) {
                trySend(
                    notificationInfo.toEvent(
                        type = AirshipPushEventType.BackgroundAction,
                        action = actionButtonInfo.toLanternAction()
                    )
                )
            }

            override fun onNotificationDismissed(notificationInfo: NotificationInfo) {
                trySend(notificationInfo.toEvent(AirshipPushEventType.Dismissed))
            }
        }

        Airship.push.addPushListener(pushListener)
        Airship.push.addPushTokenListener(tokenListener)
        Airship.push.addNotificationStatusListener(statusListener)
        Airship.push.notificationListener = notificationListener

        awaitClose {
            Airship.push.removePushListener(pushListener)
            Airship.push.removePushTokenListener(tokenListener)
            Airship.push.removeNotificationStatusListener(statusListener)
            Airship.push.notificationListener = previousNotificationListener
        }
    }

    override suspend fun getPushNotificationStatus(): AirshipPushNotificationStatus {
        return Airship.push.pushNotificationStatus.toLanternStatus()
    }

    override suspend fun createNotificationChannel(config: NotificationChannelConfig) {
        Airship.push.notificationChannelRegistry.createNotificationChannel(
            NotificationChannelCompat(
                config.id.value,
                config.name,
                config.importance.toAndroidImportance()
            ).apply {
                description = config.description
                showBadge = config.showBadge
            }
        )
    }

    override suspend fun setForegroundNotificationDisplayEnabled(enabled: Boolean) {
        Airship.push.foregroundNotificationDisplayPredicate = Predicate { enabled }
    }

    private fun NotificationInfo.toEvent(
        type: AirshipPushEventType,
        action: AirshipNotificationAction? = null
    ): AirshipPushEvent {
        return message.toEvent(
            type = type,
            notificationPosted = null,
            notificationId = notificationId,
            notificationTag = notificationTag,
            action = action
        )
    }

    private fun PushMessage.toEvent(
        type: AirshipPushEventType,
        notificationPosted: Boolean? = null,
        notificationId: Int? = null,
        notificationTag: String? = null,
        action: AirshipNotificationAction? = null
    ): AirshipPushEvent {
        return AirshipPushEvent(
            type = type,
            title = title,
            alert = alert,
            summary = summary,
            sendId = sendId,
            metadata = metadata,
            notificationId = notificationId,
            notificationTag = notificationTag ?: this.notificationTag,
            notificationPosted = notificationPosted,
            action = action
        )
    }

    private fun NotificationActionButtonInfo.toLanternAction(): AirshipNotificationAction {
        return AirshipNotificationAction(
            buttonId = buttonId,
            foreground = isForeground,
            description = description
        )
    }

    private fun PushNotificationStatus.toLanternStatus(): AirshipPushNotificationStatus {
        return AirshipPushNotificationStatus(
            userNotificationsEnabled = isUserNotificationsEnabled,
            notificationsAllowed = areNotificationsAllowed,
            pushPrivacyFeatureEnabled = isPushPrivacyFeatureEnabled,
            pushTokenRegistered = isPushTokenRegistered,
            optedIn = isOptIn
        )
    }

    private fun NotificationChannelImportance.toAndroidImportance(): Int {
        return when (this) {
            NotificationChannelImportance.Min -> NotificationManager.IMPORTANCE_MIN
            NotificationChannelImportance.Low -> NotificationManager.IMPORTANCE_LOW
            NotificationChannelImportance.Default -> NotificationManager.IMPORTANCE_DEFAULT
            NotificationChannelImportance.High -> NotificationManager.IMPORTANCE_HIGH
        }
    }
}
