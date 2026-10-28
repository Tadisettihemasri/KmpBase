plugins {
    alias(libs.plugins.kmpbase.kmp.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            api(libs.coil.compose)
            implementation(libs.coil.network.ktor3)
            implementation(libs.coil.svg)
            api(libs.paging.compose)
        }
        androidMain.dependencies {
            implementation(libs.coil.gif)
        }
    }
}
