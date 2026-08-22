package com.norbertotaveras.lantern.backgroundwork

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.common.util.concurrent.ListenableFuture
import com.norbertotaveras.lantern.backgroundwork.internal.BackgroundWorkRequestValidator
import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.logging.NoOpSdkLogger
import com.norbertotaveras.lantern.logging.SdkLogger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

/**
 * [BackgroundWorkScheduler] implementation backed by AndroidX WorkManager.
 */
class WorkManagerBackgroundWorkScheduler(
    private val workManager: WorkManager,
    private val workerClasses: Map<BackgroundWorkName, Class<out ListenableWorker>>,
    private val logger: SdkLogger = NoOpSdkLogger()
) : BackgroundWorkScheduler {

    /**
     * Creates a scheduler from [context] and a map of work names to WorkManager workers.
     */
    constructor(
        context: Context,
        workerClasses: Map<BackgroundWorkName, Class<out ListenableWorker>>,
        logger: SdkLogger = NoOpSdkLogger()
    ) : this(
        workManager = WorkManager.getInstance(context.applicationContext),
        workerClasses = workerClasses,
        logger = logger
    )

    override suspend fun enqueue(request: BackgroundWorkRequest): SdkResult<BackgroundWorkId> {
        return try {
            val validationError = BackgroundWorkRequestValidator.validate(request)
            if (validationError != null) {
                return SdkResult.Failure(validationError)
            }

            val workerClass = workerClasses[request.name]
                ?: return SdkResult.Failure(workerNotRegistered(request.name))

            val workRequest = request.toWorkRequest(workerClass)
            when (request.type) {
                BackgroundWorkType.OneTime -> {
                    workManager.enqueueUniqueWork(
                        request.name.value,
                        request.policy.toExistingWorkPolicy(),
                        workRequest as OneTimeWorkRequest
                    ).result.await()
                }
                is BackgroundWorkType.Periodic -> {
                    workManager.enqueueUniquePeriodicWork(
                        request.name.value,
                        request.policy.toExistingPeriodicWorkPolicy(),
                        workRequest as PeriodicWorkRequest
                    ).result.await()
                }
            }

            SdkResult.Success(BackgroundWorkId.unsafe(workRequest.id.toString()))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Throwable) {
            logger.error("Unable to enqueue background work.", exception)
            SdkResult.Failure(
                SdkError(
                    code = BackgroundWorkErrorCodes.ENQUEUE_FAILED,
                    message = "Unable to enqueue background work.",
                    cause = exception
                )
            )
        }
    }

    override suspend fun cancel(name: BackgroundWorkName): SdkResult<Unit> {
        return try {
            workManager.cancelUniqueWork(name.value).result.await()
            SdkResult.Success(Unit)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Throwable) {
            logger.error("Unable to cancel background work.", exception)
            SdkResult.Failure(
                SdkError(
                    code = BackgroundWorkErrorCodes.CANCEL_FAILED,
                    message = "Unable to cancel background work.",
                    cause = exception
                )
            )
        }
    }

    override suspend fun getWorkInfo(name: BackgroundWorkName): SdkResult<BackgroundWorkInfo?> {
        return try {
            val workInfo = workManager.getWorkInfosForUniqueWork(name.value)
                .await()
                .firstOrNull()
            SdkResult.Success(workInfo?.toBackgroundWorkInfo(name))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Throwable) {
            logger.error("Unable to query background work.", exception)
            SdkResult.Failure(
                SdkError(
                    code = BackgroundWorkErrorCodes.QUERY_FAILED,
                    message = "Unable to query background work.",
                    cause = exception
                )
            )
        }
    }

    override fun observeWorkInfo(name: BackgroundWorkName): Flow<BackgroundWorkInfo?> {
        return workManager.getWorkInfosForUniqueWorkFlow(name.value)
            .map { workInfos -> workInfos.firstOrNull()?.toBackgroundWorkInfo(name) }
            .catch { throwable ->
                logger.error("Unable to observe background work ${name.value}.", throwable)
                emit(null)
            }
    }

    private fun workerNotRegistered(name: BackgroundWorkName): SdkError {
        return SdkError(
            code = BackgroundWorkErrorCodes.WORKER_NOT_REGISTERED,
            message = "No WorkManager worker is registered for background work ${name.value}."
        )
    }

    private suspend fun <T> ListenableFuture<T>.await(): T {
        if (isDone) {
            return get()
        }

        return suspendCancellableCoroutine { continuation ->
            addListener(
                {
                    try {
                        continuation.resume(get())
                    } catch (throwable: Throwable) {
                        continuation.cancel(throwable)
                    }
                },
                DirectExecutor
            )
            continuation.invokeOnCancellation { cancel(true) }
        }
    }

    private object DirectExecutor : Executor {
        override fun execute(command: Runnable) {
            command.run()
        }
    }
}

private fun BackgroundWorkRequest.toWorkRequest(
    workerClass: Class<out ListenableWorker>
): WorkRequest {
    val constraints = constraints.toWorkManagerConstraints()
    val inputData = input.toWorkData()

    return when (val workType = type) {
        BackgroundWorkType.OneTime -> {
            OneTimeWorkRequest.Builder(workerClass)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setInitialDelay(initialDelayMillis, TimeUnit.MILLISECONDS)
                .addTag(name.value)
                .build()
        }
        is BackgroundWorkType.Periodic -> {
            val builder = if (workType.flexIntervalMillis == null) {
                PeriodicWorkRequest.Builder(
                    workerClass,
                    workType.repeatIntervalMillis,
                    TimeUnit.MILLISECONDS
                )
            } else {
                PeriodicWorkRequest.Builder(
                    workerClass,
                    workType.repeatIntervalMillis,
                    TimeUnit.MILLISECONDS,
                    workType.flexIntervalMillis,
                    TimeUnit.MILLISECONDS
                )
            }

            builder
                .setConstraints(constraints)
                .setInputData(inputData)
                .setInitialDelay(initialDelayMillis, TimeUnit.MILLISECONDS)
                .addTag(name.value)
                .build()
        }
    }
}

private fun BackgroundWorkConstraints.toWorkManagerConstraints(): Constraints {
    return Constraints.Builder()
        .setRequiredNetworkType(if (requiresNetwork) NetworkType.CONNECTED else NetworkType.NOT_REQUIRED)
        .setRequiresCharging(requiresCharging)
        .setRequiresBatteryNotLow(requiresBatteryNotLow)
        .setRequiresStorageNotLow(requiresStorageNotLow)
        .build()
}

private fun Map<String, String>.toWorkData(): Data {
    val builder = Data.Builder()
    forEach { (key, value) -> builder.putString(key, value) }
    return builder.build()
}

private fun WorkInfo.toBackgroundWorkInfo(name: BackgroundWorkName): BackgroundWorkInfo {
    return BackgroundWorkInfo(
        id = BackgroundWorkId.unsafe(id.toString()),
        name = name,
        status = state.toBackgroundWorkStatus(),
        progress = progress.keyValueMap.toStringMap(),
        output = outputData.keyValueMap.toStringMap()
    )
}

private fun WorkInfo.State.toBackgroundWorkStatus(): BackgroundWorkStatus {
    return when (this) {
        WorkInfo.State.ENQUEUED -> BackgroundWorkStatus.Enqueued
        WorkInfo.State.RUNNING -> BackgroundWorkStatus.Running
        WorkInfo.State.SUCCEEDED -> BackgroundWorkStatus.Succeeded
        WorkInfo.State.FAILED -> BackgroundWorkStatus.Failed
        WorkInfo.State.CANCELLED -> BackgroundWorkStatus.Cancelled
        WorkInfo.State.BLOCKED -> BackgroundWorkStatus.Blocked
    }
}

private fun BackgroundWorkPolicy.toExistingWorkPolicy(): ExistingWorkPolicy {
    return when (this) {
        BackgroundWorkPolicy.KeepExisting -> ExistingWorkPolicy.KEEP
        BackgroundWorkPolicy.ReplaceExisting -> ExistingWorkPolicy.REPLACE
        BackgroundWorkPolicy.Append -> ExistingWorkPolicy.APPEND
    }
}

private fun BackgroundWorkPolicy.toExistingPeriodicWorkPolicy(): ExistingPeriodicWorkPolicy {
    return when (this) {
        BackgroundWorkPolicy.KeepExisting -> ExistingPeriodicWorkPolicy.KEEP
        BackgroundWorkPolicy.ReplaceExisting,
        BackgroundWorkPolicy.Append -> ExistingPeriodicWorkPolicy.UPDATE
    }
}

private fun Map<String, Any?>.toStringMap(): Map<String, String> {
    return mapValues { (_, value) -> value.toString() }
}
