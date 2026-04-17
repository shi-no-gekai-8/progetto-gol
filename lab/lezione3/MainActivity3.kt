package com.example.lifecycledemo

import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "LifecycleDemo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DemoScreen()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}

@Composable
fun DemoScreen() {
    var counterRemember by remember { mutableIntStateOf(0) }
    var counterSaveable by rememberSaveable { mutableIntStateOf(0) }

    var timerSeconds by rememberSaveable { mutableIntStateOf(0) }
    var isRunning by rememberSaveable { mutableStateOf(false) }

    // Side effect sicuro: il timer gira solo quando isRunning = true
    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000)
            timerSeconds++
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Demo Lifecycle + Stato + Side Effects",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Apri Logcat e filtra per: LifecycleDemo",
            style = MaterialTheme.typography.bodyLarge
        )

        Divider()

        CounterCard(
            title = "1) Contatore con remember",
            description = "Questo valore sopravvive ai recomposition, ma NON alla rotazione.",
            value = counterRemember,
            onIncrement = { counterRemember++ },
            onReset = { counterRemember = 0 }
        )

        CounterCard(
            title = "2) Contatore con rememberSaveable",
            description = "Questo valore sopravvive anche ai configuration changes, come la rotazione.",
            value = counterSaveable,
            onIncrement = { counterSaveable++ },
            onReset = { counterSaveable = 0 }
        )

        TimerCard(
            seconds = timerSeconds,
            isRunning = isRunning,
            onToggle = { isRunning = !isRunning },
            onReset = {
                isRunning = false
                timerSeconds = 0
            }
        )

        InstructionsCard()
    }
}

@Composable
fun CounterCard(
    title: String,
    description: String,
    value: Int,
    onIncrement: () -> Unit,
    onReset: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Valore: $value",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onIncrement) {
                    Text("+1")
                }
                OutlinedButton(onClick = onReset) {
                    Text("Reset")
                }
            }
        }
    }
}

@Composable
fun TimerCard(
    seconds: Int,
    isRunning: Boolean,
    onToggle: () -> Unit,
    onReset: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "3) Timer con LaunchedEffect",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Il timer aumenta ogni secondo solo quando è in esecuzione.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Secondi: $seconds",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onToggle) {
                    Text(if (isRunning) "Pausa" else "Play")
                }
                OutlinedButton(onClick = onReset) {
                    Text("Reset")
                }
            }
        }
    }
}

@Composable
fun InstructionsCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Cose da provare in diretta",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("• Premi +1 sui due contatori.")
            Text("• Ruota il dispositivo/emulatore.")
            Text("• Osserva: il primo si resetta, il secondo no.")
            Text("• Avvia il timer.")
            Text("• Premi Home e poi torna nell'app.")
            Text("• Guarda Logcat per vedere onPause / onStop / onStart / onResume.")
        }
    }
}