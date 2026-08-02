import java.util.Properties

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

val localProperties = Properties().apply {
    val localPropertiesFile = file("local.properties")

    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

fun localProperty(name: String) = providers.provider {
    localProperties.getProperty(name).orEmpty()
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            name = "gitHubPackages"
            url = uri("https://maven.pkg.github.com/norbertotaveras/android_mobilefoundation_framework")
            credentials {
                username = providers.gradleProperty("GITHUB_PACKAGES_USERNAME")
                    .orElse(providers.environmentVariable("GITHUB_PACKAGES_USERNAME"))
                    .orElse(providers.environmentVariable("GITHUB_ACTOR"))
                    .orElse(localProperty("GITHUB_PACKAGES_USERNAME"))
                    .orElse("")
                    .get()
                password = providers.gradleProperty("GITHUB_PACKAGES_TOKEN")
                    .orElse(providers.environmentVariable("GITHUB_PACKAGES_TOKEN"))
                    .orElse(providers.environmentVariable("GITHUB_TOKEN"))
                    .orElse(localProperty("GITHUB_PACKAGES_TOKEN"))
                    .orElse("")
                    .get()
            }
            content {
                includeGroup("com.norbertotaveras.mobilefoundation")
            }
        }
    }
}

rootProject.name = "MobileFoundationFramework"
include(":app")
include(":sdk-core")
include(":logging")
include(":auth-core")
include(":auth-firebase")
include(":auth-google")
include(":auth-firebase-google")
include(":permissions")
include(":secure-storage")
include(":network-okhttp")
include(":remote-config")
include(":remote-config-firebase")
include(":feature-flags")
include(":notifications")
include(":notifications-firebase")
include(":media-picker")
