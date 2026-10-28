plugins {
    alias(libs.plugins.kmpbase.kmp.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            api(libs.moko.permissions)
            api(libs.moko.permissions.compose)
            // Only notifications by default. Camera/location/gallery artifacts link extra platform APIs
            // (and need Info.plist usage strings on iOS) — add them in the feature that needs them.
            api(libs.moko.permissions.notifications)
        }
    }
}
