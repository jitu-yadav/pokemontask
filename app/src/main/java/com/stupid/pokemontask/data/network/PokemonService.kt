package com.stupid.pokemontask.data.network

import com.stupid.pokemontask.data.network.dtomodel.PokemonDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonService {
    @GET
    suspend fun getPokemonList(
        @Query("offset") page: Int = 0,
        @Query("limit") limit: Int = 20
    ): List<PokemonDto>

    @GET
    suspend fun getPokemonDetails(@Query("") id: Int)
}
