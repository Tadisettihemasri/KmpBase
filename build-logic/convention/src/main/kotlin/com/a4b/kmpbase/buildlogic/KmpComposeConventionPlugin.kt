package com.a4b.kmpbase.buildlogic

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Shared module with Compose Multiplatform UI.
 * Compose reports: ./gradlew assembleRelease -PcomposeCompilerReports=true
 */
class KmpComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(KmpLibraryConventionPlugin::class.java)
        pluginManager.apply(libs.pluginId("composeMultiplatform"))
        pluginManager.apply(libs.pluginId("composeCompiler"))

        extensions.configure<ComposeCompilerGradlePluginExtension> {
            stabilityConfigurationFiles.add(rootProject.layout.projectDirectory.file("compose_stability.conf"))
            if (findProperty("composeCompilerReports") == "true") {
                metricsDestination.set(layout.buildDirectory.dir("compose_metrics"))
                reportsDestination.set(layout.buildDirectory.dir("compose_reports"))
            }
        }

        extensions.configure<KotlinMultiplatformExtension> {
            (this as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryTarget> {
                // Required for Compose Multiplatform resources on Android.
                androidResources.enable = true
            }

            sourceSets.commonMain.dependencies {
                implementation(libs.lib("compose-runtime"))
                implementation(libs.lib("compose-foundation"))
                implementation(libs.lib("compose-ui"))
                implementation(libs.lib("compose-material3"))
                implementation(libs.lib("compose-components-resources"))
                implementation(libs.lib("compose-ui-tooling-preview"))
                implementation(libs.lib("androidx-lifecycle-viewmodel-compose"))
                implementation(libs.lib("androidx-lifecycle-runtime-compose"))
            }
        }
    }
}
