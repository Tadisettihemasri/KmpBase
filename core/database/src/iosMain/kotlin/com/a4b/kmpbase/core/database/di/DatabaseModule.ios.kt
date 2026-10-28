package com.a4b.kmpbase.core.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.a4b.kmpbase.core.database.AppDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

internal actual val platformDatabaseModule: Module = module {
    factory<RoomDatabase.Builder<AppDatabase>> {
        Room.databaseBuilder<AppDatabase>(name = "${documentDirectory()}/${AppDatabase.NAME}")
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String = requireNotNull(
    NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )?.path,
) { "Documents directory unavailable" }
