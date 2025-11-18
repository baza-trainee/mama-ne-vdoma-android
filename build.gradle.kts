buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.11.0")
        classpath("com.google.gms:google-services:4.4.4")
        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.3")
    }
}

plugins {
    id("com.android.application") version "8.11.0" apply false
    id("org.jetbrains.kotlin.android") version "2.2.20" apply false
    kotlin("plugin.serialization") version "2.2.20"
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.20" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
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