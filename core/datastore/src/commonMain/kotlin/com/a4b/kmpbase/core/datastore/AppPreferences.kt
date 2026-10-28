package com.a4b.kmpbase.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Thin typed wrapper over Preferences DataStore. Declare keys next to the feature that owns them:
 * `val ONBOARDED = booleanPreferencesKey("onboarded")`.
 */
class AppPreferences(private val dataStore: DataStore<Preferences>) {

    fun <T> observe(key: Preferences.Key<T>, default: T): Flow<T> =
        dataStore.data.map { it[key] ?: default }.distinctUntilChanged()

    suspend fun <T> get(key: Preferences.Key<T>, default: T): T = observe(key, default).first()

    suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        dataStore.edit { it[key] = value }
    }

    suspend fun <T> remove(key: Preferences.Key<T>) {
        dataStore.edit { it.remove(key) }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
