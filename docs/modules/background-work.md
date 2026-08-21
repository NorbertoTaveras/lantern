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

## No-Op Scheduler

```kotlin
val scheduler: BackgroundWorkScheduler = NoOpBackgroundWorkScheduler()
```

Use the no-op scheduler for disabled environments or app layers that should not schedule work.

## Boundaries

Worker classes stay in the consuming app. The SDK schedules app-owned workers; it does not own background task business logic.
