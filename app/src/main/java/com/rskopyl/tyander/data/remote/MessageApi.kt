package com.rskopyl.tyander.data.remote

import com.rskopyl.tyander.BuildConfig
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface MessageApi {

    @POST("path?translate_to=en")
    @Headers(
        "X-RapidAPI-Host: waifu.p.rapidapi.com",
        "X-RapidAPI-Key: ${BuildConfig.RAPIDAPI_KEY}"
    )
    suspend fun getResponseText(
        @Query("message")
        messageText: String
    ): String
}