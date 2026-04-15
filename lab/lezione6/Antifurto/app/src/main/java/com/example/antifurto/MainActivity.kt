package com.example.antifurto

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    /**
     * Avvia il foreground service che fa da "guardiano":
     * ascolta l'accelerometro e, se rileva un movimento forte,
     * fa partire l'allarme.
     */
    private fun startAlarmService() {
        val intent = Intent(this, AlarmService::class.java).apply {
            action = AlarmService.ACTION_START
        }
        ContextCompat.startForegroundService(this, intent)
    }

    /**
     * Ferma completamente il service e l'eventuale suono.
     */
    private fun stopAlarmService() {
        val intent = Intent(this, AlarmService::class.java).apply {
            action = AlarmService.ACTION_STOP
        }
        startService(intent)
    }

    /**
     * Launcher per chiedere il permesso notifiche su Android 13+.
     * Se l'utente accetta, avviamo direttamente l'antifurto.
     */
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startAlarmService()
            } else {
                Toast.makeText(
                    this,
                    "Senza notifiche la demo perde il pulsante rapido per spegnere l'allarme.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private fun requestNotificationPermissionIfNeededAndStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val alreadyGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!alreadyGranted) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                return
            }
        }

        startAlarmService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AlarmScreen(
                        onStartClicked = { requestNotificationPermissionIfNeededAndStart() },
                        onStopClicked = { stopAlarmService() }
                    )
                }
            }
        }
    }
}

@Composable
fun AlarmScreen(
    onStartClicked: () -> Unit,
    onStopClicked: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    // Controllo se il device/emulatore ha un accelerometro disponibile.
    val hasAccelerometer = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null
    }

    // Stato letto dal service.
    // Per una demo didattica va bene leggerlo periodicamente.
    var serviceRunning by remember { mutableStateOf(AlarmService.isRunning) }
    var alarmPlaying by remember { mutableStateOf(AlarmService.isAlarmPlaying) }

    LaunchedEffect(Unit) {
        while (true) {
            serviceRunning = AlarmService.isRunning
            alarmPlaying = AlarmService.isAlarmPlaying
            delay(500)
        }
    }

    val statusText = when {
        alarmPlaying -> "🚨 ALLARME ATTIVO"
        serviceRunning -> "🟡 Antifurto armato: sto monitorando il telefono"
        else -> "🟢 Antifurto spento"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Demo Antifurto",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Stato",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(statusText)
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Come funziona",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "1) Premi \"Attiva antifurto\".\n" +
                            "2) Il service resta vivo in background.\n" +
                            "3) Se il telefono si muove troppo, parte la sirena.\n" +
                            "4) Puoi spegnerla dalla notifica o dal pulsante qui sotto."
                )
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Istruzioni per l'emulatore",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Per simulare l'accelerometro:\n\n" +
                            "• Avvia l'emulatore\n" +
                            "• Clicca sui tre puntini laterali (...)\n" +
                            "• Apri Extended Controls\n" +
                            "• Vai in Virtual Sensors / Device Pose\n" +
                            "• Muovi i controlli di rotazione o inclinazione\n\n" +
                            "Se il sensore sembra troppo sensibile o troppo debole, " +
                            "cambia la soglia nel file AlarmService."
                )
            }
        }

        if (!hasAccelerometer) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Questo emulatore/device non espone un accelerometro. " +
                                "Per la demo serve un AVD con accelerometro attivo oppure un telefono reale."
                    )
                }
            }
        }

        Button(
            onClick = onStartClicked,
            enabled = hasAccelerometer && !serviceRunning,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Attiva antifurto")
        }

        OutlinedButton(
            onClick = onStopClicked,
            enabled = serviceRunning,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Disattiva tutto")
        }
    }
}