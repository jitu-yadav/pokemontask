package com.stupid.pokemontask.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20
    ): PokemonListResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetails(@Path("id") id: Int): PokemonDetailsDto
}

data class PokemonListResponse(val results: List<PokemonListItemDto>)

data class PokemonListItemDto(
    val name: String,
    val url: String
) {
    fun extractId(): Int? = url.trimEnd('/').substringAfterLast('/').toIntOrNull()
}

data class PokemonDetailsDto(
    val id: Int,
    val name: String
)
