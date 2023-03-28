package com.rskopyl.tyander.data.preferences

import kotlinx.coroutines.flow.Flow

interface AppPreferencesManager {

    fun getAll(): Flow<AppPreferences>

    fun toggleNightMode()
}