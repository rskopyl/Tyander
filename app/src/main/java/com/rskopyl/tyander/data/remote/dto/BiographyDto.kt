package com.rskopyl.tyander.data.remote.dto

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BiographyDto(
    @SerialName("firstname")
    val name: String,
    @SerialName("email")
    val email: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("birthday")
    val birthday: LocalDate,
    @SerialName("address")
    val address: AddressDto
) {
    @Serializable
    data class AddressDto(
        @SerialName("city")
        val city: String,
        @SerialName("country")
        val country: String
    )
}

@Serializable
data class BiographyListDto(
    @SerialName("data")
    val biographies: List<BiographyDto>
)