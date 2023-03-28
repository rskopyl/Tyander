package com.rskopyl.tyander.repository.impl

import com.rskopyl.tyander.data.model.Character
import com.rskopyl.tyander.data.remote.BiographyApi
import com.rskopyl.tyander.data.remote.ImageApi
import com.rskopyl.tyander.data.remote.getRandom
import com.rskopyl.tyander.repository.CharacterRepository
import com.rskopyl.tyander.repository.impl.mapper.CharacterMapper
import com.rskopyl.tyander.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val imageApi: ImageApi,
    private val biographyApi: BiographyApi
) : CharacterRepository {

    override fun getRandom(
        count: Int
    ): Flow<Resource<List<Character>>> = flow {
        emit(Resource.Loading())
        try {
            coroutineScope {
                val images = async(Dispatchers.IO) {
                    imageApi.getRandom(count).images
                }
                val biographies = async(Dispatchers.IO) {
                    biographyApi.getRandom(count).biographies
                }
                val characters = CharacterMapper.map(
                    images.await().zip(biographies.await())
                )
                emit(Resource.Success(characters))
            }
        } catch (e: UnknownHostException) {
            emit(Resource.Error())
        }
    }
}