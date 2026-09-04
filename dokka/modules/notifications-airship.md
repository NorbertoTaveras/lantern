# Module lantern-notifications-airship

Airship notification bridge for Lantern notification contracts.

# Package com.norbertotaveras.lantern.notifications.airship

Use this package when app code wants to bridge Airship push/channel state into Lantern notification contracts without making provider-neutral notification code depend on Airship APIs.

The Airship bridge keeps Airship SDK initialization and configuration app-owned. Use `AirshipSdkPushGateway` after Airship has been initialized, or provide a custom `AirshipPushGateway` for tests and advanced integrations. Lantern exposes `NotificationTokenProvider` and user-notification enablement helpers around that gateway.

Airship channel IDs are created asynchronously, so token lookup can return a failure while the channel is still unavailable. Channel deletion is not supported by this bridge; use `AirshipUserNotificationsManager` to enable or disable user-visible notifications.
