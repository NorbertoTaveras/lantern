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
