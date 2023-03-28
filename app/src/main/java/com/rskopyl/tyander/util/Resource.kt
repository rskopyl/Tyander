package com.rskopyl.tyander.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformWhile

sealed class Resource<T> {
    class Loading<T> : Resource<T>()
    data class Success<T>(val data: T) : Resource<T>()
    class Error<T> : Resource<T>()
}

fun <T> Resource<T>.dataOrNull(): T? {
    return if(this is Resource.Success) data else null
}

fun <T> Flow<Resource<T>>.completeWhenLoaded(): Flow<Resource<T>> {
    return transformWhile { resource ->
        emit(resource)
        resource is Resource.Loading
    }
}