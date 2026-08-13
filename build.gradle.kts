buildscript {
    dependencies {
        classpath(libs.gradle)
        classpath(libs.google.services)
        classpath(libs.firebase.crashlytics.gradle)
    }
}

plugins {
    id("com.android.application") version "9.3.1" apply false
    kotlin("plugin.serialization") version "2.4.10" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.10" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
    id("com.android.compose.screenshot") version "0.0.1-alpha16" apply false
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            if (project.findProperty("myapp.enableComposeCompilerReports") == "true") {
                val metricsPath = project.layout.buildDirectory.dir("compose_metrics").get().asFile.absolutePath
                freeCompilerArgs.addAll(
                    listOf(
                        "-P",
                        "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$metricsPath",
                        "-P",
                        "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$metricsPath"
                    )
                )
            }
        }
    }
}