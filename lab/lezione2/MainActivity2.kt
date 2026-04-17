package com.example.myapplication2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ToDoApp()
                }
            }
        }
    }
}

data class TaskItem(
    val id: Int,
    val text: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoApp() {
    var inputText by rememberSaveable { mutableStateOf("") }
    val taskList = remember { mutableStateListOf<TaskItem>() }
    var nextId by rememberSaveable { mutableStateOf(0) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("To-Do List Demo") }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Aggiungi un impegno",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Nuova attività") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                ElevatedButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            taskList.add(
                                TaskItem(
                                    id = nextId++,
                                    text = inputText.trim()
                                )
                            )
                            inputText = ""
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Scrivi qualcosa prima di aggiungere")
                            }
                        }
                    }
                ) {
                    Text("Aggiungi")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (taskList.isEmpty()) {
                Text(
                    text = "Nessun task presente. Inseriscine uno!",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text(
                    text = "Task presenti: ${taskList.size}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = taskList,
                        key = { it.id }
                    ) { task ->
                        TaskRow(
                            task = task,
                            onDelete = {
                                taskList.remove(task)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Task rimosso: ${task.text}")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskRow(
    task: TaskItem,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDelete() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = task.text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )

            HorizontalDivider(modifier = Modifier.width(8.dp))

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Elimina task"
                )
            }
        }
    }
}