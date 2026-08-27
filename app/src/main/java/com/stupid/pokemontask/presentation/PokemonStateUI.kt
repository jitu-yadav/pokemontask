package com.stupid.pokemontask.presentation

import com.stupid.pokemontask.domain.model.Pokemon

sealed class PokemonStateUI {
    data class Success(val pokemonList: List<Pokemon>) : PokemonStateUI()
    data class Error(val message: String): PokemonStateUI()
    object Loading: PokemonStateUI()

}