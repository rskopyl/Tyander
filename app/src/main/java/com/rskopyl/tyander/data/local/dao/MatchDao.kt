package com.rskopyl.tyander.data.local.dao

import androidx.room.*
import com.rskopyl.tyander.data.model.Match
import com.rskopyl.tyander.data.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {

    @Query("SELECT * FROM match")
    fun getAll(): Flow<List<Match>>

    @Query("SELECT * FROM match " +
           "JOIN ( " +
           "    SELECT * FROM message " +
           "    WHERE (match_id, date_time) IN ( " +
           "        SELECT match_id, MAX(date_time) " +
           "        FROM message " +
           "        GROUP BY match_id " +
           "    )" +
           ") AS last_message ON match.id = last_message.match_id " +
           "ORDER BY last_message.date_time DESC")
    fun getAllWithLastMessage(): Flow<Map<Match, Message>>

    @Query("SELECT * FROM match WHERE id = :id")
    fun getById(id: Long): Flow<Match?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(match: Match)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(match: Match)

    @Query("DELETE FROM match")
    suspend fun deleteAll()

    @Query("DELETE FROM match WHERE id = :id")
    suspend fun deleteById(id: Long)
}