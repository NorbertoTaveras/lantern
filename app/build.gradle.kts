import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.norbertotaveras.mobilefoundationframework"
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
        applicationId = "com.norbertotaveras.mobilefoundationframework"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
        resValue(
            "string",
            "firebase_web_client_id",
            "\"$firebaseWebClientId\""
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

val mobileFoundationVersion = providers.gradleProperty("SAMPLE_APP_MOBILE_FOUNDATION_VERSION")
    .orElse("0.1.0-dev.2-SNAPSHOT")

dependencies {
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-sdk-core:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-logging:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-core:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-firebase:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-google:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-firebase-google:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-permissions:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-secure-storage:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-remote-config:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-remote-config-firebase:${mobileFoundationVersion.get()}")
    implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-feature-flags:${mobileFoundationVersion.get()}")
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
