# Airship Message Center Compose

`message-center-airship-compose` provides a Lantern entry point for Airship's official Jetpack Compose Message Center UI.

```kotlin
val lanternAirshipVersion = "0.2.0"

implementation("io.github.norbertotaveras.lantern:lantern-message-center-airship-compose:$lanternAirshipVersion")
```

!!! note "Release availability"
    This module is available starting in Lantern `0.2.0`. Keep public app builds on the latest Maven Central version that contains the artifact you are using.

## Use It For

- Embedding Airship's Message Center inbox in a Compose screen.
- Keeping Airship Message Center UI dependency separate from push/audience helpers.
- Giving app code a stable Lantern wrapper while Airship owns the actual inbox UI.

!!! info "App-owned Airship setup"
    Airship initialization, channel creation, Message Center dashboard content, push provider setup,
    theming decisions, and navigation stay in the consuming app.

## Display Message Center

Use the Lantern wrapper where your app wants to show the complete Airship Message Center:

```kotlin
LanternAirshipMessageCenterScreen(
    showListNavigateUpIcon = true,
    onNavigateUp = { navController.popBackStack() }
)
```

The wrapper delegates to Airship's Compose `MessageCenterScreen` and wraps it in Airship's `MessageCenterTheme`.

## Navigation Ownership

`onNavigateUp` belongs to your app. Connect it to your navigation stack, drawer, or destination close behavior:

```kotlin
LanternAirshipMessageCenterScreen(
    showListNavigateUpIcon = true,
    onNavigateUp = onCloseMessageCenter
)
```

## Relationship To Notifications Airship

Use `notifications-airship` for push, channel ID/token access, audience, contact, and privacy controls.

Use `message-center-airship-compose` when your app also wants Airship's Message Center UI:

```kotlin
implementation("io.github.norbertotaveras.lantern:lantern-notifications-airship:$lanternAirshipVersion")
implementation("io.github.norbertotaveras.lantern:lantern-message-center-airship-compose:$lanternAirshipVersion")
```

## App Responsibilities

Lantern does not author Message Center content, create Airship dashboard campaigns, or decide how your app routes into the inbox. Those responsibilities remain in the application and Airship dashboard.
