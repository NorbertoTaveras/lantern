# Airship Preference Center Compose

`preference-center-airship-compose` provides a Lantern entry point for Airship's official Jetpack Compose Preference Center UI.

```kotlin
val lanternAirshipVersion = "0.2.0"

implementation("io.github.norbertotaveras.lantern:lantern-preference-center-airship-compose:$lanternAirshipVersion")
```

!!! note "Release availability"
    This module is available starting in Lantern `0.2.0`. Keep public app builds on the latest Maven Central version that contains the artifact you are using.

## Use It For

- Embedding Airship Preference Centers in a Compose screen.
- Keeping Airship Preference Center UI dependency separate from push/audience helpers.
- Letting users manage dashboard-configured subscription preferences.

!!! info "App-owned Airship setup"
    Airship initialization, Preference Center IDs, dashboard configuration, legal copy,
    subscription taxonomy, theming decisions, and navigation stay in the consuming app.

## Display Preference Center

Use the Lantern wrapper with the Preference Center identifier configured in Airship:

```kotlin
LanternAirshipPreferenceCenterScreen(
    identifier = "my-first-pref-center",
    onNavigateUp = { navController.popBackStack() }
)
```

The wrapper delegates to Airship's Compose `PreferenceCenterScreen` and wraps it in Airship's `PreferenceCenterTheme`.

## Navigation Ownership

`onNavigateUp` belongs to your app. Connect it to your navigation stack, drawer, or destination close behavior:

```kotlin
LanternAirshipPreferenceCenterScreen(
    identifier = preferenceCenterId,
    onNavigateUp = onClosePreferenceCenter
)
```

## Relationship To Notifications Airship

Use `notifications-airship` for push, channel ID/token access, audience, contact, and privacy controls.

Use `preference-center-airship-compose` when your app also wants Airship's Preference Center UI:

```kotlin
implementation("io.github.norbertotaveras.lantern:lantern-notifications-airship:$lanternAirshipVersion")
implementation("io.github.norbertotaveras.lantern:lantern-preference-center-airship-compose:$lanternAirshipVersion")
```

## App Responsibilities

Lantern does not create Preference Center definitions, legal wording, or subscription list taxonomy. Those responsibilities remain in your application and Airship dashboard.
