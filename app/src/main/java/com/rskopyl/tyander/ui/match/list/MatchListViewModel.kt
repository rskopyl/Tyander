package com.rskopyl.tyander.ui.match.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rskopyl.tyander.data.model.Match
import com.rskopyl.tyander.repository.MatchRepository
import com.rskopyl.tyander.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MatchListViewModel @Inject constructor(
    private val matchRepository: MatchRepository
) : ViewModel() {

    val matches: StateFlow<Resource<List<Match>>> = matchRepository
        .getAll()
        .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading())

    fun deleteAllMatches() {
        matchRepository.deleteAll()
    }
}