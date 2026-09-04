# Module lantern-notifications-airship

Airship notification bridge for Lantern notification contracts.

# Package com.norbertotaveras.lantern.notifications.airship

Use this package when app code wants to bridge Airship push/channel state into Lantern notification contracts without making provider-neutral notification code depend on Airship APIs.

The first Airship slice keeps Airship SDK initialization and configuration app-owned. A consuming app or the later runtime adapter supplies an `AirshipPushGateway`, while Lantern exposes `NotificationTokenProvider` and user-notification enablement helpers around that gateway.
