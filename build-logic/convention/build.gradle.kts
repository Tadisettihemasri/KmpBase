plugins {
    `kotlin-dsl`
}

group = "com.a4b.kmpbase.buildlogic"

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.compose.compiler.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "kmpbase.kmp.library"
            implementationClass = "com.a4b.kmpbase.buildlogic.KmpLibraryConventionPlugin"
        }
        register("kmpCompose") {
            id = "kmpbase.kmp.compose"
            implementationClass = "com.a4b.kmpbase.buildlogic.KmpComposeConventionPlugin"
        }
        register("kmpFeature") {
            id = "kmpbase.kmp.feature"
            implementationClass = "com.a4b.kmpbase.buildlogic.KmpFeatureConventionPlugin"
        }
    }
}
