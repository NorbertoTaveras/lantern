package com.norbertotaveras.mobilefoundation.permissions

import com.norbertotaveras.mobilefoundation.core.SdkError

/**
 * Result of a permission request operation.
 *
 * [error] is populated when the SDK could not launch or complete the request flow. In that case,
 * [states] still contains the best current state for each requested permission.
 */
data class PermissionResult(
    val states: Map<SdkPermission, PermissionState>,
    val error: SdkError? = null
) {
    /**
     * True when every returned state is granted.
     */
    val allGranted: Boolean = states.values.all { it.status == PermissionStatus.Granted }

    companion object {
        /**
         * Creates a result for a single permission state.
         */
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
