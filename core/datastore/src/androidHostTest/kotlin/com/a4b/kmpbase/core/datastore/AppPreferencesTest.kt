package com.a4b.kmpbase.core.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class AppPreferencesTest {

    private val name = stringPreferencesKey("name")
    private val onboarded = booleanPreferencesKey("onboarded")

    private fun newPreferences(): AppPreferences {
        val file = File.createTempFile("prefs", ".preferences_pb").apply { delete() }
        return AppPreferences(createPreferencesDataStore(file.absolutePath))
    }

    @Test
    fun returnsDefaultUntilSet() = runTest {
        val prefs = newPreferences()
        assertEquals(false, prefs.get(onboarded, default = false))
        prefs.set(onboarded, true)
        assertEquals(true, prefs.get(onboarded, default = false))
    }

    @Test
    fun observeEmitsUpdatesAndRemoval() = runTest {
        val prefs = newPreferences()
        prefs.observe(name, default = "").test {
            assertEquals("", awaitItem())
            prefs.set(name, "Asha")
            assertEquals("Asha", awaitItem())
            prefs.remove(name)
            assertEquals("", awaitItem())
        }
    }
}
