package com.example.laboratorio5

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    // 1. GLI "ATTREZZI DEL MESTIERE"
    // Client Google: ci serve per parlare con il satellite GPS
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Il nostro archivio: ci serve per salvare i passi
    private lateinit var stepDataStore: StepDataStore

    // IL MOTORE DI AVVIAMENTO: Questa funzione parte appena l'utente apre l'app
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Prepariamo gli attrezzi prima di disegnare lo schermo
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        stepDataStore = StepDataStore(this)

        // setContent avvia la grafica dell'app (Jetpack Compose)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Chiamiamo la nostra "centralina"
                    GPSPedometerApp(
                        fusedLocationClient = fusedLocationClient,
                        stepDataStore = stepDataStore
                    )
                }
            }
        }
    }
}

// 2. IL DIRETTORE D'ORCHESTRA: Decide cosa far vedere all'utente
@Composable
fun GPSPedometerApp(
    fusedLocationClient: FusedLocationProviderClient,
    stepDataStore: StepDataStore
) {
    val context = LocalContext.current

    // MEMORIA A BREVE TERMINE: 'remember' e 'mutableStateOf'
    // L'app ricorda se ha il permesso. Se questo valore cambia, la grafica si aggiorna da sola!
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Memoria per salvare le coordinate (Latitudine e Longitudine)
    var lastKnownLocation by remember { mutableStateOf<Location?>(null) }

    // IL TUBO DELL'ACQUA: 'collectAsState'
    // Ci mettiamo in ascolto del DataStore. Ogni volta che i passi cambiano,
    // questo valore si aggiorna in tempo reale. Se è la prima volta, partiamo da 0.
    val savedSteps by stepDataStore.totalStepsFlow.collectAsState(initial = 0)

    // IL VIGILE: È la finestrella di Android che chiede "Vuoi dare il permesso?"
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Se l'utente dice "Sì", aggiorniamo la nostra memoria!
        hasLocationPermission = isGranted
    }

    // L'INNESCATORE: 'LaunchedEffect'
    // Questa parte di codice scatta SOLO quando 'hasLocationPermission' cambia.
    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            // Se abbiamo il permesso, chiediamo al GPS l'ultima posizione conosciuta
            try {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        lastKnownLocation = location // Salviamo le coordinate trovate
                    }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    // IL BIVIO LOGICO: L'interfaccia si adatta in base ai permessi
    if (hasLocationPermission) {
        // PERMESSO OK: Mostriamo i dati e i bottoni per giocare
        PermissionGrantedScreen(
            location = lastKnownLocation,
            savedSteps = savedSteps,
            onFakeSaveExample = {
                // Quando cliccano il bottone, salviamo +100 passi in background
                (context as? ComponentActivity)?.lifecycleScope?.launch {
                    stepDataStore.saveSteps(savedSteps + 100)
                }
            }
        )
    } else {
        // NESSUN PERMESSO: Mostriamo la schermata di blocco con il bottone per chiederlo
        PermissionDeniedScreen(
            onRequestPermission = {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        )
    }
}

// 3. I MATTONCINI VISIVI: Schermata "Permesso Negato"
@Composable
fun PermissionDeniedScreen(
    onRequestPermission: () -> Unit
) {
    // Column: Mette tutti gli elementi uno sotto l'altro (in colonna)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center, // Centra tutto verticalmente
        horizontalAlignment = Alignment.CenterHorizontally // Centra tutto orizzontalmente
    ) {
        // L'icona del GPS sbarrato
        Icon(
            imageVector = Icons.Default.LocationOff,
            contentDescription = "GPS non disponibile",
            modifier = Modifier.size(72.dp)
        )

        // Spacer: Crea uno spazio vuoto (come l'invio sulla tastiera)
        Spacer(modifier = Modifier.height(16.dp))

        // I testi informativi
        Text(
            text = "Devi abilitare il GPS per usare l'app!",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "In questa fase stiamo costruendo solo lo scaffolding: senza permesso non possiamo leggere la posizione.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Il bottone che lancia la richiesta di permesso
        Button(onClick = onRequestPermission) {
            Text("Concedi permesso")
        }
    }
}

// 4. I MATTONCINI VISIVI: Schermata "Permesso Concesso" (Tutto funziona!)
@Composable
fun PermissionGrantedScreen(
    location: Location?,
    savedSteps: Int,
    onFakeSaveExample: () -> Unit
) {
    // Box: Un contenitore per centrare facilmente tutto lo schermo
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // Icona del GPS attivo
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "GPS attivo",
                modifier = Modifier.size(72.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Permesso GPS concesso ✅",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Mostriamo in tempo reale il valore che ci arriva dal DataStore
            Text(
                text = "Passi salvati nel DataStore: $savedSteps",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Se il satellite ha risposto, stampiamo le coordinate
            if (location != null) {
                Text(
                    text = "Latitudine: ${location.latitude}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Longitudine: ${location.longitude}",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                // Messaggio di sicurezza per chi testa l'app sull'emulatore del PC
                Text(
                    text = "Nessuna posizione disponibile al momento.\nSull'emulatore devi simulare una location.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Il bottone che simula una camminata salvando finti passi
            Button(onClick = onFakeSaveExample) {
                Text("Salva +100 passi (demo)")
            }
        }
    }
}