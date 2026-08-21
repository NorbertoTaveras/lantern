# Module mobilefoundation-background-work

Background work scheduling contracts and WorkManager implementation.

# Package com.norbertotaveras.mobilefoundation.backgroundwork

Defines background work names, IDs, constraints, policies, request types, status models, scheduler contracts, a no-op scheduler, and a WorkManager-backed scheduler.

Consumers provide their own `ListenableWorker` classes and register them by SDK work name.
