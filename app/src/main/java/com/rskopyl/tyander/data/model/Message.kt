package com.rskopyl.tyander.data.model

import androidx.room.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Entity(
    tableName = "message",
    foreignKeys = [
        ForeignKey(
            entity = Match::class,
            parentColumns = ["id"],
            childColumns = ["match_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("match_id")]
)
data class Message(
    @ColumnInfo(name = "match_id")
    val matchId: Long,
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "author")
    val author: Author,
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

    enum class Author { USER, CHARACTER }
}