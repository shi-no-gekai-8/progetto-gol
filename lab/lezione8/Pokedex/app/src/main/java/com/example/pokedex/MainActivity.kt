package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokedex.ui.theme.PokedexTheme

data class Pokemon(
    val name: String,
    val type: String,
    val imageRes: Int
)

val pokemonList = listOf(
    Pokemon("Bulbasaur", "Erba/Veleno", R.drawable.bulbasaur),
    Pokemon("Ivysaur", "Erba/Veleno", R.drawable.ivysaur),
    Pokemon("Venusaur", "Erba/Veleno", R.drawable.venusaur),
    Pokemon("Charmander", "Fuoco", R.drawable.charmander),
    Pokemon("Charmeleon", "Fuoco", R.drawable.charmeleon),
    Pokemon("Charizard", "Fuoco/Volante", R.drawable.charizard),
    Pokemon("Squirtle", "Acqua", R.drawable.squirtle),
    Pokemon("Wartortle", "Acqua", R.drawable.wartortle),
    Pokemon("Blastoise", "Acqua", R.drawable.blastoise),
    Pokemon("Pikachu", "Elettro", R.drawable.pikachu)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PokedexScreen(
                        pokemon = pokemonList,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PokedexScreen(
    pokemon: List<Pokemon>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(pokemon) { currentPokemon ->
            PokemonCard(pokemon = currentPokemon)
        }
    }
}

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = pokemon.imageRes),
                contentDescription = pokemon.name,
                modifier = Modifier.size(72.dp)
            )

            Spacer(modifier = Modifier.size(16.dp))

            Column {
                Text(
                    text = pokemon.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = pokemon.type,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonCardPreview() {
    PokedexTheme {
        PokemonCard(
            pokemon = Pokemon(
                name = "Bulbasaur",
                type = "Erba/Veleno",
                imageRes = android.R.drawable.ic_menu_gallery
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PokedexScreenPreview() {
    PokedexTheme {
        PokedexScreen(
            pokemon = listOf(
                Pokemon("Bulbasaur", "Erba/Veleno", android.R.drawable.ic_menu_gallery),
                Pokemon("Charmander", "Fuoco", android.R.drawable.ic_menu_gallery),
                Pokemon("Squirtle", "Acqua", android.R.drawable.ic_menu_gallery)
            )
        )
    }
}