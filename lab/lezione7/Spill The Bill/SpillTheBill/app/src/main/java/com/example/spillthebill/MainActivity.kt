package com.example.spillthebill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.spillthebill.ui.theme.SpillTheBillTheme

// La MainActivity è il punto di ingresso dell'app (il nostro "main")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Abilitiamo il design "Edge-to-Edge" per usare tutto lo schermo, inclusa la barra di stato
        enableEdgeToEdge()

        setContent {
            // Applichiamo il tema dell'applicazione generato da Android Studio
            SpillTheBillTheme {
                // Surface è il "foglio" su cui poggia la nostra interfaccia
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Chiamiamo la nostra funzione principale della schermata
                    SplitBillScreen()
                }
            }
        }
    }
}

@Composable
fun SplitBillScreen() {
    /* 1. DEFINIZIONE DELLO STATO
       Usiamo 'remember' perché altrimenti, a ogni ridisegno della UI, le variabili verrebbero resettate.
       'mutableStateOf("")' crea un contenitore che, quando cambia, avvisa Compose di aggiornare lo schermo.
    */
    var totalBill by remember { mutableStateOf("") }
    var peopleCount by remember { mutableStateOf("") }

    /* 2. LOGICA DI CALCOLO
       I TextField restituiscono sempre Stringhe. Dobbiamo convertirle in Numeri per fare i calcoli.
       '.toDoubleOrNull()' evita il crash se l'utente scrive lettere o lascia vuoto (restituisce null).
       '?: 0.0' è l'operatore Elvis: se il risultato è null, usa 0.0 come valore di default.
    */
    val total = totalBill.toDoubleOrNull() ?: 0.0
    val people = peopleCount.toIntOrNull() ?: 0

    // Calcoliamo la quota solo se il numero di persone è maggiore di zero per evitare errori matematici
    val quotaPerPersona = if (people > 0) {
        total / people
    } else {
        0.0
    }

    /* 3. COSTRUZIONE DEL LAYOUT
       La Column organizza gli elementi dall'alto verso il basso.
    */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp), // Margine interno per non toccare i bordi dello schermo
        verticalArrangement = Arrangement.Center, // Centra i componenti verticalmente
        horizontalAlignment = Alignment.CenterHorizontally // Centra i componenti orizzontalmente
    ) {
        // Campo di input per il totale
        OutlinedTextField(
            value = totalBill,
            onValueChange = { totalBill = it }, // Aggiorna lo stato ogni volta che l'utente scrive
            label = { Text("Totale del conto") },
            modifier = Modifier.fillMaxWidth(),
            // Forza la tastiera a mostrare solo i numeri
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // Campo di input per il numero di persone
        OutlinedTextField(
            value = peopleCount,
            onValueChange = { peopleCount = it },
            label = { Text("Numero di persone") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        /* 4. VISUALIZZAZIONE RISULTATO
           "%.2f" serve a formattare il numero con solo due cifre decimali (es. 12.50 invece di 12.5)
        */
        Text(
            text = "Quota a persona: %.2f €".format(quotaPerPersona),
            style = MaterialTheme.typography.headlineSmall, // Applichiamo uno stile predefinito più grande
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}