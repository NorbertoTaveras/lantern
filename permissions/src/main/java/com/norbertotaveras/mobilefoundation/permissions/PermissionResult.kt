package com.norbertotaveras.mobilefoundation.permissions

data class PermissionResult(
    val states: Map<SdkPermission, PermissionState>
) {
    val allGranted: Boolean = states.values.all { it.status == PermissionStatus.Granted }

    companion object {
        fun single(state: PermissionState): PermissionResult {
            return PermissionResult(states = mapOf(state.permission to state))
        }
    }
}
