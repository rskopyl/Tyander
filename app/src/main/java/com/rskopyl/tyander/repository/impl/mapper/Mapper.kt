package com.rskopyl.tyander.repository.impl.mapper

interface Mapper<A, B> {

    fun map(entity: A): B

    fun map(entities: Collection<A>): List<B> =
        entities.map(::map)
}