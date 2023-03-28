package com.rskopyl.tyander.data.remote

import com.rskopyl.tyander.data.remote.dto.ImageDto
import com.rskopyl.tyander.data.remote.dto.ImageListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {

    @GET("search")
    suspend fun getRandom(
        @Query("many") many: Boolean
    ): ImageListDto
}

suspend fun ImageApi.getRandom(count: Int): ImageListDto {
    val images = mutableListOf<ImageDto>()
    while (images.size < count) {
        images += getRandom(many = true).images
    }
    return ImageListDto(images.take(count))
}