package com.stupid.pokemontask.domain.usecase

import com.stupid.pokemontask.data.network.dtomodel.PokemonDto
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {


    suspend fun getPokemonList(page:Int = 0, limit: Int = 20) : Flow<List<PokemonDto>>


    suspend fun getPokemonDetails(id: Int)
}