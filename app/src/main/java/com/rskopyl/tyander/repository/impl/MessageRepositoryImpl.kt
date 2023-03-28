package com.rskopyl.tyander.repository.impl

import com.rskopyl.tyander.data.local.dao.MessageDao
import com.rskopyl.tyander.data.model.Message
import com.rskopyl.tyander.data.remote.MessageApi
import com.rskopyl.tyander.di.ApplicationScope
import com.rskopyl.tyander.repository.MessageRepository
import com.rskopyl.tyander.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao,
    private val messageApi: MessageApi,
    @ApplicationScope
    private val applicationScope: CoroutineScope
) : MessageRepository {

    override fun getByMatchId(
        matchId: Long
    ): Flow<Resource<List<Message>>> = channelFlow {
        send(Resource.Loading())
        messageDao.getByMatchId(matchId).collectLatest { messages ->
            send(Resource.Success(messages))
        }
    }

    override fun insert(message: Message) {
        applicationScope.launch(Dispatchers.IO) {
            messageDao.insert(message)
            val responseMessage = try {
                Message(
                    matchId = message.matchId,
                    text = messageApi.getResponseText(message.text),
                    author = Message.Author.CHARACTER
                )
            } catch (e: UnknownHostException) {
                return@launch
            }
            messageDao.insert(responseMessage)
        }
    }

    override fun deleteAll() {
        applicationScope.launch(Dispatchers.IO) {
            messageDao.deleteAll()
        }
    }

    override fun deleteByMatchId(matchId: Long) {
        applicationScope.launch(Dispatchers.IO) {
            messageDao.deleteByMatchId(matchId)
        }
    }
}