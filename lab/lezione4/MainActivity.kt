package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MeteoScreen()
                }
            }
        }
    }
}

/* ------------------------------------------------------------------
   1) DATA CLASS: rappresentano il JSON che arriva dal server
   ------------------------------------------------------------------

   Open-Meteo ci restituisce un JSON simile a questo:

   {
     "latitude": 40.85,
     "longitude": 14.27,
     "timezone": "Europe/Rome",
     "current": {
       "time": "2026-04-11T14:15",
       "temperature_2m": 21.3,
       "wind_speed_10m": 8.4,
       "weather_code": 1
     }
   }

   Noi creiamo delle classi Kotlin con gli stessi nomi dei campi,
   così Retrofit + Gson fanno il parsing automaticamente.
*/

data class MeteoResponse(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val current: CurrentWeather
)

data class CurrentWeather(
    val time: String,
    val temperature_2m: Double,
    val wind_speed_10m: Double,
    val weather_code: Int
)

/* ------------------------------------------------------------------
   2) INTERFACCIA RETROFIT
   ------------------------------------------------------------------

   Qui descriviamo la chiamata HTTP.
   Retrofit genererà da solo il codice necessario.

   Base URL:
   https://api.open-meteo.com/

   Endpoint:
   v1/forecast

   Query params:
   - latitude
   - longitude
   - current
   - timezone
*/
interface OpenMeteoApi {

    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String,
        @Query("timezone") timezone: String
    ): MeteoResponse
}

/* ------------------------------------------------------------------
   3) COSTRUZIONE DI RETROFIT
   ------------------------------------------------------------------

   In un progetto "vero" questo starebbe fuori dalla MainActivity,
   magari in un object o in una dependency injection.
   Ma per una demo è perfetto tenerlo qui, tutto visibile.
*/
object RetrofitInstance {

    val api: OpenMeteoApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenMeteoApi::class.java)
    }
}

/* ------------------------------------------------------------------
   4) UI STATE
   ------------------------------------------------------------------

   Quando facciamo rete, l'interfaccia deve sapere in che stato siamo:
   - caricamento
   - successo
   - errore

   Questo è uno dei concetti più importanti della lezione.
*/
sealed class MeteoUiState {
    object Loading : MeteoUiState()
    data class Success(val data: MeteoResponse) : MeteoUiState()
    data class Error(val message: String) : MeteoUiState()
}

@Composable
fun MeteoScreen() {

    // Stato della UI: all'inizio siamo in caricamento
    var uiState by remember { mutableStateOf<MeteoUiState>(MeteoUiState.Loading) }

    // Piccolo trucco: un contatore che cambia quando premiamo "Riprova".
    // Siccome LaunchedEffect dipende da retryKey, ogni volta che cambia
    // la coroutine riparte e rifà la chiamata di rete.
    var retryKey by remember { mutableStateOf(0) }

    /* --------------------------------------------------------------
       5) LAUNCHED EFFECT
       --------------------------------------------------------------

       È il posto "sicuro" di Compose per lanciare una coroutine
       collegata al ciclo di vita della schermata.

       Dentro facciamo la chiamata HTTP in modo asincrono.
       Non blocchiamo il Main Thread.
    */
    LaunchedEffect(retryKey) {
        uiState = MeteoUiState.Loading

        try {
            // Coordinate di Napoli, così la demo è più vicina ai ragazzi :)
            val response = RetrofitInstance.api.getCurrentWeather(
                latitude = 40.8518,
                longitude = 14.2681,
                current = "temperature_2m,wind_speed_10m,weather_code",
                timezone = "auto"
            )

            uiState = MeteoUiState.Success(response)

        } catch (e: Exception) {
            // Qualsiasi errore di rete/parsing finisce qui
            uiState = MeteoUiState.Error(
                message = e.message ?: "Errore sconosciuto durante la chiamata di rete"
            )
        }
    }

    // UI principale
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Demo Retrofit + JSON",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (val state = uiState) {

            is MeteoUiState.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Sto contattando il server...")
            }

            is MeteoUiState.Success -> {
                WeatherCard(
                    response = state.data,
                    onRefreshClick = {
                        retryKey++
                    }
                )
            }

            is MeteoUiState.Error -> {
                Text(
                    text = "Errore di rete 😢",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = state.message,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { retryKey++ }) {
                    Text("Riprova")
                }
            }
        }
    }
}

@Composable
fun WeatherCard(
    response: MeteoResponse,
    onRefreshClick: () -> Unit
) {
    val current = response.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Meteo attuale",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Timezone: ${response.timezone}")
            Text("Ora rilevazione: ${current.time}")
            Text("Temperatura: ${current.temperature_2m} °C")
            Text("Vento: ${current.wind_speed_10m} km/h")
            Text("Codice meteo: ${current.weather_code}")
            Text("Descrizione: ${weatherCodeToItalian(current.weather_code)}")

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Button(onClick = onRefreshClick) {
                    Text("Aggiorna")
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        // Qui puoi aggiungere in futuro il cambio città
                    }
                ) {
                    Text("Altra città")
                }
            }
        }
    }
}

/* ------------------------------------------------------------------
   6) FUNZIONE DI SUPPORTO
   ------------------------------------------------------------------

   Open-Meteo restituisce un weather_code numerico.
   Per la demo è più bello tradurlo in testo leggibile.
*/
fun weatherCodeToItalian(code: Int): String {
    return when (code) {
        0 -> "Cielo sereno"
        1, 2, 3 -> "Parzialmente nuvoloso"
        45, 48 -> "Nebbia"
        51, 53, 55 -> "Pioviggine"
        61, 63, 65 -> "Pioggia"
        71, 73, 75 -> "Neve"
        80, 81, 82 -> "Rovesci"
        95 -> "Temporale"
        else -> "Condizione meteo non riconosciuta"
    }
}