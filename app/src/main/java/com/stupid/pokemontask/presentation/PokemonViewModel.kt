package com.stupid.pokemontask.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stupid.pokemontask.domain.usecase.GetPokemonListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(private val getPokemonListUseCase: GetPokemonListUseCase) :
    ViewModel() {

    private companion object {
        const val PAGE_SIZE = 20
    }

    private val _pokemonState = MutableStateFlow<PokemonStateUI>(PokemonStateUI.Loading)
    private var offset = 0
    private var isRequestInFlight = false
    private var reachedEnd = false

    val pokemonState: StateFlow<PokemonStateUI> get() = _pokemonState.asStateFlow()

    init {
        loadInitialPokemonList()
    }

    fun loadNextPage() {
        if (isRequestInFlight || reachedEnd || _pokemonState.value is PokemonStateUI.Loading) {
            return
        }
        fetchPokemonPage(isInitialLoad = false)
    }

    fun retryInitialLoad() {
        if (isRequestInFlight) return
        loadInitialPokemonList()
    }

    private fun loadInitialPokemonList() {
        offset = 0
        reachedEnd = false
        fetchPokemonPage(isInitialLoad = true)
    }

    private fun fetchPokemonPage(isInitialLoad: Boolean) {
        viewModelScope.launch {
            isRequestInFlight = true
            val existingList = (_pokemonState.value as? PokemonStateUI.Success)?.pokemonList.orEmpty()

            if (isInitialLoad) {
                _pokemonState.value = PokemonStateUI.Loading
            } else {
                _pokemonState.value = PokemonStateUI.Success(
                    pokemonList = existingList,
                    isLoadingMore = true,
                    canLoadMore = true,
                    loadMoreError = null
                )
            }

            try {
                val newItems = getPokemonListUseCase.getPokemonList(offset = offset, limit = PAGE_SIZE).first()
                val mergedList = if (isInitialLoad) {
                    newItems
                } else {
                    (existingList + newItems).distinctBy { it.id }
                }

                reachedEnd = newItems.size < PAGE_SIZE
                offset = mergedList.size

                _pokemonState.value = PokemonStateUI.Success(
                    pokemonList = mergedList,
                    isLoadingMore = false,
                    canLoadMore = !reachedEnd
                )
            } catch (throwable: Throwable) {
                val message = throwable.message ?: "Unable to load Pokemon list"
                if (isInitialLoad || existingList.isEmpty()) {
                    _pokemonState.value = PokemonStateUI.Error(
                        message
                    )
                } else {
                    _pokemonState.value = PokemonStateUI.Success(
                        pokemonList = existingList,
                        isLoadingMore = false,
                        canLoadMore = true,
                        loadMoreError = message
                    )
                }
            } finally {
                isRequestInFlight = false
            }
        }
    }
}