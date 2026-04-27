package com.example.laboratorio4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                MovieFinderScreen()
            }
        }
    }
}

/*
    Questa sealed class rappresenta i possibili stati della schermata.

    Quando facciamo una chiamata Internet, la UI può trovarsi in tre stati:

    1. Loading -> stiamo aspettando la risposta dal server
    2. Success -> abbiamo ricevuto correttamente i dati
    3. Error   -> qualcosa è andato storto
*/
sealed class MovieUiState {
    object Loading : MovieUiState()

    data class Success(
        val movie: MovieResponse
    ) : MovieUiState()

    data class Error(
        val message: String
    ) : MovieUiState()
}

@Composable
fun MovieFinderScreen() {

    /*
        Stato della schermata.

        All'inizio mettiamo Loading perché appena la schermata parte
        vogliamo fare la chiamata a Internet.
    */
    var uiState by remember {
        mutableStateOf<MovieUiState>(MovieUiState.Loading)
    }

    /*
        Inserire qui la propria API key di OMDb.

        Per ora è una costante nello scaffolding.
        In un'app reale non bisognerebbe lasciare le API key scritte direttamente
        nel codice sorgente.
    */
    val apiKey = "81fb539c"

    /*
        LaunchedEffect viene eseguito quando il Composable entra nello schermo.

        Lo usiamo perché la chiamata di rete è asincrona:
        non vogliamo bloccare la UI mentre aspettiamo Internet.
    */
    LaunchedEffect(Unit) {
        try {
            /*
                Chiamata fissa dello scaffolding.

                In questa fase cerchiamo sempre "Batman".
                La ricerca dinamica sarà parte della sfida autonoma.
            */
            val movie = RetrofitClient.api.getMovieByTitle(
                apiKey = apiKey,
                title = "Batman"
            )

            /*
                Se la chiamata va a buon fine, salviamo il film nello stato Success.
                Compose ridisegnerà automaticamente la UI.
            */
            uiState = MovieUiState.Success(movie)

        } catch (exception: Exception) {

            /*
                Se qualcosa va storto, ad esempio:
                - Internet assente
                - API key sbagliata
                - server non raggiungibile

                mostriamo uno stato di errore.
            */
            uiState = MovieUiState.Error(
                message = "Errore durante il caricamento del film."
            )
        }
    }

    /*
        Layout principale della schermata.

        Usiamo una Column per disporre gli elementi in verticale.
    */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Movie Finder",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        /*
            Qui decidiamo cosa mostrare in base allo stato corrente della UI.
        */
        when (val state = uiState) {

            /*
                Stato 1: caricamento.
                Mostriamo una rotellina mentre Retrofit lavora.
            */
            is MovieUiState.Loading -> {
                CircularProgressIndicator()

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Caricamento film...")
            }

            /*
                Stato 2: successo.
                Mostriamo i primi dati testuali del film.
            */
            is MovieUiState.Success -> {
                MovieCard(movie = state.movie)
            }

            /*
                Stato 3: errore.
                Mostriamo un messaggio semplice all'utente.
            */
            is MovieUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun MovieCard(movie: MovieResponse) {

    /*
        Card base per mostrare il risultato.

        Per ora mostriamo solo testo.
        La locandina con Coil sarà parte della sfida autonoma.
    */
    Card {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = movie.title ?: "Titolo non disponibile",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Anno: ${movie.year ?: "N/D"}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movie.plot ?: "Trama non disponibile",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "URL poster: ${movie.poster ?: "N/D"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}