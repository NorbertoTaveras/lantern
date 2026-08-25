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
val token = tokenProvider.getToken()
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

## Topics

```kotlin
val topicManager = FirebaseMessagingTopicManager()
topicManager.subscribe(NotificationTopic.unsafe("product-updates"))
topicManager.unsubscribe(NotificationTopic.unsafe("product-updates"))
```

## Boundaries

The app owns notification display UI, notification click handling, Firebase project setup, and Android notification permission prompts. The SDK provides typed contracts and provider helpers.
