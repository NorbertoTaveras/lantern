plugins {
    id("com.android.application")
}

android {
    namespace = "com.norbertotaveras.lantern.consumer.smoke"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.norbertotaveras.lantern.consumer.smoke"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            optimization {
                enable = true
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

val lanternVersion = providers.gradleProperty("CONSUMER_SMOKE_LANTERN_VERSION")
    .orElse(providers.gradleProperty("LANTERN_VERSION"))
    .orElse("0.1.1-SNAPSHOT")

dependencies {
    implementation("io.github.norbertotaveras.lantern:lantern-core:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-logging:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-auth-core:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-auth-firebase:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-auth-google:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-auth-firebase-google:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-permissions:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-secure-storage:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-network-okhttp:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-remote-config:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-remote-config-firebase:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-feature-flags:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-notifications:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-notifications-firebase:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-media-picker:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-analytics:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-analytics-firebase:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-deep-links:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-background-work:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-app-versioning:${lanternVersion.get()}")
}
