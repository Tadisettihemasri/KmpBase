package com.a4b.kmpbase.di

import com.a4b.kmpbase.config.BuildKonfig
import com.a4b.kmpbase.core.common.config.AppConfig
import com.a4b.kmpbase.core.common.di.commonModule
import com.a4b.kmpbase.core.common.log.AppLogger
import com.a4b.kmpbase.core.database.di.databaseModule
import com.a4b.kmpbase.core.datastore.di.dataStoreModule
import com.a4b.kmpbase.core.network.di.networkModule
import org.koin.core.lazyModules
import org.koin.core.module.LazyModule
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.core.context.startKoin

fun appConfig(isDebug: Boolean) = AppConfig(
    baseUrl = BuildKonfig.BASE_URL,
    environment = BuildKonfig.ENVIRONMENT,
    isDebug = isDebug,
)

/**
 * Feature DI modules, e.g. `lazyModule { viewModelOf(::HomeViewModel) }`.
 * They are loaded on a background thread after startKoin returns, so they never block the first frame.
 */
private val featureModules: List<LazyModule> = emptyList()

/**
 * Registers definitions only. Every `single {}` is created on first `get()`, so nothing heavy
 * (HttpClient, Room, DataStore) is built during startup.
 *
 * @param platformModules loaded after the core modules so they can override defaults
 *        (e.g. Firebase-backed telemetry on Android).
 */
fun initKoin(
    config: AppConfig,
    platformModules: List<Module> = emptyList(),
    platformDeclaration: KoinAppDeclaration = {},
) {
    AppLogger.init(config.isDebug)
    startKoin {
        platformDeclaration()
        modules(listOf(commonModule(config), networkModule, databaseModule, dataStoreModule) + platformModules)
        lazyModules(featureModules)
    }
}
