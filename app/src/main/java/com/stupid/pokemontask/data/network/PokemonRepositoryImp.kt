package com.stupid.pokemontask.data.network

import com.stupid.pokemontask.domain.usecase.PokemonRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class PokemonRepositoryImp @Inject constructor(val api: PokemonService) : PokemonRepository {

    override suspend fun getPokemonList(
         page: Int,
        limit: Int
    ) = flow {
        val list = api.getPokemonList(page, limit)
        emit(list)
    }


    override suspend fun getPokemonDetails(id: Int) {
        val details = api.getPokemonDetails(id)
        // emit(list)
    }
}