package com.example.diarioalimentare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

// Assicurati che questi import corrispondano alla struttura delle tue cartelle!
import com.example.diarioalimentare.data.Pasto
import com.example.diarioalimentare.ui.theme.PastoViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: PastoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DiarioScreen(
                        innerPadding = innerPadding,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

// 1. Funzione che associa la categoria a un colore
fun getCategoriaColor(categoria: String): Color {
    return when(categoria.lowercase()){
        "colazione" -> Color(0xFF8BC34A) // Verde
        "pranzo" -> Color(0xFFFF9800)    // Arancione
        "cena" -> Color(0xFF9C27B0)      // Viola
        else -> Color(0xFF607D8B)        // Grigio (per gli Snack o altro)
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Serve per usare il DropdownMenu in Material3
@Composable
fun DiarioScreen(
    innerPadding: PaddingValues,
    viewModel: PastoViewModel
) {
    val pasti by viewModel.pasti.collectAsState()

    var nome by remember { mutableStateOf("") }
    var calorie by remember { mutableStateOf("") }

    // Variabili per gestire il menu a tendina delle Categorie
    val opzioniCategoria = listOf("Colazione", "Pranzo", "Cena", "Snack")
    var categoriaSelezionata by remember { mutableStateOf(opzioniCategoria[0]) }
    var menuEspanso by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Input Nome
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nome pasto") }
        )

        // Input Calorie (con tastierino numerico)
        OutlinedTextField(
            value = calorie,
            onValueChange = { calorie = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Calorie") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // Menu a tendina per la Categoria
        ExposedDropdownMenuBox(
            expanded = menuEspanso,
            onExpandedChange = { menuEspanso = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = categoriaSelezionata,
                onValueChange = {},
                readOnly = true, // L'utente non può scrivere, deve scegliere dal menu
                label = { Text("Categoria") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuEspanso) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = menuEspanso,
                onDismissRequest = { menuEspanso = false }
            ) {
                opzioniCategoria.forEach { opzione ->
                    DropdownMenuItem(
                        text = { Text(opzione) },
                        onClick = {
                            categoriaSelezionata = opzione
                            menuEspanso = false
                        }
                    )
                }
            }
        }

        // Bottone Aggiungi
        Button(
            onClick = {
                val calorieInt = calorie.toIntOrNull()
                if (nome.isNotBlank() && calorieInt != null) {
                    // Ora passiamo anche la categoria al ViewModel!
                    viewModel.inserisciPasto(nome, calorieInt, categoriaSelezionata)

                    // Svuotiamo i campi (lasciamo la categoria com'è per comodità)
                    nome = ""
                    calorie = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Aggiungi pasto")
        }

        // La lista dei Pasti
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Usiamo l'id come chiave per evitare glitch grafici durante lo swipe
            items(items = pasti, key = { it.id }) { pasto ->

                // Usiamo il nuovo componente con lo swipe integrato
                PastoItemSwipable(
                    pasto = pasto,
                    onDelete = { viewModel.eliminaPasto(pasto) }
                )

            }
        }
    }
}

// 2. Il componente grafico per il singolo pasto con SWIPE TO DELETE
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastoItemSwipable(
    pasto: Pasto,
    onDelete: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                Color.Red
            } else {
                Color.LightGray
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 2.dp) // Leggero margine per non toccare le altre card
                    .background(color, shape = RoundedCornerShape(12.dp))
                    .padding(end = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Elimina",
                    tint = Color.White
                )
            }
        },
        content = {
            // La nostra Card grafica vera e propria
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = getCategoriaColor(pasto.categoria).copy(alpha = 0.2f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = pasto.nome, style = MaterialTheme.typography.titleMedium)
                        Text(text = pasto.categoria, style = MaterialTheme.typography.labelSmall)
                    }
                    Text(text = "${pasto.calorie} kcal", fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}