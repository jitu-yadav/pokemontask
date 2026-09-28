package com.stupid.pokemontask.domain.usecase

import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetPokemonListUseCase @Inject constructor(val repo: PokemonRepository) {

    suspend fun getPokemonList(offset: Int, limit: Int) = repo.getPokemonList(offset, limit).map { pokemonDtos ->
        pokemonDtos.map { it.toDataModel() }
    }
}