package com.norbertotaveras.mobilefoundation.notifications.firebase

/**
 * Stable error codes returned by Firebase Messaging integration.
 */
object FirebaseMessagingErrorCodes {
    const val UNKNOWN = "firebase_messaging_unknown"
    const val TOKEN_UNAVAILABLE = "firebase_messaging_token_unavailable"
    const val UNREGISTER_FAILED = "firebase_messaging_unregister_failed"
    const val TOPIC_SUBSCRIPTION_FAILED = "firebase_messaging_topic_subscription_failed"
    const val TOPIC_UNSUBSCRIPTION_FAILED = "firebase_messaging_topic_unsubscription_failed"
}
