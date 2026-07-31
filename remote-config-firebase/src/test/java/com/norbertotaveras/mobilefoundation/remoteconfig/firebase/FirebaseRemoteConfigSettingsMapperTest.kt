package com.norbertotaveras.mobilefoundation.remoteconfig.firebase

import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigSettings
import org.junit.Assert.assertEquals
import org.junit.Test

class FirebaseRemoteConfigSettingsMapperTest {

    @Test
    fun toFirebaseConvertsMillisToSeconds() {
        val settings = RemoteConfigSettings(
            minimumFetchIntervalMillis = 5_000,
            fetchTimeoutMillis = 10_000
        ).toFirebase()

        assertEquals(5, settings.minimumFetchIntervalInSeconds)
        assertEquals(10, settings.fetchTimeoutInSeconds)
    }
}
