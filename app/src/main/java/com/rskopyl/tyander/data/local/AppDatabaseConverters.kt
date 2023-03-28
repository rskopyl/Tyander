package com.rskopyl.tyander.data.local

import androidx.room.TypeConverter
import com.rskopyl.tyander.data.model.Message
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

object AppDatabaseConverters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String =
        date.toString()

    @TypeConverter
    fun toLocalDate(isoString: String): LocalDate =
        LocalDate.parse(isoString)

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime): String =
        dateTime.toString()

    @TypeConverter
    fun toLocalDateTime(isoString: String): LocalDateTime =
        LocalDateTime.parse(isoString)

    @TypeConverter
    fun fromMessageAuthor(author: Message.Author): String =
        author.toString()

    @TypeConverter
    fun toMessageAuthor(enumString: String): Message.Author =
        Message.Author.valueOf(enumString)
}