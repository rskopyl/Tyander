package com.rskopyl.tyander.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
    @SerialName("url")
    val url: String
)

@Serializable
data class ImageListDto(
    @SerialName("images")
    val images: List<ImageDto>
)