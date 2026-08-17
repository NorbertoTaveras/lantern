package com.norbertotaveras.mobilefoundation.backgroundwork

import com.norbertotaveras.mobilefoundation.core.SdkResult
import kotlinx.coroutines.flow.Flow

interface BackgroundWorkScheduler {
    suspend fun enqueue(request: BackgroundWorkRequest): SdkResult<BackgroundWorkId>

    suspend fun cancel(name: BackgroundWorkName): SdkResult<Unit>

    suspend fun getWorkInfo(name: BackgroundWorkName): SdkResult<BackgroundWorkInfo?>

    fun observeWorkInfo(name: BackgroundWorkName): Flow<BackgroundWorkInfo?>
}
