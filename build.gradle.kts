import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

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
        }
    }
}
