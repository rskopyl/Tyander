package com.rskopyl.tyander.repository

import com.rskopyl.tyander.data.model.Match
import com.rskopyl.tyander.data.model.Message
import com.rskopyl.tyander.util.Resource
import kotlinx.coroutines.flow.Flow

interface MatchRepository {

    fun getAll(): Flow<Resource<List<Match>>>

    fun getAllWithLastMessage(): Flow<Resource<Map<Match, Message>>>

    fun getById(id: Long): Flow<Resource<Match>>

    fun insert(match: Match)

    fun update(match: Match)

    fun deleteAll()

    fun deleteById(id: Long)
}