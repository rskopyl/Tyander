package com.rskopyl.tyander.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rskopyl.tyander.data.local.dao.MatchDao
import com.rskopyl.tyander.data.local.dao.MessageDao
import com.rskopyl.tyander.data.model.Match
import com.rskopyl.tyander.data.model.Message

@Database(
    version = 1,
    entities = [Match::class, Message::class],
    exportSchema = true
)
@TypeConverters(AppDatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val matchDao: MatchDao
    abstract val messageDao: MessageDao
}