package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.permissions.PermissionManager
import com.norbertotaveras.mobilefoundation.permissions.PermissionState
import com.norbertotaveras.mobilefoundation.permissions.PermissionStatus
import com.norbertotaveras.mobilefoundation.permissions.SdkPermission

/**
 * [NotificationPermissionManager] backed by the reusable Android runtime permissions module.
 *
 * The consuming app still owns lifecycle-aware permission launcher registration through the
 * [PermissionManager] implementation it provides.
 */
class AndroidNotificationPermissionManager(
    private val permissionManager: PermissionManager
) : NotificationPermissionManager {

    override fun check(): NotificationPermissionState {
        return permissionManager.check(SdkPermission.Notifications).toNotificationPermissionState()
    }

    override suspend fun request(): SdkResult<NotificationPermissionState> {
        val result = permissionManager.request(SdkPermission.Notifications)
        val error = result.error
        if (error != null) {
            return SdkResult.Failure(error)
        }

        val state = result.states[SdkPermission.Notifications]
            ?: permissionManager.check(SdkPermission.Notifications)
        return SdkResult.Success(state.toNotificationPermissionState())
    }

    private fun PermissionState.toNotificationPermissionState(): NotificationPermissionState {
        return NotificationPermissionState(
            status = status.toNotificationPermissionStatus(),
            canRequest = status == PermissionStatus.Denied || status == PermissionStatus.NotDetermined
        )
    }

    private fun PermissionStatus.toNotificationPermissionStatus(): NotificationPermissionStatus {
        return when (this) {
            PermissionStatus.Granted -> NotificationPermissionStatus.Granted
            PermissionStatus.Denied -> NotificationPermissionStatus.Denied
            PermissionStatus.PermanentlyDenied -> NotificationPermissionStatus.PermanentlyDenied
            PermissionStatus.NotDetermined -> NotificationPermissionStatus.NotDetermined
            PermissionStatus.Unsupported -> NotificationPermissionStatus.NotRequired
        }
    }
}
