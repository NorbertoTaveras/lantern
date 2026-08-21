# Background Work

`background-work` schedules, cancels, queries, and observes WorkManager work through SDK models.

```kotlin
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-background-work:$mobileFoundationVersion")
```

## Use It For

- Scheduling one-time or periodic work.
- Applying work constraints.
- Canceling work by name.
- Querying current work state.
- Observing work state changes.
- Keeping WorkManager setup behind `BackgroundWorkScheduler`.

## Basic Usage

```kotlin
val workName = BackgroundWorkName.unsafe("sync-profile")
val scheduler = WorkManagerBackgroundWorkScheduler(
    context = context,
    workerClasses = mapOf(workName to SyncProfileWorker::class.java)
)

scheduler.enqueue(
    BackgroundWorkRequest(
        name = workName,
        type = BackgroundWorkType.OneTime
    )
)
```

## Constraints And Input

```kotlin
scheduler.enqueue(
    BackgroundWorkRequest(
        name = BackgroundWorkName.unsafe("upload-logs"),
        type = BackgroundWorkType.OneTime,
        policy = BackgroundWorkPolicy.ReplaceExisting,
        constraints = BackgroundWorkConstraints(
            requiresNetwork = true,
            requiresCharging = false
        ),
        input = mapOf("reason" to "manual")
    )
)
```

## Periodic Work

```kotlin
scheduler.enqueue(
    BackgroundWorkRequest(
        name = BackgroundWorkName.unsafe("refresh-config"),
        type = BackgroundWorkType.Periodic(
            repeatIntervalMillis = 12 * 60 * 60 * 1000L
        ),
        constraints = BackgroundWorkConstraints(requiresNetwork = true)
    )
)
```

WorkManager enforces platform minimums for periodic work intervals. Keep worker classes and business logic in the app.

## Query, Observe, And Cancel

```kotlin
val infoResult = scheduler.getWorkInfo(workName)

scheduler.observeWorkInfo(workName).collect { info ->
    val status = info?.status
}

scheduler.cancel(workName)
```

## No-Op Scheduler

```kotlin
val scheduler: BackgroundWorkScheduler = NoOpBackgroundWorkScheduler()
```

Use the no-op scheduler for disabled environments or app layers that should not schedule work.

## Boundaries

Worker classes stay in the consuming app. The SDK schedules app-owned workers; it does not own background task business logic.
