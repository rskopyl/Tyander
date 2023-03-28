package com.rskopyl.tyander.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rskopyl.tyander.data.preferences.AppPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    appPreferencesManager: AppPreferencesManager
) : ViewModel() {

    val isNightModeEnabled: StateFlow<Boolean?> = appPreferencesManager
        .getAll()
        .mapLatest { it.isNightModeEnabled }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}