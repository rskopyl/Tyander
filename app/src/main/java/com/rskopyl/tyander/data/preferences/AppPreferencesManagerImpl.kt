package com.rskopyl.tyander.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.rskopyl.tyander.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppPreferencesManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @ApplicationScope
    private val applicationScope: CoroutineScope
) : AppPreferencesManager {

    override fun getAll(): Flow<AppPreferences> =
        dataStore.data.map(::mapPreferences)

    override fun toggleNightMode() {
        applicationScope.launch(Dispatchers.IO) {
            val isDarkModeEnabled = getAll().first().isNightModeEnabled
            dataStore.edit { preferences ->
                preferences[Keys.NIGHT_MODE] = !isDarkModeEnabled
            }
        }
    }

    private fun mapPreferences(preferences: Preferences): AppPreferences {
        return AppPreferences(
            isNightModeEnabled = preferences[Keys.NIGHT_MODE] ?: false
        )
    }

    private object Keys {

        val NIGHT_MODE = booleanPreferencesKey("night_mode")
    }
}