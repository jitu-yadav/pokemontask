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
}
