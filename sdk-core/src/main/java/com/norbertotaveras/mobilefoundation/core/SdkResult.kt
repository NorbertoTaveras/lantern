package com.norbertotaveras.mobilefoundation.core

sealed interface SdkResult<out T> {
    data class Success<T>(val data: T) : SdkResult<T>
    data class Failure(val error: SdkError) : SdkResult<Nothing>
}