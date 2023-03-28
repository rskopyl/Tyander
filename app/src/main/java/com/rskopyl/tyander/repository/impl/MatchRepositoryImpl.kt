package com.rskopyl.tyander.repository.impl

import com.rskopyl.tyander.data.local.dao.MatchDao
import com.rskopyl.tyander.data.model.Match
import com.rskopyl.tyander.data.model.Message
import com.rskopyl.tyander.di.ApplicationScope
import com.rskopyl.tyander.repository.MatchRepository
import com.rskopyl.tyander.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MatchRepositoryImpl @Inject constructor(
    private val matchDao: MatchDao,
    @ApplicationScope
    private val applicationScope: CoroutineScope
) : MatchRepository {

    override fun getAll(): Flow<Resource<List<Match>>> = channelFlow {
        send(Resource.Loading())
        matchDao.getAll().collectLatest { matches ->
            send(Resource.Success(matches))
        }
    }

    override fun getAllWithLastMessage():
            Flow<Resource<Map<Match, Message>>> = channelFlow {
        send(Resource.Loading())
        matchDao.getAllWithLastMessage().collectLatest { matchToLastMessage ->
            send(Resource.Success(matchToLastMessage))
        }
    }

    override fun getById(id: Long): Flow<Resource<Match>> = channelFlow {
        send(Resource.Loading())
        matchDao.getById(id).collectLatest { match ->
            send(
                if (match != null) Resource.Success(match)
                else Resource.Error()
            )
        }
    }

    override fun insert(match: Match) {
        applicationScope.launch(Dispatchers.IO) {
            matchDao.insert(match)
        }
    }

    override fun update(match: Match) {
        applicationScope.launch(Dispatchers.IO) {
            matchDao.update(match)
        }
    }

    override fun deleteAll() {
        applicationScope.launch(Dispatchers.IO) {
            matchDao.deleteAll()
        }
    }

    override fun deleteById(id: Long) {
        applicationScope.launch(Dispatchers.IO) {
            matchDao.deleteById(id)
        }
    }
}