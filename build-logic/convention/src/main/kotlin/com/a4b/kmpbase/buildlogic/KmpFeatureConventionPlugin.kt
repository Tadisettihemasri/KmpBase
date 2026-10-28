package com.a4b.kmpbase.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Feature module: Compose UI + Koin ViewModels + type-safe navigation routes.
 * Data modules (core:network, core:database, core:datastore) are NOT added here on purpose:
 * a feature declares only the ones it actually uses.
 */
class KmpFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(KmpComposeConventionPlugin::class.java)
        pluginManager.apply(libs.pluginId("kotlinSerialization"))

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain.dependencies {
                implementation(project(":core:common"))
                implementation(project(":core:ui"))
                implementation(project(":core:designsystem"))
                implementation(libs.lib("koin-core"))
                implementation(libs.lib("koin-compose"))
                implementation(libs.lib("koin-compose-viewmodel"))
                implementation(libs.lib("navigation-compose"))
                implementation(libs.lib("kotlinx-serialization-json"))
            }
            sourceSets.commonTest.dependencies {
                implementation(libs.lib("turbine"))
                implementation(libs.lib("koin-test"))
            }
        }
    }
}
