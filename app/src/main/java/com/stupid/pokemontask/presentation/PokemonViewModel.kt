package com.stupid.pokemontask.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stupid.pokemontask.domain.usecase.GetPokemonListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(private val getPokemonListUseCase: GetPokemonListUseCase): ViewModel() {

    private val _pokemonState = MutableStateFlow<PokemonStateUI>(PokemonStateUI.Loading)

    val pokemonStat: StateFlow<PokemonStateUI> get() = _pokemonState.asStateFlow()

    init {
        loadPokemonList()
    }

    fun loadPokemonList() {
        viewModelScope.launch {
            val pokemonList = getPokemonListUseCase.getPokemonList(0)

            pokemonList.collect { list->
                _pokemonState.update {
                    PokemonStateUI.Success(list)
                }
            }
        }

    }



}