package com.stupid.pokemontask.domain.usecase

import com.stupid.pokemontask.data.network.dtomodel.PokemonDto
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GetPokemonListUseCase @Inject constructor(val repo: PokemonRepository) {

    suspend fun getPokemonList(page: Int) = flow {
        repo.getPokemonList(page).collect { pokemonDtos ->
            val pokemonList = pokemonDtos.map {
                it.toDataModel()
            }

            emit(pokemonList)
        }
    }

}