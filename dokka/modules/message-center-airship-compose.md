# Module lantern-message-center-airship-compose

Airship Message Center Compose wrapper for Lantern apps.

# Package com.norbertotaveras.lantern.messagecenter.airship.compose

Use this package when a Compose app wants to embed Airship's official Message Center UI through a Lantern module entry point.

The module depends on Airship's Message Center Compose artifact and delegates rendering to Airship's `MessageCenterScreen`. It keeps Airship initialization, dashboard content, theming policy, and navigation ownership in the consuming app.

Use `notifications-airship` for push, channel ID/token access, audience, contact, and privacy/data collection controls. Add this module only when the app also needs Airship Message Center UI.
