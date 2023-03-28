package com.rskopyl.tyander.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import kotlinx.datetime.*

data class Character(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "phone")
    val phone: String,
    @ColumnInfo(name = "birthday")
    val birthday: LocalDate,
    @Embedded(prefix = "address_")
    val address: Address,
    @ColumnInfo(name = "image_url")
    val imageUrl: String
) {
    val age: Int
        get() = birthday.yearsUntil(
            Clock.System.todayIn(TimeZone.UTC)
        )

    data class Address(
        @ColumnInfo(name = "city")
        val city: String,
        @ColumnInfo(name = "country")
        val country: String
    )
}