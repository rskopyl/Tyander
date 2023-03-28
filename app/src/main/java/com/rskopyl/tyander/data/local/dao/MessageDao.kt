package com.rskopyl.tyander.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rskopyl.tyander.data.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("SELECT * FROM message WHERE match_id = :matchId " +
           "ORDER BY date_time DESC")
    fun getByMatchId(matchId: Long): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(message: Message)

    @Query("DELETE FROM message")
    suspend fun deleteAll()

    @Query("DELETE FROM message WHERE match_id = :matchId")
    suspend fun deleteByMatchId(matchId: Long)
}