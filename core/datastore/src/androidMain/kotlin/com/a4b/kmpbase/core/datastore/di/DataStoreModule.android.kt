package com.a4b.kmpbase.core.datastore.di

import com.a4b.kmpbase.core.datastore.PREFERENCES_FILE_NAME
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformDataStoreModule: Module = module {
    factory(PreferencesPath) { androidContext().filesDir.resolve(PREFERENCES_FILE_NAME).absolutePath }
}
