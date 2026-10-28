package com.a4b.kmpbase.core.database.di

import com.a4b.kmpbase.core.common.log.AppLogger
import com.a4b.kmpbase.core.database.AppDatabase
import com.a4b.kmpbase.core.database.buildAppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

/** Provides the platform `RoomDatabase.Builder<AppDatabase>`. */
internal expect val platformDatabaseModule: Module

val databaseModule: Module = module {
    includes(platformDatabaseModule)
    single<AppDatabase> {
        AppLogger.lazyInit("AppDatabase")
        buildAppDatabase(get())
    }
    single { get<AppDatabase>().cacheDao() }
}
