package com.rskopyl.tyander.repository

import com.rskopyl.tyander.data.model.Message
import com.rskopyl.tyander.util.Resource
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    fun getByMatchId(matchId: Long): Flow<Resource<List<Message>>>

    fun insert(message: Message)

    fun deleteAll()

    fun deleteByMatchId(matchId: Long)
}