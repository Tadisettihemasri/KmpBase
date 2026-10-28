plugins {
    alias(libs.plugins.kmpbase.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.coroutines.core)
            api(libs.kermit)
            implementation(libs.koin.core)
        }
    }
}
