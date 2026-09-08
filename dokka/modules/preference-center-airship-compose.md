# Module lantern-preference-center-airship-compose

Airship Preference Center Compose wrapper for Lantern apps.

# Package com.norbertotaveras.lantern.preferencecenter.airship.compose

Use this package when a Compose app wants to embed Airship's official Preference Center UI through a Lantern module entry point.

The module depends on Airship's Preference Center Compose artifact and delegates rendering to Airship's `PreferenceCenterScreen`. It keeps Airship initialization, Preference Center IDs, dashboard configuration, legal wording, theming policy, and navigation ownership in the consuming app.

Use `notifications-airship` for push, channel ID/token access, audience, contact, and privacy/data collection controls. Add this module only when the app also needs Airship Preference Center UI.
