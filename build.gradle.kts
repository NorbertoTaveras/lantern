import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SourcesJar
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.GradleException
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.jetbrains.dokka.gradle.DokkaExtension
import java.net.URI
import java.util.Properties
import java.util.jar.JarFile

val localProperties = Properties().apply {
    val localPropertiesFile = file("local.properties")

    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

fun localProperty(name: String) = providers.provider {
    localProperties.getProperty(name)
}

val mobileFoundationVersion = providers.gradleProperty("MOBILE_FOUNDATION_VERSION")
    .orElse("0.1.0-SNAPSHOT")
val mobileFoundationSourceRef = providers.gradleProperty("MOBILE_FOUNDATION_SOURCE_REF")
    .orElse(providers.environmentVariable("GITHUB_SHA"))
    .orElse("develop")

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.dokka)
}

subprojects {
    group = "com.norbertotaveras.mobilefoundation"
    version = mobileFoundationVersion.get()

    plugins.withId("com.android.application") {
        extensions.configure<ApplicationExtension>("android") {
            lint {
                abortOnError = true
                checkDependencies = true
                warningsAsErrors = true
                disable += "GradleDependency"
                disable += "AndroidGradlePluginVersion"
                disable += "UseTomlInstead"
            }
        }
    }

    plugins.withId("com.android.library") {
        apply(plugin = "com.vanniktech.maven.publish")
        apply(plugin = "org.jetbrains.dokka")

        extensions.configure<LibraryExtension>("android") {
            defaultConfig {
                consumerProguardFiles("consumer-rules.pro")
            }

            lint {
                abortOnError = true
                checkDependencies = true
                warningsAsErrors = true
                disable += "GradleDependency"
                disable += "AndroidGradlePluginVersion"
                disable += "UseTomlInstead"
            }
        }

        extensions.configure<MavenPublishBaseExtension>("mavenPublishing") {
            coordinates(
                groupId = project.group.toString(),
                artifactId = "mobilefoundation-${project.name}",
                version = project.version.toString(),
            )
            configure(
                AndroidSingleVariantLibrary(
                    variant = "release",
                    sourcesJar = SourcesJar.Sources(),
                    javadocJar = JavadocJar.Empty(),
                )
            )
            publishToMavenCentral(automaticRelease = true)

            val signingKey = providers.gradleProperty("signingInMemoryKey").orNull
            if (!signingKey.isNullOrBlank()) {
                signAllPublications()
            }

            pom {
                name.set("Mobile Foundation ${project.name}")
                description.set("Mobile Foundation SDK module ${project.name}.")
                inceptionYear.set("2026")
                url.set("https://github.com/NorbertoTaveras/android_mobilefoundation_framework")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("NorbertoTaveras")
                        name.set("Norberto Taveras")
                        url.set("https://github.com/NorbertoTaveras")
                    }
                }
                scm {
                    url.set("https://github.com/NorbertoTaveras/android_mobilefoundation_framework")
                    connection.set("scm:git:git://github.com/NorbertoTaveras/android_mobilefoundation_framework.git")
                    developerConnection.set("scm:git:ssh://git@github.com/NorbertoTaveras/android_mobilefoundation_framework.git")
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
                                .orElse(providers.environmentVariable("GITHUB_PACKAGES_REPOSITORY"))
                                .orElse(localProperty("GITHUB_PACKAGES_REPOSITORY"))
                                .orElse(providers.environmentVariable("GITHUB_REPOSITORY"))
                                .orElse("NorbertoTaveras/android_mobilefoundation_packages")
                                .get()
                        }"
                    )
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
                }
            }
        }

        extensions.configure<DokkaExtension>("dokka") {
            dokkaPublications.html {
                moduleName.set("Mobile Foundation ${project.name}")
                moduleVersion.set(project.version.toString())
                suppressObviousFunctions.set(true)
                suppressInheritedMembers.set(true)
                failOnWarning.set(false)
            }
            dokkaSourceSets.configureEach {
                val moduleIncludeFile = rootProject.layout.projectDirectory
                    .file("dokka/modules/${project.name}.md")
                    .asFile
                if (moduleIncludeFile.isFile) {
                    includes.from(moduleIncludeFile)
                }
                sourceLink {
                    localDirectory.set(project.layout.projectDirectory.dir("src/main/java"))
                    remoteUrl.set(
                        URI(
                            "https://github.com/NorbertoTaveras/android_mobilefoundation_framework/blob/" +
                                "${mobileFoundationSourceRef.get()}/${project.name}/src/main/java"
                        )
                    )
                    remoteLineSuffix.set("#L")
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
    "analytics",
    "analytics-firebase",
    "deep-links",
    "background-work",
    "app-versioning",
)

dependencies {
    sdkModuleNames.forEach { moduleName ->
        dokka(project(":$moduleName"))
    }
}

extensions.configure<DokkaExtension>("dokka") {
    dokkaPublications.html {
        moduleName.set("Mobile Foundation SDK")
        moduleVersion.set(mobileFoundationVersion)
        outputDirectory.set(layout.buildDirectory.dir("dokka/public-api"))
        suppressObviousFunctions.set(true)
        suppressInheritedMembers.set(true)
        failOnWarning.set(false)
    }
}

val generatedApiDocsDirectory = layout.projectDirectory.dir("docs/generated/api")

tasks.register<Delete>("cleanGeneratedApiDocs") {
    group = "documentation"
    description = "Removes generated API reference documentation from the public docs tree."
    delete(generatedApiDocsDirectory)
}

tasks.register<Copy>("generatePublicApiDocs") {
    group = "documentation"
    description = "Generates public SDK API reference documentation for the docs website."
    dependsOn("cleanGeneratedApiDocs", ":dokkaGeneratePublicationHtml")
    from(layout.buildDirectory.dir("dokka/public-api"))
    into(generatedApiDocsDirectory)
}

tasks.register("checkPublicKdocCoverage") {
    group = "verification"
    description = "Checks public SDK declarations have nearby KDoc for generated API docs."

    doLast {
        val publicDeclarationPatterns = listOf(
            Regex("""^\s*(data\s+)?(sealed\s+)?(class|interface|object|enum class|data class)\s+"""),
            Regex("""^\s*(suspend\s+)?fun\s+"""),
        )
        val failures = mutableListOf<String>()

        sdkModuleNames.forEach { moduleName ->
            val sourceDirectory = project(":$moduleName").projectDir.resolve("src/main/java")
            if (!sourceDirectory.isDirectory) {
                return@forEach
            }

            sourceDirectory
                .walkTopDown()
                .filter { it.isFile && it.extension == "kt" }
                .filterNot { it.toPath().any { pathPart -> pathPart.toString() == "internal" } }
                .forEach { sourceFile ->
                    val lines = sourceFile.readLines()
                    val hasInternalTopLevelType = lines.any { line ->
                        Regex("""^\s*internal\s+(class|object|interface|enum class)\s+""").containsMatchIn(line)
                    }
                    if (hasInternalTopLevelType) {
                        return@forEach
                    }

                    lines.forEachIndexed { index, line ->
                        val trimmedLine = line.trim()
                        if (
                            trimmedLine.startsWith("private ") ||
                            trimmedLine.startsWith("internal ") ||
                            trimmedLine.startsWith("override ") ||
                            trimmedLine.startsWith("public override ") ||
                            trimmedLine == "companion object"
                        ) {
                            return@forEachIndexed
                        }

                        if (publicDeclarationPatterns.none { it.containsMatchIn(line) }) {
                            return@forEachIndexed
                        }

                        val previousMeaningfulLine = lines
                            .take(index)
                            .asReversed()
                            .firstOrNull { previousLine ->
                                val trimmedPreviousLine = previousLine.trim()
                                trimmedPreviousLine.isNotEmpty() && !trimmedPreviousLine.startsWith("@")
                            }
                            ?.trim()

                        if (previousMeaningfulLine != "*/") {
                            failures += "$moduleName/${sourceFile.relativeTo(project(":$moduleName").projectDir)}:${index + 1}: $trimmedLine"
                        }
                    }
                }
        }

        if (failures.isNotEmpty()) {
            throw GradleException(
                buildString {
                    appendLine("Public KDoc coverage check failed.")
                    failures.forEach { appendLine("- $it") }
                    appendLine("Add concise KDoc for public SDK declarations that appear in generated API docs.")
                }
            )
        }
    }
}

tasks.register("checkGeneratedApiDocs") {
    group = "verification"
    description = "Checks generated API reference docs and public docs links."
    dependsOn("checkPublicKdocCoverage", "generatePublicApiDocs")

    doLast {
        val generatedApiDirectory = generatedApiDocsDirectory.asFile
        val generatedIndexFile = generatedApiDirectory.resolve("index.html")
        check(generatedIndexFile.isFile) {
            "Expected generated API reference index at ${generatedIndexFile.path}."
        }

        val generatedHtmlFiles = generatedApiDirectory
            .walkTopDown()
            .filter { it.isFile && it.extension == "html" }
            .toList()
        check(generatedHtmlFiles.isNotEmpty()) {
            "Expected generated API reference HTML files under ${generatedApiDirectory.path}."
        }

        sdkModuleNames.forEach { moduleName ->
            val moduleIncludeFile = layout.projectDirectory.file("dokka/modules/$moduleName.md").asFile
            check(moduleIncludeFile.isFile) {
                "Expected Dokka module overview include at ${moduleIncludeFile.path}."
            }
        }

        val generatedHtml = generatedHtmlFiles.joinToString(separator = "\n") { it.readText() }
        check(generatedHtml.contains("Use this module when app or feature code should depend on analytics behavior")) {
            "Expected generated API reference docs to include module Dokka overview content."
        }
        val sourceLinkPattern = Regex(
            """https://github\.com/NorbertoTaveras/android_mobilefoundation_framework/blob/[^"]+/[^"]+\.kt#L\d+"""
        )
        check(sourceLinkPattern.containsMatchIn(generatedHtml)) {
            "Expected generated API reference docs to include GitHub source links with #L line anchors."
        }

        val apiReferencePage = layout.projectDirectory.file("docs/api-reference.md").asFile
        check(apiReferencePage.readText().contains("generated/api/index.html")) {
            "Expected docs/api-reference.md to link to generated/api/index.html."
        }
    }
}

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
            val releasePublication = publishing.publications
                .filterIsInstance<MavenPublication>()
                .singleOrNull { it.artifactId == "mobilefoundation-$moduleName" }
                ?: error("Expected :$moduleName to publish one Maven publication for mobilefoundation-$moduleName.")

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
            "analytics" to listOf(
                "com.google.firebase",
                "com.newrelic",
                "com.datadog",
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

tasks.register("checkSdkAndroidMetadata") {
    group = "verification"
    description = "Checks SDK Android library metadata and app-only config boundaries."

    doLast {
        val expectedNamespacePrefix = "com.norbertotaveras.mobilefoundation"
        val expectedCompileSdkRelease = 37
        val expectedCompileSdkMinorApi = 1
        val expectedMinSdk = 24
        val forbiddenPluginIds = listOf(
            "com.android.application",
            "com.google.gms.google-services",
        )
        val forbiddenConfigFiles = listOf(
            "google-services.json",
            "GoogleService-Info.plist",
        )
        val failures = mutableListOf<String>()

        sdkModuleNames.forEach { moduleName ->
            val sdkProject = project(":$moduleName")
            val buildFile = sdkProject.projectDir.resolve("build.gradle.kts")
            val buildFileText = buildFile.readText()

            if (!sdkProject.plugins.hasPlugin("com.android.library")) {
                failures += ":$moduleName must apply com.android.library."
            }

            forbiddenPluginIds.forEach { pluginId ->
                if (sdkProject.plugins.hasPlugin(pluginId) || buildFileText.contains(pluginId)) {
                    failures += ":$moduleName must not apply app-only plugin $pluginId."
                }
            }

            val namespace = Regex("""namespace\s*=\s*"([^"]+)"""")
                .find(buildFileText)
                ?.groupValues
                ?.get(1)
            if (namespace.isNullOrBlank()) {
                failures += ":$moduleName must declare an Android namespace."
            } else if (!namespace.startsWith(expectedNamespacePrefix)) {
                failures += ":$moduleName namespace must start with $expectedNamespacePrefix, but was $namespace."
            }

            val compileSdkRelease = Regex("""version\s*=\s*release\((\d+)\)""")
                .find(buildFileText)
                ?.groupValues
                ?.get(1)
                ?.toIntOrNull()
            if (compileSdkRelease != expectedCompileSdkRelease) {
                failures += ":$moduleName compileSdk release must be $expectedCompileSdkRelease, but was $compileSdkRelease."
            }

            val compileSdkMinorApi = Regex("""minorApiLevel\s*=\s*(\d+)""")
                .find(buildFileText)
                ?.groupValues
                ?.get(1)
                ?.toIntOrNull()
            if (compileSdkMinorApi != expectedCompileSdkMinorApi) {
                failures += ":$moduleName compileSdk minorApiLevel must be $expectedCompileSdkMinorApi, but was $compileSdkMinorApi."
            }

            val minSdk = Regex("""minSdk\s*=\s*(\d+)""")
                .find(buildFileText)
                ?.groupValues
                ?.get(1)
                ?.toIntOrNull()
            if (minSdk != expectedMinSdk) {
                failures += ":$moduleName minSdk must be $expectedMinSdk, but was $minSdk."
            }

            forbiddenConfigFiles.forEach { fileName ->
                val configFiles = sdkProject.projectDir
                    .walkTopDown()
                    .filter { it.isFile && it.name == fileName }
                    .filterNot { it.toPath().any { pathPart -> pathPart.toString() == "build" } }
                    .toList()
                configFiles.forEach { configFile ->
                    failures += ":$moduleName must not own app config file ${configFile.relativeTo(sdkProject.projectDir)}."
                }
            }
        }

        if (failures.isNotEmpty()) {
            throw GradleException(
                buildString {
                    appendLine("SDK Android metadata check failed.")
                    failures.forEach { appendLine("- $it") }
                    appendLine("Keep SDK modules as publishable Android libraries and app configuration inside :app.")
                }
            )
        }
    }
}

tasks.register("checkSdkDependencyAllowlist") {
    group = "verification"
    description = "Checks SDK modules only depend on allowed project modules and external groups."

    doLast {
        val dependencyConfigurationNames = setOf(
            "api",
            "implementation",
            "compileOnly",
            "runtimeOnly",
        )
        val commonExternalGroups = setOf(
            "org.jetbrains.kotlin",
            "org.jetbrains.kotlinx",
        )
        val allowedProjectDependencies = mapOf(
            "sdk-core" to emptySet<String>(),
            "logging" to setOf("sdk-core"),
            "auth-core" to setOf("sdk-core", "logging"),
            "auth-firebase" to setOf("sdk-core", "logging", "auth-core"),
            "auth-google" to setOf("sdk-core", "logging"),
            "auth-firebase-google" to setOf("sdk-core", "logging", "auth-core", "auth-google", "auth-firebase"),
            "permissions" to setOf("sdk-core", "logging"),
            "secure-storage" to setOf("sdk-core", "logging"),
            "network-okhttp" to setOf("sdk-core", "logging"),
            "remote-config" to setOf("sdk-core", "logging"),
            "remote-config-firebase" to setOf("sdk-core", "logging", "remote-config"),
            "feature-flags" to setOf("sdk-core", "logging", "remote-config"),
            "notifications" to setOf("sdk-core", "logging", "deep-links", "permissions"),
            "notifications-firebase" to setOf("sdk-core", "logging", "notifications"),
            "media-picker" to setOf("sdk-core", "logging"),
            "analytics" to setOf("sdk-core", "logging"),
            "analytics-firebase" to setOf("sdk-core", "logging", "analytics"),
            "deep-links" to setOf("sdk-core", "logging"),
            "background-work" to setOf("sdk-core", "logging"),
            "app-versioning" to setOf("sdk-core", "logging"),
        )
        val allowedExternalGroups = mapOf(
            "sdk-core" to commonExternalGroups,
            "logging" to commonExternalGroups,
            "auth-core" to commonExternalGroups,
            "auth-firebase" to commonExternalGroups + setOf("com.google.firebase"),
            "auth-google" to commonExternalGroups + setOf(
                "androidx.credentials",
                "com.google.android.libraries.identity.googleid",
            ),
            "auth-firebase-google" to commonExternalGroups + setOf("com.google.firebase"),
            "permissions" to commonExternalGroups + setOf("androidx.core"),
            "secure-storage" to commonExternalGroups + setOf(
                "androidx.datastore",
                "androidx.datastore.preferences",
            ),
            "network-okhttp" to commonExternalGroups + setOf("com.squareup.okhttp3"),
            "remote-config" to commonExternalGroups,
            "remote-config-firebase" to commonExternalGroups + setOf("com.google.firebase"),
            "feature-flags" to commonExternalGroups,
            "notifications" to commonExternalGroups,
            "notifications-firebase" to commonExternalGroups + setOf("com.google.firebase"),
            "media-picker" to commonExternalGroups + setOf("androidx.activity"),
            "analytics" to commonExternalGroups,
            "analytics-firebase" to commonExternalGroups + setOf("com.google.firebase"),
            "deep-links" to commonExternalGroups,
            "background-work" to commonExternalGroups + setOf("androidx.work"),
            "app-versioning" to commonExternalGroups,
        )
        val failures = mutableListOf<String>()

        sdkModuleNames.forEach { moduleName ->
            val sdkProject = project(":$moduleName")
            val allowedProjects = allowedProjectDependencies[moduleName]
                ?: error("Missing project dependency allowlist for :$moduleName.")
            val allowedGroups = allowedExternalGroups[moduleName]
                ?: error("Missing external dependency allowlist for :$moduleName.")
            val dependencies = sdkProject.configurations
                .matching { it.name in dependencyConfigurationNames }
                .flatMap { it.dependencies }

            dependencies.filterIsInstance<ProjectDependency>().forEach { dependency ->
                val dependencyModuleName = dependency.path.removePrefix(":")
                if (dependencyModuleName !in allowedProjects) {
                    failures += ":$moduleName must not depend on project ${dependency.path}."
                }
            }

            dependencies
                .filterNot { it is ProjectDependency }
                .forEach { dependency ->
                    val dependencyGroup = dependency.group.orEmpty()
                    if (dependencyGroup.isBlank()) {
                        failures += ":$moduleName has dependency ${dependency.name} without a group."
                        return@forEach
                    }

                    if (dependencyGroup !in allowedGroups) {
                        failures += ":$moduleName must not depend on ${dependency.group}:${dependency.name}."
                    }
                }
        }

        if (failures.isNotEmpty()) {
            throw GradleException(
                buildString {
                    appendLine("SDK dependency allowlist check failed.")
                    failures.forEach { appendLine("- $it") }
                    appendLine("Keep each SDK module limited to the project modules and provider libraries it owns.")
                }
            )
        }
    }
}

tasks.register("checkConsumerShrinkerRules") {
    group = "verification"
    description = "Checks SDK consumer shrinker rules stay scoped and production-safe."

    doLast {
        val consumerRuleDeclaration = Regex("""consumerProguardFiles\s*\(([^)]*)\)""")
        val dangerousRulePatterns = listOf(
            Regex("""^-dontshrink\b"""),
            Regex("""^-dontoptimize\b"""),
            Regex("""^-dontobfuscate\b"""),
            Regex("""^-keep\s+class\s+\*\*"""),
            Regex("""^-keep\s+class\s+\*\s*\{\s*\*\s*;\s*}"""),
            Regex("""^-keep\s+class\s+androidx\.\*\*"""),
            Regex("""^-keep\s+class\s+kotlin\.\*\*"""),
            Regex("""^-keep\s+class\s+kotlinx\.\*\*"""),
            Regex("""^-keep\s+class\s+com\.google\.firebase\.\*\*"""),
            Regex("""^-keep\s+class\s+okhttp3\.\*\*"""),
        )
        val failures = mutableListOf<String>()

        sdkModuleNames.forEach { moduleName ->
            val moduleDirectory = project(":$moduleName").projectDir
            val expectedRuleFile = moduleDirectory.resolve("consumer-rules.pro")
            if (!expectedRuleFile.isFile) {
                failures += ":$moduleName is missing consumer-rules.pro."
            }

            val buildFile = moduleDirectory.resolve("build.gradle.kts")
            val buildFileText = buildFile.readText()
            val declaredRuleFiles = consumerRuleDeclaration
                .findAll(buildFileText)
                .flatMap { match ->
                    Regex(""""([^"]+\.pro)"""")
                        .findAll(match.groupValues[1])
                        .map { it.groupValues[1] }
                }
                .toList()

            declaredRuleFiles.forEach { ruleFilePath ->
                val ruleFile = moduleDirectory.resolve(ruleFilePath)
                if (!ruleFile.isFile) {
                    failures += ":$moduleName declares missing consumer shrinker rule file $ruleFilePath."
                    return@forEach
                }
            }

            val ruleFiles = moduleDirectory
                .walkTopDown()
                .filter { it.isFile && it.extension == "pro" }
                .filterNot { it.toPath().any { pathPart -> pathPart.toString() == "build" } }
                .toList()

            ruleFiles.forEach { ruleFile ->
                ruleFile.readLines().forEachIndexed { index, rawLine ->
                    val line = rawLine.trim()
                    if (line.isBlank() || line.startsWith("#")) {
                        return@forEachIndexed
                    }

                    dangerousRulePatterns.forEach { pattern ->
                        if (pattern.containsMatchIn(line)) {
                            failures += ":$moduleName has broad consumer shrinker rule ${ruleFile.relativeTo(moduleDirectory)}:${index + 1}: $line"
                        }
                    }
                }
            }
        }

        if (failures.isNotEmpty()) {
            throw GradleException(
                buildString {
                    appendLine("SDK consumer shrinker rule check failed.")
                    failures.forEach { appendLine("- $it") }
                    appendLine("Keep SDK consumer rules minimal and module-specific. Prefer bundled dependency rules when available.")
                }
            )
        }
    }
}

tasks.register("checkApiCompatibility") {
    group = "verification"
    description = "Checks generated release publication metadata and SDK binary API signatures."
    dependsOn(
        sdkModuleNames.flatMap { moduleName ->
            listOf(
                ":$moduleName:generatePomFileForMavenPublication",
                ":$moduleName:generateMetadataFileForMavenPublication",
            )
        },
    )

    doLast {
        sdkModuleNames.forEach { moduleName ->
            val sdkProject = project(":$moduleName")
            val publicationDirectory = sdkProject.layout.buildDirectory
                .dir("publications/maven")
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

val apiBaselineDirectory = layout.projectDirectory.dir("api")

fun releaseClassesJar(moduleName: String) = project(":$moduleName").layout.buildDirectory
    .file("intermediates/compile_library_classes_jar/release/bundleLibCompileToJarRelease/classes.jar")
    .get()
    .asFile

fun publicClassNames(jarFile: File): List<String> {
    return JarFile(jarFile).use { jar ->
        jar.entries()
            .asSequence()
            .map { it.name }
            .filter { it.endsWith(".class") }
            .filterNot { it.startsWith("META-INF/") }
            .filterNot { it.endsWith("/R.class") || it.contains("/R$") }
            .filterNot { it.endsWith("/BuildConfig.class") }
            .filterNot { it.contains("/internal/") }
            .filterNot { it.endsWith("\$WhenMappings.class") }
            .filterNot { it.substringAfterLast('/').matches(Regex(""".+\$.*\$\d+\.class""")) }
            .map { it.removeSuffix(".class").replace('/', '.') }
            .sorted()
            .toList()
    }
}

fun sdkApiSignature(
    moduleName: String,
    jarFile: File
): String {
    val signature = StringBuilder()
    signature.appendLine("# Module: $moduleName")
    signature.appendLine("# Generated by ./gradlew updateSdkApiBaseline")
    signature.appendLine()

    publicClassNames(jarFile).forEach { className ->
        val process = ProcessBuilder(
            "javap",
            "-classpath",
            jarFile.absolutePath,
            "-protected",
            "-s",
            className,
        )
            .redirectErrorStream(true)
            .start()
        val output = process.inputStream.bufferedReader().readText()
        val exitCode = process.waitFor()
        check(exitCode == 0) {
            "Unable to inspect API signature for $className in ${jarFile.path}:\n$output"
        }
        val normalizedOutput = output
            .lines()
            .filterNot { it.startsWith("Compiled from ") }
            .filterNot { it.contains(" access$") }
            .joinToString(separator = "\n")
            .trim()

        if (normalizedOutput.isNotBlank()) {
            signature.appendLine(normalizedOutput)
            signature.appendLine()
        }
    }

    return signature.toString()
}

tasks.register("updateSdkApiBaseline") {
    group = "verification"
    description = "Updates committed SDK binary API signature baselines."
    dependsOn(sdkModuleNames.map { moduleName -> ":$moduleName:bundleLibCompileToJarRelease" })

    doLast {
        val baselineDirectory = apiBaselineDirectory.asFile
        baselineDirectory.mkdirs()

        sdkModuleNames.forEach { moduleName ->
            val baselineFile = baselineDirectory.resolve("$moduleName.api")
            baselineFile.writeText(
                sdkApiSignature(
                    moduleName = moduleName,
                    jarFile = releaseClassesJar(moduleName),
                )
            )
        }
    }
}

tasks.register("checkSdkBinaryApi") {
    group = "verification"
    description = "Checks SDK release binary API signatures against committed baselines."
    dependsOn(sdkModuleNames.map { moduleName -> ":$moduleName:bundleLibCompileToJarRelease" })

    doLast {
        val currentDirectory = layout.buildDirectory
            .dir("reports/api/current")
            .get()
            .asFile
        currentDirectory.mkdirs()

        val failures = mutableListOf<String>()

        sdkModuleNames.forEach { moduleName ->
            val baselineFile = apiBaselineDirectory.file("$moduleName.api").asFile
            val currentSignature = sdkApiSignature(
                moduleName = moduleName,
                jarFile = releaseClassesJar(moduleName),
            )
            val currentFile = currentDirectory.resolve("$moduleName.api")
            currentFile.writeText(currentSignature)

            if (!baselineFile.isFile) {
                failures += ":$moduleName is missing API baseline ${baselineFile.path}."
                return@forEach
            }

            val baselineSignature = baselineFile.readText()
            if (baselineSignature != currentSignature) {
                failures += ":$moduleName API changed. Current signature written to ${currentFile.path}."
            }
        }

        if (failures.isNotEmpty()) {
            throw GradleException(
                buildString {
                    appendLine("SDK binary API check failed.")
                    failures.forEach { appendLine("- $it") }
                    appendLine("Review the API change, then run ./gradlew updateSdkApiBaseline if it is intentional.")
                }
            )
        }
    }
}

tasks.named("checkApiCompatibility") {
    dependsOn("checkSdkBinaryApi")
}

tasks.register("checkPublishingReadiness") {
    group = "verification"
    description = "Runs CI publishing, architecture, and API metadata checks for SDK modules."
    dependsOn(
        "checkPublishingGroup",
        "checkSdkArchitecture",
        "checkSdkAndroidMetadata",
        "checkSdkDependencyAllowlist",
        "checkConsumerShrinkerRules",
        "checkApiCompatibility",
    )
}
