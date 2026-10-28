package com.a4b.kmpbase.core.datastore.di

import com.a4b.kmpbase.core.datastore.PREFERENCES_FILE_NAME
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
internal actual val platformDataStoreModule: Module = module {
    factory(PreferencesPath) {
        val documents = requireNotNull(
            NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )?.path,
        ) { "Documents directory unavailable" }
        "$documents/$PREFERENCES_FILE_NAME"
    }
}
