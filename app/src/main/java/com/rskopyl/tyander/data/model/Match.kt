package com.rskopyl.tyander.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Entity(tableName = "match")
data class Match(
    @Embedded(prefix = "character_")
    val character: Character,
    @ColumnInfo(name = "is_new")
    val isNew: Boolean,
    @ColumnInfo(name = "date_time")
    val dateTime: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = EMPTY_ID
) {
    companion object {

        const val EMPTY_ID = 0L
    }
}