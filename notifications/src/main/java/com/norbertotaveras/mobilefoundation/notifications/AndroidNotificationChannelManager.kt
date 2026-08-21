package com.norbertotaveras.mobilefoundation.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.logging.NoOpSdkLogger
import com.norbertotaveras.mobilefoundation.logging.SdkLogger

/**
 * Android [NotificationChannelManager] backed by the platform [NotificationManager].
 *
 * Channel operations return success on Android versions below Oreo because notification channels
 * are not supported or required on those platform versions.
 */
class AndroidNotificationChannelManager(
    private val notificationManager: NotificationManager,
    private val logger: SdkLogger = NoOpSdkLogger()
) : NotificationChannelManager {

    /**
     * Creates a channel manager from [context].
     */
    constructor(
        context: Context,
        logger: SdkLogger = NoOpSdkLogger()
    ) : this(
        notificationManager = context.applicationContext.getSystemService(NotificationManager::class.java),
        logger = logger
    )

    @SuppressLint("NewApi")
    override suspend fun createChannel(config: NotificationChannelConfig): SdkResult<Unit> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return SdkResult.Success(Unit)
        }

        return try {
            val channel = NotificationChannel(
                config.id.value,
                config.name,
                config.importance.toAndroidImportance()
            ).apply {
                description = config.description
                setShowBadge(config.showBadge)
            }
            notificationManager.createNotificationChannel(channel)
            SdkResult.Success(Unit)
        } catch (throwable: Throwable) {
            logger.error("Unable to create notification channel ${config.id.value}.", throwable)
            SdkResult.Failure(channelOperationFailure(throwable))
        }
    }

    @SuppressLint("NewApi")
    override suspend fun deleteChannel(id: NotificationChannelId): SdkResult<Unit> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return SdkResult.Success(Unit)
        }

        return try {
            notificationManager.deleteNotificationChannel(id.value)
            SdkResult.Success(Unit)
        } catch (throwable: Throwable) {
            logger.error("Unable to delete notification channel ${id.value}.", throwable)
            SdkResult.Failure(channelOperationFailure(throwable))
        }
    }

    private fun channelOperationFailure(throwable: Throwable): SdkError {
        return SdkError(
            code = NotificationErrorCodes.CHANNEL_OPERATION_FAILED,
            message = throwable.localizedMessage ?: "Notification channel operation failed.",
            cause = throwable
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
