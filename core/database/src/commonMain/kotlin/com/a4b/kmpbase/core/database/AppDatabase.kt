package com.a4b.kmpbase.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.a4b.kmpbase.core.database.cache.CacheDao
import com.a4b.kmpbase.core.database.cache.CacheEntryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

/**
 * Single app database. Add entities here and bump [version]; write a Migration for every bump
 * (schemas are exported to core/database/schemas).
 */
@Database(entities = [CacheEntryEntity::class], version = 1, exportSchema = true)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cacheDao(): CacheDao

    companion object {
        const val NAME = "kmpbase.db"
    }
}

// Room's KSP processor generates the actual implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

/** Building does not open the DB; the connection is opened on the first query, on [Dispatchers.IO]. */
internal fun buildAppDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase = builder
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()
