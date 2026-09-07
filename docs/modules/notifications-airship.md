# Airship Notifications

`notifications-airship` bridges Airship push, channel audience, contact identity, and privacy/data collection controls into Lantern's notification and result contracts.

```kotlin
implementation("io.github.norbertotaveras.lantern:lantern-notifications:$lanternVersion")
implementation("io.github.norbertotaveras.lantern:lantern-notifications-airship:$lanternVersion")
```

!!! note "Release availability"
    The Airship module is part of the next Lantern release line. Keep public app builds on the
    latest Maven Central version that contains the artifact you are using.

## Use It For

- Reading the Airship channel ID as a Lantern notification token.
- Enabling or disabling Airship user-visible notifications.
- Observing push received, posted, opened, dismissed, action, token, and status events.
- Creating Airship notification channels from Lantern channel config.
- Controlling foreground notification display behavior.
- Updating channel tags, attributes, and subscription lists.
- Identifying contacts with Airship named users.
- Updating contact attributes and scoped subscription lists.
- Connecting app consent flows to Airship privacy feature toggles.

!!! info "App-owned Airship setup"
    Airship app keys, app secrets, site, push provider setup, Firebase Cloud Messaging setup,
    notification icon, default notification channel, dashboard campaigns, and notification
    permission timing all stay in the consuming app.

## Initialize Airship

Apps can create Airship config manually with Lantern's config helper:

```kotlin
val options = AirshipConfigOptionsFactory.create(
    AirshipNotificationConfig(
        appKey = "YOUR_AIRSHIP_APP_KEY",
        appSecret = "YOUR_AIRSHIP_APP_SECRET",
        site = AirshipNotificationSite.US,
        notificationIconResId = R.drawable.ic_notification,
        notificationAccentColor = 0xFF1A73E8.toInt(),
        notificationChannel = "default",
        userNotificationsEnabled = false
    )
)
```

Or subclass `LanternAirshipAutopilot` when your app uses Airship's manifest-driven Autopilot setup:

```kotlin
class AppAirshipAutopilot : LanternAirshipAutopilot() {
    override fun createLanternAirshipConfig(context: Context): AirshipNotificationConfig {
        return AirshipNotificationConfig(
            appKey = BuildConfig.AIRSHIP_APP_KEY,
            appSecret = BuildConfig.AIRSHIP_APP_SECRET,
            site = AirshipNotificationSite.US,
            notificationIconResId = R.drawable.ic_notification,
            notificationChannel = "default",
            userNotificationsEnabled = false
        )
    }
}
```

Keep credentials out of source control. Use build config, environment-specific config, or your app's existing secret management flow.

The Lantern sample app stays in demo mode unless these values are present in local `local.properties`:

```properties
AIRSHIP_APP_KEY=your_airship_app_key
AIRSHIP_APP_SECRET=your_airship_app_secret
AIRSHIP_SITE=US
```

When those values are missing, the Airship screen shows the exact missing property name and keeps
using the credential-free demo gateway. When they are present, the sample attempts Airship `takeOff`
and switches to the real SDK gateways only if Airship initializes successfully. The screen never
prints the actual key or secret.

## Channel Token And Notification Enablement

Airship identifies an app install with an Airship channel ID. Lantern exposes that channel ID through the notification-token contract:

```kotlin
val pushGateway = AirshipSdkPushGateway()
val tokenProvider = AirshipNotificationTokenProvider(pushGateway)
val notificationManager = AirshipUserNotificationsManager(pushGateway)

when (val tokenResult = tokenProvider.getToken()) {
    is SdkResult.Success -> {
        sendPushTokenToBackend(tokenResult.data.value)
    }
    is SdkResult.Failure -> {
        logger.warn("Airship channel unavailable: ${tokenResult.error.code}")
    }
}

notificationManager.enableUserNotifications()
```

Channel ID lookup can fail while Airship is still registering the channel. Treat that as a retryable app state, not a fatal setup failure.

## Push Events And Foreground Display

Use `AirshipPushEventsManager` to observe Airship push events through a Flow:

```kotlin
val pushEventsManager = AirshipPushEventsManager(AirshipSdkPushEventGateway())

pushEventsManager.observePushEvents().collect { event ->
    when (event.type) {
        AirshipPushEventType.Received -> logger.debug("Airship push received")
        AirshipPushEventType.Opened -> openDeepLink(event)
        AirshipPushEventType.TokenUpdated -> syncChannelId(event.pushToken)
        else -> Unit
    }
}
```

Foreground notification display remains an app product decision:

```kotlin
pushEventsManager.setForegroundNotificationDisplayEnabled(enabled = true)
```

!!! warning "Notification listener ownership"
    Airship exposes a single notification listener. `AirshipSdkPushEventGateway` installs a
    listener while its Flow is collected and restores the previous listener when collection closes.
    If your app already owns an Airship notification listener, coordinate that ownership explicitly.

## Notification Channels

```kotlin
pushEventsManager.createNotificationChannel(
    NotificationChannelConfig(
        id = NotificationChannelId.unsafe("product_updates"),
        name = "Product updates",
        description = "Product news and account updates.",
        importance = NotificationChannelImportance.Default
    )
)
```

Apps still own the default channel strategy and notification presentation policy.

## Channel Audience

Use channel audience helpers for device/channel-level segmentation:

```kotlin
val audienceManager = AirshipAudienceManager(AirshipSdkAudienceGateway())

audienceManager.addTags(setOf("premium", "android"))
audienceManager.setAttribute(
    name = "plan",
    value = AirshipAudienceAttributeValue.StringValue("premium")
)
audienceManager.subscribeToLists(setOf("weekly-updates"))
```

Channel tags and attributes should use stable names that match your Airship dashboard taxonomy.

## Contact Identity

Use contact helpers when your signed-in user model should be attached to Airship named users:

```kotlin
val contactManager = AirshipContactManager(AirshipSdkContactGateway())

contactManager.identify("user-123")
contactManager.setAttribute(
    name = "tier",
    value = AirshipAudienceAttributeValue.StringValue("gold")
)
contactManager.subscribeToLists(
    listIds = setOf("weekly-updates"),
    scope = AirshipContactSubscriptionScope.Email
)
```

Reset the contact when the app signs out:

```kotlin
contactManager.reset()
```

## Privacy And Data Collection

Use privacy helpers to connect your app's consent flow to Airship feature toggles:

```kotlin
val privacyManager = AirshipPrivacyManager(AirshipSdkPrivacyGateway())

privacyManager.setEnabledFeatures(
    setOf(
        AirshipPrivacyFeature.Push,
        AirshipPrivacyFeature.TagsAndAttributes,
        AirshipPrivacyFeature.Contacts
    )
)

privacyManager.disableFeatures(setOf(AirshipPrivacyFeature.Analytics))
```

Your app still owns legal review, consent copy, privacy policy, and the decision about which Airship features can run for each user.

!!! info "Optional Airship UI"
    Airship Message Center, Preference Center, and In-App Experiences should be added through
    their own optional Lantern modules when available.
