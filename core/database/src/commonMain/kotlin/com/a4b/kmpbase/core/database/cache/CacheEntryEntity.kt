package com.a4b.kmpbase.core.database.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Generic key → JSON cache for offline-first responses (e.g. the last fetched home feed). */
@Entity(tableName = "cache_entries")
data class CacheEntryEntity(
    @PrimaryKey val key: String,
    val json: String,
    val updatedAtMillis: Long,
)
