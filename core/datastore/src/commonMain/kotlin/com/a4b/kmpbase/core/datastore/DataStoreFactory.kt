package com.a4b.kmpbase.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

internal const val PREFERENCES_FILE_NAME = "kmpbase.preferences_pb"

/** One DataStore instance per file for the whole process — DataStore throws if two instances share a file. */
fun createPreferencesDataStore(absolutePath: String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(produceFile = { absolutePath.toPath() })
