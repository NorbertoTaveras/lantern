# Notifications

`notifications` defines provider-neutral notification models and contracts. `notifications-firebase` adds Firebase Messaging support.

```kotlin
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-notifications:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-notifications-firebase:$mobileFoundationVersion")
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

## Topics

```kotlin
val topicManager = FirebaseMessagingTopicManager()
topicManager.subscribe(NotificationTopic.unsafe("product-updates"))
topicManager.unsubscribe(NotificationTopic.unsafe("product-updates"))
```

## Boundaries

The app owns notification display UI, notification click handling, Firebase project setup, and Android notification permission prompts. The SDK provides typed contracts and provider helpers.
