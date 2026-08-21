# Analytics

`analytics` defines provider-neutral analytics contracts. `analytics-firebase` implements those contracts with Firebase Analytics.

```kotlin
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-analytics:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-analytics-firebase:$mobileFoundationVersion")
```

## Use It For

- Tracking typed analytics events.
- Setting and clearing analytics user IDs.
- Setting user properties.
- Resetting analytics state.
- Swapping provider implementations behind `AnalyticsProvider`.

## Basic Usage

```kotlin
val analyticsProvider: AnalyticsProvider = FirebaseAnalyticsProvider(context)

analyticsProvider.track(
    AnalyticsEvent(
        name = AnalyticsEventName.unsafe("screen_view"),
        parameters = mapOf("screen" to AnalyticsValue.StringValue("home"))
    )
)
```

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

## Disabled Analytics

```kotlin
val analyticsProvider: AnalyticsProvider = NoOpAnalyticsProvider()
```

## Boundaries

Analytics event naming and privacy policy are app decisions. The SDK provides typed event plumbing and provider mapping.
