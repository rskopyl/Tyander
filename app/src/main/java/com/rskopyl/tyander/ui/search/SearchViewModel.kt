package com.rskopyl.tyander.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rskopyl.tyander.data.model.Character
import com.rskopyl.tyander.data.model.Match
import com.rskopyl.tyander.data.preferences.AppPreferencesManager
import com.rskopyl.tyander.repository.CharacterRepository
import com.rskopyl.tyander.repository.MatchRepository
import com.rskopyl.tyander.util.Resource
import com.rskopyl.tyander.util.completeWhenLoaded
import com.rskopyl.tyander.util.dataOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val matchRepository: MatchRepository,
    private val appPreferencesManager: AppPreferencesManager
) : ViewModel() {

    private val mainCharacters =
        MutableStateFlow<Resource<List<Character>>>(Resource.Loading())

    private val cachedCharacters =
        MutableStateFlow<Resource<List<Character>>>(Resource.Loading())

    val characters: StateFlow<Resource<List<Character>>>
        get() = mainCharacters.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            characterRepository
                .getRandom(CHARACTER_COUNT)
                .completeWhenLoaded()
                .collectLatest { characters ->
                    cachedCharacters.value = characters
                }
            reloadCharactersFromCache()
        }
    }

    private fun reloadCharactersFromCache() {
        viewModelScope.launch(Dispatchers.IO) {
            cachedCharacters.completeWhenLoaded().collectLatest { cache ->
                mainCharacters.update { main ->
                    if (main is Resource.Success &&
                        cache is Resource.Success
                    ) {
                        val lastMain = main.data.takeLast(1)
                        Resource.Success(lastMain + cache.data)
                    } else {
                        cache
                    }
                }
            }
            characterRepository
                .getRandom(CHARACTER_COUNT - 1)
                .completeWhenLoaded()
                .collectLatest { characters ->
                    cachedCharacters.value = characters
                }
        }
    }

    fun saveCharacter(index: Int) {
        val match = Match(
            character = mainCharacters.value
                .dataOrNull()
                ?.get(index) ?: return,
            isNew = true
        )
        matchRepository.insert(match)
        if (index >= CHARACTER_COUNT - 2) {
            reloadCharactersFromCache()
        }
    }

    fun skipCharacter(index: Int) {
        if (index >= CHARACTER_COUNT - 2) {
            reloadCharactersFromCache()
        }
    }

    fun toggleNightMode() {
        appPreferencesManager.toggleNightMode()
    }

    private companion object {

        const val CHARACTER_COUNT = 30
    }
}