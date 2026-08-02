import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.plugins.signing.SigningExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.services) apply false
}

subprojects {
    group = "com.norbertotaveras.mobilefoundation"
    version = providers.gradleProperty("MOBILE_FOUNDATION_VERSION")
        .orElse("0.1.0-SNAPSHOT")
        .get()

    plugins.withId("com.android.application") {
        extensions.configure<ApplicationExtension>("android") {
            lint {
                abortOnError = true
                checkDependencies = true
                warningsAsErrors = true
                disable += "GradleDependency"
                disable += "AndroidGradlePluginVersion"
            }
        }
    }

    plugins.withId("com.android.library") {
        apply(plugin = "maven-publish")
        apply(plugin = "signing")

        extensions.configure<LibraryExtension>("android") {
            lint {
                abortOnError = true
                checkDependencies = true
                warningsAsErrors = true
                disable += "GradleDependency"
                disable += "AndroidGradlePluginVersion"
            }

            publishing {
                singleVariant("release") {
                    withSourcesJar()
                }
            }
        }

        extensions.configure<PublishingExtension>("publishing") {
            repositories {
                maven {
                    name = "mobileFoundationLocal"
                    url = rootProject.layout.buildDirectory
                        .dir("local-maven")
                        .get()
                        .asFile
                        .toURI()
                }
                maven {
                    name = "gitHubPackages"
                    url = uri(
                        "https://maven.pkg.github.com/${
                            providers.gradleProperty("GITHUB_PACKAGES_REPOSITORY")
                                .orElse(providers.environmentVariable("GITHUB_REPOSITORY"))
                                .orElse("norbertotaveras/android_mobilefoundation_framework")
                                .get()
                        }"
                    )
                    credentials {
                        username = providers.gradleProperty("GITHUB_PACKAGES_USERNAME")
                            .orElse(providers.environmentVariable("GITHUB_ACTOR"))
                            .orElse("")
                            .get()
                        password = providers.gradleProperty("GITHUB_PACKAGES_TOKEN")
                            .orElse(providers.environmentVariable("GITHUB_TOKEN"))
                            .orElse("")
                            .get()
                    }
                }
            }
        }

        afterEvaluate {
            extensions.configure<PublishingExtension>("publishing") {
                publications {
                    create<MavenPublication>("release") {
                        from(components["release"])

                        groupId = project.group.toString()
                        artifactId = "mobilefoundation-${project.name}"
                        version = project.version.toString()

                        pom {
                            name.set("Mobile Foundation ${project.name}")
                            description.set("Mobile Foundation SDK module ${project.name}.")
                        }
                    }
                }
            }

            val signingKey = providers.gradleProperty("MAVEN_SIGNING_KEY")
                .orElse(providers.environmentVariable("MAVEN_SIGNING_KEY"))
                .orNull
            val signingPassword = providers.gradleProperty("MAVEN_SIGNING_PASSWORD")
                .orElse(providers.environmentVariable("MAVEN_SIGNING_PASSWORD"))
                .orNull

            if (!signingKey.isNullOrBlank() && !signingPassword.isNullOrBlank()) {
                extensions.configure<SigningExtension>("signing") {
                    useInMemoryPgpKeys(signingKey, signingPassword)
                    sign(extensions.getByType(PublishingExtension::class.java).publications["release"])
                }
            }
        }
    }
}

val sdkModuleNames = listOf(
    "sdk-core",
    "logging",
    "auth-core",
    "auth-firebase",
    "auth-google",
    "auth-firebase-google",
    "permissions",
    "secure-storage",
    "network-okhttp",
    "remote-config",
    "remote-config-firebase",
    "feature-flags",
    "notifications",
    "notifications-firebase",
    "media-picker",
)

tasks.register("checkPublishingGroup") {
    group = "verification"
    description = "Checks SDK Maven publication coordinates before CI publishing."

    doLast {
        sdkModuleNames.forEach { moduleName ->
            val sdkProject = project(":$moduleName")
            check(sdkProject.plugins.hasPlugin("com.android.library")) {
                "Expected :$moduleName to be an Android library module."
            }
            check(sdkProject.group.toString() == "com.norbertotaveras.mobilefoundation") {
                "Expected :$moduleName group to be com.norbertotaveras.mobilefoundation, but was ${sdkProject.group}."
            }
            check(sdkProject.version.toString().isNotBlank()) {
                "Expected :$moduleName version to be set."
            }

            val publishing = sdkProject.extensions.getByType(PublishingExtension::class.java)
            val releasePublication = publishing.publications.findByName("release") as? MavenPublication
                ?: error("Expected :$moduleName to publish a Maven publication named release.")

            check(releasePublication.groupId == "com.norbertotaveras.mobilefoundation") {
                "Expected :$moduleName publication groupId to be com.norbertotaveras.mobilefoundation."
            }
            check(releasePublication.artifactId == "mobilefoundation-$moduleName") {
                "Expected :$moduleName artifactId to be mobilefoundation-$moduleName, but was ${releasePublication.artifactId}."
            }
            check(releasePublication.version == sdkProject.version.toString()) {
                "Expected :$moduleName publication version to match project version."
            }
        }
    }
}

tasks.register("checkSdkArchitecture") {
    group = "verification"
    description = "Checks SDK module boundaries and provider isolation rules."

    doLast {
        val dependencyConfigurationNames = setOf(
            "api",
            "implementation",
            "compileOnly",
            "runtimeOnly",
        )
        val forbiddenUiGroups = listOf(
            "androidx.compose",
            "androidx.navigation",
        )
        val providerIsolationRules = mapOf(
            "sdk-core" to listOf(
                "com.google.firebase",
                "androidx.credentials",
                "com.google.android.libraries.identity.googleid",
            ),
            "auth-core" to listOf(
                "com.google.firebase",
                "androidx.credentials",
                "com.google.android.libraries.identity.googleid",
            ),
            "auth-google" to listOf(
                "com.google.firebase",
            ),
            "remote-config" to listOf(
                "com.google.firebase",
            ),
            "notifications" to listOf(
                "com.google.firebase",
            ),
        )

        sdkModuleNames.forEach { moduleName ->
            val sdkProject = project(":$moduleName")
            val dependencies = sdkProject.configurations
                .matching { it.name in dependencyConfigurationNames }
                .flatMap { it.dependencies }

            dependencies.filterIsInstance<ProjectDependency>().forEach { dependency ->
                check(dependency.path != ":app") {
                    "SDK module :$moduleName must not depend on the sample app module."
                }
            }

            dependencies.forEach { dependency ->
                val dependencyGroup = dependency.group.orEmpty()
                check(forbiddenUiGroups.none { dependencyGroup == it || dependencyGroup.startsWith("$it.") }) {
                    "SDK module :$moduleName must stay UI-independent, but depends on ${dependency.group}:${dependency.name}."
                }

                providerIsolationRules[moduleName].orEmpty().forEach { forbiddenGroup ->
                    check(dependencyGroup != forbiddenGroup && !dependencyGroup.startsWith("$forbiddenGroup.")) {
                        "SDK module :$moduleName must not depend on provider group $forbiddenGroup."
                    }
                }
            }
        }
    }
}

tasks.register("checkApiCompatibility") {
    group = "verification"
    description = "Checks generated release publication metadata used to validate the SDK public API surface."
    dependsOn(
        sdkModuleNames.flatMap { moduleName ->
            listOf(
                ":$moduleName:generatePomFileForReleasePublication",
                ":$moduleName:generateMetadataFileForReleasePublication",
            )
        },
    )

    doLast {
        sdkModuleNames.forEach { moduleName ->
            val sdkProject = project(":$moduleName")
            val publicationDirectory = sdkProject.layout.buildDirectory
                .dir("publications/release")
                .get()
                .asFile
            val pomFile = publicationDirectory.resolve("pom-default.xml")
            val moduleMetadataFile = publicationDirectory.resolve("module.json")

            check(pomFile.isFile) {
                "Expected release POM metadata for :$moduleName at ${pomFile.path}."
            }
            check(moduleMetadataFile.isFile) {
                "Expected Gradle module metadata for :$moduleName at ${moduleMetadataFile.path}."
            }
        }
    }
}

tasks.register("checkPublishingReadiness") {
    group = "verification"
    description = "Runs CI publishing, architecture, and API metadata checks for SDK modules."
    dependsOn(
        "checkPublishingGroup",
        "checkSdkArchitecture",
        "checkApiCompatibility",
    )
}
