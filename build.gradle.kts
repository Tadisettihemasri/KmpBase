buildscript {
    dependencies {
        // AGP 8.13's bundled R8 can't fully read Kotlin 2.3 metadata (D8 "Should never be called" warnings).
        // Pin a newer R8 until AGP is bumped. https://developer.android.com/build/kotlin-support
        classpath(libs.r8)
    }
}

plugins {
    // Declared once here so every module shares one classloader for these plugins.
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidKmpLibrary) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.buildkonfig) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.detekt)
}

dependencies {
    detektPlugins(libs.detekt.compose.rules)
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(files("detekt.yml"))
    basePath = projectDir.absolutePath
    source.setFrom(
        fileTree(".") {
            include("**/src/**/*.kt")
            exclude("**/build/**", "**/generated/**", "build-logic/**")
        },
    )
}
