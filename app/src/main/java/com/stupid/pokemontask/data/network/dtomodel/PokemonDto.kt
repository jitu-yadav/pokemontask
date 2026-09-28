package com.stupid.pokemontask.data.network.dtomodel

import com.stupid.pokemontask.domain.model.Pokemon

data class PokemonDto(val id: Int, val name: String, val imageUrl: String) {
    fun toDataModel(): Pokemon {
        return Pokemon(
            id = id,
            name = name,
            imageUrl = imageUrl
        )
    }

    companion object {
        fun buildImageUrl(id: Int): String {
            return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
        }
    }
}
