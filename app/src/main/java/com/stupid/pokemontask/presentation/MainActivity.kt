package com.stupid.pokemontask.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stupid.pokemontask.ui.theme.PokemonTaskTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

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
    val viewModel: PokemonViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.loadPokemonList()
    }

    val list = viewModel.pokemonStat.collectAsStateWithLifecycle()

    when (list) {
        is PokemonStateUI.Success -> {
            LazyColumn() {
                items() {
                    //async

                    // if(list.getValue( size-1) i)
                }
            }
        }

        is PokemonStateUI.Loading -> {

        }

        is PokemonStateUI.Error -> {

        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokemonTaskTheme {
        Greeting("Android")
    }
}