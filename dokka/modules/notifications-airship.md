# Module lantern-notifications-airship

Airship notification bridge for Lantern notification contracts.

# Package com.norbertotaveras.lantern.notifications.airship

Use this package when app code wants to bridge Airship push/channel state into Lantern notification contracts without making provider-neutral notification code depend on Airship APIs.

The Airship bridge keeps Airship SDK credentials and application resources app-owned. Use `AirshipNotificationConfig` with `AirshipConfigOptionsFactory` when manually creating Airship config, or subclass `LanternAirshipAutopilot` when using Airship's manifest-driven Autopilot setup. Use the Airship SDK gateways after Airship has been initialized, or provide custom gateways for tests and advanced integrations.

Lantern exposes `NotificationTokenProvider`, user-notification enablement helpers, push event observation, foreground notification display control, Airship notification channel registration, channel tags, channel attributes, channel subscription lists, contact identity, contact attributes, contact subscription lists, and privacy/data collection feature toggles through Lantern result contracts.

Airship channel IDs are created asynchronously, so token lookup can return a failure while the channel is still unavailable. Channel deletion is not supported by this bridge; use `AirshipUserNotificationsManager` to enable or disable user-visible notifications.

The consuming app still owns Airship app key, app secret, site, FCM/provider setup, notification icon, accent color, notification channel, and whether user-visible notifications should be enabled after Airship is ready.

Airship In-App Experiences, Message Center, and Preference Center are not included in this module because they add product-specific and often UI-specific setup. Those integrations should be added as separate provider modules or optional UI modules when Lantern is ready to expose them publicly.
