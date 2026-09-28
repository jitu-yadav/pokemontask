package com.stupid.pokemontask.data.network

import com.stupid.pokemontask.data.network.dtomodel.PokemonDto
import com.stupid.pokemontask.domain.usecase.PokemonRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class PokemonRepositoryImp @Inject constructor(val api: PokemonService) : PokemonRepository {

    override suspend fun getPokemonList(
        offset: Int,
        limit: Int
    ) = flow {
        val response = api.getPokemonList(offset = offset, limit = limit)
        emit(
            response.results.mapIndexed { index, item ->
                val pokemonId = item.extractId() ?: (offset + index + 1)
                PokemonDto(
                    id = pokemonId,
                    name = item.name,
                    imageUrl = PokemonDto.buildImageUrl(pokemonId)
                )
            }
        )
    }


    override suspend fun getPokemonDetails(id: Int) {
        api.getPokemonDetails(id)
    }
}