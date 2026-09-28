package com.stupid.pokemontask.presentation

import com.stupid.pokemontask.domain.model.Pokemon

sealed class PokemonStateUI {
    data class Success(
        val pokemonList: List<Pokemon>,
        val isLoadingMore: Boolean = false,
        val canLoadMore: Boolean = true,
        val loadMoreError: String? = null
    ) : PokemonStateUI()

    data class Error(val message: String) : PokemonStateUI()
    object Loading : PokemonStateUI()

}