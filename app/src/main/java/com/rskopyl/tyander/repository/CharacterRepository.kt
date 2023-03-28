package com.rskopyl.tyander.repository

import com.rskopyl.tyander.data.model.Character
import com.rskopyl.tyander.util.Resource
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    fun getRandom(count: Int): Flow<Resource<List<Character>>>
}