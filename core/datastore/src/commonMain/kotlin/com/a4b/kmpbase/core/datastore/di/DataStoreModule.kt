package com.a4b.kmpbase.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.a4b.kmpbase.core.common.log.AppLogger
import com.a4b.kmpbase.core.datastore.AppPreferences
import com.a4b.kmpbase.core.datastore.createPreferencesDataStore
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val PreferencesPath = named("preferencesPath")

/** Provides the absolute preferences file path under [PreferencesPath]. */
internal expect val platformDataStoreModule: Module

val dataStoreModule: Module = module {
    includes(platformDataStoreModule)
    single<DataStore<Preferences>> {
        AppLogger.lazyInit("DataStore")
        createPreferencesDataStore(get(PreferencesPath))
    }
    single { AppPreferences(get()) }
}
