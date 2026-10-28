package com.a4b.kmpbase.core.common.config

/** Values that differ per environment/build type. Created once by the app module and shared through Koin. */
data class AppConfig(
    val baseUrl: String,
    val environment: String,
    val isDebug: Boolean,
)
