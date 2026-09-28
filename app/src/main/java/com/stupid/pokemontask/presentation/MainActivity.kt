package com.stupid.pokemontask.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stupid.pokemontask.ui.theme.PokemonTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonTaskTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PokemonList(innerPadding)
                }
            }
        }
    }
}

@Composable
fun PokemonList(innerPadding: PaddingValues) {
    val viewModel: PokemonViewModel = hiltViewModel()
    val uiState by viewModel.pokemonState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    when (val state = uiState) {
        is PokemonStateUI.Success -> {
            LaunchedEffect(
                listState,
                state.pokemonList.size,
                state.canLoadMore,
                state.isLoadingMore
            ) {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                    .collect { lastVisibleIndex ->
                        val shouldLoadMore = lastVisibleIndex != null &&
                            state.canLoadMore &&
                            !state.isLoadingMore &&
                            state.pokemonList.isNotEmpty() &&
                            lastVisibleIndex >= state.pokemonList.lastIndex - 4

                        if (shouldLoadMore) {
                            viewModel.loadNextPage()
                        }
                    }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(
                    items = state.pokemonList,
                    key = { it.id }
                ) { pokemon ->
                    Text(
                        text = "${pokemon.id}. ${pokemon.name.replaceFirstChar { it.uppercase() }}",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }

                item(key = "paginationFooter") {
                    when {
                        state.isLoadingMore -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        state.loadMoreError != null -> {
                            Text(
                                text = state.loadMoreError,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                            )
                        }
                    }
                }
            }
        }

        is PokemonStateUI.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is PokemonStateUI.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = state.message,
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(onClick = viewModel::retryInitialLoad) {
                        Text(text = "Retry")
                    }
                }
            }
        }
    }
}