package com.a4b.kmpbase.buildlogic

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Base for every shared module: Android (KMP library) + iOS (arm64 device, arm64 simulator).
 * Namespace is derived from the Gradle path, so module build files stay tiny.
 */
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("kotlinMultiplatform"))
        pluginManager.apply(libs.pluginId("androidKmpLibrary"))

        extensions.configure<KotlinMultiplatformExtension> {
            jvmToolchain(17)

            (this as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryTarget> {
                namespace = defaultNamespace()
                compileSdk = libs.version("android-compileSdk").toInt()
                minSdk = libs.version("android-minSdk").toInt()
                compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
                withHostTest {}
            }

            iosArm64()
            iosSimulatorArm64()

            compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }

            sourceSets.commonTest.dependencies {
                implementation(libs.lib("kotlin-test"))
                implementation(libs.lib("kotlinx-coroutines-test"))
            }
        }
    }
}
