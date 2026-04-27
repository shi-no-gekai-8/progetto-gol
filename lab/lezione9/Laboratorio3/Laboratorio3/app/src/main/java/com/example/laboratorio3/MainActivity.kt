package com.example.laboratorio3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// ------------------------------
// MODELLO DATI
// ------------------------------

// Questa data class rappresenta UNA domanda del quiz.
// Ogni domanda ha:
// - il testo della domanda
// - una lista di possibili risposte
// - l'indice della risposta corretta
data class Question(
    val text: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Gestisce meglio lo spazio dello schermo sui telefoni moderni

        setContent {
            MaterialTheme {
                QuizApp()
            }
        }
    }
}

@Composable
fun QuizApp() {
    // Il NavController è "il volante" della nostra app:
    // serve per dire all'app in quale schermata andare.
    val navController = rememberNavController()

    // ------------------------------
    // MOCK DATA
    // ------------------------------
    // Qui creiamo una lista finta di 5 domande.
    // Per ora ci basta per costruire la struttura dell'app.
    val questions = listOf(
        Question(
            text = "Quale linguaggio usiamo in Android Studio per questo corso?",
            answers = listOf("Python", "Kotlin", "JavaScript", "Swift"),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Jetpack Compose serve per...",
            answers = listOf("Creare interfacce", "Gestire database", "Fare videogiochi", "Installare app"),
            correctAnswerIndex = 0
        ),
        Question(
            text = "Quale componente gestisce la navigazione?",
            answers = listOf("RecyclerView", "IntentFilter", "NavController", "LazyColumn"),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Quante schermate ha il quiz di oggi?",
            answers = listOf("1", "2", "3", "10"),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Cosa tiene traccia della schermata corrente nel NavHost?",
            answers = listOf("La rotta", "Il colore", "Il padding", "Il bottone"),
            correctAnswerIndex = 0
        )
    )

    // ------------------------------
    // STATO DELL'APP
    // ------------------------------
    // currentQuestionIndex tiene traccia di quale domanda stiamo mostrando.
    // All'inizio siamo alla domanda 0 (cioè la prima).
    var currentQuestionIndex by remember {
        mutableIntStateOf(0)
    }

    // score tiene traccia del punteggio dell'utente.
    // All'inizio è 0.
    var score by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        // Il NavHost è "la mappa" della nostra app.
        // Qui definiamo:
        // - la schermata iniziale
        // - tutte le possibili destinazioni
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {

            // ------------------------------
            // SCHERMATA HOME
            // ------------------------------
            composable("home") {
                HomeScreen(
                    onStartClick = {
                        // Quando l'utente preme "Inizia Quiz":
                        // 1. resettiamo indice e punteggio
                        // 2. navighiamo verso la schermata quiz
                        currentQuestionIndex = 0
                        score = 0
                        navController.navigate("quiz")
                    }
                )
            }

            // ------------------------------
            // SCHERMATA QUIZ
            // ------------------------------
            composable("quiz") {
                QuizScreen(
                    question = questions[currentQuestionIndex],
                    questionNumber = currentQuestionIndex + 1,
                    totalQuestions = questions.size,
                    score = score,
                    onAnswerClick = { selectedIndex ->

                        // Controlliamo se la risposta scelta è corretta
                        if (selectedIndex == questions[currentQuestionIndex].correctAnswerIndex) {
                            score++
                        }

                        // Questo è SOLO scaffolding:
                        // se non siamo ancora all'ultima domanda, passiamo alla successiva.
                        // Per ora NON navighiamo alla schermata risultati.
                        if (currentQuestionIndex < questions.lastIndex) {
                            currentQuestionIndex++
                        }

                        // Quando arriverete alla fase successiva,
                        // qui aggiungerete la navigazione ai risultati.
                    }
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    onStartClick: () -> Unit
) {
    // Schermata iniziale molto semplice:
    // titolo + bottone per iniziare il quiz
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Laboratorio 3 - App Quiz",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Scaffolding: Home + Quiz + stato base",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onStartClick) {
            Text("Inizia Quiz")
        }
    }
}

@Composable
fun QuizScreen(
    question: Question,
    questionNumber: Int,
    totalQuestions: Int,
    score: Int,
    onAnswerClick: (Int) -> Unit
) {
    // Questa schermata mostra:
    // - il numero della domanda attuale
    // - il punteggio corrente
    // - il testo della domanda
    // - i bottoni con le 4 risposte
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Domanda $questionNumber di $totalQuestions",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Punteggio attuale: $score",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = question.text,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Creiamo un bottone per ogni risposta disponibile.
        // forEachIndexed ci dà:
        // - index = posizione della risposta
        // - answer = testo della risposta
        question.answers.forEachIndexed { index, answer ->
            Button(
                onClick = {
                    onAnswerClick(index)
                },
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                Text(answer)
            }
        }
    }
}