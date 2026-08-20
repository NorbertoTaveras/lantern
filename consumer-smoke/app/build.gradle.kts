plugins {
    id("com.android.application")
}

android {
    namespace = "com.norbertotaveras.mobilefoundation.consumer.smoke"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.norbertotaveras.mobilefoundation.consumer.smoke"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

val mobileFoundationVersion = providers.gradleProperty("CONSUMER_SMOKE_MOBILE_FOUNDATION_VERSION")
    .orElse(providers.gradleProperty("MOBILE_FOUNDATION_VERSION"))
    .orElse("0.1.0-SNAPSHOT")

dependencies {
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-sdk-core:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-logging:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-core:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-firebase:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-google:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-firebase-google:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-permissions:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-secure-storage:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-network-okhttp:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-remote-config:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-remote-config-firebase:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-feature-flags:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-notifications:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-notifications-firebase:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-media-picker:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-analytics:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-analytics-firebase:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-deep-links:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-background-work:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-app-versioning:${mobileFoundationVersion.get()}")
}
