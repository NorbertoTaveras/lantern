package com.norbertotaveras.mobilefoundation.core

/**
 * Standard SDK result wrapper used instead of throwing for expected operation failures.
 */
sealed interface SdkResult<out T> {
    /**
     * Successful SDK result containing [data].
     */
    data class Success<T>(val data: T) : SdkResult<T>
    /**
     * Failed SDK result containing a normalized [SdkError].
     */
    data class Failure(val error: SdkError) : SdkResult<Nothing>
}
