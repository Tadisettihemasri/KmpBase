import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    alias(libs.plugins.kmpbase.kmp.compose)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            binaryOption("bundleId", "com.a4b.kmpbase.ComposeApp")
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            implementation(projects.core.network)
            implementation(projects.core.database)
            implementation(projects.core.datastore)
            implementation(projects.core.designsystem)
            implementation(projects.core.ui)
            // Feature modules: implementation(projects.features.home)

            implementation(libs.koin.core)
            implementation(libs.koin.core.coroutines)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.navigation.compose)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

// Environment values. Pick a flavor with -Pbuildkonfig.flavor=prod (default: dev, see gradle.properties).
buildkonfig {
    packageName = "com.a4b.kmpbase.config"
    defaultConfigs {
        buildConfigField(STRING, "ENVIRONMENT", "dev")
        buildConfigField(STRING, "BASE_URL", "https://api-dev.example.com/")
    }
    defaultConfigs("prod") {
        buildConfigField(STRING, "ENVIRONMENT", "prod")
        buildConfigField(STRING, "BASE_URL", "https://api.example.com/")
    }
}
