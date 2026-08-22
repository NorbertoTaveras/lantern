package com.norbertotaveras.lantern.backgroundwork

import com.norbertotaveras.lantern.core.SdkResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NoOpBackgroundWorkSchedulerTest {

    @Test
    fun enqueueStoresWorkInfo() = runBlocking {
        val scheduler = NoOpBackgroundWorkScheduler()
        val name = BackgroundWorkName.unsafe("sync")

        val result = scheduler.enqueue(
            BackgroundWorkRequest(
                name = name,
                type = BackgroundWorkType.OneTime
            )
        )

        assertTrue(result is SdkResult.Success)
        val info = scheduler.getWorkInfo(name)
        assertTrue(info is SdkResult.Success)
        assertEquals(BackgroundWorkStatus.Enqueued, (info as SdkResult.Success).data?.status)
    }

    @Test
    fun cancelMarksWorkCancelled() = runBlocking {
        val scheduler = NoOpBackgroundWorkScheduler()
        val name = BackgroundWorkName.unsafe("sync")

        scheduler.enqueue(
            BackgroundWorkRequest(
                name = name,
                type = BackgroundWorkType.OneTime
            )
        )
        scheduler.cancel(name)

        val info = scheduler.getWorkInfo(name)
        assertTrue(info is SdkResult.Success)
        assertEquals(BackgroundWorkStatus.Cancelled, (info as SdkResult.Success).data?.status)
    }
}
