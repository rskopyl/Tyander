package com.rskopyl.tyander.repository.impl.mapper

import com.rskopyl.tyander.data.model.Character
import com.rskopyl.tyander.data.remote.dto.BiographyDto
import com.rskopyl.tyander.data.remote.dto.ImageDto

object CharacterMapper : Mapper<Pair<ImageDto, BiographyDto>, Character> {

    override fun map(entity: Pair<ImageDto, BiographyDto>): Character {
        val (image, biography) = entity
        return Character(
            name = biography.name,
            email = biography.email,
            phone = biography.phone,
            birthday = biography.birthday,
            address = Character.Address(
                city = biography.address.city,
                country = biography.address.country
            ),
            imageUrl = image.url
        )
    }
}