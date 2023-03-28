package com.rskopyl.tyander.ui.chat.messaging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rskopyl.tyander.data.model.Match
import com.rskopyl.tyander.data.model.Message
import com.rskopyl.tyander.repository.MatchRepository
import com.rskopyl.tyander.repository.MessageRepository
import com.rskopyl.tyander.util.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ChatMessagingViewModel @AssistedInject constructor(
    @Assisted private val matchId: Long,
    matchRepository: MatchRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    val match: StateFlow<Resource<Match>> = matchRepository
        .getById(matchId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading())

    val messages: StateFlow<Resource<List<Message>>> = messageRepository
        .getByMatchId(matchId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading())

    fun sendMessage(text: String) {
        val message = Message(
            matchId = matchId,
            text = text,
            author = Message.Author.USER
        )
        messageRepository.insert(message)
    }

    fun deleteMessages() {
        messageRepository.deleteByMatchId(matchId)
    }

    @AssistedFactory
    interface DaggerFactory {

        fun create(matchId: Long): ChatMessagingViewModel
    }
}