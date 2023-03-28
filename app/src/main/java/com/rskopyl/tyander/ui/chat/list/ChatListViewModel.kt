package com.rskopyl.tyander.ui.chat.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rskopyl.tyander.data.model.Match
import com.rskopyl.tyander.data.model.Message
import com.rskopyl.tyander.repository.MatchRepository
import com.rskopyl.tyander.repository.MessageRepository
import com.rskopyl.tyander.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    matchRepository: MatchRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    val matchToLastMessage: StateFlow<Resource<Map<Match, Message>>> = matchRepository
        .getAllWithLastMessage()
        .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading())

    fun deleteAllChats() {
        messageRepository.deleteAll()
    }
}