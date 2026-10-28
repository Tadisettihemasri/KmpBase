package com.a4b.kmpbase.core.database.cache

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CacheDao {
    @Query("SELECT * FROM cache_entries WHERE `key` = :key")
    fun observe(key: String): Flow<CacheEntryEntity?>

    @Query("SELECT * FROM cache_entries WHERE `key` = :key")
    suspend fun get(key: String): CacheEntryEntity?

    @Upsert
    suspend fun upsert(entry: CacheEntryEntity)

    @Query("DELETE FROM cache_entries WHERE `key` = :key")
    suspend fun delete(key: String)

    @Query("DELETE FROM cache_entries WHERE updatedAtMillis < :olderThanMillis")
    suspend fun deleteOlderThan(olderThanMillis: Long)

    @Query("DELETE FROM cache_entries")
    suspend fun clear()
}
