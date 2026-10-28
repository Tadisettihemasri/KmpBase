package com.a4b.kmpbase.core.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.a4b.kmpbase.core.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformDatabaseModule: Module = module {
    factory<RoomDatabase.Builder<AppDatabase>> {
        val context = androidContext()
        Room.databaseBuilder<AppDatabase>(
            context = context,
            name = context.getDatabasePath(AppDatabase.NAME).absolutePath,
        )
    }
}
