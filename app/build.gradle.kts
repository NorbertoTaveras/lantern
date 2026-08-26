import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
}

val lanternVersion = providers.gradleProperty("SAMPLE_APP_LANTERN_VERSION")
    .orElse("0.1.1-SNAPSHOT")

android {
    namespace = "com.norbertotaveras.lanternsample"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    val localProperties = Properties().apply {
        val localPropertiesFile = rootProject.file("local.properties")

        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { load(it) }
        }
    }

    val firebaseWebClientId: String =
        localProperties.getProperty("FIREBASE_WEB_CLIENT_ID", "")

    defaultConfig {
        applicationId = "com.norbertotaveras.lanternsample"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
        resValue(
            "string",
            "firebase_web_client_id",
            "\"$firebaseWebClientId\""
        )
        buildConfigField(
            "String",
            "LANTERN_VERSION",
            "\"${lanternVersion.get()}\""
        )
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
        resValues = true
        compose = true
    }
}

dependencies {
    implementation("io.github.norbertotaveras.lantern:lantern-core:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-logging:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-auth-core:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-auth-firebase:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-auth-google:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-auth-firebase-google:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-permissions:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-secure-storage:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-remote-config:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-remote-config-firebase:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-feature-flags:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-network-okhttp:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-notifications:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-notifications-firebase:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-media-picker:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-analytics:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-analytics-firebase:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-deep-links:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-background-work:${lanternVersion.get()}")
    implementation("io.github.norbertotaveras.lantern:lantern-app-versioning:${lanternVersion.get()}")
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
