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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
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
