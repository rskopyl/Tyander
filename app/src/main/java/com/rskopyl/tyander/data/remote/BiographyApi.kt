package com.rskopyl.tyander.data.remote

import com.rskopyl.tyander.data.remote.dto.BiographyListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface BiographyApi {

    @GET("persons?_gender=female")
    suspend fun getRandom(
        @Query("_quantity")
        count: Int,
        @Query("_birthday_start")
        birthdayStart: String = "1992-01-01",
        @Query("_birthday_end")
        birthdayEnd: String = "2002-01-01"
    ): BiographyListDto
}