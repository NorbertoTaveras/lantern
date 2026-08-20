package com.norbertotaveras.mobilefoundation.remoteconfig.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigSettings

/**
 * Converts SDK remote config settings into Firebase Remote Config settings.
 */
fun RemoteConfigSettings.toFirebase(): FirebaseRemoteConfigSettings {
    return FirebaseRemoteConfigSettings.Builder()
        .setMinimumFetchIntervalInSeconds(minimumFetchIntervalMillis / MILLIS_PER_SECOND)
        .setFetchTimeoutInSeconds(fetchTimeoutMillis / MILLIS_PER_SECOND)
        .build()
}

private const val MILLIS_PER_SECOND = 1_000L
