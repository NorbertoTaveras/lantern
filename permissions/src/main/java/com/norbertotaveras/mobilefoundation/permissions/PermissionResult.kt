package com.norbertotaveras.mobilefoundation.permissions

import com.norbertotaveras.mobilefoundation.core.SdkError

data class PermissionResult(
    val states: Map<SdkPermission, PermissionState>,
    val error: SdkError? = null
) {
    val allGranted: Boolean = states.values.all { it.status == PermissionStatus.Granted }

    companion object {
        fun single(
            state: PermissionState,
            error: SdkError? = null
        ): PermissionResult {
            return PermissionResult(
                states = mapOf(state.permission to state),
                error = error
            )
        }
    }
}
