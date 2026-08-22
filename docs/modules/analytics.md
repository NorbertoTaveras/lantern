# Analytics

`analytics` defines provider-neutral analytics contracts. `analytics-firebase` implements those contracts with Firebase Analytics.

```kotlin
implementation("com.norbertotaveras.lantern:lantern-analytics:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-analytics-firebase:$lanternVersion")
```

## Use It For

- Tracking typed analytics events.
- Setting and clearing analytics user IDs.
- Setting user properties.
- Resetting analytics state.
- Swapping provider implementations behind `AnalyticsProvider`.

!!! info "Firebase Analytics setup"
    `analytics-firebase` expects Firebase Analytics to be configured by the consuming app. Keep
    `google-services.json`, Firebase app registration, analytics collection policy, and consent
    decisions in the app.

!!! warning "Privacy and naming are app policy"
    The SDK provides typed event plumbing. Your app still owns analytics event naming, user consent,
    privacy review, and the decision about which identifiers or properties are safe to send.

## Basic Usage

```kotlin
val analyticsProvider: AnalyticsProvider = FirebaseAnalyticsProvider(context)

val trackResult = analyticsProvider.track(
    AnalyticsEvent(
        name = AnalyticsEventName.unsafe("screen_view"),
        parameters = mapOf("screen" to AnalyticsValue.StringValue("home"))
    )
)
```

Handle `SdkResult.Failure` near your app logging layer when providers reject event names or values.

## User State

```kotlin
analyticsProvider.setUserId(AnalyticsUserId.unsafe("user-123"))
analyticsProvider.setUserProperty(
    AnalyticsUserProperty(
        name = AnalyticsUserPropertyName.unsafe("plan"),
        value = AnalyticsValue.StringValue("pro")
    )
)
```

Clear user state when the app signs out:

```kotlin
analyticsProvider.setUserId(null)
analyticsProvider.reset()
```

## Disabled Analytics

```kotlin
val analyticsProvider: AnalyticsProvider = NoOpAnalyticsProvider()
```

## Boundaries

Analytics event naming and privacy policy are app decisions. The SDK provides typed event plumbing and provider mapping.
