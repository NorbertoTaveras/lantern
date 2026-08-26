# Notifications

`notifications` defines provider-neutral notification models and contracts. `notifications-firebase` adds Firebase Messaging support.

```kotlin
implementation("io.github.norbertotaveras.lantern:lantern-notifications:$lanternVersion")
implementation("io.github.norbertotaveras.lantern:lantern-notifications-firebase:$lanternVersion")
```

## Use It For

- Parsing notification payload data.
- Resolving notification deep links.
- Modeling notification tokens.
- Managing notification topics.
- Managing notification channels.
- Checking notification permission state.
- Reading Firebase Messaging tokens and topic operations.

!!! info "Firebase Messaging setup"
    `notifications-firebase` expects Firebase Messaging to be configured by the consuming app.
    Keep `google-services.json`, Firebase project setup, notification icons/channels, and Android
    notification permission UI in the app.

!!! tip "Android notification permission"
    On Android 13 and newer, notification delivery depends on the app requesting
    `POST_NOTIFICATIONS`. The SDK models notification permission state, but the app owns the
    lifecycle-aware permission request.

## Payload Parsing

```kotlin
val parser = DefaultNotificationPayloadParser()
val payload = parser.parse(data = remoteMessage.data)
```

## Firebase Messaging Token

```kotlin
val tokenProvider = FirebaseMessagingTokenProvider()

when (val result = tokenProvider.getToken()) {
    is SdkResult.Success -> {
        val token = result.data
        sendPushTokenToBackend(token.value)
    }
    is SdkResult.Failure -> {
        logger.error("Unable to read FCM token: ${result.error.code}")
    }
}
```

The token value is the Firebase Cloud Messaging registration token. The Firebase Installation ID is
available as optional metadata when Firebase returns it:

```kotlin
val installationId = token.metadata["firebase_installation_id"]
```

Observe token state when the app wants a simple stream of the latest token known to the provider:

```kotlin
tokenProvider.tokenUpdates.collect { token ->
    if (token != null) {
        sendPushTokenToBackend(token.value)
    }
}
```

## Android Notification Channels

```kotlin
val channelManager = AndroidNotificationChannelManager(context)

channelManager.createChannel(
    NotificationChannelConfig(
        id = NotificationChannelId.unsafe("product_updates"),
        name = "Product updates",
        description = "Product news and account updates.",
        importance = NotificationChannelImportance.Default
    )
)
```

On Android versions before Oreo, channel operations complete successfully without creating platform
channels because notification channels are not supported there.

## Notification Permission

```kotlin
val notificationPermissionManager = AndroidNotificationPermissionManager(permissionManager)

val currentState = notificationPermissionManager.check()
val requestResult = notificationPermissionManager.request()
```

The `permissionManager` is provided by the `permissions` module and owns the app-provided runtime
permission launcher.

Use the check result to decide whether to show an app rationale before triggering a runtime request:

```kotlin
when (currentState.status) {
    NotificationPermissionStatus.Granted -> enableNotifications()
    NotificationPermissionStatus.NotDetermined -> showNotificationOptIn()
    NotificationPermissionStatus.Denied,
    NotificationPermissionStatus.PermanentlyDenied -> showSettingsEducation()
}
```

## Topics

```kotlin
val topicManager = FirebaseMessagingTopicManager()
val topic = NotificationTopic.unsafe("product-updates")

topicManager.subscribe(topic)
topicManager.unsubscribe(topic)
```

Keep topic names stable and backend-owned. Treat topic subscription as a user preference or account
state decision from the app layer.

## Boundaries

The app owns notification display UI, notification click handling, Firebase project setup, and Android notification permission prompts. The SDK provides typed contracts and provider helpers.
