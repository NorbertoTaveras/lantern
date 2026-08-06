import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.GradleException
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.plugins.signing.SigningExtension
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
                disable += "UseTomlInstead"
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
                disable += "UseTomlInstead"
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
        "checkConsumerShrinkerRules",
        "checkApiCompatibility",
    )
}
