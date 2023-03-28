package com.rskopyl.tyander.ui.match.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rskopyl.tyander.data.model.Match
import com.rskopyl.tyander.repository.MatchRepository
import com.rskopyl.tyander.util.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MatchDetailsViewModel @AssistedInject constructor(
    @Assisted val matchId: Long,
    private val matchRepository: MatchRepository
) : ViewModel() {

    val match: StateFlow<Resource<Match>> = matchRepository
        .getById(matchId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading())

    init {
        viewModelScope.launch {
            val loadedMatch = match
                .filterIsInstance<Resource.Success<Match>>()
                .first().data
            matchRepository.update(loadedMatch.copy(isNew = false))
        }
    }

    fun deleteMatch() {
        matchRepository.deleteById(matchId)
    }

    @AssistedFactory
    interface DaggerFactory {

        fun create(matchId: Long): MatchDetailsViewModel
    }
}